package treechopper.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import treechopper.common.command.TCHCommand;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.handler.EventHandler;
import treechopper.common.network.ClientSettingsMessage;
import treechopper.common.network.ServerSettingsMessage;
import treechopper.proxy.CommonProxy;

import java.io.File;

import static treechopper.core.TreeChopper.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES, guiFactory = GUI_FACTORY, acceptableRemoteVersions = "*")

public class TreeChopper {

    public static final String MOD_ID = "treechopper";
    public static final String MOD_NAME = "Tree Chopper";
    public static final String MOD_VERSION = "1.2.5";
    public static final String GUI_FACTORY = "treechopper.client.gui.GuiTCHFactory";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.0.2486,)";
    public static SimpleNetworkWrapper m_Network;

    @SidedProxy(serverSide = "treechopper.proxy.ServerProxy", clientSide = "treechopper.proxy.CommonProxy")
    private static CommonProxy commonProxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(new File(new File(event.getModConfigurationDirectory(), "treechopper"), "treechopper.cfg"));

        m_Network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        m_Network.registerMessage(ServerSettingsMessage.MsgHandler.class, ServerSettingsMessage.class, 0, Side.CLIENT);
        m_Network.registerMessage(ClientSettingsMessage.MsgHandler.class, ClientSettingsMessage.class, 1, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(commonProxy);
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TCHCommand());
    }
}
