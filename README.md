# Even question distribution

## Background
A number of different questions for a project had to be distributed in a, good, even way in an array.
There were two different types of questions; 'Recipe', and 'Curated', as well as 'Interactive' questions that were a special type of Recipe questions.

If there was an Interactive question, it was to be placed first, and if there was a Curated question it was to be placed last. The other questions were to be evenly distributed inbetween.

**Key:**

I = Interactive question

R = Recipe question

C = Curated question

**Array format:**

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
|---|---|---|---|---|---|---|---|---|---|
| I | C/R | C/R | C/R | C/R | C/R | C/R | C/R | C/R | C |

Because the first and last elements are always in the same position if they exist, the problem array essentially looks like this (ofc, the total number of elements varies):

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
| C/R | C/R | C/R | C/R | C/R | C/R | C/R | C/R |

The problem was to evenly distribute these varying number of curated, and recipe questions in the array.

## Initial thought process
The initial thought that came to us were to divide the total number of questions with the number of recipe questions, round that down, and use that number as the distance between the recipe questions in the array, so as to distribute the recipe questions as far from eachother as possible.

## Initial solution candidates

### Solution A

One way to interpret distance was 'the number of position increases needed to reach an R from another R' this interpretation produced arrays like this:

```javascript
8/3 ≈ 2.7
floor(2.7) = 2
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
|   | R |   | R |   | R |   |   |
| 1 | 2 | 1 | 2 | 1 | 2 | - | - |

```javascript
9/3 = 3
floor(3) = 3 
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|---|
|   |   | R |   |   | R |   |   | R |
| 1 | 2 | 3 | 1 | 2 | 3 | 1 | 2 | 3 |

### Solution B

Another distance interpretation was 'the number of cells in between Rs'. This interpretation produced arrays like this:

```javascript
8/3 ≈ 2.7
floor(2.7) = 2
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |   |
|---|---|---|---|---|---|---|---|---|
|   |   | R |   |   | R |   |   | ~~R~~ (index out of bounds) |
| 1 | 2 |   | 1 | 2 |   | 1 | 2 |   |

```javascript
9/3 = 3
floor(3) = 3 
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

# Centering the questions

Taking the total number of questions divided by the number of recipe questions as the distance, places the recipe questions as far away from eachother, and the start of the range, as possible.
While this might be desierable in some cases, it means that the questions are not perfectly evenly distributed in the range.

Example:
```javascript
8/2 = 4
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
|   |   |   | R |   |   |   | R |
| 1 | 2 | 3 | 4 | 1 | 2 | 3 | 4 |

To put the recipe questions as far away from eachother as they are from the ends of the array, a small adjustment to the calculation is needed.
The adjustment is simply changing the distance calculation to what it would be if there were one extra recipeQuestion.

Example:
```javascript
8+1/2+1 = 3
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |   |
|---|---|---|---|---|---|---|---|---|
|   |   | R |   |   | R |   |   | ~~R~~(ignored) |
| 1 | 2 | 3 | 1 | 2 | 3 | 1 | 2 |   |

Example if the array contained 80 total elements, and 4 recipe questions:
```javascript
80+1/4+1 = 16.2
floor(16.2) = 16
```
| 0-15 | 16 | 17-31 | 32 | 33-47 | 48 | 49-63 | 64 | 65-80 | ~~81~~ |
|:----:|----|:-----:|----|:-----:|----|:-----:|----|:-----:|----|
|      |  R |       |  R |       |  R |       |  R |       | ~~R~~(ignored) |
|  15  | 16 |   15  | 16 |   15  | 16 |   15  | 16 |   15  | 16 |

# Problems with rounding down

A problem with solution A and solution B is that they simply round the ideal distance between Rs down, discard the remainder, and never account for it. 

This has the effect of elements often getting distributed more on the left side of the array than they should.

Example:
```javascript
9/5 = 1.8
floor(1.8) = 1
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|---|
| R | R | R | R | R |   |   |   |   |
| 1 | 1 | 1 | 1 | 1 | - | - | - | - |

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

This solution makes the distribution more balanced.

Example:
```javascript
9/5 = 1.8
floor(1.8) = 1 // remainder 8
floor(1.8 + 8) = 2 // remainder 6
floor(1.8 + 6) = 2 // remainder 4
floor(1.8 + 4) = 2 // remainder 2
floor(1.8 + 2) = 2
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|---|
| R |   | R |   | R |   | R |   | R |
| 1 | 1 | 2 | 1 | 2 | 1 | 2 | 1 | 2 |

# Conclusion

Evenly distributing a number of elements in an array of buckets is not a trivial task.

If one simply uses the total number of elements divided by the number of elements to be distributed as the distance between elements, the elements are not perfectly distributed.

Example:
```javascript
8/4 = 2
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
|   | R |   | R |   | R |   | R |
| 1 | 2 | 1 | 2 | 1 | 2 | 1 | 2 |

You can attempt to center the elements by adjusting the distance calculation to what it would be if there were one more element to be distributed,
but there is still the problem of the calculation producing fractions.

In this case, making the calculation adjustment has a big impact on distribution when simply rounding the number down.

Example:
```javascript
8+1/4+1 = 1.8
floor(1.8) = 1
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|
| R | R | R | R | ~~R~~(ignored) |   |   |   |
| 1 | 1 | 1 | 1 | 1 | - | - | - |

To fix this, we can account for the remainder that is left after rounding, each time we calculate the next distance.

Example:
```javascript
8+1/4+1 = 1.8
floor(1.8) = 1 // remainder 8
floor(1.8 + 8) = 2 // remainder 6
floor(1.8 + 6) = 2 // remainder 4
floor(1.8 + 4) = 2 // remainder 2
floor(1.8 + 2) = 2
```
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |   |
|---|---|---|---|---|---|---|---|---|
| R |   | R |   | R |   | R |   | ~~R~~(ignored) |
| 1 | 1 | 2 | 1 | 2 | 1 | 2 |   |   |

# Bonus solution D

Another way to approach this whole problem is to make use of weights and sorting.

This solution calculates the ideal distance between recipe questions as well as between curated questions, if they were to be distributed independently in their own range. 
```
distR = questionCount / questionCountR
distC = questionCount / questionCountC
```
Then this distance, times each questions position in its own array + 1, is used as its weight.
All questions are then added to the same array, which is sorted by weight.
This works very well.

Example:

```javascript
distR = 7/2 = 3.5

* 1 = 3.5
* 2 = 7

distC = 7/5 = 1.4

* 1 = 1.4
* 2 = 2.8
* 3 = 4.2
* 4 = 5.6
* 5 = 7
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 |
|---|---|---|---|---|---|---|
| C | C | R | C | C | R/C | R/C |
| x1 | x2 | x1 | x3 | x4 | x2/x5 | x2/x5 |
| 1.4 | 2.8 | 3.5 | 4.2 | 5.6 | 7.0 | 7.0 |

As you can see, this solution also has the problem of being heavy on the right side of the range.

A possible way to alleviate this, is to subtract half of distR from each final weight, and do the same with distC.

Example:

```javascript
distR = 7/2 = 3.5
3.5 / 2 = 1.75

* 1 - 1.75 = 1.75
* 2 - 1.75 = 5.25

distC = 7/5 = 1.4
1.4 / 2 = 0.7

* 1 - 0.7 = 0.7
* 2 - 0.7 = 2.1
* 3 - 0.7 = 3.5
* 4 - 0.7 = 4.9
* 5 - 0.7 = 6.3
```

| 0 | 1 | 2 | 3 | 4 | 5 | 6 |
|---|---|---|---|---|---|---|
| C | R | C | C | C | R | C |
| x1 | x1 | x1 | x3 | x4 | x2 | x5 |
| 0.7 | 1.75 | 2.1 | 3.5 | 4.9 | 5.25 | 6.3 |