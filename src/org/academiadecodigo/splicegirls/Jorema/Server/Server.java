package org.academiadecodigo.splicegirls.Jorema.Server;

public class Server {

    private GameLogic gameLogic;





    public Server(GameLogic gameLogic) {

        this.gameLogic = gameLogic;


    }


    private void start() {

    }


    private void setUpPlayer() {

        //gameLogic.addPlayer(name);

    }



    private class ServerWorker implements Runnable {

        @Override
        public void run() {

        }
    }
}
