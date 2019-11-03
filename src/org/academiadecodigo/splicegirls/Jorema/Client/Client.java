package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Utils.ErrorMessages;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public final static String DEFAULT_NAME = "CLIENT";

    // The client socket
    private Socket socket;
    private Display display;

    public Client(String serverName, int serverPort, Display display) {

        this.display = display;

        try {

            // Connect to server
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            //startConnection();

        } catch (UnknownHostException ex) {

            System.out.println("Unknown host: " + ex.getMessage());
            System.exit(1);

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }


    public void init() {

        System.out.println("INIT HAS BEGUN");

        try {
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream sockOut = new DataOutputStream(socket.getOutputStream());
            ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

            singleExecutor.submit(new MessageReader());

            gameStart(sockIn, sockOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameStart(BufferedReader sockIn, DataOutputStream sockOut) {

        int currentRound = 1;

        String checkRoom = "";
        String unTested = readLine(sockIn);
        if (isValidMsg(unTested)){
            checkRoom = receiveMsg(unTested);
        }

        System.out.println(checkRoom);


        int numberOfRounds = 1;
        String numOfRoundsStrg = readLine(sockIn);
        if (isValidMsg(numOfRoundsStrg)){
            numberOfRounds = receiveNumber(numOfRoundsStrg);
        }

        System.out.println("Game will have " + numberOfRounds + " rounds.");


        int numOfPlayers = 1;
        String numOfPlayersStrg = readLine(sockIn);
        if (isValidMsg(numOfPlayersStrg)){
            numOfPlayers = receiveNumber(numOfPlayersStrg);
        }


        System.out.println(numberOfRounds + " players will be joining in");

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

            String vote = display.askVoteQuestion(createAnswersArray(numberOfRounds, sockIn));

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

    private String readLine(BufferedReader sockIn){
        try {
            return sockIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Messages.SERVER_NOT_CONNECTED_ERROR);
            System.exit(-1);
        }
        return null;
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

    private String[] createAnswersArray(int numOfPlayers, BufferedReader sockIn) {
        String[] answers = new String[numOfPlayers];

        for (int i = 0; i < answers.length; i++) {

            String answer;
            String unTested = readLine(sockIn);
            if (isValidMsg(unTested)){
                answer = receiveMsg(unTested);
            } else {
                i--;
                continue;
            }

            answers[i] = answer;
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
                String message = receiveMsg(readLine(sockIn));
                if (message.equals(key)){
                    break;
                }
            }
    }

    private int receiveNumber(String serverMsg){
        return Integer.parseInt(receiveMsg(serverMsg));
    }

    private String receiveMsg(String serverMsg){


            if (serverMsg == null){
                System.out.println(Messages.SERVER_NOT_CONNECTED_ERROR);
                System.exit(-1);
            }
            if (serverMsg.equals(Messages.PLAYER_DISCONNECTED_NOT_ENOUGH_PLAYERS)){
                System.out.println(Messages.PLAYER_DISCONNECTED_NOT_ENOUGH_PLAYERS);
                System.exit(-1);
            }

        return serverMsg;
    }

    private boolean isValidMsg(String serverMsg) {

        if (serverMsg.startsWith(ErrorMessages.ERR)
                || serverMsg.startsWith(ErrorMessages.TIME)) {
            return false;
        }
        return true;
    }


    private class MessageReader implements Runnable {

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

}
