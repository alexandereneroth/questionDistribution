package se.alen.labs.questiondistribution;

import java.util.*;

public class Implementation {

    public static List<Question> distributeQuestionsInList(RecipeQuestion interactiveQuestion, List<RecipeQuestion> recipeQuestionsList,
                                                           List<CuratedQuestion> curatedQuestionsList) {
        final Deque<Question> recipeQuestions = new ArrayDeque<>(recipeQuestionsList);
        final Deque<Question> curatedQuestions = new ArrayDeque<>(curatedQuestionsList);

        final List<Question> questions = new ArrayList<>();

        int numRecipeQuestions = recipeQuestions.size();
        int numCuratedQuestions = curatedQuestions.size();

        boolean appendCuratedQuestionAtEnd = false;
        // Reserve a spot at the end for a curated question (since we always want to end with one of these.
        if (numCuratedQuestions > 0) {
            appendCuratedQuestionAtEnd = true;
            numCuratedQuestions--;
        }

        final int numQuestions = numRecipeQuestions + numCuratedQuestions;

        // The ideal distance between recipe questions
        final float wish = (numRecipeQuestions == 0) ? 0 : (float) numQuestions / numRecipeQuestions;

        // Holds the remainder of the ideal distance after it has been rounded to a position integer
        float remainder = 0;

        float distanceToRecipeQuestion = (int) Math.floor(wish);
        int numUnplacedRecipeQuestions = numRecipeQuestions;
        for (int i = 0; i < numQuestions; i++) {

            final boolean shouldPlaceRecipeQuestion = distanceToRecipeQuestion == 1 && numUnplacedRecipeQuestions > 0;
            if (shouldPlaceRecipeQuestion) {

                questions.add(recipeQuestions.pop());
                numUnplacedRecipeQuestions--;

                final int roundedWish = (int) Math.floor(wish);
                // Add to the remainder each time, so as to account for previous rounding in future calculations
                remainder += wish - roundedWish;
                final int amortizedRoundedWish = (int) Math.floor(wish + remainder);

                if (amortizedRoundedWish != roundedWish) {
                    remainder = (wish + remainder) - (int) Math.floor(wish + remainder);
                }

                distanceToRecipeQuestion = amortizedRoundedWish;

            } else {
                questions.add(curatedQuestions.pop());

                distanceToRecipeQuestion--;
            }
        }

        if (interactiveQuestion != null) {
            questions.add(0, interactiveQuestion);
        }

        if (appendCuratedQuestionAtEnd) {
            questions.add(curatedQuestions.pop());
        }

        return questions;
    }

    public interface Question {
        String getName();
    }

    public static class RecipeQuestion implements Question {
        private String name;

        public RecipeQuestion(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString(){
            return getName(); // to make test assertion error messages human readable.
        }
    }

    public static class CuratedQuestion implements Question {
        private String name;

        public CuratedQuestion(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString(){
            return getName(); // to make test assertion error messages human readable.
        }
    }
}