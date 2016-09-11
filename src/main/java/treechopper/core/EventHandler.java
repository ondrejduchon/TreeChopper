package treechopper.core;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
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

        if (StaticHandler.serverSide) {

            if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

                if (event.getPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                    if (ConfigHandler.axeTypes.contains(event.getPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe
                        StaticHandler.control = true;

                        if (StaticHandler.control)
                            treeHandler.treeAnalyze(event.getWorld(), event.getPos());

                    } else
                        StaticHandler.control = false;
                } else
                    StaticHandler.control = false;
            } else
                StaticHandler.control = false;

            if (StaticHandler.control) {

                if (!StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId())) {

                    logDestroyCount = treeHandler.treeDestroy(event);

                    if (!event.getPlayer().isCreative())
                        event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

                    if (ConfigHandler.plantSapling)
                        treeHandler.plantSapling(event.getWorld(), event.getPos());
                }
            }
        } else {
            if (StaticHandler.control) {

                if (!StaticHandler.shiftPress) {

                    logDestroyCount = treeHandler.treeDestroy(event);

                    if (!event.getPlayer().isCreative())
                        event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

                    if (ConfigHandler.plantSapling)
                        treeHandler.plantSapling(event.getWorld(), event.getPos());
                }
            }
        }
    }

    @SubscribeEvent
    public void interactWithTree(PlayerInteractEvent event) {

        if (StaticHandler.printName && event.getSide().isClient()) {
            event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Block: " + ChatFormatting.GOLD + event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName()));
            if (event.getEntityPlayer().getHeldItemMainhand() != null)
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Main hand item: " + ChatFormatting.GOLD + event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName()));
        }

        float logCount = 0f;
        int axeDurability = 0;

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
        TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed));
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
