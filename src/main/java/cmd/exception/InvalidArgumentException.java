package cmd.exception;

public class InvalidArgumentException extends CmdLineException{
    public InvalidArgumentException(String s) {
        super(s);
    }

    public InvalidArgumentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
