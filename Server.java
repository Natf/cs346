import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;


public class Server
{
    private ServerSocket serverSocket;
    private String serverAddress;
    private String serverFolder;

    public Server(int serverPort) throws IOException
    {
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

    public String read(FileSuite fileSuite) {
        return fileSuite.read();
    }

    public void write(FileSuite fileSuite, String data, String transaction) {
        String variable = transaction.substring(transaction.indexOf("(") + 1, transaction.indexOf(")")).trim();
        String value = data.substring(data.indexOf(variable) + variable.length() + 2).trim();
        fileSuite.write(value);
    }
}