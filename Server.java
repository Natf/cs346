import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Server
{
    ServerSocket serverSocket;
    Socket client;
    OutputStream os;
    PrintWriter out;
    InputStream is;
    BufferedReader in;
    String phrase;

    public Server() throws IOException
    {
        try {
            //prepare file reader and buffer
            FileReader fileReader = new FileReader("server.config");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //three lines of addresses: NSW, SSW and server
            nsw = new NorthSideWrap(Integer.parseInt(bufferedReader.readLine()));
            ssw = new SouthSideWrap(Integer.parseInt(bufferedReader.readLine()));
            serverSocket = new ServerSocket(Integer.parseInt(bufferedReader.readLine()));

            //close the file
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find the client config file");
        } catch (IOException e) {
            System.out.println("File reading error");
            System.out.println("Make sure addresses are in correct format");
        }
        phrase = "Hello world!";
    }


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.connectLogger();
        server.connectClient();
        server.read();
        server.write();
        server.close();
    }

    private void connectLogger() throws IOException, SocketException {
        //need to connect the logger via NSW and SSW
        nsw.connect();
        ssw.connect();
    }

    private void connectClient() throws IOException, SocketException {
        System.out.println("Waiting on client");
        client = serverPort.accept();
        System.out.println("Client connected");

        //Setup streams
        os = client.getOutputStream();
        out = new PrintWriter(os, true);
        is = client.getInputStream();
        in = new BufferedReader(new InputStreamReader(is));

    }

    private void read() throws IOException, SocketException {
        String words = in.readLine();
        System.out.println("Have read in from the client: ");
        System.out.println(words);
    }

    private void write() throws IOException, SocketException {
        System.out.println("Directing to client: " + phrase);
        out.println(phrase);
    }

    private void close() throws IOException, SocketException {
        System.out.println("Closing server");
        out.close();
        in.close();
        os.close();
        is.close();
        client.close();
        nsw.close();
        ssw.close();
        serverPort.close();
    }
}