import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

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
            serverLog = "./" + serverPort + "/ServerLog.txt";
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("File reading error");
            System.out.println("Make sure addresses are in correct format");
        }

        System.out.println("Initialising server on " + this.serverAddress + ":" + serverPort);
        this.serverSocket = new ServerSocket(serverPort, 10, InetAddress.getByName(this.serverAddress));
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
            } else {
                data += transaction.get(i);
            }
        }
        return read;
    }

    public String read(Map<String, FileSuite> fileSuites, String fileId) {
        if (!fileSuites.containsKey(fileId)) {
            fileSuites.put(fileId, new FileSuite(fileId, serverCount, rValue, wValue));
        }
        while(fileSuites.get(fileId).isLocked()) {

        }
        String value = fileSuites.get(fileId).read();
        this.log("Reading from " + fileId + ", " + value);
        return value;
    }

    public void write(Map<String, FileSuite> fileSuites, String fileId, String data, String transaction) {
        if (!fileSuites.containsKey(fileId)) {
            fileSuites.put(fileId, new FileSuite(fileId, serverCount, rValue, wValue));
        }
        fileSuites.get(fileId).setLocked(true);
        String variable = transaction.substring(transaction.indexOf("(") + 1, transaction.indexOf(")")).trim();
        String value = data.substring(data.indexOf(variable) + variable.length() + 2).trim();
        this.log("Writing to " + fileId + ", value " + value);
        fileSuites.get(fileId).write(value);
        fileSuites.get(fileId).setLocked(false);
    }

    private void log(String string) {
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File(serverLog), true ));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            writer.println(timestamp + " : " + string);
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}