package treechopper.core;

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
    public static boolean decayLeaves = false;
    public static int breakSpeed = 1;
    public static int breakSpeedVar;

    public static List<String> logTypes = new ArrayList<String>();
    public static List<String> axeTypes = new ArrayList<String>();

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    public static void setIgnoreDurability(boolean ignoreDurability) {
        ConfigHandler.ignoreDurability = ignoreDurability;
    }

    public static void setPlantSapling(boolean plantSapling) {
        ConfigHandler.plantSapling = plantSapling;
    }

    public static void setDecayLeaves(boolean decayLeaves) {
        ConfigHandler.decayLeaves = decayLeaves;
    }

    public static void setBreakSpeed(int breakSpeed) {
        ConfigHandler.breakSpeed = breakSpeed;
    }

    public static void loadConfig() {
        Property property;

        try {
            ignoreDurability = config.get("Settings", "Ignore axe durability", false).getBoolean();

            plantSapling = config.get("Settings", "Plant sapling automatically", false).getBoolean();

            decayLeaves = config.get("Settings", "Decay leaves", true).getBoolean();

            breakSpeed = config.get("Settings", "Break speed [DEFAULT: 1]", 1).getInt();
            breakSpeedVar = breakSpeed;

            property = config.get("Data", "Log Types", new String[]{"tile.log", "tile.log_0", "tile.log_1", "tile.log_2", "tile.log_3", "tile.log_4", "tile.pamCinnamon", "tile.pamPaperbark", "tile.pamMaple"}, "Put new log between < and >");
            logTypes = ImmutableList.copyOf(property.getStringList());

            property = config.get("Data", "Axe Types", new String[]{"item.hatchetWood", "item.hatchetStone", "item.hatchetIron", "item.hatchetGold", "item.hatchetDiamond", "item.mud_axe", "item.psi:psimetalAxe"}, "Put new axe between < and >");
            axeTypes = ImmutableList.copyOf(property.getStringList());

        } catch (Exception e) {
            System.out.println("[TreeChop] Cannot load config");
        } finally {
            if (config.hasChanged())
                config.save();
        }
    }

    public static void writeConfig(boolean decayLeaves, boolean plantSapling, boolean ignoreDurability, int breakSpeed) {
        config.get("Settings", "Decay leaves", true).set(decayLeaves);
        config.get("Settings", "Ignore axe durability", false).set(ignoreDurability);
        config.get("Settings", "Plant sapling automatically", false).set(plantSapling);
        config.get("Settings", "Break speed [DEFAULT: 1]", 1).set(breakSpeed);

        config.save();
    }
}
