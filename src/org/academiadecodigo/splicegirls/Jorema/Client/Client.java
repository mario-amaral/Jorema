package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

public class Client {

    private Socket socket;
    private Display display;
    private String serverName;
    private int serverPort;

    public Client(String serverName, int serverPort, Display display) {

        this.serverName = serverName;
        this.serverPort = serverPort;
        this.display = display;
    }

    public void startConnection(){

        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream sockOut = new DataOutputStream(socket.getOutputStream());
            gameStart(sockIn, sockOut);

        } catch (UnknownHostException e) {

            System.out.println(e.getMessage());
            System.exit(1);

        } catch (IOException ex) {

            System.out.println(Messages.CANNOT_CONNECT_TO_SERVER);
            System.exit(1);
        }
}

    public void gameStart(BufferedReader sockIn, DataOutputStream sockOut) {

        int currentRound = 1;

        int numberOfRounds = receiveNumber(sockIn);
        System.out.println("Game will have " + numberOfRounds + " rounds.");

        int numberOfPlayers = receiveNumber(sockIn);
        System.out.println(numberOfPlayers + " players will be joining in.\n");

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
}
