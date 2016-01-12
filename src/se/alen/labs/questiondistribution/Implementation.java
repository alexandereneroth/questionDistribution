package se.alen.labs.questiondistribution;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Floats;

import java.util.*;

public class Implementation {

    protected static List<Question> distributeQuestionsInList(RecipeQuestion interactive, List<RecipeQuestion> recipeQuestions, List<CuratedQuestion> curatedQuestions) {
        final List<Question> questions = new ArrayList<>();

        int noOfCuratedQuestions = curatedQuestions.size();
        boolean appendCuratedQuestionAtEnd = false;

        // Reserve a spot at the end for a curated question (since we always want to end with one of these.
        if (noOfCuratedQuestions > 0) {
            appendCuratedQuestionAtEnd = true;
            noOfCuratedQuestions--;
        }

        final int totalNumberOfQuestions = recipeQuestions.size() + noOfCuratedQuestions;

        final float rs = (float) totalNumberOfQuestions / ((float) recipeQuestions.size());
        final float cs = (float) totalNumberOfQuestions / (float) noOfCuratedQuestions;

        final List<SortedQuestion> sortedQuestions = new ArrayList<>();

        for (int n = 0; n < recipeQuestions.size(); n++) {
            sortedQuestions.add(new SortedQuestion(recipeQuestions.get(n), (rs * (float) (1 + n)) - (rs / 2f)));
        }

        for (int n = 0; n < noOfCuratedQuestions; n++) {
            sortedQuestions.add(new SortedQuestion(curatedQuestions.get(n), (cs * (float) (1 + n)) - (cs / 2f)));
        }

        Collections.sort(sortedQuestions, new QuestionOrder());

        for (SortedQuestion sq : sortedQuestions) {
            questions.add(sq.getQuestion());
        }

        if(interactive != null) {
            questions.add(0, interactive);
        }

        if (appendCuratedQuestionAtEnd) {
            questions.add(curatedQuestions.get(curatedQuestions.size() - 1));
        }

        return questions;
    }

    private static class QuestionOrder extends Ordering<SortedQuestion> {
        @Override
        public int compare(SortedQuestion s1, SortedQuestion s2) {
            return Floats.compare(s1.getWeight(), s2.getWeight());
        }
    }

    private static class SortedQuestion {
        private Question question;
        private float weight;

        public SortedQuestion(Question question, float weight) {
            this.question = question;
            this.weight = weight;
        }

        public Question getQuestion() {
            return question;
        }

        public float getWeight() {
            return weight;
        }
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