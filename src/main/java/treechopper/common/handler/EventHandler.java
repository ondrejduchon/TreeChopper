package treechopper.common.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.network.ServerSettingsMessage;
import treechopper.core.TreeChopper;

public class EventHandler {

    @SubscribeEvent
    public void OnServerConnect(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        TreeChopper.m_Network.sendTo(new ServerSettingsMessage(ConfigurationHandler.reverseShift, ConfigurationHandler.disableShift), (EntityPlayerMP) loggedInEvent.player);
    }
}
