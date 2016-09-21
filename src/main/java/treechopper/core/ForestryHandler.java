package treechopper.core;

import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Optional;

/**
 * Created by Duchy on 9/19/2016.
 */

@Optional.Interface(iface = "", modid = "forestry")
public class ForestryHandler {

    public static void plantForestrySapling(World world, BlockPos position, String leafVariant, BlockEvent.BreakEvent event) {

        ITree tree = TreeManager.treeRoot.getMember(event.getState().getBlock().getPickBlock(event.getState(), null, world, position, null));
        TreeManager.treeRoot.plantSapling(world, tree, event.getPlayer().getGameProfile(), position);
    }
}
