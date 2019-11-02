package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.splicegirls.Jorema.Server.QCard;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.util.HashMap;
import java.util.LinkedList;


public class DisplayImp implements Display {


    public DisplayImp (Client client) {
    }

    private Client client;

    private Prompt prompt = new Prompt(System.in, System.out);
    private static final int NUMBER_OF_PLAYERS = 3;



    @Override
    public void showWelcomeMessage() {

        System.out.println(Messages.WELCOME_MESSAGE);

    }

    @Override
    public String askName() {

        StringInputScanner askName = new StringInputScanner();
        askName.setMessage(Messages.INSERT_NAME);
        return prompt.getUserInput(askName);

    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);


    }

    @Override
    public String askQuestionCard(QCard qCard) {

        StringInputScanner askQCard = new StringInputScanner();
       // askQCard.setMessage(qCard.getMessage());
        return prompt.getUserInput(askQCard);

    }


    @Override
    public int askVoteQuestion() {

        IntegerInputScanner vote = new IntegerRangeInputScanner(1, NUMBER_OF_PLAYERS);
        vote.setError(Messages.VOTE_ERROR);

        vote.setMessage(Messages.VOTE_QUESTION);
        return prompt.getUserInput(vote);

<<<<<<< HEAD

=======
>>>>>>> a1e550d73acea65957248d30d63b114b8478bbfc
    }


    @Override
<<<<<<< HEAD
    public void showVoteQuestion() {


    }
=======
>>>>>>> a1e550d73acea65957248d30d63b114b8478bbfc

    public void showResult(HashMap<String,String> winnerMap) {

        System.out.println(Messages.SHOW_RESULT);

        for (String name: winnerMap.keySet()) {

            System.out.println(name);
            System.out.println(winnerMap.get(name));
        }

    }


<<<<<<< HEAD

    public void showResult() {
=======
    @Override
    public void showFinalResult(LinkedList<String> winners) {
>>>>>>> a1e550d73acea65957248d30d63b114b8478bbfc

        System.out.println(Messages.FINAL_RESULT);
        for (int i = 0; i < winners.size(); i++) {

<<<<<<< HEAD

    @Override
    public void showFinalResult() {
=======
            System.out.println(winners.get(i));
        }
>>>>>>> a1e550d73acea65957248d30d63b114b8478bbfc

        System.out.println(Messages.THANK_YOU);
    }
}
