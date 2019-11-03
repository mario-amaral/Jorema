package org.academiadecodigo.splicegirls.Jorema.Server.Store;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;

public interface QCardStore {

    public void addCard(QCard card);

    public void removeCard(QCard usedCard);

    public QCard getCard(int index);

    public QCard getRandomCard();

    public boolean exists(QCard card);

}
