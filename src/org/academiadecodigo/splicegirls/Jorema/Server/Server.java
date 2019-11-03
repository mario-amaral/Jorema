package org.academiadecodigo.splicegirls.Jorema.Server;

import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStore;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;
import org.academiadecodigo.splicegirls.Jorema.Utils.Values;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private GameLogic gameLogic;
    private List<GameWorker> gameWorkers = Collections.synchronizedList(new ArrayList<GameWorker>());
    //private List<ErrWriter> errWriters = Collections.synchronizedList(new ArrayList<ErrWriter>());
    private QCardStore qCardStore;
    private PlayerStore playerStore;

    int connectionCount = 0;

    private QCard randomQCard;
    private volatile int playersReady = 0;
    private volatile int playersReadyToReset = 0;

    public Server(QCardStore qCardStore, PlayerStore playerStore, GameLogic gameLogic) {

        this.gameLogic = gameLogic;
        this.qCardStore = qCardStore;
        this.playerStore = playerStore;
    }


    public void startConnection(int port) {


        try {
            Lock lock = new Lock();

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started: " + serverSocket);

            while (true) {

                // Block waiting for client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player client accepted: " + clientSocket);
                connectionCount++;


                if (connectionCount > Values.NUMBER_OF_PLAYERS){
                    try {
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        out.writeBytes(Messages.ROOM_FULL);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        clientSocket.close();
                    }
                }

                try {
                    // Create a new Server Worker
                    String name = "Player-" + connectionCount;
                    GameWorker gameWorker = new GameWorker(name, clientSocket, lock);
                    //ErrWriter errWriter = new ErrWriter(name, clientSocket);

                    gameWorkers.add(gameWorker);
                   // errWriters.add(errWriter);

                    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Values.NUMBER_OF_PLAYERS * 2);

                    fixedThreadPool.submit(gameWorker);
                    //fixedThreadPool.submit(errWriter);

                } catch (IOException ex) {
                    System.out.println("Error receiving client connection: " + ex.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Unable to start server on port " + port);
        }
    }

    private void sendAll(String message) {

        // Acquire lock for safe iteration
        synchronized (gameWorkers) {

            for (GameWorker worker : gameWorkers) {
                worker.send(message);
            }
        }
    }

    private void generateRandomCard() {
        randomQCard = qCardStore.getRandomCard();
    }




    private class GameWorker implements Runnable {

        // Immutable state, no need to lock
        final private String threadName; // !!
        final private Socket clientSocket;
        final private BufferedReader in;
        final private DataOutputStream out;
        final private Lock lock;

        private GameWorker(String threadName, Socket clientSocket, Lock lock) throws IOException {

            this.threadName = threadName;
            this.clientSocket = clientSocket;
            this.lock = lock;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());

        }

        @Override
        public void run() {

            System.out.println(threadName + " is now connected.");

            serverScript();

        }

        private void serverScript() {

            String playerName = threadName; //this will be renamed below, when we receive the playername form client
            int currentRound = 1;

            send(Messages.GAME_START);

            //Sending the number of rounds
            send(String.valueOf(Values.NUMBER_OF_ROUNDS));

            //Sendign the number of players
            send(String.valueOf(Values.NUMBER_OF_PLAYERS));

            //Asking player name
            playerName = readClientLine(threadName);
            playerStore.addPlayer(playerName);

            send(checkReady());

            while (currentRound <= Values.NUMBER_OF_ROUNDS) {

                generateRandomCard();

                send(checkReady());

                send(randomQCard.getMessage());

                discardCard();

                playerStore.getPlayer(playerName).setCurrentAnswer(readClientLine(playerName));

                send(checkReady());

                sendAll(playerStore.getPlayer(playerName).getCurrentAnswer());

                playerStore.getPlayer(playerName).setMyVote(readClientLine(playerName));

                System.out.println(playerStore.getPlayer(playerName).getMyVote());

                send(checkReady());

                sendRoundResults();

                currentRound++;
            }

            sendFinalResult();
            connectionCount = 0;

        }

        private String readClientLine(String name) {

            String line = null;

            try {
                line = in.readLine();
                if (line == null || line.isEmpty()) {

                    System.out.println(threadName + " closed, exiting...");
                    in.close();
                    clientSocket.close();
                    removePlayer(name);
                    gameWorkers.remove(this);
                    return null;
                }
            } catch (IOException e) {
                System.out.println("Receiving error on " + name + " : " + e.getMessage());
                gameWorkers.remove(this);
                if (connectionCount < Values.MINIMUM_NUMBER_OF_PLAYERS){
                    sendAll(Messages.PLAYER_DISCONNECTED_NOT_ENOUGH_PLAYERS);
                    System.exit(-1);
                }
            }

            return line;
        }

        private void removePlayer(String name){

            if (name.equals(threadName)) {
                //Player not added yet, nothing to remove
                return;
            } else {
                playerStore.removePlayer(name);
            }
        }

        private String readyCounter() {

            playersReadyToReset = 0;
            synchronized (lock) {

                playersReady++;

                while (true) {

                    try {
                        if (playersReady == Values.NUMBER_OF_PLAYERS) {
                            lock.notifyAll();
                            break;
                        }
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return Messages.GO_COMMAND;
            }
        }

        private void sendRoundResults() {
            LinkedList<Player> winners = gameLogic.returnRoundWinners(playerStore.getPlayerTable());

            send(String.valueOf(winners.size()));

            for (Player winner: winners) {

                send(winner.getName());
                send(winner.getCurrentAnswer());

            }
        }

        private void sendFinalResult() {
            LinkedList<Player> finalWinners = gameLogic.returnFinalWinners(playerStore.getPlayerTable());

            send(String.valueOf(finalWinners.size()));

            for (Player p: finalWinners) {

                send(p.getName());
            }
        }

        private void resetReadyCounter(){

            synchronized (lock) {

                playersReadyToReset++;

                if (playersReadyToReset == Values.NUMBER_OF_PLAYERS) {
                    playersReady = 0;
                }
            }
        }

        private String checkReady() {

            String result = readyCounter();
            resetReadyCounter();
            return result;
        }


        private void discardCard() {

            synchronized (lock) {
                if (qCardStore.exists(randomQCard)) {
                    qCardStore.removeCard(randomQCard);
                }
            }
        }


        private void send(String message) {

            try {
                out.writeBytes(message + "\n");

            } catch (IOException ex) {
                System.out.println("Error sending message to " + threadName + " : " + ex.getMessage());
            }
        }
    }

   /* private class ErrWriter implements Runnable {
        // Immutable state, no need to lock
        final private String name;
        final private Socket clientSocket;
        final private BufferedReader in;
        final private BufferedWriter out;


        private ErrWriter(String name, Socket clientSocket) throws IOException {

            this.name = name;
            this.clientSocket = clientSocket;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        }

        @Override
        public void run() {

            System.out.println("Thread " + name + " started");

            try {


                while (!clientSocket.isClosed()) {

                    // Blocks waiting for client messages
                    String line = in.readLine();

                    if (line == null) {

                        System.out.println("Client " + name + " closed, exiting...");
                        errWriters.remove(this);

                        in.close();
                        clientSocket.close();
                        continue;

                    }

                }


            } catch (IOException ex) {
                System.out.println("Receiving error on " + name + " : " + ex.getMessage());
            }

        }

        private void send(String origClient, String message) {

            try {

                out.write(origClient + ": " + message);
                out.newLine();
                out.flush();

            } catch (IOException ex) {
                System.out.println("Error sending message to Client " + name + " : " + ex.getMessage());
            }
        }
    } */

    private class Lock {}
}
