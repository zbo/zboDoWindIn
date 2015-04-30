package stock;

import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

/**
 * Created by twer on 14-10-27.
 */
public abstract class SubCmd {
    public abstract void execute(final PrintStream out);

    protected void InsertPanToken(Map<String, String> panToken) {
        System.out.println(String.format("[Oracle]: start @ "+new Date()));
    }
}
