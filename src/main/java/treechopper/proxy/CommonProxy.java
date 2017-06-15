package treechopper.proxy;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.common.handler.TreeHandler;

public class CommonProxy {

    @SubscribeEvent
    public void interactWithTree(PlayerInteractEvent interactEvent){
        TreeHandler treeHandler = new TreeHandler();

        treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());
    }
}