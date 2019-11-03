package org.academiadecodigo.splicegirls.Jorema.Server;

import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStoreImp;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStoreImp;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.util.LinkedList;

public class ServerApp {

    public final static int DEFAULT_PORT = 6666;

    public static void main(String[] args) {

        int port = DEFAULT_PORT;

        try {

            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            LinkedList<QCard> qCardList = new LinkedList<QCard>();
            QuestionList questionList = new QuestionList();

            PlayerStore playerStore = new PlayerStoreImp();
            QCardStore qCardStore = new QCardStoreImp(qCardList, questionList);
            GameLogic gameLogic = new GameLogic();


            Server JoremaServer = new Server(qCardStore, playerStore, gameLogic);
            JoremaServer.startConnection(port);

        } catch (NumberFormatException ex) {

            System.out.println(Messages.COMMAND_LINE_USAGE);
            System.exit(1);

        }
    }
}
