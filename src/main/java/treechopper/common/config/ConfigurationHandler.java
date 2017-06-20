package treechopper.common.config;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.core.TreeChopper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationHandler {
    public static Configuration config;

    public static boolean decayLeaves;
    public static boolean reverseShift;
    public static boolean plantSapling;

    public static List<String> axeTypes = new ArrayList<>();
    public static List<String> blockWhiteList = new ArrayList<>();

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        try {
            decayLeaves = config.getBoolean("Decay leaves", "Settings", true, "Cut down whole tree - wooden blocks and leaves");
            reverseShift = config.getBoolean("Reverse shift", "Settings", false, "Reverse shift function - Mod works with shift pressing");
            plantSapling = config.getBoolean("Plant sapling", "Settings", false, "Automatic sapling plant on tree chop");

            axeTypes = ImmutableList.copyOf(config.getStringList("Whitelisted items", "Data", new String[]{
                    "item.hatchetWood",
                    "item.hatchetStone",
                    "item.hatchetIron",
                    "item.hatchetGold",
                    "item.hatchetDiamond",
                    "item.mud_axe",
                    "item.psi:psimetalAxe",
                    "item.tconstruct.lumberaxe",
                    "item.tconstruct.mattock",
                    "item.tconstruct.hatchet",
                    "item.brain_stone_axe",
                    "item.stable_pulsating_brain_stone_axe",
                    "item.adobe_axe",
                    "item.ObsidianAxe",
                    "item.LazuliAxe",
                    "item.OsmiumAxe",
                    "item.BronzeAxe",
                    "item.GlowstoneAxe",
                    "item.SteelAxe",
                    "item.LapisLazuliAxe",
                    "item.peridotAxe",
                    "item.rubyAxe",
                    "item.sapphireAxe",
                    "item.bronzeAxe",
                    "item.mud_axe",
                    "ic2.chainsaw",
                    "item.itemcrystalaxe",
                    "item.itemchargedcrystalaxe",
                    "item.axe_copper",
                    "item.axe_silver",
                    "item.axe_lead",
                    "item.axe_dawnstone",
                    "item.axe_aluminum",
                    "item.axe_bronze",
                    "item.axe_electrum",
                    "item.axe_nickel",
                    "item.axe_tin",
                    "item.WoodPaxel",
                    "item.StonePaxel",
                    "item.IronPaxel",
                    "item.DiamondPaxel",
                    "item.GoldPaxel",
                    "item.ObsidianPaxel",
                    "item.LapisLazuliPaxel",
                    "item.OsmiumPaxel",
                    "item.BronzePaxel",
                    "item.GlowstonePaxel",
                    "item.SteelPaxel",
                    "item.ma.inferium_axe",
                    "item.ma.prudentium_axe",
                    "item.ma.prudentium_axe",
                    "item.ma.intermedium_axe",
                    "item.ma.superium_axe",
                    "item.ma.supremium_axe",
                    "item.psimetal_axe",
                    "item.netheraxt",
                    "item.stahlaxt",
                    "item.teufelseisenaxt",
                    "item.flintAxt",
                    "item.flint_axe",
                    "item.bone_axe",
                    "item.emerald_axe",
                    "item.obsidian_axe",
                    "item.diamond_multi",
                    "item.emerald_multi",
                    "item.golden_multi",
                    "item.iron_multi",
                    "item.obsidian_multi",
                    "item.stone_multi",
                    "item.wooden_multi",
                    "item.natura.ghostwood_axe",
                    "item.natura.bloodwood_axe",
                    "item.natura.darkwood_axe",
                    "item.natura.fusewood_axe",
                    "item.natura.netherquartz_axe",
                    "item.terraAxe",
                    "item.elementiumAxe",
                    "item.manasteelAxe",
                    "item.actuallyadditions.item_axe_quartz",
                    "item.actuallyadditions.item_axe_emerald",
                    "item.actuallyadditions.item_axe_obsidian",
                    "item.actuallyadditions.item_axe_crystal_red",
                    "item.actuallyadditions.item_axe_crystal_blue",
                    "item.actuallyadditions.item_axe_crystal_light_blue",
                    "item.actuallyadditions.item_axe_crystal_black",
                    "item.actuallyadditions.item_axe_crystal_green",
                    "item.actuallyadditions.item_axe_crystal_white",
                    "item.daxe",
                    "item.aaxe",
                    "item.coraxe",
                    "item.dreadiumaxe",
                    "item.ethaxiumaxe",
                    "item.crystal_axe",
                    "item.crystal_axe_bone",
            }, "Put here allowed items(axes) - whitelist"));

            blockWhiteList = ImmutableList.copyOf(config.getStringList("Whitelisted blocks", "Data", new String[]{
                    "",
            }, "Put here allowed blocks(wood) - whitelist"));

        } catch (Exception e) {
            System.out.printf("Cannot load Tree Chopper config.");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(TreeChopper.MOD_ID)) {
            loadConfiguration();
        }
    }
}
