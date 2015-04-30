package cmd.usage;

public class UsageItem {
    private final String name;
    private final String usage;

    public UsageItem(String name, String usage) {
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }
}
