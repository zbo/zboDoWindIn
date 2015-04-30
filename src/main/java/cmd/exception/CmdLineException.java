package cmd.exception;

public class CmdLineException extends Exception{
    public CmdLineException(String s) {
        super(s);
    }

    public CmdLineException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
