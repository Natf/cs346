import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server
{
    private ServerSocket serverSocket;
    private String serverAddress;
    private String serverLog;
    private int serverCount;
    private int rValue;
    private int wValue;

    public Server(int serverPort, int serverCount, int rValue, int wValue) throws IOException
    {
        this.rValue = rValue;
        this.wValue = wValue;
        this.serverCount = serverCount;

        try {
            loadConfig();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("File reading error");
            System.out.println("Make sure addresses are in correct format");
        }

        System.out.println("Initialising server on " + this.serverAddress + ":" + serverPort);
        this.serverSocket = new ServerSocket(serverPort, 10, InetAddress.getByName(this.serverAddress));
    }

    public void close() throws IOException, SocketException
    {
        System.out.println("Closing server");
        serverSocket.close();
    }

    private void loadConfig() throws FileNotFoundException, IOException
    {
        loadConfig("config");
    }

    private void loadConfig(String name) throws FileNotFoundException, IOException
    {
        //prepare file reader and buffer
        FileReader fileReader = new FileReader(name + ".yml");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String readLine;

        while ((readLine = bufferedReader.readLine()) != null) {
            String[] parsedLine = readLine.split("[: ]");
            switch (parsedLine[0]) {
                case "hostname":
                    this.serverAddress = parsedLine[2];
                    break;
            }
        }

        //close the file
        bufferedReader.close();
    }

    public void sendTransactions(Map<String, FileSuite> fileSuites, ArrayList<String> transactions) {
        for(String transaction : transactions) {
            doTransaction(fileSuites, parseTransaction(transaction));
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

    public String doTransaction(Map<String, FileSuite> fileSuites, ArrayList<String> transaction) {
        String read = "";
        String data = "";

        for (int i = 1; i < transaction.size(); i ++) {
            if(transaction.get(i).contains("Write")) {
                String fileId = transaction.get(i).substring(transaction.get(i).indexOf("(") + 1, transaction.get(i).indexOf(")"));
                write(fileSuites, fileId, data, transaction.get(i));
            } else if(transaction.get(i).contains("Read")) {
                String fileId = transaction.get(i).substring(transaction.get(i).indexOf("(") + 1, transaction.get(i).indexOf(")"));
                read += read(fileSuites, fileId);
                System.out.println("read: " + read);
            } else {
                data += transaction.get(i);
            }
        }
        return read;
    }

    public String read(Map<String, FileSuite> fileSuites, String fileId) {
        if (!fileSuites.containsKey(fileId)) {
            fileSuites.put(fileId, new FileSuite(serverCount, rValue, wValue));
        }
        return fileSuites.get(fileId).read();
    }

    public void write(Map<String, FileSuite> fileSuites, String fileId, String data, String transaction) {
        if (!fileSuites.containsKey(fileId)) {
            fileSuites.put(fileId, new FileSuite(serverCount, rValue, wValue));
        }
        String variable = transaction.substring(transaction.indexOf("(") + 1, transaction.indexOf(")")).trim();
        String value = data.substring(data.indexOf(variable) + variable.length() + 2).trim();
        fileSuites.get(fileId).write(value);
    }
}