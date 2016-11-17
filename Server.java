import java.io.*;
import java.net.*;


public class Server
{
    private ServerSocket serverSocket;
    private String serverAddress;
    private int serverPort;
    private BufferedReader clientIn;

    public Server(int serverPort) throws IOException
    {
        this.serverPort = serverPort;
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

    private void connectClient() throws IOException, SocketException
    {
        System.out.println("Waiting on client");
        Socket client = serverSocket.accept();
        System.out.println("Client connected");

        //Setup streams
        clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private void read() throws IOException, SocketException
    {
        String words = clientIn.readLine();
        System.out.println("Have read in from the client: ");
        System.out.println(words);
    }

    public void close() throws IOException, SocketException
    {
        System.out.println("Closing server");
        serverSocket.close();
        clientIn.close();
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