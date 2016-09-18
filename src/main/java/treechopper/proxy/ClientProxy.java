package treechopper.proxy;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import treechopper.core.ConfigHandler;
import treechopper.core.StaticHandler;
import treechopper.core.TreeHandler;

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
                event.setNewSpeed((10.3f * event.getOriginalSpeed() / ((((float) logCount) + 10f) / 1.06f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else if (event.getOriginalSpeed() <= 12.0f)
                event.setNewSpeed((16.5f * event.getOriginalSpeed() / ((((float) logCount) + 20f) / 1.26f)) * ((float) (ConfigHandler.breakSpeed / 10)));

            else
                event.setNewSpeed((26f * event.getOriginalSpeed() / ((((float) logCount) + 35f) / 1.56f)) * ((float) (ConfigHandler.breakSpeed / 10)));

        } else
            event.setNewSpeed(event.getOriginalSpeed());
    }

    @Override
    public void interactTree(PlayerInteractEvent event) {
        float logCount;
        int axeDurability;
        StaticHandler.choppable = false;

        if (StaticHandler.printNames) { // No text formation because of forge diferences may cause error
            event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Block: " + event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName()));
            if (event.getEntityPlayer().getHeldItemMainhand() != null)
                event.getEntityPlayer().addChatMessage(new TextComponentTranslation("Main hand item: " + event.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName()));
        }

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
                        StaticHandler.choppable = true;
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
    public void destroyTree(BlockEvent.BreakEvent event) {
        int logDestroyCount;

        if (!event.getPlayer().isSneaking() && StaticHandler.choppable) {

            logDestroyCount = treeHandler.treeDestroy(event);

            if (!event.getPlayer().isCreative()) {
                if (event.getPlayer().getHeldItemMainhand() != null)
                    event.getPlayer().getHeldItemMainhand().setItemDamage(event.getPlayer().getHeldItemMainhand().getItemDamage() + logDestroyCount); // Axe damage increase with size of tree
            }

            if (ConfigHandler.plantSapling) {
                if (ConfigHandler.plantSaplingTree) {
                    event.getWorld().setBlockToAir(event.getPos());
                    event.setCanceled(true);
                }
                treeHandler.plantSapling(event.getWorld(), event.getPos());
            }
        }
    }
}
