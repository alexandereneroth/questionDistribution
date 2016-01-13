package se.alen.labs.questiondistribution;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class ImplementationTest {

    private static final String MSG_INVALID_LIST = "Other list contents/ordering expected. \nActual list: ";

    final Implementation.RecipeQuestion interactive = new Implementation.RecipeQuestion("I");
    // Intentional uppercase, to make them stand out more in the array list constructors (for readability).
    final Implementation.RecipeQuestion RQ1 = new Implementation.RecipeQuestion("RQ1");
    final Implementation.RecipeQuestion RQ2 = new Implementation.RecipeQuestion("RQ2");
    final Implementation.RecipeQuestion RQ3 = new Implementation.RecipeQuestion("RQ3");
    final Implementation.RecipeQuestion RQ4 = new Implementation.RecipeQuestion("RQ4");
    final Implementation.CuratedQuestion cq1 = new Implementation.CuratedQuestion("CQ1");
    final Implementation.CuratedQuestion cq2 = new Implementation.CuratedQuestion("CQ2");
    final Implementation.CuratedQuestion cq3 = new Implementation.CuratedQuestion("CQ3");
    final Implementation.CuratedQuestion cq4 = new Implementation.CuratedQuestion("CQ4");
    final Implementation.CuratedQuestion cq5 = new Implementation.CuratedQuestion("CQ5");
    final Implementation.CuratedQuestion cq6 = new Implementation.CuratedQuestion("CQ6");
    final Implementation.CuratedQuestion cq7 = new Implementation.CuratedQuestion("CQ7");

    List<Implementation.RecipeQuestion> recipeQuestions = new ArrayList<>();
    List<Implementation.CuratedQuestion> curatedQuestions = new ArrayList<>();

    @Test
    public void shouldCorrectlyDistribute_7CQ_NoInteractiveQuestion() throws Exception {

        curatedQuestions = newArrayList(cq1, cq2, cq3, cq4, cq5, cq6, cq7);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(null, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_7CQ_NoInteractiveQuestion", questions);

        assertEquals(newArrayList(cq1, cq2, cq3, cq4, cq5, cq6, cq7), questions);
    }

    @Test
    public void shouldCorrectlyDistribute_2RQ_6CQ_NoInteractiveQuestion() throws Exception {

        recipeQuestions = Arrays.asList(RQ1, RQ2);
        curatedQuestions = Arrays.asList(cq1, cq2, cq3, cq4, cq5, cq6);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(null, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_2RQ_6CQ_NoInteractiveQuestion", questions);

        assertTrue(MSG_INVALID_LIST + generateListString(questions),
                Arrays.asList(cq1, cq2, RQ1, cq3, cq4, cq5, RQ2, cq6).equals(questions)
                        || Arrays.asList(cq1, RQ1, cq2, cq3, cq4, RQ2, cq5, cq6).equals(questions)
                        || Arrays.asList(cq1, cq2, cq3, RQ1, cq4, cq5, RQ2, cq6).equals(questions)
                        || Arrays.asList(cq1, RQ1, cq2, cq3, RQ2, cq4, cq5, cq6).equals(questions)
        );
    }

    @Test
    public void shouldCorrectlyDistribute_2CQ() throws Exception {

        curatedQuestions = newArrayList(cq1, cq2);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_2CQ", questions);

        assertEquals(newArrayList(interactive, cq1, cq2), questions);
    }

    @Test
    public void shouldCorrectlyDistribute_1RQ_3CQ() throws Exception {

        recipeQuestions = newArrayList(RQ1);
        curatedQuestions = newArrayList(cq1, cq2, cq3);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_1RQ_3CQ", questions);

        assertEquals(newArrayList(interactive, cq1, cq2, RQ1, cq3), questions);

        assertTrue(MSG_INVALID_LIST + generateListString(questions),
                newArrayList(interactive, cq1, cq2, RQ1, cq3).equals(questions)
                        || newArrayList(interactive, cq1, RQ1, cq2, cq3).equals(questions));
    }

    @Test
    public void shouldCorrectlyDistribute_4RQ_5CQ() throws Exception {

        recipeQuestions = Arrays.asList(RQ1, RQ2, RQ3, RQ4);
        curatedQuestions = Arrays.asList(cq1, cq2, cq3, cq4, cq5);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_4RQ_6CQ", questions);

        assertTrue(MSG_INVALID_LIST + generateListString(questions),
                newArrayList(interactive, cq1, RQ1, cq2, RQ2, cq3, RQ3, cq4, RQ4, cq5).equals(questions)
                        || newArrayList(interactive, RQ1, cq1, RQ2, cq2, RQ3, cq3, RQ4, cq4, cq5).equals(questions));

    }

    @Test
    public void shouldCorrectlyDistribute_2RQ_6CQ() throws Exception {

        recipeQuestions = newArrayList(RQ1, RQ2);
        curatedQuestions = newArrayList(cq1, cq2, cq3, cq4, cq5, cq6);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_2RQ_6CQ", questions);

        assertTrue(MSG_INVALID_LIST + generateListString(questions),
                newArrayList(interactive, cq1, RQ1, cq2, cq3, RQ2, cq4, cq5, cq6).equals(questions)
                        || newArrayList(interactive, cq1, cq2, RQ1, cq3, cq4, cq5, RQ2, cq6).equals(questions)
                        || newArrayList(interactive, cq1, cq2, cq3, RQ1, cq4, cq5, RQ2, cq6).equals(questions));
    }

    @Test
    public void shouldCorrectlyDistribute_3RQ_2CQ() throws Exception {
        recipeQuestions = newArrayList(RQ1, RQ2, RQ3);
        curatedQuestions = newArrayList(cq1, cq2);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        printQuestions("shouldCorrectlyDistribute_3RQ_2CQ", questions);

        assertTrue(MSG_INVALID_LIST + generateListString(questions),
                newArrayList(interactive, cq1, RQ1, RQ2, RQ3, cq2).equals(questions)
                        || newArrayList(interactive, RQ1, cq1, RQ2, RQ3, cq2).equals(questions)
                        || newArrayList(interactive, RQ1, RQ2, cq1, RQ3, cq2).equals(questions));
    }

    private void printQuestions(String heading, List<Implementation.Question> questions) {
        System.out.println("\n" + heading + "\n" + generateListString(questions));
    }

    /**
     * Generates a string like this one: [1, 2, 3, 4, 5]
     */
    private <T> String generateListString(List<T> list) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < list.size() - 2; i++) {
            stringBuilder.append(list.get(i)).append(", ");
        }
        stringBuilder.append(list.get(list.size() - 1)).append("]");

        return stringBuilder.toString();
    }
}
