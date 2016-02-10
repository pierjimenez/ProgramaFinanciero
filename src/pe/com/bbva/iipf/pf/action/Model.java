package pe.com.bbva.iipf.pf.action;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Model {

    private static final String[] DEFAULT_VALUES = { "foot", "frog", "fat", "hand", "cat", "dog" };

    private static final List<String> values = new LinkedList<String>();

    public static void setValues(List<String> newValues) {
        values.clear();
        if (newValues != null) {
            values.addAll(newValues);
        }
    }

    public static List<String> getValues() {
        return values;
    }

    public static List<String> getDefaultValues() {
        return Arrays.asList(DEFAULT_VALUES);
    }

    public static List<String> getAvailableValues() {
        List<String> available = new LinkedList<String>();
        for (String value : DEFAULT_VALUES) {
            if (!values.contains(value)) {
                available.add(value);
            }
        }
        return available;
    }
}

