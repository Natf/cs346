import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private Socket socket;
    private String serverAddress;
    private int serverPort;
    private Scanner scanner;

    public Client()
    {
        try {
            loadConfig();
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, serverPort));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("File reading error");
            System.out.println("Make sure addresses are in correct format");
        }

        this.scanner = new Scanner(System.in);
    }

    public static void main(String args[]) throws IOException, SocketException
    {
        Client client = new Client();
        client.request();
        client.close();
    }

    public void request() throws IOException, SocketException
    {
        System.out.println("What are we sending to the server?");
        String words = scanner.nextLine();
//        out.println(words);
    }

    public void close() throws IOException, SocketException
    {
        System.out.println("Closing the client");
        socket.close();
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
                case "port":
                    this.serverPort = Integer.parseInt(parsedLine[2]);
                    break;
            }
        }

        //close the file
        bufferedReader.close();
    }
}