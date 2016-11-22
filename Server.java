import java.io.*;
import java.net.*;


public class Server
{
    private ServerSocket serverSocket;
    private String serverAddress;
    private static final int startingServerPort = 9030;
    private int serverPort;
    private int serverId;

    public Server(int serverId) throws IOException
    {
        this.serverId = serverId;
        this.serverPort = startingServerPort + serverId;
        try {
            loadConfig();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("File reading error");
            System.out.println("Make sure addresses are in correct format");
        }

        System.out.println("Initialising server on " + this.serverAddress + ":" + this.serverPort);
        this.serverSocket = new ServerSocket(this.serverPort, 10, InetAddress.getByName(this.serverAddress));
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
}