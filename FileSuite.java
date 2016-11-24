import java.util.ArrayList;
import java.util.Arrays;

public class FileSuite
{
    private SuiteEntry[] suites;
    private int rValue;
    private int wValue;

    public FileSuite(int serverCount, int rValue, int wValue) {
        this.rValue = rValue;
        this.wValue = wValue;

        suites = new SuiteEntry[serverCount];
        for (int i = 0; i < serverCount; i++) {
            suites[i] = new SuiteEntry();
        }
    }

    public ArrayList<SuiteEntry> collectReadQuorum() {
        // until the first representative responds we don't have a seed for the voting rules
//        while (boolean firstResponded = true) { crowdLarger(); }

        SuiteEntry[] index = suites;
        ArrayList<SuiteEntry> quorum = new ArrayList<>();
        int votes = 0;

        while (true) {
            for(int i = 0; i < index.length; i++) {
                if (index[i].isVersionKnown()) {
                    quorum.add(index[i]);
                    votes += index[i].getVotes();
                    if (votes > rValue) {
                        return quorum;
                    }
                }
            }
        }
    }

    public ArrayList<SuiteEntry> collectWriteQuorum() {
        ArrayList<SuiteEntry> list = new ArrayList<>();
        list.addAll(Arrays.asList(suites));
        return list;
    }

    public void write(String data) {
        ArrayList<SuiteEntry> quorum = collectWriteQuorum();

        for (int i = 0; i < quorum.size(); i++) {
            final int quorumId = i;
            new Thread(new Runnable() {
                public void run() {
                    quorum.get(quorumId).write(data); // not sure where this is found
                }
            }).start();
        }
    }

    public String read() {
        ArrayList<SuiteEntry> quorum = collectReadQuorum();
        return quorum.get(0).read(); // not sure where this is found
    }
}