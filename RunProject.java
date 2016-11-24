import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunProject
{
    private static Server[] servers;
    private static FileSuite[] files;
    private static ArrayList<String> instructions;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 ) {
            servers = new Server[Integer.parseInt(args[0])];

            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                System.out.println("Starting server " + (i+1) +" of " + args[0]);
                servers[i] = new Server(i);
            }
        } else {
            String[] noServers = {"4"};
            main(noServers);
        }

        try {
            loadTransactions();
        } catch (Exception e) {
            System.out.println("An error occurred loading transactions: " + e);
        }

        System.out.println(instructions);

        for(int i = 0; i < instructions.size(); i++) {
            doTransaction(instructions.get(i));
        }
    }

    private static String doTransaction(String instruction) {
        ArrayList<String> transaction = parseTransaction(instruction);
        String data = "";
        for (int i = 1; i < transaction.size(); i ++) {
            if(transaction.get(i).contains("Write")) {
                write(data, transaction.get(i));
            } else if(transaction.get(i).contains("Read")) {
                String read = read(transaction.get(i));
                System.out.println("Transaction: " + transaction.get(i) + " - Returned: " + read);
                return read;
            } else {
                data += transaction.get(i);
            }
        }

        return "";
    }

    private static ArrayList<String> parseTransaction(String instruction) {
        ArrayList<String> transaction = new ArrayList<>();
        Pattern regex = Pattern.compile("(\\[([0-9]|,)*\\])");
        Matcher matcher = regex.matcher(instruction);

        if (matcher.find()) {
            transaction.add(matcher.group(0));
        }

        instruction = instruction.substring(instruction.indexOf(":")+1);
        int i = 0;
        for (String[] steps = instruction.split(";"); i < steps.length; i++) {
            transaction.add(steps[i].trim());
        }
        return transaction;
    }

    private static void loadTransactions() throws IOException {
        instructions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("trans.txt"));

        for (String instruction = reader.readLine(); instruction != null; instruction = reader.readLine()) {
            instructions.add(instruction);
        }
    }

    public static String read(String transaction) {
        return "";
    }

    public static void write(String data, String transaction) {

    }
}