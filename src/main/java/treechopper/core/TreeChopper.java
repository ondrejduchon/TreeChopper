package treechopper.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Duchy on 8/11/2016.
 */

@Mod(modid = TreeChopper.MODID, version = "1.0.2", guiFactory = "treechopper.client.gui.GuiFactory")

public class TreeChopper {
    public static SimpleNetworkWrapper network;
    public static final String MODID = "treechopper";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        ConfigHandler.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
        network.registerMessage(ClientMessage.Handler.class, ClientMessage.class, 0, Side.SERVER);
        network.registerMessage(ServerMessage.Handler.class, ServerMessage.class, 1, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
