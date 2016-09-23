package treechopper.proxy;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import treechopper.common.config.ConfigHandler;
import treechopper.common.handler.TreeHandler;
import treechopper.core.StaticHandler;

/**
 * Created by Duchy on 9/1/2016.
 */

public class ClientProxy extends CommonProxy {
    public static int logCount;
    private TreeHandler treeHandler = new TreeHandler();

    @Override
    public void breakSpeedPlayer(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {

        if (logCount > 1) {

            if (event.getOriginalSpeed() <= 4.0f)
                event.setNewSpeed((5.7f * event.getOriginalSpeed() / ((((float) logCount) + 5f) / 0.96f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 8.0f)
                event.setNewSpeed((10.3f * event.getOriginalSpeed() / ((((float) logCount) + 10f) / 0.4f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 12.0f)
                event.setNewSpeed((16.5f * event.getOriginalSpeed() / ((((float) logCount) + 20f) / 0.2f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else
                event.setNewSpeed((26f * event.getOriginalSpeed() / ((((float) logCount) + 35f) / 0.1f)) * ((float) (ConfigHandler.breakSpeed / 10)));

        } else
            event.setNewSpeed(event.getOriginalSpeed());

        /*System.out.println("Original breakSpeed: " + event.getOriginalSpeed());
        System.out.println("New breakSpeed: " + event.getNewSpeed());
        System.out.println("LogCount: " + logCount);*/
    }

    @Override
    public void interactTree(PlayerInteractEvent event) {

        if (event.getSide().isServer()) {
            StaticHandler.playerHoldShift.put(event.getEntityPlayer().getEntityId(), event.getEntityPlayer().isSneaking());

            if (StaticHandler.playerPrintUnName.contains(event.getEntityPlayer().getEntityId()) && event.getSide().isServer()) { // No text formation because of forge diferences may cause error
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Block: " + event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName()));
                if (event.getEntityPlayer().getHeldItemMainhand() != null)
                    event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Main hand item: " + event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName()));
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("-"));
            }

            return;
        }

        float logCount;
        int axeDurability;

        if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

            if (event.getEntityPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                if (ConfigHandler.axeTypes.contains(event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe

                    logCount = treeHandler.treeAnalyze(event.getWorld(), event.getPos());
                    axeDurability = event.getEntityPlayer().getHeldItemMainhand().getItem().getMaxDamage() + 1 - event.getEntityPlayer().getHeldItemMainhand().getItemDamage();

                    if (logCount > axeDurability && !ConfigHandler.ignoreDurability && !event.getEntityPlayer().isSneaking()) {
                        if (event.getSide().isClient()) {
                            String notEnoughDur = ChatFormatting.WHITE + "[" + ChatFormatting.GOLD + "TreeChop" + ChatFormatting.WHITE + "] Not enough durability..";
                            event.getEntityPlayer().addChatMessage(new TextComponentString(notEnoughDur));
                        }
                        ClientProxy.logCount = 0;
                        return;
                    }

                    if (!event.getEntityPlayer().isSneaking()) {
                        ClientProxy.logCount = (int) logCount;
                    } else
                        ClientProxy.logCount = 0;
                } else
                    ClientProxy.logCount = 0;
            } else
                ClientProxy.logCount = 0;
        } else
            ClientProxy.logCount = 0;
    }

    @Override
    public void destroyTree(BlockEvent.BreakEvent event) { // Open to LAN
        int logDestroyCount;
        float logCount = 0f;
        int axeDurability = 0;

        if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

            if (event.getPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                if (ConfigHandler.axeTypes.contains(event.getPlayer().getHeldItemMainhand().getUnlocalizedName())) { // Player holds allowed axe

                    logCount = treeHandler.treeAnalyze(event.getWorld(), event.getPos());
                    axeDurability = event.getPlayer().getHeldItemMainhand().getItem().getMaxDamage() + 1 - event.getPlayer().getHeldItemMainhand().getItemDamage();

                } else
                    return;
            } else
                return;
        } else
            return;

        if (!StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId())) {

            if (logCount > axeDurability && !ConfigHandler.ignoreDurability)
                return;

            logDestroyCount = treeHandler.treeDestroy(event);

            if (!event.getPlayer().isCreative())
                event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

            if (ConfigHandler.plantSapling) {
                if (ConfigHandler.plantSaplingTree) {
                    event.getWorld().destroyBlock(event.getPos(), true);
                    event.getWorld().setBlockToAir(event.getPos());
                    event.setCanceled(true);
                }
                treeHandler.plantSapling(event.getWorld(), event.getPos(), event);
            }
        }
    }
}
