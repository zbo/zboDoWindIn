package stock;

import cmd.annotation.Option;
import com.google.inject.Inject;
import stock.data.WindInDataGraber;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by twer on 15-4-30.
 */
public class WindInDataCmd extends SubCmd{
    @Option(name = "-m", aliases = "--market-type", usage = "market class name", metaVar = "<market type>", required = true)
    private String market;

    @Inject
    private WindInDataGraber cmd;
    @Override
    public void execute(PrintStream out) {
        System.out.println("run WindIn");
        try {
            cmd.grab(market);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
