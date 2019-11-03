package org.academiadecodigo.splicegirls.Jorema.Client;
import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;
import org.academiadecodigo.splicegirls.Jorema.Utils.Values;
import java.io.IOException;
import java.net.UnknownHostException;

public class ClientApp {

    public static void main(String[] args) {

        String serverAddress = Values.DEFAULT_SERVER_ADDRESS;
        int port = Values.DEFAULT_SERVER_PORT;

        try {
            if (args.length > 0){
                serverAddress = args[0];
                port = Integer.parseInt(args[1]);
            }

            System.out.println("Trying to establish connection");
            Client client = new Client (serverAddress, port, new DisplayImp());

            client.startConnection();

        } catch (UnknownHostException ex) {

            System.out.println("Unknown host: " + ex.getMessage());
            System.exit(1);

        } catch (NumberFormatException ex) {

            System.out.println(Messages.CLIENT_COMMAND_LINE_USAGE);
            System.out.println("Invalid port number " + args[1]);
            System.out.print(1);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}