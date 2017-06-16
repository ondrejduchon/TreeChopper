package treechopper.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.core.TreeChopper;

public class ConfigurationHandler {
    public static Configuration config;
    public static boolean decayLeaves;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        try {
            decayLeaves = config.getBoolean("Decay leaves", "Settings", true, "Cut down whole tree - wooden blocks and leaves");
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
