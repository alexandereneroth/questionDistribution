# Even question distribution

## Background
A number of different questions for a project had to be distributed in a, good, even way in an array.
There were two different types of questions; 'Recipe', and 'Curated', as well as 'Interactive' questions that were a special type of Recipe questions.

If there was an Interactive question, it was to be placed first, and if there was a Curated question it was to be placed last. The other questions was to be evenly distributed inbetween.

**Key:**

I = Interactive question

R = Recipe question

C = Curated question

**Array format:**

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
|---|---|---|---|---|---|---|---|---|---|
| I | C/R | C/R | C/R | C/R | C/R | C/R | C/R | C/R | C |

Because the first and last elements are always in the same position if they exist, the problem array essentially looks like this:

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
| C/R | C/R | C/R | C/R | C/R | C/R | C/R | C/R |

The problem was to evenly distribute these varying number of curated, and recipe questions in the array.

## Initial thought process
The initial thought that came to us were to divide the total number of questions with the number of recipe questions, round that down, and use that number as the distance between the recipe questions in the array, so as to distribute the recipe questions as far from eachother as possible.

## Initial solution candidates

### Solution A

One way to interpret distance was 'the number of position increases needed to reach an R from another R' this interpretation produced arrays like this:

```
8/3 ≈ 2.7
roundDown(2.7) = 2
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
|   | R |   | R |   | R |   |   |
| 1 | 2 | 1 | 2 | 1 | 2 | - | - |

```
9/3 = 3
roundDown(3) = 3 
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|---|
|   |   | R |   |   | R |   |   | R |
| 1 | 2 | 3 | 1 | 2 | 3 | 1 | 2 | 3 |

### Solution B

Another distance interpretation was 'the number of cells in between Rs'. This interpretation produced arrays like this:

```
8/3 ≈ 2.7
roundDown(2.7) = 2
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |   |
|---|---|---|---|---|---|---|---|---|
|   |   | R |   |   | R |   |   | ~~R~~ (index out of bounds) |
| 1 | 2 |   | 1 | 2 |   | 1 | 2 |   |

```
9/3 = 3
roundDown(3) = 3 
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |   |   |   |
|---|---|---|---|---|---|---|---|---|---|---|---|
|   |   |   | R |   |   |   | R |   |   |   | ~~R~~ (index out of bounds) |
| 1 | 2 | 3 |   | 1 | 2 | 3 |   | 1 | 2 | 3 |   |

As you can see this solution had the problem of overshooting the array bounds. The proposed solution was to insert overshooting Rs at the beginning of the array:

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |   |   |   |
|---|---|---|---|---|---|---|---|---|---|---|---|
| R |   |   | R |   |   |   | R |   |   |   | ~~R~~ (moved to 0) |
| 1 | 2 | 3 |   | 1 | 2 | 3 |   | 1 | 2 | 3 |   |

# Problems with rounding down

A problem with solution A and solution B is that they simply round the ideal distance between Rs down, discard the remainder, and never account for it. 

This has the effect of elements often getting distributed more on the left side of the array than they should.

After some googling a solution for this was found in a [StackOverflow answer written by pid](http://stackoverflow.com/questions/27330331/how-do-i-optimally-distribute-values-over-an-array-of-percentages). This article gave the inspiration for soluton C.

# Solution C

Solution C is the same as solution A, with the difference that the remainder that is left when rounding the distance down is saved, and accounted for in future distance calculations.

**Algorithm**
```javascript
var wish = questionCount / recipeQuestionCount
var dist = floor(wish)
var remainder = 0
for(var i = 0; i < cuestionCount; i++) {
    if(dist == 1) {
        placeRecipeQuestionAtIdx(i)
        // increasing remainder
        remainder += wish - floor(wish)

        dist = floor(wish + remainder)

        // remove what has already been accounted for from remainder
        if(floor(wish + remainder) != floor(wish)) {
            remainder = (wish + remainder) - floor(wish + remainder)
        }
    } else {
        dist--
    }
}
```

This solution causes arrays to be a bit more balanced.

Example:
```
7/2 = 3.5
roundDown(3.5) = 3
roundDown(3.5 + 0.5) = 4
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 |
|---|---|---|---|---|---|---|
|   |   | R |   |   |   | R |
| 1 | 2 | 3 | 1 | 2 | 3 | 4 |

# Solution D

Another way to approach this whole problem is to make use of weights and sorting.

This solution calculates the ideal distance between recipe questions as well as between curated questions(if they were to be distributed independently in their own range), and uses this distance, times the recipe question/curated questions position+1 in their own array, as a weight.
All questions are then added to the same array and are sorted by their weight.
This works very well.

Example:

```
weightR = 7/2 = 3.5
weightC = 7/5 = 1.4
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 |
|---|---|---|---|---|---|---|
| C | C | R | C | C | R/C | R/C |
| x1 | x2 | x1 | x3 | x4 | x2/x5 | x2/x5 |
| 1.4 | 2.8 | 3.5 | 4.2 | 5.6 | 7.0 | 7.0 |

# Centering the Rs

Taking the total number of questions divided by the number of recipe questions as the distance, places the recipe questions as far away from eachother as possible in the range.
While this might be desierable in some cases, it means that the curated questions are not evenly distributed on either side of the recipe questions.

Example:
```
8/2 = 4
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
|   |   |   | R |   |   |   | R |
| 1 | 2 | 3 | 4 | 1 | 2 | 3 | 4 |

To put the recipe questions as far away from eachother as they are from the ends of the array, a small adjustment to the calculation is needed.
The adjustment is simply changing the distance calculation to what it would be if there were one extra recipeQuestion.

Example:
```
8+1/2+1 = 3
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |   |
|---|---|---|---|---|---|---|---|---|
|   |   | R |   |   | R |   |   | ~~R~~(ignored) |
| 1 | 2 | 3 | 1 | 2 | 3 | 1 | 2 |   |


# Final solution

As far from eachother as possible
```
8/4 = 2
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
|   | R |   | R |   | R |   | R |
| 1 | 2 | 1 | 2 | 1 | 2 | 1 | 2 |

As far from eachother, and the ends, as possible

Example of big rounding impact:
```
8+1/4+1 = 1.8
roundDown(1.8) = 1
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
| R | R | R | R | ~~R~~(ignored) |   |   |   |
| 1 | 1 | 1 | 1 | 1 | - | - | - |

Rounding remainder accounted for:

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |   |
|---|---|---|---|---|---|---|---|---|
| R |   | R |   | R |   | R |   | ~~R~~(ignored) |
| 1.8 |   | 2.6 |   | 2.4 |   | 2.2 |   |   |
| +0 |   | +.8 |   | +.6 |   | +.4 |   |   |


