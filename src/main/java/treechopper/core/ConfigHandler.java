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
    public static boolean ignoreDurabilityVar;

    public static boolean plantSapling = false;
    public static boolean plantSaplingVar;

    public static boolean decayLeaves = false;
    public static boolean decayLeavesVar;

    public static double breakSpeed = 1.0;
    public static double breakSpeedVar;

    public static List<String> logTypes = new ArrayList<String>();
    public static List<String> axeTypes = new ArrayList<String>();

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    public static void loadConfig() {
        Property property;

        try {
            ignoreDurability = config.get("Settings", "Ignore axe durability", false).getBoolean();
            ignoreDurabilityVar = ignoreDurability;

            plantSapling = config.get("Settings", "Plant sapling automatically", false).getBoolean();
            plantSaplingVar = plantSapling;

            //decayLeaves = config.get("Settings", "Decay leaves", false).getBoolean();
            //decayLeavesVar = decayLeaves;

            breakSpeed = config.get("Settings", "Break speed [DEFAULT: 1.0]", 1.0).getDouble();
            breakSpeedVar = breakSpeed;

            property = config.get("Data", "Log Types", new String[]{"tile.log", "tile.log_0", "tile.log_1", "tile.log_2", "tile.log_3", "tile.log_4", "tile.pamCinnamon", "tile.pamPaperbark", "tile.pamMaple"}, "Put new log between < and >");
            logTypes = ImmutableList.copyOf(property.getStringList());

            property = config.get("Data", "Axe Types", new String[]{"item.hatchetWood", "item.hatchetStone", "item.hatchetIron", "item.hatchetGold", "item.hatchetDiamond", "item.mud_axe", "item.psi:psimetalAxe"}, "Put new axe between < and >");
            axeTypes = ImmutableList.copyOf(property.getStringList());

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (config.hasChanged())
                config.save();
        }
    }
}
