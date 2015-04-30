package cmd;

import cmd.annotation.Argument;
import cmd.annotation.Option;
import cmd.annotation.SubCommand;
import cmd.annotation.SubCommands;
import cmd.exception.CmdLineException;
import cmd.option.Args;
import cmd.option.OptionHandlers;
import cmd.option.OptionType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static reflection.ClassUtil.*;

public class CmdLineParser {
    private Object cmdObject;

    public CmdLineParser(Object cmd) {
        cmdObject = cmd;
    }

    public void parse(String[] args) throws CmdLineException {
        parseObj(cmdObject, new Args(args));

        validate(cmdObject);
    }

    private void validate(Object cmdObject) throws CmdLineException {
        List<Class<?>> classTree = getClassTree(cmdObject.getClass());

        for (Class<?> theClass : classTree) {
            for(Field field : theClass.getDeclaredFields()) {
                validateRequiredOption(cmdObject, field);
                validateRequirdArgument(cmdObject, field);
                validateRequiredSubcommand(cmdObject, field);
            }
        }
    }

    private void validateRequiredSubcommand(Object cmdObject, Field field) throws CmdLineException {
        SubCommands subCommands = field.getAnnotation(SubCommands.class);

        if (subCommands != null) {
            Object subCommand = getFieldValue(cmdObject, field);

            if (subCommands.required() && subCommand == null) {
                throw new CmdLineException("Required sub command is missing: " + subCommands.metaVar());
            }

            if (subCommand != null) {
                validate(subCommand);
            }
        }
    }

    private void validateRequirdArgument(Object cmdObject, Field field) throws CmdLineException {
        Argument argument = field.getAnnotation(Argument.class);
        if (argument != null && argument.required() && getFieldValue(cmdObject, field) == null) {
            throw new CmdLineException("Required argument is missing: " + argument.metaVar());
        }
    }

    private void validateRequiredOption(Object cmdObject, Field field) throws CmdLineException {
        Option option = field.getAnnotation(Option.class);

        if (option != null && option.required() && getFieldValue(cmdObject, field) == null) {
            throw new CmdLineException("Required option is missing: " + option.name() + (isNullOrEmpty(option.metaVar()) ? "":" " + option.metaVar()));
        }
    }

    private void parseObj(Object cmdObject, Args args) throws CmdLineException {
        Map<String, Field> optionFieldMap = getOptions(cmdObject.getClass());
        List<Field> arguments = getArguments(cmdObject.getClass());

        int argIndex = 0;
        while(args.hasNext()) {
            String arg = args.next();
            Field field;
            OptionType optionType;

            if (arg.startsWith("-")) {
                //parse option
                String optionName = arg.contains("=") ? arg.split("=")[0] : arg;
                field = optionFieldMap.get(optionName);
                if (field == null) {
                    throw new CmdLineException("Unknown option: " + arg);
                }
                optionType = OptionType.Option;
            }else {
                //parse argument
                if (argIndex >= arguments.size()) {
                    throw new CmdLineException("Unknown argument: " + arg);
                }
                field = arguments.get(argIndex++);
                SubCommands subCommands = field.getAnnotation(SubCommands.class);
                if (subCommands != null) {
                    //subcommand argument
                    for (SubCommand subCommand : subCommands.commands()) {
                        if (subCommand.name().equalsIgnoreCase(arg)) {
                            try {
                                Object subCmdObj = subCommand.impl().newInstance();
                                setFieldValue(cmdObject, field, subCmdObj);
                                parseObj(subCmdObj, args);
                            } catch (InstantiationException | IllegalAccessException e) {
                                throw new CmdLineException(e.getMessage(), e);
                            }
                            return;
                        }
                    }

                    throw new CmdLineException("Unknown argument or command: " + arg);
                } else {
                    optionType = OptionType.Argument;
                }
            }

            Object val = OptionHandlers.handlerForType(field.getType()).parse(optionType, args, arg, field);

            setFieldValue(cmdObject, field, val);
        }
    }

    private List<Field> getArguments(Class<?> aClass) {
        List<Field> fields = new ArrayList<>();
        List<Class<?>> classTree = getClassTree(aClass);

        for (Class<?> theClass : classTree) {
            for(Field field : theClass.getDeclaredFields()) {
                SubCommands subCommands = field.getAnnotation(SubCommands.class);
                Argument argument = field.getAnnotation(Argument.class);

                if (subCommands != null || argument != null) {
                    fields.add(field);
                }
            }
        }

        return fields;
    }

    private Map<String, Field> getOptions(Class<?> aClass) {
        Map<String, Field> optionFieldMap = new HashMap<>();
        List<Class<?>> classTree = getClassTree(aClass);

        for (Class<?> theClass : classTree) {
            for(Field field : theClass.getDeclaredFields()) {
                Option option = field.getAnnotation(Option.class);

                if (option != null) {
                    optionFieldMap.put(option.name(), field);

                    for (String aliase : option.aliases()) {
                        optionFieldMap.put(aliase, field);
                    }
                }
            }
        }

        return optionFieldMap;
    }
}