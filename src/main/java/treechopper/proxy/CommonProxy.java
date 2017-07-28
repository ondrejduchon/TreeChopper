package treechopper.proxy;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.handler.TreeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class PlayerInteract {

    public BlockPos m_BlockPos; // Interact block position
    public float m_LogCount;
    public int m_AxeDurability;

    public PlayerInteract(BlockPos blockPos, float logCount, int axeDurability) {
        m_BlockPos = blockPos;
        m_LogCount = logCount;
        m_AxeDurability = axeDurability;
    }
};

public class CommonProxy {

    public static Map<UUID, Boolean> m_PlayerPrintNames = new HashMap<>();
    protected static Map<UUID, PlayerInteract> m_PlayerData = new HashMap<>();
    protected TreeHandler treeHandler;

    @SubscribeEvent
    public void InteractWithTree(PlayerInteractEvent interactEvent) {

        if (interactEvent.getSide().isClient() && m_PlayerPrintNames.containsKey(interactEvent.getEntityPlayer().getPersistentID()) && m_PlayerPrintNames.get(interactEvent.getEntityPlayer().getPersistentID())) {
            interactEvent.getEntityPlayer().sendMessage(new TextComponentTranslation(I18n.format("proxy.printBlock") + " " + interactEvent.getWorld().getBlockState(interactEvent.getPos()).getBlock().getUnlocalizedName()));
            interactEvent.getEntityPlayer().sendMessage(new TextComponentTranslation(I18n.format("proxy.printMainHand") + " " + interactEvent.getEntityPlayer().getHeldItemMainhand().getUnlocalizedName()));
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

            int axeDurability = interactEvent.getEntityPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getEntityPlayer().getHeldItemMainhand().getItemDamage();

            if (m_PlayerData.containsKey(interactEvent.getEntityPlayer().getPersistentID()) &&
                    m_PlayerData.get(interactEvent.getEntityPlayer().getPersistentID()).m_BlockPos.equals(interactEvent.getPos()) &&
                    m_PlayerData.get(interactEvent.getEntityPlayer().getPersistentID()).m_AxeDurability == axeDurability) {
                return;
            }

            treeHandler = new TreeHandler();
            logCount = treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());

            /*System.out.println("Max damage: " + interactEvent.getEntityPlayer().getHeldItemMainhand().getMaxDamage());
            System.out.println("Item damage: " + interactEvent.getEntityPlayer().getHeldItemMainhand().getItemDamage());*/

            if (axeDurability < logCount) {
                m_PlayerData.remove(interactEvent.getEntityPlayer().getPersistentID());
                return;
            }

            if (logCount > 1) {
                m_PlayerData.put(interactEvent.getEntityPlayer().getPersistentID(), new PlayerInteract(interactEvent.getPos(), logCount, axeDurability));
            }
        } else {
            m_PlayerData.remove(interactEvent.getEntityPlayer().getPersistentID());
        }
    }

    @SubscribeEvent
    public void BreakingBlock(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed breakSpeed) {

        if (m_PlayerData.containsKey(breakSpeed.getEntityPlayer().getPersistentID())) {

            BlockPos blockPos = m_PlayerData.get(breakSpeed.getEntityPlayer().getPersistentID()).m_BlockPos;

            if (blockPos.equals(breakSpeed.getPos())) {
                breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (m_PlayerData.get(breakSpeed.getEntityPlayer().getPersistentID()).m_LogCount / 2.0f));
            } else {
                breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
            }
        }
    }

    @SubscribeEvent
    public void DestroyWoodBlock(BlockEvent.BreakEvent breakEvent) {

        if (m_PlayerData.containsKey(breakEvent.getPlayer().getPersistentID())) {

            BlockPos blockPos = m_PlayerData.get(breakEvent.getPlayer().getPersistentID()).m_BlockPos;

            if (blockPos.equals(breakEvent.getPos())) {
                treeHandler.DestroyTree(breakEvent.getWorld(), breakEvent.getPlayer());

                if (!breakEvent.getPlayer().isCreative() && breakEvent.getPlayer().getHeldItemMainhand().isItemStackDamageable()) {

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
            test = false;
        }

        return test;
    }
}