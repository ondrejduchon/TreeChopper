package treechopper.common.config;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duchy on 8/28/2016.
 */

public class ConfigHandler {
    public static Configuration config;

    public static boolean ignoreDurability = false;
    public static boolean plantSapling = false;
    public static boolean plantSaplingTree = false;
    public static boolean decayLeaves = false;
    public static int breakSpeed = 1;
    public static int breakSpeedVar;

    public static boolean roots = false;

    public static List<String> logTypes = new ArrayList<String>();
    public static List<String> axeTypes = new ArrayList<String>();

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }

        if (config.hasKey("Settings", "Break speed [DEFAULT: 1]"))
            config.renameProperty("Settings", "Break speed [DEFAULT: 1]", "Obsolete option, no effect");

    }

    public static void setIgnoreDurability(boolean ignoreDurability) {
        ConfigHandler.ignoreDurability = ignoreDurability;
    }

    public static void setPlantSapling(boolean plantSapling) {
        ConfigHandler.plantSapling = plantSapling;
    }

    public static void setPlantSaplingTree(boolean plantSaplingTree) {
        ConfigHandler.plantSaplingTree = plantSaplingTree;
    }

    public static void setDecayLeaves(boolean decayLeaves) {
        ConfigHandler.decayLeaves = decayLeaves;
    }

    public static void setBreakSpeed(int breakSpeed) {
        ConfigHandler.breakSpeed = breakSpeed;
    }

    public static void setRoots(boolean roots) {
        ConfigHandler.roots = roots;
    }

    public static void loadConfig() {
        Property property;

        try {
            ignoreDurability = config.get("Settings", "Ignore axe durability", false).getBoolean();

            plantSapling = config.get("Settings", "Plant sapling automatically", false).getBoolean();

            plantSaplingTree = config.get("Settings", "Plant sapling on tree position", false).getBoolean();

            decayLeaves = config.get("Settings", "Decay leaves", true).getBoolean();

            roots = config.get("Settings", "Look for roots", false).getBoolean();

            breakSpeed = config.get("Settings", "Break speed [DEFAULT: 10]", 10).getInt();
            breakSpeedVar = breakSpeed;

            property = config.get("Data", "Log Types", new String[]{
                    "tile.log"
                    , "tile.log_0"
                    , "tile.log_1"
                    , "tile.log_2"
                    , "tile.log_3"
                    , "tile.log_4"
                    , "tile.pamCinnamon"
                    , "tile.pamPaperbark"
                    , "tile.pamMaple"
                    , "tile.for.pile_wood"
                    , "tile.for.logs.vanilla.fireproof.1"
                    , "tile.for.logs.vanilla.fireproof.0"
                    , "tile.for.logs.fireproof.7"
                    , "tile.for.logs.fireproof.6"
                    , "tile.for.logs.fireproof.5"
                    , "tile.for.logs.fireproof.4"
                    , "tile.for.logs.fireproof.3"
                    , "tile.for.logs.fireproof.2"
                    , "tile.for.logs.fireproof.1"
                    , "tile.for.logs.fireproof.0"
                    , "tile.for.logs.7"
                    , "tile.for.logs.6"
                    , "tile.for.logs.5"
                    , "tile.for.logs.4"
                    , "tile.for.logs.3"
                    , "tile.for.logs.2"
                    , "tile.for.logs.1"
                    , "tile.for.logs.0"
                    , "ic2.rubber_wood"
            }, "Put new log between < and >");

            logTypes = ImmutableList.copyOf(property.getStringList());

            property = config.get("Data", "Axe Types", new String[]{
                    "item.hatchetWood"
                    , "item.hatchetStone"
                    , "item.hatchetIron"
                    , "item.hatchetGold"
                    , "item.hatchetDiamond"
                    , "item.mud_axe"
                    , "item.psi:psimetalAxe"
                    , "item.tconstruct.lumberaxe"
                    , "item.tconstruct.mattock"
                    , "item.tconstruct.hatchet"
                    , "item.brain_stone_axe"
                    , "item.stable_pulsating_brain_stone_axe"
            }, "Put new axe between < and >");

            axeTypes = ImmutableList.copyOf(property.getStringList());

        } catch (Exception e) {
            System.out.println("[TreeChop] Cannot load config");
        } finally {
            if (config.hasChanged())
                config.save();
        }
    }

    public static void writeConfig(boolean decayLeaves, boolean plantSapling, boolean ignoreDurability, int breakSpeed, boolean plantSaplingTree, boolean roots) {
        config.get("Settings", "Decay leaves", true).set(decayLeaves);
        config.get("Settings", "Ignore axe durability", false).set(ignoreDurability);
        config.get("Settings", "Plant sapling automatically", false).set(plantSapling);
        config.get("Settings", "Plant sapling on tree position", false).set(plantSaplingTree);
        config.get("Settings", "Break speed [DEFAULT: 10]", 10).set(breakSpeed);
        config.get("Settings", "Look for roots", false).set(roots);

        config.save();
    }
}
