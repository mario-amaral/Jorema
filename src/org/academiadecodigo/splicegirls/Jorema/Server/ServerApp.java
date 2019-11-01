package org.academiadecodigo.splicegirls.Jorema.Server;

import org.academiadecodigo.splicegirls.Jorema.Utils.Messages;

public class ServerApp {

    public final static int DEFAULT_PORT = 6666;

    public static void main(String[] args) {

        int port = DEFAULT_PORT;

        try {

            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            Server JoremaServer = new Server();
            JoremaServer.startConnection(port);

        } catch (NumberFormatException ex) {

            System.out.println(Messages.COMMAND_LINE_USAGE);
            System.exit(1);

        }
    }
}
