package cmd.option;

public class Args {
    private String[] args;
    private int pos = 0;

    public Args(String[] args) {
        this.args = args.clone();
    }

    public int size() {
        return args.length;
    }

    public String next() {
        return args[pos++];
    }

    public boolean hasNext() {
        return pos < args.length;
    }
}
