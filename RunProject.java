import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class RunProject
{
    private static final Scanner in = new Scanner(System.in);
    private static Server[] servers;
    private static Map<String, FileSuite> fileSuites = new LinkedHashMap<>();
    private static ArrayList<String> instructions;
    private static int rValue;
    private static int wValue;

    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 ) {
            int NumServers = Integer.parseInt(args[0]);
            setRWValues(NumServers);
            startServers(NumServers);
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

        for(int i = 0; i < instructions.size(); i++) {
            ArrayList<String> transaction = parseTransaction(instructions.get(i));
            final int server = Integer.parseInt(transaction.get(0).substring(transaction.get(0).indexOf(",") + 1, transaction.get(0).indexOf("]"))) -1;
            blockWhileLocked(instructions.get(i));
            Thread thread = new Thread() {
                public void run() {
                    servers[server].doTransaction(fileSuites, transaction);
                }
            };
            thread.start();
        }
    }

    private static void blockWhileLocked(String instruction) {
        if(instruction.contains("Write")) {
            String fileId = instruction.substring(instruction.indexOf("Write"));
            fileId = fileId.substring(fileId.indexOf("(") + 1, fileId.indexOf(")")).trim();
            if (!fileSuites.containsKey(fileId)) {
                fileSuites.put(fileId, new FileSuite(fileId, servers.length, rValue, wValue));
            }
            while(fileSuites.get(fileId).isLocked()) {}
            fileSuites.get(fileId).setLocked(true);
        }
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

    private static void startServers(int serverCount) throws IOException {
        servers = new Server[serverCount];

        for (int i = 0; i < serverCount; i++) {
            System.out.println("Starting server " + (i+1) +" of " + serverCount);
            new File("./" + (9030+i) +"/").mkdirs();
            servers[i] = new Server(9030+i, serverCount, rValue, wValue);
        }
    }

    private static void setRWValues(int quorumlimit) {
        boolean Qvalid = false;
        String QrString = "Please enter a value for Qr:";
        String QwString = "Please enter a value for Qw:";
        while(!Qvalid) {
        System.out.print(QrString);
        int rEntry = Integer.parseInt(in.nextLine());
        if (rEntry < 1){QrString = "Invalid value for Qr. Qr must be at least 1. Please enter a valid value for Qr:";}
        else if (rEntry <= quorumlimit){
        rValue = rEntry;
        Qvalid = true;
        }
        else QrString = "Invalid value for Qr. Qr cannot exceed number of servers. Please enter a valid value for Qr:";
        }
        Qvalid = false;
        while(!Qvalid) {
        System.out.print(QwString);
        int wEntry = Integer.parseInt(in.nextLine());
        if (wEntry < 1){QwString = "Invalid value for Qw. Qw must be at least 1. Please enter a valid value for Qw:";}
        else if (wEntry <= quorumlimit){
        wValue = wEntry;
        Qvalid = true;
        }
        else QwString = "Invalid value for Qw. Qw cannot exceed number of servers. Please enter a valid value for Qw:";
        }
        
        
    }

    private static void loadTransactions() throws IOException {
        instructions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("trans.txt"));

        for (String instruction = reader.readLine(); instruction != null; instruction = reader.readLine()) {
            instructions.add(instruction);
        }
    }
}