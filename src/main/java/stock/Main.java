package stock;

import cmd.CmdLineParser;
import cmd.UsageGenerator;
import cmd.exception.CmdLineException;
import com.google.inject.Guice;
import org.apache.log4j.Logger;

import java.io.PrintStream;

import static java.lang.String.format;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.exit(new Main().process(args, System.out, "toolsPersistenceUnit"));
    }

    public int process(String[] args, PrintStream printStream, final String persistenceUnit) {
        MainCmd cmd = Guice.createInjector().getInstance(MainCmd.class);
        CmdLineParser parser = new CmdLineParser(cmd);

        try {
            parser.parse(args);

            if (cmd.isHelp() || cmd.getSubCmd() == null) {
                usage(cmd.getClass(), printStream);
            } else {
                cmd.getSubCmd().execute(printStream);
            }
            return 0;
        } catch (CmdLineException e) {
            printStream.println(format("Invalid usage: %s", e.getMessage()));
            usage(cmd.getClass(), printStream);
            return -1;
        } catch (RuntimeException e) {
            LOGGER.error("Unexpected exception: ", e);
            printStream.println("Unexpected error occurred, please check log file to see more error details.");
            return -1;
        }
    }

    private void usage(Class<?> objClass, PrintStream printStream) {
        new UsageGenerator().usage(objClass, printStream);
    }
}
