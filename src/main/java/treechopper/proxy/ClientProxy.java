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
                event.setNewSpeed((5.7f * event.getOriginalSpeed() / ((((float) logCount) + 5f) / 0.96f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 8.0f)
                event.setNewSpeed((10.3f * event.getOriginalSpeed() / ((((float) logCount) + 10f) / 1.06f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 12.0f)
                event.setNewSpeed((16.5f * event.getOriginalSpeed() / ((((float) logCount) + 20f) / 1.26f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else
                event.setNewSpeed((26f * event.getOriginalSpeed() / ((((float) logCount) + 35f) / 1.56f)) * ((float) (ConfigHandler.breakSpeed / 10)));

        } else
            event.setNewSpeed(event.getOriginalSpeed());

    }
}
