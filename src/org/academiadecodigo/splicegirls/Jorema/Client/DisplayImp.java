package org.academiadecodigo.splicegirls.Jorema.Client;
<<<<<<< HEAD
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.splicegirls.Jorema.Server.QCard;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.util.HashMap;
=======
>>>>>>> 028ee617235f68c40b83d2bfb83a9eb103d3b319

public class DisplayImp implements Display {

    private Client client;
<<<<<<< HEAD
    private Prompt prompt = new Prompt(System.in, System.out);
    private static final int NUMBER_OF_PLAYERS = 3;

=======
>>>>>>> 028ee617235f68c40b83d2bfb83a9eb103d3b319

    @Override
    public void showWelcomeMessage() {

<<<<<<< HEAD
        System.out.println(Messages.WELCOMEMESSAGE);

    }

    @Override
    public String askName() {

        StringInputScanner askName = new StringInputScanner();
        askName.setMessage(Messages.INSERTNAME);
        return prompt.getUserInput(askName);
=======
    }

    @Override
    public void askName() {
>>>>>>> 028ee617235f68c40b83d2bfb83a9eb103d3b319

    }

    @Override
    public void showMessage(String message) {

<<<<<<< HEAD


    }

    @Override
    public String askQuestionCard(QCard qCard) {

        StringInputScanner askQCard = new StringInputScanner();
        askQCard.setMessage(qCard.getMessage());
        return prompt.getUserInput(askQCard);

    }


    @Override
    public int askVoteQuestion() {

        IntegerInputScanner vote = new IntegerRangeInputScanner(1, NUMBER_OF_PLAYERS);
        vote.setError(Messages.VOTEERROR);

        vote.setMessage(Messages.VOTEQUESTION);
        return prompt.getUserInput(vote);
        

=======
    }

    @Override
    public void showQCard() {

    }

    @Override
    public void showVoteQuestion() {
>>>>>>> 028ee617235f68c40b83d2bfb83a9eb103d3b319

    }

    @Override
<<<<<<< HEAD
    public void showResult(HashMap map) {

        System.out.println(Messages.SHOWRESULT + map.getWinner);

        


    }


=======
    public void showResult() {

    }

>>>>>>> 028ee617235f68c40b83d2bfb83a9eb103d3b319
    @Override
    public void showFinalResult() {

    }
}
