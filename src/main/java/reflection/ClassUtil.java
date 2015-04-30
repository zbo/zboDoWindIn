package reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ClassUtil {
    private ClassUtil() {
    }

    public static void setFieldValue(Object cmdObject, Field field, Object val) {
        try {
            field.setAccessible(true);
            field.set(cmdObject, val);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Failed to set value to field: " + field.getName() + " for class: " + cmdObject.getClass().getName(), e);
        }
    }

    public static Object getFieldValue(Object cmdObject, Field field) {
        try {
            field.setAccessible(true);
            return field.get(cmdObject);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Failed to get value for field: " + field.getName() + " for class: " + cmdObject.getClass().getName(), e);
        }
    }

    public static List<Class<?>> getClassTree(Class<?> aClass) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?> theClass = aClass;
        while (theClass != null) {
            classes.add(0, theClass);
            theClass = theClass.getSuperclass();
        }
        return classes;
    }
}
