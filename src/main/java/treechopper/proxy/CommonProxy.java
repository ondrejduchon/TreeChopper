package treechopper.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.handler.TreeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class PlayerInteract {

    public PlayerInteract(BlockPos blockPos, float logCount) {
        m_BlockPos = blockPos;
        m_LogCount = logCount;
    }

    public BlockPos m_BlockPos; // Interact block position
    public float m_LogCount;
};

public class CommonProxy {

    @SubscribeEvent
    public void InteractWithTree(PlayerInteractEvent interactEvent) {

        if (interactEvent.getSide().isServer()) { // Server - Singleplayer/LAN

            treeHandler = new TreeHandler();
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

    @SubscribeEvent
    public void BreakingBlock(PlayerEvent.BreakSpeed breakSpeed) {

        if (breakSpeed.getEntityPlayer().getServer() == null) { // Server - Singleplayer/LAN

            if (m_PlayerData.containsKey(breakSpeed.getEntityPlayer().getPersistentID())) {

                BlockPos blockPos = m_PlayerData.get(breakSpeed.getEntityPlayer().getPersistentID()).m_BlockPos;

                if (blockPos.equals(breakSpeed.getPos())) {
                    breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (m_PlayerData.get(breakSpeed.getEntityPlayer().getPersistentID()).m_LogCount / 2.0f));
                } else {
                    breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
                }
            }

            //System.out.println(breakSpeed.getNewSpeed());
        }
    }

    @SubscribeEvent
    public void DestroyWoodBlock(BlockEvent.BreakEvent breakEvent) {

        if (m_PlayerData.containsKey(breakEvent.getPlayer().getPersistentID())) {

            BlockPos blockPos = m_PlayerData.get(breakEvent.getPlayer().getPersistentID()).m_BlockPos;

            if (blockPos.equals(breakEvent.getPos())) {
                treeHandler.DestroyTree(breakEvent.getWorld(), breakEvent.getPlayer());

                if (!breakEvent.getPlayer().isCreative()) {

                    int axeDurability = breakEvent.getPlayer().getHeldItemMainhand().getItemDamage() + (int) (m_PlayerData.get(breakEvent.getPlayer().getPersistentID()).m_LogCount * 1.5);

                    breakEvent.getPlayer().getHeldItemMainhand().setItemDamage(axeDurability);
                }
            }
        }
    }

    protected boolean CheckWoodenBlock(World world, BlockPos blockPos) {

        if (ConfigurationHandler.blockWhiteList.contains(world.getBlockState(blockPos).getBlock().getUnlocalizedName())) {
            return true;
        }

        if (!world.getBlockState(blockPos).getBlock().isWood(world, blockPos)) {
            return false;
        }

        return true;
    }

    protected boolean CheckItemInHand(EntityPlayer entityPlayer) {

        if (entityPlayer.getHeldItemMainhand().isEmpty()) {
            return false;
        }

        if (ConfigurationHandler.axeTypes.contains(entityPlayer.getHeldItemMainhand().getItem().getUnlocalizedName())) {
            return true;
        }

        boolean test;

        try {
            ItemAxe tmp = (ItemAxe) entityPlayer.getHeldItemMainhand().getItem();
            test = true;
        } catch (Exception e) {
            System.out.println("It is not an axe");
            test = false;
        }

        return test;
    }

    protected static Map<UUID, PlayerInteract> m_PlayerData = new HashMap<>();
    protected TreeHandler treeHandler;
}