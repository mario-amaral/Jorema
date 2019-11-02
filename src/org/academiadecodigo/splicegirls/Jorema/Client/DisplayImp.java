package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.splicegirls.Jorema.Server.QCard;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.util.HashMap;
import java.util.LinkedList;


public class DisplayImp implements Display {


    private Prompt prompt = new Prompt(System.in, System.out);



    @Override
    public void showWelcomeMessage() {

        System.out.println(Messages.WELCOME_MESSAGE);

    }

    @Override
    public String askName() {

        StringInputScanner askName = new StringInputScanner();
        askName.setMessage(Messages.INSERT_NAME);
        return prompt.getUserInput(askName) + "\n";

    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);


    }

    @Override
    public String askQuestionCard(String qCard) {

        StringInputScanner askQCard = new StringInputScanner();
        askQCard.setMessage(qCard);
        return prompt.getUserInput(askQCard) + "\n";

    }


    @Override
    public String askVoteQuestion(String[] answerList) {

        MenuInputScanner vote = new MenuInputScanner(answerList);
        vote.setError(Messages.VOTE_ERROR);

        vote.setMessage(Messages.VOTE_QUESTION);

        int index = prompt.getUserInput(vote)-1;
        return answerList[index] + "\n";
    }


    public void showResult(HashMap<String,String> winnerMap) {

        System.out.println(Messages.SHOW_RESULT);

        for (String name: winnerMap.keySet()) {

            System.out.println(name);
            System.out.println(winnerMap.get(name));
        }

    }


    @Override
    public void showFinalResult(LinkedList<String> winners) {


        System.out.println(Messages.FINAL_RESULT);
        for (int i = 0; i < winners.size(); i++) {

            System.out.println(winners.get(i));
        }


        System.out.println(Messages.THANK_YOU);
    }
}