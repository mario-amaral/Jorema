package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;

public interface Display {

    public void showWelcomeMessage();

    public String askName();

    public void showMessage(String message);

    public String askQuestionCard(QCard qCard);

    public int askVoteQuestion();

    public void showResult();

    public void showFinalResult();

}
