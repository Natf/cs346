import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunProject
{
    private static Server[] servers;
    private static Map<String, FileSuite> fileSuites = new LinkedHashMap<>();
    private static ArrayList<String> instructions;
    private static int serverCount;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 ) {
            servers = new Server[Integer.parseInt(args[0])];
            serverCount = Integer.parseInt(args[0]);

            for (int i = 0; i < serverCount; i++) {
                System.out.println("Starting server " + (i+1) +" of " + args[0]);
                servers[i] = new Server(9030+i);
            }
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

        for(int i = 0; i < instructions.size(); i++) {
            System.out.println("carrying out transaction: " + i);
            doTransaction(instructions.get(i));
        }
    }

    private static String doTransaction(String instruction) {
        ArrayList<String> transaction = parseTransaction(instruction);
        int serverNoCharacterBeginIndex = transaction.get(0).indexOf(",") + 1;
        int serverNoCharacterEndIndex = transaction.get(0).indexOf("]");
        int serverNo = Integer.parseInt(transaction.get(0).substring(serverNoCharacterBeginIndex, serverNoCharacterEndIndex));
        String data = "";
        String read = "";

        for (int i = 1; i < transaction.size(); i ++) {
            if(transaction.get(i).contains("Write")) {
                String fileId = transaction.get(i).substring(transaction.get(i).indexOf("(") + 1, transaction.get(i).indexOf(")"));
                if (!fileSuites.containsKey(fileId)) {
                    fileSuites.put(fileId, new FileSuite(serverCount));
                }
                servers[serverNo].write(fileSuites.get(fileId), data, transaction.get(i));
            } else if(transaction.get(i).contains("Read")) {
                String fileId = transaction.get(i).substring(transaction.get(i).indexOf("(") + 1, transaction.get(i).indexOf(")"));
                if (!fileSuites.containsKey(fileId)) {
                    fileSuites.put(fileId, new FileSuite(serverCount));
                }
                read += servers[serverNo].read(fileSuites.get(fileId));
                System.out.println("read: " + read);
            } else {
                data += transaction.get(i);
            }
        }

        return read;
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
}