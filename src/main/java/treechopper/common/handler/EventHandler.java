package treechopper.common.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.internal.EntitySpawnHandler;
import treechopper.common.config.ConfigHandler;
import treechopper.common.network.ClientMessage;
import treechopper.common.network.ServerMessage;
import treechopper.core.StaticHandler;
import treechopper.core.TreeChopper;

import static treechopper.common.config.ConfigHandler.loadConfig;

/**
 * Created by Duchy on 8/11/2016.
 */

public class EventHandler {

    @SubscribeEvent
    public void onServerConnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) { // Send server settings to a player
        TreeChopper.network.sendTo(new ServerMessage(ConfigHandler.breakSpeed, ConfigHandler.ignoreDurability), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) { // Send player settings to a server
        if (event.getSide().isClient() && !StaticHandler.sended) {
            TreeChopper.network.sendToServer(new ClientMessage(ConfigHandler.reverseShift, event.getEntityPlayer().getEntityId()));
            StaticHandler.sended = true;
        }
    }

    @SubscribeEvent
    public void onServerDisconnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) { // Clear server data
        StaticHandler.playerHoldShift.remove(event.player.getEntityId());
        StaticHandler.playerPrintUnName.remove(event.player.getEntityId());

        StaticHandler.playerReverseShift.remove(event.player.getEntityId());
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) { // Reset breakSpeed after leave the server
        ConfigHandler.breakSpeed = ConfigHandler.breakSpeedVar;
        StaticHandler.sended = false;
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) { // This doesnt work in ConfigHandler, WTF
        if (event.getModID().equalsIgnoreCase(TreeChopper.MODID))
            loadConfig();
    }
}
