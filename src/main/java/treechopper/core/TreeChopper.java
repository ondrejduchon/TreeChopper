package treechopper.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import treechopper.common.config.ConfigurationHandler;
import treechopper.proxy.CommonProxy;

import java.io.File;

import static treechopper.core.TreeChopper.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES, guiFactory = GUI_FACTORY)

public class TreeChopper {

    public static final String MOD_ID = "treechopper";
    public static final String MOD_NAME = "Tree Chopper";
    public static final String MOD_VERSION = "1.2.0";
    public static final String GUI_FACTORY = "";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[13.20.0.2279,)";

    @SidedProxy(serverSide = "treechopper.proxy.ServerProxy", clientSide = "treechopper.proxy.CommonProxy")
    private static CommonProxy commonProxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(new File(new File(event.getModConfigurationDirectory(), "treechopper"), "treechopper.cfg"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(commonProxy);
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
