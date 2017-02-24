package treechopper.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Duchy on 8/23/2016.
 */

public class StaticHandler {
    public static Map<Integer, Boolean> playerHoldShift = new HashMap<Integer, Boolean>();
    public static Set<Integer> playerPrintUnName = new HashSet<Integer>();
    public static Map<Integer, Boolean> playerReverseShift = new HashMap<Integer, Boolean>();

    public static boolean sended = false;
}
