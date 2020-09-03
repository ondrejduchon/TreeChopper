package treechopper.proxy;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import treechopper.common.config.Configuration;
import treechopper.common.handler.TreeHandler;

public class ServerProxy extends CommonProxy {

//  @Override
//  public void InteractWithTree(PlayerInteractEvent.LeftClickBlock interactEvent) {
//
//    if (m_PlayerPrintNames.containsKey(interactEvent.getPlayer().getUniqueID()) && m_PlayerPrintNames.get(interactEvent.getPlayer().getUniqueID())) {
//      interactEvent.getPlayer().sendMessage(new TranslationTextComponent("Block: " + interactEvent.getWorld().getBlockState(interactEvent.getPos()).getBlock().getTranslationKey()), interactEvent.getPlayer().getUniqueID());
//      interactEvent.getPlayer().sendMessage(new TranslationTextComponent("Main hand item: " + interactEvent.getPlayer().getHeldItemMainhand().getTranslationKey()), interactEvent.getPlayer().getUniqueID());
//    }
//
//    int logCount;
//    boolean shifting = true;
//
//    if (!Configuration.COMMON.disableShift.get()) {
//      if (interactEvent.getPlayer().isSneaking() && !Configuration.COMMON.reverseShift.get()) {
//        shifting = false;
//      }
//
//      if (!interactEvent.getPlayer().isSneaking() && Configuration.COMMON.reverseShift.get()) {
//        shifting = false;
//      }
//    }
//
//    if (CheckWoodenBlock(interactEvent.getWorld(), interactEvent.getPos()) && CheckItemInHand(interactEvent.getPlayer()) && shifting) {
//
//      int axeDurability = interactEvent.getPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getPlayer().getHeldItemMainhand().getDamage();
//
//      if (m_PlayerData.containsKey(interactEvent.getPlayer().getUniqueID()) &&
//              m_PlayerData.get(interactEvent.getPlayer().getUniqueID()).m_BlockPos.equals(interactEvent.getPos()) &&
//              m_PlayerData.get(interactEvent.getPlayer().getUniqueID()).m_AxeDurability == axeDurability) {
//        return;
//      }
//
//      treeHandler = new TreeHandler();
//      logCount = treeHandler.AnalyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getPlayer());
//
//      if (interactEvent.getPlayer().getHeldItemMainhand().isDamageable() && axeDurability < logCount) {
//        m_PlayerData.remove(interactEvent.getPlayer().getUniqueID());
//        return;
//      }
//
//      if (logCount > 1) {
//        m_PlayerData.put(interactEvent.getPlayer().getUniqueID(), new PlayerInteract(interactEvent.getPos(), logCount, axeDurability));
//      }
//    } else {
//      m_PlayerData.remove(interactEvent.getPlayer().getUniqueID());
//    }
//  }
}
