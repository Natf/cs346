public class SuiteEntry
{
    private String name;
    private int version;
    private int votes;

    public SuiteEntry(String name, int version, int votes)
    {
        this.name = name;
        this.version = version;
        this.votes = votes;
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
}
