package treechopper.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import treechopper.core.TreeChopper;

/**
 * Handles the mod's configuration file.
 *
 * TODO: Currently (1.16.2) the forge project does not have a config gui screen. There are PRs that are under review for adding
 *  it. Therefore, the only reasonable way to handle configurations is through the server command interface.
 */
@Mod.EventBusSubscriber(modid = TreeChopper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configuration {
    public static class Common {
        public final ForgeConfigSpec.BooleanValue decayLeaves;
        public final ForgeConfigSpec.BooleanValue reverseShift;
        public final ForgeConfigSpec.BooleanValue disableShift;
        public final ForgeConfigSpec.BooleanValue plantSapling;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Tree Chopper Configuration").push("common");

            decayLeaves = builder
                    .comment("Cut down whole tree - wooden blocks and leaves.")
                    .translation("command.decay_leaves_info")
                    .define("decayLeaves", true);

            reverseShift = builder
                    .comment("Reverse shift function - Mod works with shift pressing.")
                    .translation("command.reverse_shift_info")
                    .define("reverseShift", false);

            disableShift = builder
                    .comment("Disable shift function - Always chop trees regardless of shift pressing.")
                    .translation("command.disable_shift_info")
                    .define("disableShift", false);

            plantSapling = builder
                    .comment("Automatic sapling plant on tree chop.")
                    .translation("command.plant_sapling_info")
                    .define("plantSapling", false);
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common common;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        common = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            ModConfig clientConfig = event.getConfig();
        }
    }
}