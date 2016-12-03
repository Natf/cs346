import java.util.ArrayList;

public class FileSuite
{
    private SuiteEntry[] suites;
    private int currentVersion = 1;
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
        while (true) {
            ArrayList<SuiteEntry> list = new ArrayList<>();
            int readVotes = 0;
            for (SuiteEntry suite : suites) {
                if (suite.isVersionKnown()) {
                    readVotes += suite.getVotes();
                    if (suite.getVersionNo() == currentVersion) {
                        list.add(suite);
                        if (readVotes >= wValue) {
                            currentVersion++;
                            return list;
                        }
                    }
                }
            }
            if (readVotes > rValue) {
                for (SuiteEntry suite : suites) {
                    if (suite.getVersionNo() != currentVersion) {
                        suite.setVersionKnown(false);
                        // do copy
                    }
                }
            }
        }
    }

    public void write(String data) {
        ArrayList<SuiteEntry> quorum = collectWriteQuorum();

        for (int i = 0; i < quorum.size(); i++) {
            quorum.get(i).write(data); // not sure where this is found
        }
    }

    public String read() {
        ArrayList<SuiteEntry> quorum = collectReadQuorum();
        return quorum.get(0).read(); // not sure where this is found
    }
}