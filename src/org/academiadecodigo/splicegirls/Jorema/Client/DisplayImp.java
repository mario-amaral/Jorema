package org.academiadecodigo.splicegirls.Jorema.Client;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.splicegirls.Jorema.Server.QCard;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

public class DisplayImp implements Display {

    private Client client;
    private Prompt prompt = new Prompt(System.in, System.out);


    @Override
    public void showWelcomeMessage() {

        System.out.println(Messages.WELCOMEMESSAGE);

    }

    @Override
    public String askName() {

        StringInputScanner askName = new StringInputScanner();
        askName.setMessage(Messages.INSERTNAME);
        return prompt.getUserInput(askName);

    }

    @Override
    public void showMessage(String message) {



    }

    @Override
    public String askQuestionCard(QCard qCard) {

        StringInputScanner askQCard = new StringInputScanner();
        askQCard.setMessage(qCard.getMessage());
        return prompt.getUserInput(askQCard);

    }


    @Override
    public int askVoteQuestion() {

        IntegerInputScanner vote = new IntegerRangeInputScanner(1, );
        vote.setError(Messages.VOTEERROR);

        vote.setMessage(Messages.VOTEQUESTION);
        return prompt.getUserInput(vote);
        


    }

    @Override
    public void showResult() {



    }

    @Override
    public void showFinalResult() {

    }
}
