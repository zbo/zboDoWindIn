package cmd.option;

import cmd.annotation.Argument;
import cmd.annotation.Option;
import cmd.annotation.SubCommands;
import cmd.exception.InvalidArgumentException;
import com.google.common.base.Joiner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

public final class OptionHandlers {
    private static final Map<Class<?>, OptionHandler> optionHandlers = new HashMap<>();

    static {
        optionHandlers.put(Boolean.class, new BooleanOptionHandler());
        optionHandlers.put(boolean.class, new BooleanOptionHandler());

        optionHandlers.put(char.class, new CharOptionHandler());
        optionHandlers.put(Character.class, new CharOptionHandler());

        optionHandlers.put(short.class, new ShortOptionHandler());
        optionHandlers.put(Short.class, new ShortOptionHandler());

        optionHandlers.put(int.class, new IntegerOptionHandler());
        optionHandlers.put(Integer.class, new IntegerOptionHandler());

        optionHandlers.put(long.class, new LongOptionHandler());
        optionHandlers.put(Long.class, new LongOptionHandler());

        optionHandlers.put(float.class, new FloatOptionHandler());
        optionHandlers.put(Float.class, new FloatOptionHandler());

        optionHandlers.put(double.class, new DoubleOptionHandler());
        optionHandlers.put(Double.class, new DoubleOptionHandler());

        optionHandlers.put(String.class, new StringOptionHandler());
        optionHandlers.put(Enum.class, new EnumOptionHandler());
    }

    private OptionHandlers() {
    }

    public static OptionHandler handlerForType(Class<?> type) {
        Class<?> type1 = type;
        if (type1.isEnum()) {
            type1 = Enum.class;
        }

        OptionHandler handler = optionHandlers.get(type1);

        if (handler == null) {
            throw new IllegalStateException("Could not find handler for type: " + type1.getName());
        }

        return handler;
    }

    private static class BooleanOptionHandler implements OptionHandler{
        private static final Set<String> trueValues = new HashSet<>(asList("true", "yes", "on"));

        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) {
            return optionType == OptionType.Option || trueValues.contains(args.next().toLowerCase());
        }
    }

    private static abstract class SingleValueOptionHandler implements OptionHandler {
        protected String argValue(OptionType option, Args args, String arg) throws InvalidArgumentException {
            if (option == OptionType.Option) {
                if (arg.contains("=")) {
                    String[] options = arg.split("=", 2);
                    if (options.length < 2) {
                        throw new InvalidArgumentException("Missing option value for: " + options[0]);
                    }

                    return options[1];
                } else {
                    if (args.hasNext()) {
                        return args.next();
                    }

                    throw new InvalidArgumentException("Missing option value for: " + arg);
                }
            }else{
                return arg;
            }
        }
    }

    private static class CharOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException {
            String val = argValue(optionType, args, arg);

            return val.charAt(0);
        }
    }

    private static class ShortOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException{
            String val = argValue(optionType, args, arg);

            return Short.parseShort(val);
        }
    }

    private static class IntegerOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException{
            String val = argValue(optionType, args, arg);

            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("Invalid value for integer option: " + val, e);
            }
        }
    }

    private static class LongOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException{
            String val = argValue(optionType, args, arg);

            try{
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("Invalid value for long option: " + val, e);
            }
        }
    }

    private static class FloatOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException{
            String val = argValue(optionType, args, arg);

            try{
                return Float.parseFloat(val);
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("Invalid value for float option: " + val, e);
            }
        }
    }

    private static class DoubleOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException{
            String val = argValue(optionType, args, arg);

            try{
                return Double.parseDouble(val);
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("Invalid value for double option: " + val, e);
            }
        }
    }

    private static class StringOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException{
            return argValue(optionType, args, arg);
        }
    }

    private static class EnumOptionHandler extends SingleValueOptionHandler{
        @Override
        public Object parse(OptionType optionType, Args args, String arg, Field field) throws InvalidArgumentException {
            String val;
            Object[] enumConstants = field.getType().getEnumConstants();
            String availableValues = Joiner.on(", ").join(enumConstants);

            try{
                val = argValue(optionType, args, arg);

                for (Object enumConstant : enumConstants) {
                    if(enumConstant.toString().equalsIgnoreCase(val)){
                        return enumConstant;
                    }
                }
            }catch(InvalidArgumentException e){
                throw new InvalidArgumentException(String.format("Missing value for %s: available values are: %s", fieldDescription(field), availableValues), e);
            }

            throw new InvalidArgumentException(String.format("Invalid value for %s: '%s', available values are: %s", fieldDescription(field), val, availableValues));
        }

        private String fieldDescription(Field field) {
            Option option = field.getAnnotation(Option.class);
            if (option != null) {
                return option.metaVar();
            }

            Argument argument = field.getAnnotation(Argument.class);
            if (argument != null) {
                return argument.metaVar();
            }

            SubCommands subCommands = field.getAnnotation(SubCommands.class);
            if (subCommands != null) {
                return subCommands.metaVar();
            }

            return "";
        }
    }
}
