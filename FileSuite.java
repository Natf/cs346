import java.util.ArrayList;
import java.util.Arrays;

public class FileSuite
{
    private String suiteName;
    private int versionNumber;
    private boolean[] type;
    private SuiteEntry[] suite;
    private boolean firstResponded;
    private int rValue;
    private int wValue;

    public FileSuite(int serverCount) {
        suite = new SuiteEntry[serverCount];
        for (int i = 0; i < serverCount; i++) {
            suite[i] = new SuiteEntry();
        }
    }

    public ArrayList<SuiteEntry> collectReadQuorum() {
        // until the first representative responds we don't have a seed for the voting rules
//        while (boolean firstResponded = true) { crowdLarger(); }
        return collectWriteQuorum();

//        SuiteEntry[] index = suite;
//        ArrayList<SuiteEntry> quorum = new ArrayList<>();
//        int votes = 0;
//
//        while (true) {
//            for(int i = 0; i < index.length; i++) {
//                if (index[i].isVersionKnown()) {
//                    quorum.add(index[i]);
//                    votes += index[i].getVotes();
//                    if (votes > rValue) {
//                        return quorum;
//                    }
//                }
//            }
//        }
    }

    public ArrayList<SuiteEntry> collectWriteQuorum() {
        ArrayList<SuiteEntry> list = new ArrayList<>();
        list.addAll(Arrays.asList(suite));
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