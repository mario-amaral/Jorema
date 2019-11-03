package org.academiadecodigo.splicegirls.Jorema.Client;

import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;
import org.academiadecodigo.splicegirls.Jorema.Utils.Values;
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


            gameStart(sockIn, sockOut);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



    public void gameStart(BufferedReader sockIn, DataOutputStream sockOut) {

        int currentRound = 1;

        System.out.println("GAME HAS STARTEDED");

        display.showWelcomeMessage();
        try {
            sockOut.writeBytes(display.askName());

        display.showMessage(Messages.WAITING_FOR_PLAYERS);
            System.out.println(" waiting");

        waitFor(Messages.GO_COMMAND, sockIn);
            System.out.println("out of waiting");

        while (currentRound <= Values.NUMBER_OF_ROUNDS) {

            display.showMessage("ROUND " + currentRound);

            waitFor(Messages.GO_COMMAND, sockIn);

            String answer = display.askQuestionCard(sockIn.readLine());
            sockOut.writeBytes(answer);

            display.showMessage(Messages.WAITING_FOR_ANSWERS);

            waitFor(Messages.GO_COMMAND, sockIn);

            String vote = display.askVoteQuestion(createAnswersArray(sockIn));
            sockOut.writeBytes(vote);

            display.showMessage(Messages.WAITING_FOR_VOTES);

            waitFor(Messages.GO_COMMAND, sockIn);

            display.showResult(createResultMap(sockIn));

            display.showMessage(Messages.ROUND_OVER);
            currentRound++;
        }

            System.out.println("out of the loop");
            display.showFinalResult(createWinnerList(sockIn));



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String[] createAnswersArray(BufferedReader sockIn) throws IOException {
        String[] answers = new String[Values.NUMBER_OF_PLAYERS];

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

    private LinkedList<String> createWinnerList (BufferedReader sockIn) throws IOException {

        LinkedList<String> winnerList = new LinkedList<>();
        int numOfWinners = Integer.parseInt(sockIn.readLine());

        for (int i = 0; i < numOfWinners; i++) {
            winnerList.add(sockIn.readLine());
        }

        return winnerList;
    }




    private void waitFor(String key,BufferedReader sockIn){
        try {
            while (true){
                if (sockIn.readLine().equals(key)){
                    break;
                }
            }
            display.showMessage(Messages.GO_COMMAND);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
