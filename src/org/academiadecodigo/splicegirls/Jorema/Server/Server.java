package org.academiadecodigo.splicegirls.Jorema.Server;

import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStore;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private GameLogic gameLogic;
    private List<ServerWorker> workers = Collections.synchronizedList(new ArrayList<ServerWorker>());
    private QCardStore qCardStore;
    private PlayerStore playerStore;
    int connectionCount = 0;

    public  final int MINIMUM_NUMBER_OF_PLAYERS = 3;
    public final int NUMBER_OF_PLAYERS = 2;
    public final int NUMBER_OF_ROUNDS = 2;

    private volatile int playersReady = 0;
    private volatile int playersReadyToReset = 0;

    public Server(QCardStore qCardStore, PlayerStore playerStore, GameLogic gameLogic) {

        this.gameLogic = gameLogic;
        this.qCardStore = qCardStore;
        this.playerStore = playerStore;
    }

    private void start() {

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

                try {
                    // Create a new Server Worker
                    connectionCount++;
                    String name = "Player-" + connectionCount;
                    ServerWorker worker = new ServerWorker(name, clientSocket, lock);
                    workers.add(worker);

                    // Serve the client connection with a new Thread
                    Thread thread = new Thread(worker);
                    thread.setName(name);
                    thread.start();

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
        synchronized (workers) {

            for (ServerWorker worker : workers) {
                worker.send(message);
            }

        }

    }

    private class ServerWorker implements Runnable {

        // Immutable state, no need to lock
        final private String threadName; // !!
        final private Socket clientSocket;
        final private BufferedReader in;
        final private DataOutputStream out;
        final private Lock lock;

        private ServerWorker(String threadName, Socket clientSocket, Lock lock) throws IOException {

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

            //Sending the number of rounds
            send(String.valueOf(NUMBER_OF_ROUNDS));

            //Sendign the number of players
            send(String.valueOf(NUMBER_OF_PLAYERS));

            //Asking player name
            playerName = readClientLine(threadName);
            playerStore.addPlayer(playerName);

            send(checkReady());

            while (currentRound <= NUMBER_OF_ROUNDS) {

                send(qCardStore.getRandomCard());

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
                    workers.remove(this);
                    return null;
                }
            } catch (IOException e) {
                System.out.println("Receiving error on " + name + " : " + e.getMessage());
                workers.remove(this);
                if (connectionCount < MINIMUM_NUMBER_OF_PLAYERS){
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
                        if (playersReady == NUMBER_OF_PLAYERS) {
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
            LinkedList<Player> winners = gameLogic.countVotes(playerStore.getPlayerTable());

            send(String.valueOf(winners.size()));

            for (Player winner: winners) {

                send(winner.getName());
                send(winner.getCurrentAnswer());

            }
        }

        private void sendFinalResult() {
            LinkedList<Player> finalWinners = gameLogic.countScore(playerStore.getPlayerTable());

            send(String.valueOf(finalWinners.size()));

            for (Player p: finalWinners) {

                send(p.getName());
            }
        }

        private void resetReadyCounter(){

            synchronized (lock) {

                playersReadyToReset++;

                if (playersReadyToReset == NUMBER_OF_PLAYERS) {
                    playersReady = 0;
                }
            }
        }

        private String checkReady() {

            String result = readyCounter();
            resetReadyCounter();
            return result;
        }

        private void send(String message) {

            try {
                out.writeBytes(message + "\n");

            } catch (IOException ex) {
                System.out.println("Error sending message to " + threadName + " : " + ex.getMessage());
            }
        }
    }

    private class Lock {}
}
