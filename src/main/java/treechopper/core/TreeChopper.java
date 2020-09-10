package treechopper.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import treechopper.common.config.Configuration;

/**
 * Root of the TreeChopper mod.
 *    Registers the mod with forge
 *    Sets up and initializes the mod configuration
 *    Registers on the event bus
 */
@Mod("treechopper")
public class TreeChopper {

  public static final Logger LOGGER = LogManager.getLogger();
  public static final String MOD_ID = "treechopper";

  public TreeChopper() {
    // Register the setup method for mod loading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the doClientStuff method for mod loading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);

    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.commonSpec);
  }

  private void setup(final FMLCommonSetupEvent event){}

  private void doClientStuff(final FMLClientSetupEvent event) {}
}
