package cmd;

import cmd.annotation.*;
import cmd.usage.UsageItem;
import cmd.usage.UsageItems;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.padEnd;
import static java.lang.String.format;

public class UsageGenerator {
    private static final int PADDING_BEFORE_USAGE = 4;

    //TODO: generate short usage example for command
    public void usage(Class<?> objClass, PrintStream printStream) {
        usage(objClass, printStream, 0);
    }

    public void usage(Class<?> objClass, PrintStream printStream, int level) {
        Usage mainUsage = objClass.getAnnotation(Usage.class);

        if (mainUsage != null) {
            printStream.println(mainUsage.value());
            printStream.println();
        }

        UsageItems usageItems = parseOptionsAndArgs(objClass);

        printUsages(printStream, usageItems.getOptionItems(), usageItems.maxNameLength(), level);
        printUsages(printStream, usageItems.getArgumentItems(), usageItems.maxNameLength(), level);

        printSubcommandsUsages(objClass, printStream, level);
    }

    private UsageItems parseOptionsAndArgs(Class<?> objClass) {
        UsageItems usageItems = new UsageItems();

        parseOptions(usageItems, objClass);
        parseArguments(usageItems, objClass);

        return usageItems;
    }

    private UsageItems parseOptions(UsageItems usageItems, Class<?> obj) {
        List<Option> options = getFieldAnnotations(obj, Option.class);

        for (Option option : options) {
            String alias = Joiner.on(", ").skipNulls().join(option.aliases());
            String names = Joiner.on(", ").skipNulls().join(
                    option.name(),
                    isNullOrEmpty(alias) ? null : alias
            );

            String item = Joiner.on(" ").skipNulls().join(
                    names,
                    option.metaVar()
            );

            String usage = Joiner.on(" ").skipNulls().join(
                    option.required() ? "(Required)" : null,
                    option.usage()
            );

            usageItems.addOption(new UsageItem(item, usage));
        }

        return usageItems;
    }

    private UsageItems parseArguments(UsageItems usageItems, Class<?> obj) {
        List<Argument> arguments = getFieldAnnotations(obj, Argument.class);

        for (Argument argument : arguments) {
            String usage = Joiner.on(" ").skipNulls().join(
                    argument.required() ? "(Required)" : null,
                    argument.usage()
            );

            usageItems.addArgument(new UsageItem(argument.metaVar(), usage));
        }

        return usageItems;
    }

    private void printUsages(PrintStream printStream, List<UsageItem> usageItems, int maxLength, int level) {
        if (usageItems.size() > 0) {
            printStream.println(Strings.repeat(" ", level * 4) + "Options:");
        }

        for (UsageItem usageItem : usageItems) {
            printStream.println(format("%s%s%s",
                    Strings.repeat(" ", level * 4),
                    padEnd(usageItem.getName(), maxLength + PADDING_BEFORE_USAGE, ' '),
                    isNullOrEmpty(usageItem.getUsage()) ? "" : ": " + usageItem.getUsage()));
        }
    }

    private void printSubcommandsUsages(Class<?> clazz, PrintStream printStream, int level) {
        List<SubCommands> subCommandsList = getFieldAnnotations(clazz, SubCommands.class);
        String prefixAlignment = Strings.repeat(" ", level * 4);

        if (subCommandsList.size() > 0) {
            printStream.println();
            printStream.println(prefixAlignment + "The available commands:");
        }

        for (SubCommands subCommands : subCommandsList) {
            for (SubCommand subCommand : subCommands.commands()) {
                printStream.println(prefixAlignment + Joiner.on(": ").skipNulls().join(subCommand.name(), subCommand.usage()));
                usage(subCommand.impl(), printStream, level + 1);
                printStream.println();
            }
        }
    }

    private <T extends Annotation> List<T> getFieldAnnotations(Class<?> objClass, Class<T> annotationClass) {
        ArrayList<T> result = new ArrayList<>();
        Class<?> theClass = objClass;

        while (theClass != null) {
            ArrayList<T> classFields = new ArrayList<>();
            for (Field field : theClass.getDeclaredFields()) {
                T annotation = field.getAnnotation(annotationClass);
                if (annotation != null) {
                    classFields.add(annotation);
                }
            }

            result.addAll(0, classFields);
            theClass = theClass.getSuperclass();
        }

        return result;
    }
}
