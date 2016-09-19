package treechopper.core;

import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.EnumWoodType;
import forestry.api.arboriculture.ITreeRoot;
import forestry.arboriculture.IWoodTyped;
import forestry.plugins.PluginArboriculture;
import net.minecraft.block.BlockSapling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * Created by Duchy on 9/19/2016.
 */

@Optional.Interface(iface = "", modid = "forestry")
public class ForestryHandler {

    public static void plantForestrySapling(World world, BlockPos position, String leafVariant) {

    }
}
