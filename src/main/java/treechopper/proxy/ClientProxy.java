package treechopper.proxy;

import treechopper.core.ConfigHandler;

/**
 * Created by Duchy on 9/1/2016.
 */

public class ClientProxy extends CommonProxy {
    public static int logCount;

    @Override
    public void breakSpeedPlayer(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {

        if (logCount > 1) {

            if (event.getOriginalSpeed() <= 4.0f)
                event.setNewSpeed((5.2f * event.getOriginalSpeed() / ((((float) logCount) + 5f) / 1.0f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 8.0f)
                event.setNewSpeed((9.8f * event.getOriginalSpeed() / ((((float) logCount) + 10f) / 1.1f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 12.0f)
                event.setNewSpeed((16f * event.getOriginalSpeed() / ((((float) logCount) + 20f) / 1.3f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else
                event.setNewSpeed((25.5f * event.getOriginalSpeed() / ((((float) logCount) + 35f) / 1.6f)) * ((float) (ConfigHandler.breakSpeed / 10)));

        } else
            event.setNewSpeed(event.getOriginalSpeed());
    }
}
