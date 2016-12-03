import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class RunProject
{
    private static final Scanner in = new Scanner(System.in);
    private static Server[] servers;
    private static Map<String, FileSuite> fileSuites = new LinkedHashMap<>();
    private static ArrayList<String> instructions;
    private static ArrayList<String>[] serverInstructions;
    private static int rValue;
    private static int wValue;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 ) {
            setRWValues();
            startServers(Integer.parseInt(args[0]));
        } else {
            String[] noServers = {"4"};
            main(noServers);
            return;
        }

        try {
            loadTransactions();
        } catch (Exception e) {
            System.out.println("An error occurred loading transactions: " + e);
        }

        System.out.println(instructions);
        System.out.println(servers.length);

        for(int i = 0; i < servers.length; i++) {
            System.out.println("sending transactions to server " + i);
            final int server = i;
            Thread thread = new Thread() {
                public void run() {
                    servers[server].sendTransactions(fileSuites, serverInstructions[server]);
                }
            };
            thread.start();
        }
    }

    private static void startServers(int serverCount) throws IOException {
        servers = new Server[serverCount];

        for (int i = 0; i < serverCount; i++) {
            System.out.println("Starting server " + (i+1) +" of " + serverCount);
            servers[i] = new Server(9030+i, serverCount, rValue, wValue);
        }
    }

    private static void setRWValues() {
        System.out.print("Please enter a value for Qr:");
        rValue = Integer.parseInt(in.nextLine());
        System.out.print("Please enter a value for Qw:");
        wValue = Integer.parseInt(in.nextLine());
    }

    private static void loadTransactions() throws IOException {
        instructions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("trans.txt"));

        for (String instruction = reader.readLine(); instruction != null; instruction = reader.readLine()) {
            instructions.add(instruction);
        }

        serverInstructions = new ArrayList[servers.length];
        for(int i = 0; i < servers.length; i++) {
            serverInstructions[i] = new ArrayList<>();
        }

        for (String instruction : instructions) {
            int site = Integer.parseInt(instruction.substring(instruction.indexOf(',') + 1, instruction.indexOf(']'))) - 1;
            serverInstructions[site].add(instruction);
        }
    }
}