package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

public class Client {

    public final static String DEFAULT_NAME = "CLIENT";

    // The client socket
    private Socket socket;
    private Display display;
    private String serverName;
    private int serverPort;

    public Client(String serverName, int serverPort, Display display) {

        this.serverName = serverName;
        this.serverPort = serverPort;
        this.display = display;
    }

    public void startConnection() throws IOException {

            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);

        try {
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream sockOut = new DataOutputStream(socket.getOutputStream());
            gameStart(sockIn, sockOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameStart(BufferedReader sockIn, DataOutputStream sockOut) {

        int currentRound = 1;

        int numberOfRounds = receiveNumber(sockIn);
        System.out.println("Game will have " + numberOfRounds + " rounds.");

        int numberOfPlayers = receiveNumber(sockIn);
        System.out.println(numberOfPlayers + " players will be joining in");

        System.out.println("GAME HAS STARTED");

        display.showWelcomeMessage();
        try {

            sendToServer(display.askName(),sockOut);

            display.showMessage(Messages.WAITING_FOR_PLAYERS);

            waitFor(Messages.GO_COMMAND, sockIn);

        while (currentRound <= numberOfRounds) {

            display.showMessage("ROUND " + currentRound);

            waitFor(Messages.GO_COMMAND, sockIn);

            String answer = display.askQuestionCard(sockIn.readLine());

            sendToServer(answer, sockOut);

            display.showMessage(Messages.WAITING_FOR_ANSWERS);

            waitFor(Messages.GO_COMMAND, sockIn);

            String vote = display.askVoteQuestion(createAnswersArray(numberOfPlayers, sockIn));

            sendToServer(vote, sockOut);

            display.showMessage(Messages.WAITING_FOR_VOTES);

            waitFor(Messages.GO_COMMAND, sockIn);

            display.showResult(createResultMap(sockIn));

            display.showMessage(Messages.ROUND_OVER);

            currentRound++;
        }

        display.showFinalResult(createWinnerList(sockIn));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToServer(String message, DataOutputStream sockOut){

        try {
            if (socket.isClosed()){
                System.out.println(Messages.SERVER_NOT_CONNECTED_ERROR);
                System.exit(-1);
            }
            sockOut.writeBytes(message);
        } catch (IOException e) {
            System.out.println(Messages.SERVER_NOT_CONNECTED_ERROR + e.getMessage());
            System.exit(-1);
            e.printStackTrace();
        }

    }

    private String[] createAnswersArray(int numberOfPlayers, BufferedReader sockIn) throws IOException {
        String[] answers = new String[numberOfPlayers];

        for (int i = 0; i < answers.length; i++) {
            answers[i] = sockIn.readLine();
        }
        return answers;
    }

    private HashMap<String, String> createResultMap(BufferedReader sockIn) throws IOException {

        int numOfWinners = Integer.parseInt(sockIn.readLine());
        HashMap<String, String> resultMap = new HashMap<>();

        for (int i = 0; i < numOfWinners; i++) {
            resultMap.put(sockIn.readLine(), sockIn.readLine());
        }
        return resultMap;
    }

    private LinkedList<String> createWinnerList (BufferedReader sockIn){

        LinkedList<String> winnerList = new LinkedList<>();
        int numOfWinners = receiveNumber(sockIn);

        for (int i = 0; i < numOfWinners; i++) {
            winnerList.add(receiveMsg(sockIn));
        }
        return winnerList;
    }

    private void waitFor(String key,BufferedReader sockIn){

            while (true){
                if (receiveMsg(sockIn).equals(key)){
                    break;
                }
            }
            display.showMessage(Messages.GO_COMMAND);
    }

    private int receiveNumber(BufferedReader sockIn){
        return Integer.parseInt(receiveMsg(sockIn));
    }

    private String receiveMsg(BufferedReader sockIn){

        String messageFromServer = null;

        try {
            messageFromServer = sockIn.readLine();
            if (messageFromServer == null){
                System.out.println(Messages.SERVER_NOT_CONNECTED_ERROR);
                System.exit(-1);
            }

            if (messageFromServer.equals(Messages.PLAYER_DISCONNECTED_NOT_ENOUGH_PLAYERS)){
                System.out.println(Messages.PLAYER_DISCONNECTED_NOT_ENOUGH_PLAYERS);
                System.exit(-1);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Messages.SERVER_NOT_CONNECTED_ERROR);
            System.exit(-1);
        }
        return messageFromServer;
    }

/*
    // Starts handling messages
    private void startConnection() {

        // Creates a new thread to handle incoming server messages
        Thread thread = new Thread(new ChatRunnable());
        thread.start();

        try {

            BufferedWriter sockOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            while (!socket.isClosed()) {

                String consoleMessage = null;

                try {

                    // Blocks waiting for user input
                    consoleMessage = consoleIn.readLine();

                } catch (IOException ex) {
                    System.out.println("Error reading from console: " + ex.getMessage());
                    break;
                }

                if (consoleMessage == null || consoleMessage.equals("/quit")) {
                    break;
                }


                sockOut.write(consoleMessage);
                sockOut.newLine();
                sockOut.flush();

            }

            try {

                consoleIn.close();
                sockOut.close();
                socket.close();

            } catch (IOException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }

        } catch (IOException ex) {

            System.out.println("Error sending message to server: " + ex.getMessage());

        }
    }

    // Runnable to handle incoming messages from the server
    private class ChatRunnable implements Runnable {

        @Override
        public void run() {

            try {

                BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (!socket.isClosed()) {

                    // Block waiting for incoming messages from server
                    String incomingMessage = sockIn.readLine();

                    if (incomingMessage != null) {

                        System.out.println(incomingMessage);

                    } else {

                        try {

                            System.out.println("Connection closed, exiting...");
                            sockIn.close();
                            socket.close();

                        } catch (IOException ex) {
                            System.out.println("Error closing connection: " + ex.getMessage());
                        }

                    }

                }
            } catch (SocketException ex) {
                // Socket closed by other thread, no need for special handling
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
            }

            // Server closed, but main thread blocked in console readline
            System.exit(0);

        }
    }
*/
}
