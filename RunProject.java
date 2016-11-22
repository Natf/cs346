import java.io.IOException;

public class RunProject
{
    private static Server[] servers;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 ) {
            servers = new Server[Integer.parseInt(args[0])];

            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                System.out.println("Starting server " + (i+1) +" of " + args[0]);
                servers[i] = new Server(i);
            }
        }

        while(true) {}
    }

    public static void write() {

    }
}