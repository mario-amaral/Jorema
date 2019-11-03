package org.academiadecodigo.splicegirls.Jorema.Server;

import org.academiadecodigo.splicegirls.Jorema.Server.Store.PlayerStore;
import org.academiadecodigo.splicegirls.Jorema.Server.Store.QCardStore;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;
import org.academiadecodigo.splicegirls.Jorema.Utils.Values;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private GameLogic gameLogic;
    private List<ServerWorker> workers = Collections.synchronizedList(new ArrayList<ServerWorker>());
    private volatile int playersReady = 0;
    private volatile int playersReadyToReset = 0;
    private QCardStore qCardStore;
    private PlayerStore playerStore;


    public Server(QCardStore qCardStore, PlayerStore playerStore, GameLogic gameLogic) {

        this.gameLogic = gameLogic;
        this.qCardStore = qCardStore;
        this.playerStore = playerStore;
    }


    private void start() {


    }

    private void setUpPlayer(String name) {

//        gameLogic.addPlayer(name);
//        System.out.println();

    }

    public void startConnection(int port) {

        int connectionCount = 0;

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
//                    playerStore.addPlayer(name);


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


        //gameLogic.addPlayer(name);

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
        final private String name; // !!
        final private Socket clientSocket;
        final private BufferedReader in;
        final private DataOutputStream out;
        final private Lock lock;


        private ServerWorker(String name, Socket clientSocket, Lock lock) throws IOException {

            this.name = name;
            this.clientSocket = clientSocket;
            this.lock = lock;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());

        }

        @Override
        public void run() {

            System.out.println("Thread " + name + " started");

            serverScript();

        }

        private String readClientLine() {

            // Blocks waiting for client messages
            String line = null;

            try {
                line = in.readLine();
                if (line == null) {

                    System.out.println("Player " + name + " closed, exiting...");

                    //playerStore.removePlayer(name);
                    in.close();
                    clientSocket.close();
                    return null;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line;
        }

        private void serverScript() {

            int currentRound = 1;
            String name;

            name = readClientLine();
            playerStore.addPlayer(name);

            send(checkReady());

            while (currentRound <= Values.NUMBER_OF_ROUNDS) {

                send(qCardStore.getRandomCard());

                playerStore.getPlayer(name).setCurrentAnswer(readClientLine());

                send(checkReady());

                sendAll(playerStore.getPlayer(name).getCurrentAnswer());

                playerStore.getPlayer(name).setMyVote(readClientLine());

                System.out.println(playerStore.getPlayer(name).getMyVote());

                send(checkReady());

                sendRoundResults();

                currentRound++;
            }

            sendFinalResult();


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



        private void send(String message) {

            try {

                out.writeBytes(message + "\n");

            } catch (IOException ex) {
                System.out.println("Error sending message to Client " + name + " : " + ex.getMessage());
            }
        }


    }


    private class Lock {


    }

}
