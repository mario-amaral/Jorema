package org.academiadecodigo.splicegirls.Jorema.Server.Store;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;

public interface QCardStore {

    public void addCard(QCard card);

    public void removeCard(QCard usedCard);

    public String getCard(int index);

    public String getRandomCard();

}
