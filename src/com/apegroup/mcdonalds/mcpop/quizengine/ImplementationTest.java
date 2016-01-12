package com.apegroup.mcdonalds.mcpop.quizengine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static com.google.common.collect.Lists.newArrayList;

public class ImplementationTest {

    final Implementation.RecipeQuestion interactive = new Implementation.RecipeQuestion("I");
    final Implementation.RecipeQuestion rq1 = new Implementation.RecipeQuestion("RQ1");
    final Implementation.RecipeQuestion rq2 = new Implementation.RecipeQuestion("RQ2");
    final Implementation.RecipeQuestion rq3 = new Implementation.RecipeQuestion("RQ3");
    final Implementation.RecipeQuestion rq4 = new Implementation.RecipeQuestion("RQ4");
    final Implementation.CuratedQuestion cq1 = new Implementation.CuratedQuestion("CQ1");
    final Implementation.CuratedQuestion cq2 = new Implementation.CuratedQuestion("CQ2");
    final Implementation.CuratedQuestion cq3 = new Implementation.CuratedQuestion("CQ3");
    final Implementation.CuratedQuestion cq4 = new Implementation.CuratedQuestion("CQ4");
    final Implementation.CuratedQuestion cq5 = new Implementation.CuratedQuestion("CQ5");
    final Implementation.CuratedQuestion cq6 = new Implementation.CuratedQuestion("CQ6");
    final Implementation.CuratedQuestion cq7 = new Implementation.CuratedQuestion("CQ7");

    List<Implementation.RecipeQuestion> recipeQuestions = new ArrayList<>();
    List<Implementation.CuratedQuestion> curatedQuestions = new ArrayList<>();

    @Before
    public void setUp() {
        recipeQuestions = new ArrayList<>();
        curatedQuestions = new ArrayList<>();
    }

    @Test
    public void shouldCorrectlyDistribute_7CQ_NoInteractiveQuestion() throws Exception {

        curatedQuestions = newArrayList(cq1,cq2,cq3,cq4,cq5,cq6,cq7);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(null, recipeQuestions, curatedQuestions);

        assertEquals(cq1, questions.get(0));
        assertEquals(cq2, questions.get(1));
        assertEquals(cq3, questions.get(2));
        assertEquals(cq4, questions.get(3));
        assertEquals(cq5, questions.get(4));
        assertEquals(cq6, questions.get(5));
        assertEquals(cq7, questions.get(6));
    }

    @Test
    public void shouldCorrectlyDistribute_2RQ_6CQ_NoInteractiveQuestion() throws Exception {

        recipeQuestions = Arrays.asList(rq1, rq2);
        curatedQuestions = Arrays.asList(cq1, cq2, cq3, cq4, cq5, cq6);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(null, recipeQuestions, curatedQuestions);

        System.out.println("\nshouldCorrectlyDistribute_2RQ_6CQ_NoInteractiveQuestion");
        for (Implementation.Question q : questions) {
            System.out.println(q.getName());
        }

        assertTrue("Wrongly distributed list", Arrays.asList(cq1, cq2, rq1, cq3, cq4, cq5, rq2, cq6).equals(questions)
                        || Arrays.asList(cq1, cq2, cq3, rq1, cq4, cq5, rq2, cq6).equals(questions)
        );
    }

    @Test
    public void shouldCorrectlyDistribute_2CQ() throws Exception {

        curatedQuestions.add(cq1);
        curatedQuestions.add(cq2);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        System.out.println("\nshouldCorrectlyDistribute_2CQ");
        // Should print: I, CQ1, CQ2
        for (Implementation.Question q : questions) {
            System.out.println(q.getName());
        }

        assertEquals(interactive, questions.get(0));
        assertEquals(cq1, questions.get(1));
        assertEquals(cq2, questions.get(2));
    }

    @Test
    public void shouldCorrectlyDistribute_1RQ_3CQ() throws Exception {

        recipeQuestions.add(rq1);
        curatedQuestions.add(cq1);
        curatedQuestions.add(cq2);
        curatedQuestions.add(cq3);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        System.out.println("\nshouldCorrectlyDistribute_1RQ_3CQ");
        // Should print: I, CQ1, RQ1, CQ2, CQ3
        for (Implementation.Question q : questions) {
            System.out.println(q.getName());
        }

        assertEquals(interactive, questions.get(0));
        assertEquals(cq1, questions.get(1));
        assertEquals(cq2, questions.get(2));
        assertEquals(rq1, questions.get(3));
        assertEquals(cq3, questions.get(4));
    }

    @Test
    public void shouldCorrectlyDistribute_4RQ_5CQ() throws Exception {

        recipeQuestions = Arrays.asList(rq1,rq2,rq3,rq4);
        curatedQuestions = Arrays.asList(cq1,cq2,cq3,cq4,cq5);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        System.out.println("\nshouldCorrectlyDistribute_4RQ_6CQ");
        for (Implementation.Question q : questions) {
            System.out.println(q.getName());
        }

        assertEquals(interactive, questions.get(0));
        assertEquals(cq1, questions.get(1));
        assertEquals(rq1, questions.get(2));
        assertEquals(cq2, questions.get(3));
        assertEquals(rq2, questions.get(4));
        assertEquals(cq3, questions.get(5));
        assertEquals(rq3, questions.get(6));
        assertEquals(cq4, questions.get(7));
        assertEquals(rq4, questions.get(8));
        assertEquals(cq5, questions.get(9));
    }

    @Test
    public void shouldCorrectlyDistribute_2RQ_6CQ() throws Exception {

        recipeQuestions.add(rq1);
        recipeQuestions.add(rq2);
        curatedQuestions.add(cq1);
        curatedQuestions.add(cq2);
        curatedQuestions.add(cq3);
        curatedQuestions.add(cq4);
        curatedQuestions.add(cq5);
        curatedQuestions.add(cq6);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        System.out.println("\nshouldCorrectlyDistribute_2RQ_6CQ");
        for (Implementation.Question q : questions) {
            System.out.println(q.getName());
        }

        assertEquals(interactive, questions.get(0));
        assertEquals(cq1, questions.get(1));
        assertEquals(cq2, questions.get(2));
        // The position is randomly generated
        if (rq1 == questions.get(3)) {
            assertEquals(cq3, questions.get(4));
        } else if (rq1 == questions.get(4)) {
            assertEquals(cq3, questions.get(3));
        } else {
            fail();
        }

        assertEquals(cq4, questions.get(5));
        assertEquals(cq5, questions.get(6));
        assertEquals(rq2, questions.get(7));
        assertEquals(cq6, questions.get(8));
    }

    @Test
    public void shouldCorrectlyDistribute_3RQ_2CQ() throws Exception {
        recipeQuestions.add(rq1);
        recipeQuestions.add(rq2);
        recipeQuestions.add(rq3);
        curatedQuestions.add(cq1);
        curatedQuestions.add(cq2);

        final List<Implementation.Question> questions = Implementation.distributeQuestionsInList(interactive, recipeQuestions, curatedQuestions);

        System.out.println("\nshouldCorrectlyDistribute_3RQ_2CQ");
        for (Implementation.Question q : questions) {
            System.out.println(q.getName());
        }

        assertEquals(interactive, questions.get(0));

        if (cq1 == questions.get(1)) {
            assertEquals(rq1, questions.get(2));
            assertEquals(rq2, questions.get(3));
            assertEquals(rq3, questions.get(4));
        } else if (cq1 == questions.get(2)) {
            assertEquals(rq1, questions.get(1));
            assertEquals(rq2, questions.get(3));
            assertEquals(rq3, questions.get(4));
        } else if (cq1 == questions.get(3)) {
            assertEquals(rq1, questions.get(1));
            assertEquals(rq2, questions.get(2));
            assertEquals(rq3, questions.get(4));
        } else {
            fail("cq1 has an invalid position");
        }

        assertEquals(cq2, questions.get(5));
    }
}
