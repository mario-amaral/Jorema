package org.academiadecodigo.splicegirls.Jorema.Server;

public class QuestionList {

    private final String blank = "________";

    private String[] questions = {"When I was a kid I used to climb trees all the time. Until one day " + blank,
            "A mad scientist has created a new machine! It turns apples into " + blank,
            "My rent is too high! I have to pay for internet, water, heating, and " + blank};


    public String[] getQuestions() {
        return questions;
    }
}
