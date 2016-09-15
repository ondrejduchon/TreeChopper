package treechopper.core;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import treechopper.proxy.ClientProxy;

import static treechopper.core.ConfigHandler.loadConfig;

/**
 * Created by Duchy on 8/11/2016.
 */

public class EventHandler {
    private TreeHandler treeHandler = new TreeHandler();

    @SubscribeEvent
    public void choppedTree(BlockEvent.BreakEvent event) {
        int logDestroyCount;
        float logCount = 0f;
        int axeDurability = 0;

        if (StaticHandler.serverSide) {

            if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

                if (event.getPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                    if (ConfigHandler.axeTypes.contains(event.getPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe
                        StaticHandler.control = true;

                        logCount = treeHandler.treeAnalyze(event.getWorld(), event.getPos());
                        axeDurability = event.getPlayer().getHeldItemMainhand().getItem().getMaxDamage() + 1 - event.getPlayer().getHeldItemMainhand().getItemDamage();

                    } else
                        StaticHandler.control = false;
                } else
                    StaticHandler.control = false;
            } else
                StaticHandler.control = false;

            if (!StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId())) {

                if (logCount > axeDurability && !ConfigHandler.ignoreDurability)
                    StaticHandler.control = false;

                if (StaticHandler.control) {
                    logDestroyCount = treeHandler.treeDestroy(event);

                    if (!event.getPlayer().isCreative())
                        event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

                    if (ConfigHandler.plantSapling) {
                        if (ConfigHandler.plantSaplingTree) {
                            event.getWorld().setBlockToAir(event.getPos());
                            event.setCanceled(true);
                        }
                        treeHandler.plantSapling(event.getWorld(), event.getPos());
                    }
                }
            }

        } else {
            if (StaticHandler.control) {

                if (!StaticHandler.shiftPress) {

                    logDestroyCount = treeHandler.treeDestroy(event);

                    if (!event.getPlayer().isCreative())
                        event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

                    if (ConfigHandler.plantSapling)
                        if (ConfigHandler.plantSaplingTree) {
                            event.getWorld().setBlockToAir(event.getPos());
                            event.setCanceled(true);
                        }
                    treeHandler.plantSapling(event.getWorld(), event.getPos());
                }
            }
        }
    }

    @SubscribeEvent
    public void interactWithTree(PlayerInteractEvent event) {
        float logCount = 0f;
        int axeDurability = 0;

        if (StaticHandler.playerPrintUnName.contains(event.getEntityPlayer().getEntityId()) && event.getSide().isServer()) { // No text formation because of forge diferences may cause error
            event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Block: " + event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName()));
            if (event.getEntityPlayer().getHeldItemMainhand() != null)
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Main hand item: " + event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName()));
            else
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Main hand item: " + "NONE"));
        }

        if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

            if (event.getEntityPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                if (ConfigHandler.axeTypes.contains(event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe
                    StaticHandler.control = true;

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
                        StaticHandler.control = false;
                        if (event.getSide().isClient()) {
                            String notEnoughDur = ChatFormatting.WHITE + "[" + ChatFormatting.GOLD + "TreeChop" + ChatFormatting.WHITE + "] Not enough durability..";
                            event.getEntityPlayer().addChatMessage(new TextComponentString(notEnoughDur));
                        }
                    }

                    if (!StaticHandler.shiftPress && StaticHandler.control) {
                        ClientProxy.logCount = (int) logCount;
                    } else {
                        ClientProxy.logCount = 0;
                    }
                } else {
                    StaticHandler.control = false;
                    ClientProxy.logCount = 0;
                }
            } else {
                StaticHandler.control = false;
                ClientProxy.logCount = 0;
            }
        } else {
            StaticHandler.control = false;
            ClientProxy.logCount = 0;
        }

        if (StaticHandler.serverSide)
            StaticHandler.playerHoldShift.put(event.getEntityPlayer().getEntityId(), event.getEntityPlayer().isSneaking());
    }

    @SubscribeEvent
    public void onServerConnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed, ConfigHandler.ignoreDurability));
    }

    @SubscribeEvent
    public void onServerDisconnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        StaticHandler.playerPrintUnName.remove(event.player.getEntityId());
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        if (!event.isLocal())
            StaticHandler.serverSide = true;
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ConfigHandler.breakSpeed = ConfigHandler.breakSpeedVar;

        StaticHandler.serverSide = false;
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) { // This doesnt work in ConfigHandler, WTF
        if (event.getModID().equalsIgnoreCase(TreeChopper.MODID))
            loadConfig();
    }
}
