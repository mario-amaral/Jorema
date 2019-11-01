package org.academiadecodigo.splicegirls.Jorema.Client;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

public class PromptImp implements Display {

    private Client client;
    private Prompt prompt = new Prompt(System.in, System.out);


    @Override
    public void showWelcomeMessage() {

        System.out.println(Messages.WELCOMEMESSAGE);

    }

    @Override
    public void askName() {

        StringInputScanner playerName = new StringInputScanner();
        playerName.setMessage(Messages.INSERTNAME);
        prompt.getUserInput(playerName);

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showQCard() {

    }

    @Override
    public void showVoteQuestion() {

    }

    @Override
    public void showResult() {

        

    }

    @Override
    public void showFinalResult() {

    }
}
