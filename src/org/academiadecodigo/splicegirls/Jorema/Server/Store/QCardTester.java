package org.academiadecodigo.splicegirls.Jorema.Server.Store;

import org.academiadecodigo.splicegirls.Jorema.Server.QCard;
import org.academiadecodigo.splicegirls.Jorema.Server.QuestionList;

import java.util.LinkedList;

public class QCardTester {
    public static void main(String[] args) {

        LinkedList<QCard> qCardList = new LinkedList<QCard>();

        QuestionList questionList = new QuestionList();

        QCardStore cardStore = new QCardStoreImp(qCardList, questionList);

        System.out.println("The list has : " + qCardList.size());

        System.out.println(cardStore.getCard(1));
        System.out.println(cardStore.getCard(1));
    }
}
