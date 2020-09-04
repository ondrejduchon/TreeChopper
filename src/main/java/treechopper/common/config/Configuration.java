package treechopper.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import treechopper.core.TreeChopper;

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
                    .translation("command.decayLeavesInfo")
                    .define("decayLeaves", true);

            reverseShift = builder
                    .comment("Reverse shift function - Mod works with shift pressing.")
                    .translation("command.reverseShiftInfo")
                    .define("reverseShift", false);

            disableShift = builder
                    .comment("Disable shift function - Always chop trees regardless of shift pressing.")
                    .translation("command.disableShiftInfo")
                    .define("disableShift", false);

            plantSapling = builder
                    .comment("Automatic sapling plant on tree chop.")
                    .translation("command.plantSaplingInfo")
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