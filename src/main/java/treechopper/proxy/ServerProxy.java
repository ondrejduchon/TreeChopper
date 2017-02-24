package treechopper.proxy;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import treechopper.common.config.ConfigHandler;
import treechopper.common.handler.TreeHandler;
import treechopper.common.handler.mods.TConstructHandler;
import treechopper.core.StaticHandler;

/**
 * Created by Duchy on 9/1/2016.
 */

public class ServerProxy extends CommonProxy {
    private TreeHandler treeHandler = new TreeHandler();

    @Override
    public void interactTree(PlayerInteractEvent event) {
        StaticHandler.playerHoldShift.put(event.getEntityPlayer().getEntityId(), event.getEntityPlayer().isSneaking());

        if (StaticHandler.playerPrintUnName.contains(event.getEntityPlayer().getEntityId()) && event.getSide().isServer()) { // No text formation because of forge diferences may cause error
            event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Block: " + event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName()));
            if (event.getEntityPlayer().getHeldItemMainhand() != null)
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Main hand item: " + event.getEntityPlayer().getHeldItemMainhand().getItem().getUnlocalizedName()));
            event.getEntityPlayer().addChatMessage(new TextComponentTranslation("-"));
        }
    }

    @Override
    public void destroyTree(BlockEvent.BreakEvent event) {
        int logDestroyCount, axeDurability;
        float logCount;

        if (ConfigHandler.logTypes.contains(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName())) { // It is allowed log

            if (event.getPlayer().getHeldItemMainhand() != null) { // Player holds something in his main hand

                if (ConfigHandler.axeTypes.contains(event.getPlayer().getHeldItemMainhand().getItem().getUnlocalizedName())) { // Player holds allowed axe

                    if (!StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId()) && !StaticHandler.playerReverseShift.get(event.getPlayer().getEntityId()) ||
                            StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId()) && StaticHandler.playerReverseShift.get(event.getPlayer().getEntityId()))
                        logCount = treeHandler.treeAnalyze(event.getWorld(), event.getPos());
                    else
                        logCount = 1;
                    axeDurability = event.getPlayer().getHeldItemMainhand().getMaxDamage() - event.getPlayer().getHeldItemMainhand().getItemDamage();

                } else
                    return;
            } else
                return;
        } else
            return;

        if (!StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId()) && !StaticHandler.playerReverseShift.get(event.getPlayer().getEntityId()) ||
                StaticHandler.playerHoldShift.get(event.getPlayer().getEntityId()) && StaticHandler.playerReverseShift.get(event.getPlayer().getEntityId())) {

            if (logCount > axeDurability && !ConfigHandler.ignoreDurability)
                return;

            logDestroyCount = treeHandler.treeDestroy(event);

            if (!event.getPlayer().isCreative() && !TConstructHandler.tcAxes.contains(event.getPlayer().getHeldItemMainhand().getItem().getUnlocalizedName()))
                event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree

            if (!event.getPlayer().isCreative() && TConstructHandler.tcAxes.contains(event.getPlayer().getHeldItemMainhand().getItem().getUnlocalizedName()))
                event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount * 5); // Axe damage increase with size of tree

            if (ConfigHandler.plantSapling) {
                if (TConstructHandler.tcAxes.contains(event.getPlayer().getHeldItemMainhand().getItem().getUnlocalizedName()) || ConfigHandler.plantSaplingTree) {
                    event.getWorld().destroyBlock(event.getPos(), true);
                    event.getWorld().setBlockToAir(event.getPos());
                    event.setCanceled(true);
                }
                treeHandler.plantSapling(event.getWorld(), event.getPos(), event);
            }
        } else {
            if (!event.getPlayer().isCreative() && TConstructHandler.tcAxes.contains(event.getPlayer().getHeldItemMainhand().getItem().getUnlocalizedName()))
                event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + 4);
        }
    }
}
