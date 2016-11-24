import java.util.ArrayList;

public class FileSuite
{
    private String suiteName;
    private int versionNumber;
    private boolean[] type;
    private SuiteEntry suiteEntry;
    private SuiteEntry[] suite;
    private boolean firstResponded;
    private int rValue;
    private int wValue;

    public FileSuite(String suiteName)
    {

    }

    public ArrayList<SuiteEntry> collectReadQuorum() {
        // until the first representative responds we don't have a seed for the voting rules
//        while (boolean firstResponded = true) { crowdLarger(); }

        SuiteEntry[] index = suite;
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
        return null;
    }
}