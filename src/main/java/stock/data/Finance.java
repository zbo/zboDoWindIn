package stock.data;

public class Finance {
    public static String spliter="--------------------------------------------";

    public Finance() {
    }

    boolean IsSB(String line) {
        return line.toString().contains("社保");
    }

    boolean IsQfii(String line) {
        return line.toString().toUpperCase().contains("QFII");
    }
}