package treechopper.proxy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treechopper.common.handler.TreeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonProxy {

    @SubscribeEvent
    public void InteractWithTree(PlayerInteractEvent interactEvent) {

        if (interactEvent.getSide().isServer()) { // Server - Singleplayer/LAN

            treeHandler = new TreeHandler();
            int logCount;

            if (CheckWoodenBlock(interactEvent.getWorld(), interactEvent.getPos())) {
                logCount = treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getEntityPlayer());

                m_PlayerSpeed.put(interactEvent.getEntityPlayer().getPersistentID(), (float) logCount);
            } else {
                m_PlayerSpeed.remove(interactEvent.getEntityPlayer().getPersistentID());
            }
        }
    }

    @SubscribeEvent
    public void BreakingBlock(PlayerEvent.BreakSpeed event) {

        if (event.getEntityPlayer().getServer() == null) { // Server - Singleplayer/LAN

            //TODO Jestli hrac nici jiny blok, nez pocitam s blokem v map, odstrani se z mapy

            if (m_PlayerSpeed.containsKey(event.getEntityPlayer().getPersistentID())) {
                event.setNewSpeed(event.getOriginalSpeed() / (m_PlayerSpeed.get(event.getEntityPlayer().getPersistentID()) / 2.0f));
            }
            System.out.println(event.getNewSpeed());
        }

    }

    @SubscribeEvent
    public void DestroyWoodBlock(BlockEvent.BreakEvent breakEvent) {

        treeHandler.DestroyTree(breakEvent.getWorld(), breakEvent.getPlayer());
    }

    protected boolean CheckWoodenBlock(World world, BlockPos blockPos) {

        if (!world.getBlockState(blockPos).getBlock().isWood(world, blockPos)) {
            return false;
        }

        return true;
    }

    protected static Map<UUID, Float> m_PlayerSpeed = new HashMap<>();
    private TreeHandler treeHandler;
}