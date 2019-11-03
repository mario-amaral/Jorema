package org.academiadecodigo.splicegirls.Jorema.Server;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStoreImp;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStoreImp;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;
import org.academiadecodigo.splicegirls.Jorema.Utils.Values;
import java.util.LinkedList;

public class ServerApp {

    public static void main(String[] args) {

        int port = Values.DEFAULT_SERVER_PORT;
        int numberOfPlayers = Values.NUMBER_OF_PLAYERS;
        int numberOfRounds = Values.NUMBER_OF_ROUNDS;

        try {

            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
                numberOfPlayers = Integer.parseInt(args[1]);
                numberOfRounds = Integer.parseInt(args[2]);
            }

            LinkedList<QCard> qCardList = new LinkedList<>();
            QuestionList questionList = new QuestionList();
            PlayerStore playerStore = new PlayerStoreImp();
            QCardStore qCardStore = new QCardStoreImp(qCardList, questionList);
            GameLogic gameLogic = new GameLogic();
            Server JoremaServer = new Server(numberOfPlayers, numberOfRounds, qCardStore, playerStore, gameLogic);

            JoremaServer.startConnection(port);

        } catch (NumberFormatException ex) {
            System.out.println(Messages.SERVER_COMMAND_LINE_USAGE);
            System.exit(1);
        }
    }
}