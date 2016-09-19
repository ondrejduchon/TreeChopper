package treechopper.core;

import forestry.api.arboriculture.EnumForestryWoodType;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.core.ForestryAPI;
import forestry.arboriculture.PluginArboriculture;
import forestry.arboriculture.blocks.planks.BlockArbPlanks;
import forestry.arboriculture.blocks.planks.BlockForestryPlanks;
import forestry.arboriculture.render.SaplingStateMapper;
import forestry.core.blocks.BlockForestry;
import forestry.core.genetics.alleles.EnumAllele;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
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
