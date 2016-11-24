public class SuiteEntry
{
    private String name;
    private int version;
    private boolean versionKnown = false;
    private int votes;
    private String data = "";

    public SuiteEntry(String name, int version, int votes, String data)
    {
        this.name = name;
        this.version = version;
        this.votes = votes;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
