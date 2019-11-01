package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;

import java.util.HashMap;
import java.util.LinkedList;

public interface Display {

    public void showWelcomeMessage();

    public String askName();

    public void showMessage(String message);

    public String askQuestionCard(QCard qcard);

    public int askVoteQuestion();

    public void showResult(HashMap<String,String> winnerMap);

    public void showFinalResult(LinkedList<String> winners);

}
