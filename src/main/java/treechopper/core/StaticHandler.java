package treechopper.core;

/**
 * Created by Duchy on 8/23/2016.
 */

public class StaticHandler {
    private boolean everyOk = false;
    public static boolean shiftPress;
    public static boolean serverSide;

    public boolean isEveryOk() {
        return everyOk;
    }

    public void setEveryOk(boolean everyOk) {
        this.everyOk = everyOk;
    }
}
