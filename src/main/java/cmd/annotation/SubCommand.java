package cmd.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    /**
     * Name of the sub-command.
     * This must appear as-is in the command line for this sub-command to be activated.
     */
    String name();

    /**
     * The implementation class of this sub command.
     *
     * When a sub-command is selected, this class is instantiated and the rest of the arguments
     * will be parsed against this new instance.
     */
    Class<?> impl();

    String usage() default "";
}
