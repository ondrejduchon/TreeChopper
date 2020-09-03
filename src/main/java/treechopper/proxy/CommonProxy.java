package treechopper.proxy;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import treechopper.common.config.Configuration;
import treechopper.common.handler.TreeHandler;
import treechopper.core.TreeChopper;

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

@Mod.EventBusSubscriber(modid = TreeChopper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CommonProxy {

  public static Map<UUID, Boolean> m_PlayerPrintNames = new HashMap<>();
  protected static Map<UUID, PlayerInteract> m_PlayerData = new HashMap<>();
  protected static TreeHandler treeHandler = new TreeHandler();

  @SubscribeEvent
  public static void onJumpWithStick(LivingEvent.LivingJumpEvent event) {
    LivingEntity player = event.getEntityLiving();
    if(player.getHeldItemMainhand().getItem() == Items.STICK) {
      if(player.getEntityWorld().isRemote) {
        // Only log if we're on the client
        TreeChopper.LOGGER.info("Player tried to jump with a stick");
      }
      World world = player.getEntityWorld();
      // Get the player's current position.
      // playeer.getPosition()
      // Get the block below the player's location
      TreeChopper.LOGGER.info("Jumped with stick!");
    }
  }

  @SubscribeEvent
  public static void InteractWithTree(PlayerInteractEvent.LeftClickBlock interactEvent) {
    TreeChopper.LOGGER.info("Got interact");
    if (interactEvent.getSide().isClient() && m_PlayerPrintNames.containsKey(interactEvent.getPlayer().getUniqueID()) && m_PlayerPrintNames.get(interactEvent.getPlayer().getUniqueID())) {
      interactEvent.getPlayer().sendMessage(new TranslationTextComponent(I18n.format("proxy.printBlock") + " " + interactEvent.getWorld().getBlockState(interactEvent.getPos()).getBlock().getTranslationKey()), interactEvent.getPlayer().getUniqueID());
      interactEvent.getPlayer().sendMessage(new TranslationTextComponent(I18n.format("proxy.printMainHand") + " " + interactEvent.getPlayer().getHeldItemMainhand().getItem().getTranslationKey()), interactEvent.getPlayer().getUniqueID());
    }

    int logCount;
    boolean shifting = true;

    if (!Configuration.COMMON.disableShift.get()) {
      if (interactEvent.getPlayer().isSneaking() && !Configuration.COMMON.reverseShift.get()) {
        shifting = false;
      }

      if (!interactEvent.getPlayer().isSneaking() && Configuration.COMMON.reverseShift.get()) {
        shifting = false;
      }
    }

    if (CheckWoodenBlock(interactEvent.getWorld(), interactEvent.getPos()) && CheckItemInHand(interactEvent.getPlayer()) && shifting) {

      int axeDurability = interactEvent.getPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getPlayer().getHeldItemMainhand().getDamage();

      if (m_PlayerData.containsKey(interactEvent.getPlayer().getUniqueID()) &&
              m_PlayerData.get(interactEvent.getPlayer().getUniqueID()).m_BlockPos.equals(interactEvent.getPos()) &&
              m_PlayerData.get(interactEvent.getPlayer().getUniqueID()).m_AxeDurability == axeDurability) {
        return;
      }

      logCount = CommonProxy.treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getPlayer());

      if (interactEvent.getPlayer().getHeldItemMainhand().isDamageable() && axeDurability < logCount) {
        m_PlayerData.remove(interactEvent.getPlayer().getUniqueID());
        return;
      }

      if (logCount > 1) {
        m_PlayerData.put(interactEvent.getPlayer().getUniqueID(), new PlayerInteract(interactEvent.getPos(), logCount, axeDurability));
      }
    } else {
      m_PlayerData.remove(interactEvent.getPlayer().getUniqueID());
    }
  }

  @SubscribeEvent
  public static void BreakingBlock(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed breakSpeed) {
    if (m_PlayerData.containsKey(breakSpeed.getPlayer().getUniqueID())) {

      BlockPos blockPos = m_PlayerData.get(breakSpeed.getPlayer().getUniqueID()).m_BlockPos;

      if (blockPos.equals(breakSpeed.getPos())) {
        breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (m_PlayerData.get(breakSpeed.getPlayer().getUniqueID()).m_LogCount / 2.0f));
      } else {
        breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
      }
    }
  }

  @SubscribeEvent
  public static void DestroyWoodBlock(BlockEvent.BreakEvent breakEvent) {
    TreeChopper.LOGGER.info("Got Destroy");
    if (m_PlayerData.containsKey(breakEvent.getPlayer().getUniqueID())) {

      BlockPos blockPos = m_PlayerData.get(breakEvent.getPlayer().getUniqueID()).m_BlockPos;

      if (blockPos.equals(breakEvent.getPos())) {
        treeHandler.DestroyTree((World) breakEvent.getWorld(), breakEvent.getPlayer());

        if (!breakEvent.getPlayer().isCreative() && breakEvent.getPlayer().getHeldItemMainhand().isDamageable()) {

          int axeDurability = breakEvent.getPlayer().getHeldItemMainhand().getDamage() + (int) (m_PlayerData.get(breakEvent.getPlayer().getUniqueID()).m_LogCount * 1.5);

          breakEvent.getPlayer().getHeldItemMainhand().setDamage(axeDurability);
        }
      }
    }
  }

  protected static boolean CheckWoodenBlock(World world, BlockPos blockPos) {
    if (world.getBlockState(blockPos).getBlock().isIn(BlockTags.LOGS)) {
      return true;
    }

    return false;
  }

  protected static boolean CheckItemInHand(PlayerEntity entityPlayer) {

    if (entityPlayer.getHeldItemMainhand().isEmpty()) {
      return false;
    }

    if (entityPlayer.getHeldItemMainhand().getItem() instanceof AxeItem) {
      return true;
    }

    boolean test;

    try {
      AxeItem tmp = (AxeItem) entityPlayer.getHeldItemMainhand().getItem();
      test = true;
    } catch (Exception e) {
      test = false;
    }

    return test;
  }
}