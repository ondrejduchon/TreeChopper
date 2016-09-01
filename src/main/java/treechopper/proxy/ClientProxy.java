package treechopper.proxy;

import treechopper.core.ConfigHandler;

/**
 * Created by Duchy on 9/1/2016.
 */

public class ClientProxy extends CommonProxy {
    public static int logCount;

    @Override
    public void breakSpeedPlayer(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {

        System.out.println((event.getOriginalSpeed() / (logCount / 2) * ((float) ConfigHandler.breakSpeed / 1.20f)));
        //System.out.println(event.getOriginalSpeed());

        if (logCount > 1)
            event.setNewSpeed((event.getOriginalSpeed() / (logCount / 2) * ((float) ConfigHandler.breakSpeed / 1.20f)));
        else
            event.setNewSpeed(event.getOriginalSpeed());

        // TODO balance
    }
}
