package cmd.option;


import cmd.exception.InvalidArgumentException;

import java.lang.reflect.Field;

public interface OptionHandler {
    Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException;
}
