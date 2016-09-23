package treechopper.proxy;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Duchy on 9/1/2016.
 */

public class CommonProxy {

    @SubscribeEvent
    public void breakSpeedPlayer(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
    }

    @SubscribeEvent
    public void interactTree(PlayerInteractEvent event) {
    }

    @SubscribeEvent
    public void destroyTree(BlockEvent.BreakEvent event) {
    }
}
