package treechopper.proxy;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.handler.TreeHandler;

public class ServerProxy extends CommonProxy {

    @Override
    public void InteractWithTree(PlayerInteractEvent interactEvent) {

        if (interactEvent.getSide().isServer() && m_PlayerPrintNames.containsKey(interactEvent.getEntityPlayer().getPersistentID()) && m_PlayerPrintNames.get(interactEvent.getEntityPlayer().getPersistentID())) {
            interactEvent.getEntityPlayer().sendMessage(new TextComponentTranslation("Block: " + interactEvent.getWorld().getBlockState(interactEvent.getPos()).getBlock().getUnlocalizedName()));
            interactEvent.getEntityPlayer().sendMessage(new TextComponentTranslation("Main hand item: " + interactEvent.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName()));
        }

        int logCount;
        boolean shifting = true;

        if (interactEvent.getEntityPlayer().isSneaking() && !ConfigurationHandler.reverseShift) {
            shifting = false;
        }

        if (!interactEvent.getEntityPlayer().isSneaking() && ConfigurationHandler.reverseShift) {
            shifting = false;
        }

        if (CheckWoodenBlock(interactEvent.getWorld(), interactEvent.getPos()) && CheckItemInHand(interactEvent.getEntityPlayer()) && shifting) {

            if (m_PlayerData.containsKey(interactEvent.getEntityPlayer().getPersistentID()) &&
                    m_PlayerData.get(interactEvent.getEntityPlayer().getPersistentID()).m_BlockPos.equals(interactEvent.getPos())) {
                return;
            }

            treeHandler = new TreeHandler();
            logCount = treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());

            int axeDurability = interactEvent.getEntityPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getEntityPlayer().getHeldItemMainhand().getItemDamage();

            if (axeDurability < logCount) {
                m_PlayerData.remove(interactEvent.getEntityPlayer().getPersistentID());
                return;
            }

            if (logCount > 1) {
                m_PlayerData.put(interactEvent.getEntityPlayer().getPersistentID(), new PlayerInteract(interactEvent.getPos(), logCount));
            }
        } else {
            m_PlayerData.remove(interactEvent.getEntityPlayer().getPersistentID());
        }
    }
}
