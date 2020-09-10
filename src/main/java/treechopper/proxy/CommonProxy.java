package treechopper.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import treechopper.command.*;
import treechopper.common.config.Configuration;
import treechopper.common.handler.TreeHandler;
import treechopper.core.TreeChopper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class PlayerInteract {

  public BlockPos blockPos; // Interact block position
  public float logCount;
  public int axeDurability;

  public PlayerInteract(BlockPos blockPos, float logCount, int axeDurability) {
    this.blockPos = blockPos;
    this.logCount = logCount;
    this.axeDurability = axeDurability;
  }
}

/**
 * Handles all event bus listeners.  Client side only.
 */
@Mod.EventBusSubscriber(modid = TreeChopper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CommonProxy {

  private static Map<UUID, PlayerInteract> playerData = new HashMap<>();
  private static TreeHandler treeHandler = new TreeHandler();

  /**
   * Commands are registered when the server starts. This used to be on the FMLServerStartEvent
   * but the RegistercommandsEvent was created for all mods to use to register commands.
   *
   * @param event Event being listened for.
   */
  @SubscribeEvent
  public static void onServerStarting(final RegisterCommandsEvent event) {
    TreeChopperCommand.register(event.getDispatcher());
  }

  /**
   * Begin breaking the tree. We slow down the breaking depending on the number of blocks in the tree.
   *
   * @param interactEvent Event being listened for.
   */
  @SubscribeEvent
  public static void interactWithTree(PlayerInteractEvent.LeftClickBlock interactEvent) {
    int logCount;
    boolean shifting = true;

    if (!Configuration.common.disableShift.get()) {
      if (interactEvent.getPlayer().isSneaking() && !Configuration.common.reverseShift.get()) {
        shifting = false;
      }

      if (!interactEvent.getPlayer().isSneaking() && Configuration.common.reverseShift.get()) {
        shifting = false;
      }
    }

    if (checkWoodenBlock(interactEvent.getWorld(), interactEvent.getPos()) && checkItemInHand(interactEvent.getPlayer()) && shifting) {
      int axeDurability = interactEvent.getPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getPlayer().getHeldItemMainhand().getDamage();

      if (playerData.containsKey(interactEvent.getPlayer().getUniqueID()) &&
              playerData.get(interactEvent.getPlayer().getUniqueID()).blockPos.equals(interactEvent.getPos()) &&
              playerData.get(interactEvent.getPlayer().getUniqueID()).axeDurability == axeDurability) {
        return;
      }

      logCount = CommonProxy.treeHandler.analyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getPlayer()).getLogCount();

      if (interactEvent.getPlayer().getHeldItemMainhand().isDamageable() && axeDurability < logCount) {
        playerData.remove(interactEvent.getPlayer().getUniqueID());
        return;
      }

      if (logCount > 1) {
        playerData.put(interactEvent.getPlayer().getUniqueID(), new PlayerInteract(interactEvent.getPos(), logCount, axeDurability));
      }
    } else {
      playerData.remove(interactEvent.getPlayer().getUniqueID());
    }
  }

  /**
   * Slow down the block breaking depending on the number of blocks in the tree.
   *
   * @param breakSpeed Event being listened for.
   */
  @SubscribeEvent
  public static void breakingBlock(PlayerEvent.BreakSpeed breakSpeed) {
    if (playerData.containsKey(breakSpeed.getPlayer().getUniqueID())) {
      BlockPos blockPos = playerData.get(breakSpeed.getPlayer().getUniqueID()).blockPos;
      if (blockPos.equals(breakSpeed.getPos())) {
        breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (playerData.get(breakSpeed.getPlayer().getUniqueID()).logCount / 2.0f));
      } else {
        breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
      }
    }
  }

  /**
   * Breaks the wood block, the tree, and damages the axe.
   *
   * @param breakEvent Event being listened for
   */
  @SubscribeEvent
  public static void destroyWoodBlock(BlockEvent.BreakEvent breakEvent) {
    if (playerData.containsKey(breakEvent.getPlayer().getUniqueID())) {
      BlockPos blockPos = playerData.get(breakEvent.getPlayer().getUniqueID()).blockPos;

      if (blockPos.equals(breakEvent.getPos())) {
        treeHandler.destroyTree((World) breakEvent.getWorld(), breakEvent.getPlayer());

        if (!breakEvent.getPlayer().isCreative() && breakEvent.getPlayer().getHeldItemMainhand().isDamageable()) {
          int axeDurability = breakEvent.getPlayer().getHeldItemMainhand().getDamage() + (int) (playerData.get(breakEvent.getPlayer().getUniqueID()).logCount * 1.5);
          breakEvent.getPlayer().getHeldItemMainhand().setDamage(axeDurability);
        }
      }
    }
  }

  // Check if the block at @blockPos is an instance of LOGS
  // This allows any mod that is registering their block with BlockTags.LOGS
  private static boolean checkWoodenBlock(World world, BlockPos blockPos) {
    return world.getBlockState(blockPos).getBlock().isIn(BlockTags.LOGS);
  }

  // Checks if the item being held is an AxeItem
  // This allows any mod that registers their tool as an AxeItem
  private static boolean checkItemInHand(PlayerEntity entityPlayer) {
    if (entityPlayer.getHeldItemMainhand().isEmpty()) {
      return false;
    }

    return entityPlayer.getHeldItemMainhand().getItem() instanceof AxeItem;
  }
}