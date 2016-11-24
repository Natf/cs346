public class SuiteEntry
{
    private int version = 0;
    private boolean versionKnown = false;
    private int votes = 0;
    private String data = "";

    public SuiteEntry() {}

    public void write(String data) {
        System.out.println("Writing " + data); this.data = data;
    }

    public String read () {
        return data;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public boolean isVersionKnown() {
        return versionKnown;
    }

    public void setVersionKnown(boolean versionKnown) {
        this.versionKnown = versionKnown;
    }
}
