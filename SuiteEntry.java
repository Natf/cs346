public class SuiteEntry
{
    private int votes = 1;
    private int versionNo = 1;
    private boolean versionKnown = true;
    private String data = "";

    public SuiteEntry() {}

    public void write(String data) {
        System.out.println("Writing " + data);
        this.data = data;
        versionKnown = true;
        versionNo ++;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public boolean isVersionKnown() {
        return versionKnown;
    }

    public void setVersionKnown(boolean versionKnown) {
        this.versionKnown = versionKnown;
    }

    public String read () {
        return data;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
