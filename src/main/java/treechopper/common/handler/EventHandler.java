package treechopper.common.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import treechopper.common.config.ConfigHandler;
import treechopper.common.network.ServerMessage;
import treechopper.core.StaticHandler;
import treechopper.core.TreeChopper;

import static treechopper.common.config.ConfigHandler.loadConfig;

/**
 * Created by Duchy on 8/11/2016.
 */

public class EventHandler {

    @SubscribeEvent
    public void onServerConnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) { // Send settings to a player
        TreeChopper.network.sendTo(new ServerMessage(ConfigHandler.breakSpeed, ConfigHandler.ignoreDurability), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onServerDisconnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        StaticHandler.playerHoldShift.remove(event.player.getEntityId());
        StaticHandler.playerPrintUnName.remove(event.player.getEntityId());
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) { // Reset breakSpeed after leave the server
        ConfigHandler.breakSpeed = ConfigHandler.breakSpeedVar;
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) { // This doesnt work in ConfigHandler, WTF
        if (event.getModID().equalsIgnoreCase(TreeChopper.MODID))
            loadConfig();
    }
}
