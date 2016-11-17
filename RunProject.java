import java.io.*;
import java.net.*;

public class RunProject
{
    private static Server[] servers;
    private static int serverPort = 9030;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 ) {
            servers = new Server[Integer.parseInt(args[0])];

            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                System.out.println("Starting server " + (i+1) +" of " + args[0]);
                servers[i] = new Server(serverPort + i);
            }
        }

        registerOnClose();

        while(true) {}
    }

    private static void registerOnClose()
    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(servers.length);
                for (int i = 0; i < servers.length; i ++) {
                    try {
                        servers[i].close();
                    } catch(Exception e) {
                        System.out.println(e);
                        System.out.println("Error shutting down server - " + (i+1));
                    }
                }
                System.out.println("Servers closed");
            }
        });
    }
}