package treechopper.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Duchy on 8/23/2016.
 */

public class StaticHandler {
    //private boolean everyOk = false;
    //public static boolean shiftPress = false;
    //public static boolean serverSide = false;
    //public static boolean control = false;
    public static Map<Integer, Boolean> playerHoldShift = new HashMap<Integer, Boolean>();
    public static Set<Integer> playerPrintUnName = new HashSet<Integer>();
    public static boolean printNames = false;
}
