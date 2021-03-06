package org.academiadecodigo.splicegirls.Jorema.Server;

public class Player {

    private String name;
    private String currentAnswer;
    private int votes = 0;
    private int score = 0;
    private String myVote = "";

    public Player(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public int getVotes() {
        return votes;
    }

    public void incrementVotes() {
        votes++;
    }

    public void resetVotes(){
        votes = 0;
    }

    public int getScore() {
        return score;
    }

    public void resetScore(){
        score = 0;
    }

    public void incrementScore() {
        score++;
    }

    public String getMyVote() {
        return myVote;
    }

    public void setMyVote(String myVote) {
        this.myVote = myVote;
    }

    @Override
    public String toString() {
        return "name: " + name + "\n" +
                "current answer: " + currentAnswer + "\n" +
                "votes: " + votes + "\n" +
                "score: " + score;
    }
}
