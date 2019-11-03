package org.academiadecodigo.splicegirls.Jorema.Server.Store;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;
import org.academiadecodigo.splicegirls.Jorema.Server.QuestionList;
import org.academiadecodigo.splicegirls.Jorema.Utils.Random;

import java.util.LinkedList;

public class QCardStoreImp implements QCardStore {

    private LinkedList<QCard> qCardList;

    public QCardStoreImp(LinkedList<QCard> qCardList, QuestionList qList) {

        this.qCardList = qCardList;

        for (int i = 0; i < qList.getQuestions().length; i++) {

            qCardList.add(new QCard(qList.getQuestions()[i]));

        }

    }

    @Override
    public void addCard(QCard card) {

        qCardList.add(card);

    }

    @Override
    public void removeCard(QCard usedCard) {

        qCardList.remove(usedCard);

    }

    @Override
    public QCard getCard(int index) {

        return qCardList.get(index);

    }

    @Override
    public QCard getRandomCard() {

        QCard playedCard = getCard(Random.getRandInt(0, qCardList.size()));

        removeCard(playedCard);

        return playedCard;

    }
}
