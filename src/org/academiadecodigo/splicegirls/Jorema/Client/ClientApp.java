package org.academiadecodigo.splicegirls.Jorema.Client;

public class ClientApp {

    public static void main(String[] args) {

//        if (args.length != 2) {
//            System.out.println("Usage: java ChatClient <host> <port>");
//            System.exit(1);
//        }

        try {

            System.out.println("Trying to establish connection");
            Client client = new Client ("localhost", 6666, new DisplayImp());

            client.init();


        } catch (NumberFormatException ex) {

            System.out.println("Invalid port number " + args[1]);
            System.out.print(1);

        }
    }

}
