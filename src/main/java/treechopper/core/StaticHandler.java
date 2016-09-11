package treechopper.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Duchy on 8/23/2016.
 */

public class StaticHandler {
    private boolean everyOk = false;
    public static boolean shiftPress = false;
    public static boolean serverSide = false;
    public static boolean control = false;
    public static Map<Integer, Boolean> playerHoldShift = new HashMap<Integer, Boolean>();
    public static boolean printName = false;

    public boolean isEveryOk() {
        return everyOk;
    }

    public void setEveryOk(boolean everyOk) {
        this.everyOk = everyOk;
    }
}
