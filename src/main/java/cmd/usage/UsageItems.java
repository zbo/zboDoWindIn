package cmd.usage;

import java.util.ArrayList;
import java.util.List;

public class UsageItems {
    private List<UsageItem> optionItems = new ArrayList<>();
    private List<UsageItem> argumentItems = new ArrayList<>();
    private int maxNameLength;

    public void addOption(UsageItem usageItem) {
        optionItems.add(usageItem);
        if (usageItem.getName().length() > maxNameLength) {
            maxNameLength = usageItem.getName().length();
        }
    }

    public int maxNameLength() {
        return maxNameLength;
    }

    public void addArgument(UsageItem usageItem) {
        argumentItems.add(usageItem);
        if (usageItem.getName().length() > maxNameLength) {
            maxNameLength = usageItem.getName().length();
        }
    }

    public List<UsageItem> getArgumentItems() {
        return argumentItems;
    }

    public List<UsageItem> getOptionItems() {
        return optionItems;
    }
}
