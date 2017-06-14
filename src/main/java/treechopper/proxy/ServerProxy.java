package treechopper.proxy;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import treechopper.common.handler.TreeHandler;

public class ServerProxy extends CommonProxy {

    @Override
    public void interactWithTree(PlayerInteractEvent interactEvent) {
        TreeHandler treeHandler = new TreeHandler();

        treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());
    }
}
