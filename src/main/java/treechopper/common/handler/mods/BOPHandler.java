package treechopper.common.handler.mods;

import biomesoplenty.api.enums.BOPTrees;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * Created by Duchy on 8/30/2016.
 */

@Optional.Interface(iface = "biomesoplenty.common.block", modid = "biomesoplenty")
public class BOPHandler {

    public static void plantBOPSapling(World world, BlockPos position, String leafVariant) {
        BOPTrees type = BOPTrees.valueOf(leafVariant);
        world.setBlockState(position, biomesoplenty.common.block.BlockBOPSapling.paging.getVariantState(type));
    }
}
