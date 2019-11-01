package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;

import java.util.HashMap;

public interface Display {

    public void showWelcomeMessage();

    public String askName();

    public void showMessage(String message);

    public String askQuestionCard(QCard qCard);

    public int askVoteQuestion();

    public void showResult(HashMap map);

    public void showFinalResult();

}
