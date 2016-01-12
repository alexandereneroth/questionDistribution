package se.alen.labs.questiondistribution;

import java.util.*;

public class Implementation {

    private static final Random random = new Random();

    public static List<Question> distributeQuestionsInList(RecipeQuestion interactiveQuestion, List<RecipeQuestion> recipeQuestions,
                                                           List<CuratedQuestion> curatedQuestions) {
        final List<Question> questions = new ArrayList<>();

        int idxRecipeQuestions = 0;
        int idxCuratedQuestions = 0;

        int numRecipeQuestions = recipeQuestions.size();

        final int numQuestions = numRecipeQuestions + curatedQuestions.size();
        final int numQuestionsWithoutLastCuratedQuestion = numQuestions - 1;

        // If the recipe questions can't be evenly distributed,
        // they are offset one position to the right at the index of a random recipe question.
        int indexOfRecipeQuestionToOffset = getIndexOfRecipeQuestionToOffset(numRecipeQuestions, numQuestions);

        // the ideal distance between recipe questions in the final list
        float recipeQuestionIndexDelta = numQuestions;
        if (numRecipeQuestions != 0) {
            recipeQuestionIndexDelta = (float) numQuestionsWithoutLastCuratedQuestion / numRecipeQuestions;
        }

        // Initialization of the number of unplaced recipe questions in the final questions list.
        int numUnplacedRecipeQuestions = numRecipeQuestions;

        int positionsUntilRecipeQuestion = (int) recipeQuestionIndexDelta - 1;
        for (int i = 0; i < numQuestionsWithoutLastCuratedQuestion; i++) {

            final boolean shouldPlaceRecipeQuestion = positionsUntilRecipeQuestion == 0 && numUnplacedRecipeQuestions > 0;
            if (shouldPlaceRecipeQuestion) {

                final boolean shouldOffsetThisRecipeQuestion = idxRecipeQuestions == indexOfRecipeQuestionToOffset;
                if (shouldOffsetThisRecipeQuestion) {

                    questions.add(curatedQuestions.get(idxCuratedQuestions++));

                    indexOfRecipeQuestionToOffset = -1; // don't offset again
                } else {
                    questions.add(recipeQuestions.get(idxRecipeQuestions++));

                    positionsUntilRecipeQuestion = (int) recipeQuestionIndexDelta - 1;
                    numUnplacedRecipeQuestions--;
                }

            } else {
                questions.add(curatedQuestions.get(idxCuratedQuestions++));

                positionsUntilRecipeQuestion--;
            }
        }

        if (interactiveQuestion != null) {
            questions.add(0, interactiveQuestion);
        }

        questions.add(curatedQuestions.get(idxCuratedQuestions));

        return questions;
    }

    /**
     * If the recipe questions can't be evenly distributed, they are offset one position
     * to the right at the index of a random recipe question.
     *
     * @param numRecipeQuestions the total number of recipe questions (including the interactive)
     * @param numQuestions       the total number of questions
     * @return a randomly generated number, which is the index in the recipe question collection of the question
     * to be offset one position to the right.
     */
    private static int getIndexOfRecipeQuestionToOffset(int numRecipeQuestions, int numQuestions) {
        final int numQuestionsWithoutLastCuratedQuestion = numQuestions - 1;

        final boolean canEvenlyDistributeRecipeQuestions = numRecipeQuestions == 0 || (numQuestionsWithoutLastCuratedQuestion % numRecipeQuestions == 0);
        if (!canEvenlyDistributeRecipeQuestions) {
            final int numberOfRecipeQuestionsToRandomlyPickOneToOffsetFrom = numRecipeQuestions - 1; // exclude the interactive question

            return random.nextInt(numberOfRecipeQuestionsToRandomlyPickOneToOffsetFrom);
        }
        return -1;
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