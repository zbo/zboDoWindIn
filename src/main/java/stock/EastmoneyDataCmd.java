package stock;

import cmd.annotation.Option;

import java.io.PrintStream;

/**
 * Created by bob.zhu on 15-4-30.
 */
public class EastmoneyDataCmd extends SubCmd{
    @Option(name = "-m", aliases = "--market-type", usage = "market class name", metaVar = "<market type>", required = true)
    private String market;
    @Override
    public void execute(PrintStream out) {
        System.out.println("run East Money");
    }
}
