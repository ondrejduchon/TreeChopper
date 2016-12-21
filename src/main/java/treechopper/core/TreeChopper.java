package treechopper.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import treechopper.common.command.Commands;
import treechopper.common.config.ConfigHandler;
import treechopper.common.handler.EventHandler;
import treechopper.common.network.ServerMessage;
import treechopper.proxy.CommonProxy;

/**
 * Created by Duchy on 8/11/2016.
 */

@Mod(modid = TreeChopper.MODID, version = TreeChopper.VERSION, dependencies = "required-after:forge@[13.19.0.2130,)", guiFactory = "treechopper.client.gui.GuiFactory")

public class TreeChopper {
    public static SimpleNetworkWrapper network;
    public static final String MODID = "treechopper";
    public static final String VERSION = "1.1.2";

    public static boolean BoPPresent = false;
    public static boolean ForestryPresent = false;

    @Mod.Instance(MODID)
    public static TreeChopper instance;

    @SidedProxy(clientSide = "treechopper.proxy.ClientProxy", serverSide = "treechopper.proxy.ServerProxy")
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        BoPPresent = Loader.isModLoaded("biomesoplenty");
        ForestryPresent = Loader.isModLoaded("forestry");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("treechopperCH");
        network.registerMessage(ServerMessage.Handler.class, ServerMessage.class, 1, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(proxy);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void gameStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new Commands());
    }

}
