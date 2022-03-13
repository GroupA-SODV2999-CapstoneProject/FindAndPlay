package com.hfad.findandplayA.viewmodels;

import java.util.ArrayList;

public class MathProblem {

    private final ArrayList<Question> questionList = new ArrayList<Question>() {{
                    add(new Question("3 + (3 * 3) / 3 = ", "3"));
                    add(new Question("(5 + 28) / 5 - 2 = ", "11"));
                    add(new Question("9 * 11 + 10 = ", "109"));
                    add(new Question("2\u00B2 + 3\u00B2 = ", "13"));
                    add(new Question("3\u00B2 + " + "3\u00B2 = ", "18"));
                    add(new Question("4\u00B2 + " + "3\u00B2 = ", "25"));
                    add(new Question("5\u00B2 + " + "5\u00B2 = ", "50"));
                    add(new Question("5\u00B3 = ", "125"));
                    add(new Question("2\u00B3 = ", "16"));
    }};

    public Question getMathProblem() {
        int index = (int) (Math.random() * questionList.size());
        return questionList.get(index);
    }

    public static class Question {
        public String prompt;
        public String answer;

        public Question(String prompt, String answer) {
            this.prompt = prompt;
            this.answer = answer;
        }
    }
}
