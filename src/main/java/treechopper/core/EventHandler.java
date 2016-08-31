package treechopper.core;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.sun.corba.se.spi.activation.ServerNotActive;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static treechopper.core.ConfigHandler.loadConfig;

/**
 * Created by Duchy on 8/11/2016.
 */

public class EventHandler {
    private TreeHandler treeHandler = new TreeHandler();
    private StaticHandler staticHandler = new StaticHandler();

    @SubscribeEvent
    public void choppedTree(BlockEvent.BreakEvent event) {
        int logDestroyCount;

        if (StaticHandler.serverSide) {
            float logCount = 0f;
            int axeDurability = 0;

            if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

                if (event.getPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                    if (ConfigHandler.axeTypes.contains(event.getPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe
                        staticHandler.setEveryOk(true);

                        logCount = treeHandler.treeAnalyze(event.getWorld(), event.getPos());
                        axeDurability = event.getPlayer().getHeldItemMainhand().getItem().getMaxDamage() + 1 - event.getPlayer().getHeldItemMainhand().getItemDamage();

                        if (logCount > axeDurability && !ConfigHandler.ignoreDurability && !StaticHandler.shiftPress)
                            staticHandler.setEveryOk(false);

                    } else
                        staticHandler.setEveryOk(false);
                } else
                    staticHandler.setEveryOk(false);
            } else
                staticHandler.setEveryOk(false);
        }

        if (staticHandler.isEveryOk()) {

            if (!StaticHandler.shiftPress) {

                logDestroyCount = treeHandler.treeDestroy(event);
                event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

                if (ConfigHandler.plantSapling)
                    treeHandler.plantSapling(event.getWorld(), event.getPos());
            }
        }
    }

    @SubscribeEvent
    public void interactWithTree(PlayerInteractEvent event) {
        float logCount = 0f;
        int axeDurability = 0;

        if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

            if (event.getEntityPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                if (ConfigHandler.axeTypes.contains(event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe
                    staticHandler.setEveryOk(true);

                    if (event.getSide().isClient()) {

                        if (GuiScreen.isShiftKeyDown()) {
                            TreeChopper.network.sendToServer(new ClientMessage(1));
                            StaticHandler.shiftPress = true;
                        } else {
                            TreeChopper.network.sendToServer(new ClientMessage(0));
                            StaticHandler.shiftPress = false;
                        }
                    }

                    logCount = treeHandler.treeAnalyze(event.getWorld(), event.getPos());
                    axeDurability = event.getEntityPlayer().getHeldItemMainhand().getItem().getMaxDamage() + 1 - event.getEntityPlayer().getHeldItemMainhand().getItemDamage();

                    if (logCount > axeDurability && !ConfigHandler.ignoreDurability && !StaticHandler.shiftPress) {
                        staticHandler.setEveryOk(false);
                        if (event.getSide().isClient()) {
                            String notEnoughDur = ChatFormatting.WHITE + "[" + ChatFormatting.GOLD + "TreeChop" + ChatFormatting.WHITE + "] Not enough durability..";
                            event.getEntityPlayer().addChatMessage(new TextComponentString(notEnoughDur));
                        }
                    }

                    if (!StaticHandler.shiftPress && staticHandler.isEveryOk())
                        event.getWorld().getBlockState(event.getPos()).getBlock().setHardness((2.0f * ((logCount - 1) / 3f) + 2) / (float) ConfigHandler.breakSpeed); // Hardness increase witch size of tree
                    else
                        event.getWorld().getBlockState(event.getPos()).getBlock().setHardness(2.0f); // Reset hardness
                } else {
                    staticHandler.setEveryOk(false);
                    event.getWorld().getBlockState(event.getPos()).getBlock().setHardness(2.0f);
                }

            } else {
                staticHandler.setEveryOk(false);
                event.getWorld().getBlockState(event.getPos()).getBlock().setHardness(2.0f);
            }

        } else
            staticHandler.setEveryOk(false);
    }

    @SubscribeEvent
    public void onServerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.ignoreDurability, ConfigHandler.plantSapling, ConfigHandler.decayLeaves, ConfigHandler.breakSpeed));
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        if (!event.isLocal())
            StaticHandler.serverSide = true;
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ConfigHandler.breakSpeed = ConfigHandler.breakSpeedVar;
        ConfigHandler.plantSapling = ConfigHandler.plantSaplingVar;
        ConfigHandler.decayLeaves = ConfigHandler.decayLeavesVar;
        ConfigHandler.ignoreDurability = ConfigHandler.ignoreDurabilityVar;

        StaticHandler.serverSide = false;
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) { // This doesnt work in ConfigHandler, WTF
        if (event.getModID().equalsIgnoreCase(TreeChopper.MODID))
            loadConfig();
    }
}
