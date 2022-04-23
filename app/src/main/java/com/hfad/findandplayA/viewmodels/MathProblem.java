package com.hfad.findandplayA.viewmodels;

import java.util.ArrayList;

public class MathProblem {

    private final ArrayList<Question> questionList = new ArrayList<Question>() {{
                    add(new Question("3 + 3 = ", "6"));
                    add(new Question("5 + 28 = ", "33"));
                    add(new Question("9 * 10 = ", "90"));
                    add(new Question("2\u00B2 = ", "4"));
                    add(new Question("3\u00B2", "9"));
                    add(new Question("5\u00B2 = ", "25"));
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
