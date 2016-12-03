import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class SuiteEntry
{
    private int votes = 1;
    private String filename;

    public SuiteEntry(int serverId, String id) {
        this.filename = "./" + serverId + "/" + id + ".txt";
    }

    public void write(String data) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(data);
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public String read () {
        String read ="";
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            read = in.readLine();
            in.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return read;
    }

    public int getVotes() {
        return votes;
    }
}
