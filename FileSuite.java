import java.util.*;

public class FileSuite
{
    private SuiteEntry[] suites;
    private boolean locked = false;
    private int rValue;
    private int wValue;

    public FileSuite(String fileId, int serverCount, int rValue, int wValue) {
        this.rValue = rValue;
        this.wValue = wValue;

        suites = new SuiteEntry[serverCount];
        for (int i = 0; i < serverCount; i++) {
            suites[i] = new SuiteEntry(9030 + i, fileId);
        }
    }

    public ArrayList<SuiteEntry> collectReadQuorum() {
        SuiteEntry[] index = suites;
        ArrayList<SuiteEntry> quorum = new ArrayList<>();
        int votes = 0;

        while (true) {
            for(int i = 0; i < index.length; i++) {
                quorum.add(index[i]);
                votes += index[i].getVotes();
                if (votes > rValue) {
                    return quorum;
                }
            }
        }
    }

    public ArrayList<SuiteEntry> collectWriteQuorum() {
        while (true) {
            ArrayList<SuiteEntry> list = new ArrayList<>();
            int readVotes = 0;
            for (SuiteEntry suite : suites) {
                readVotes += suite.getVotes();
                list.add(suite);
                if (readVotes >= wValue) {
                    return list;
                }
            }
            if (readVotes > rValue) {
                for (SuiteEntry suite : suites) {
                        // do copy
                }
            }
        }
    }

    public void write(String data) {
        ArrayList<SuiteEntry> quorum = collectWriteQuorum();

        for (int i = 0; i < quorum.size(); i++) {
            quorum.get(i).write(data);
        }

        Set<SuiteEntry> quorumSet = new HashSet<>();
        quorumSet.addAll(quorum);
        Set<SuiteEntry> suiteSet = new HashSet<>();
        suiteSet.addAll(Arrays.asList(suites));
        quorumSet.removeAll(suiteSet);
    }

    public String read() {
        ArrayList<SuiteEntry> quorum = collectReadQuorum();
        return quorum.get(0).read();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}