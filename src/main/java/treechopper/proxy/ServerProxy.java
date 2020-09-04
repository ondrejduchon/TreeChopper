package treechopper.proxy;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import treechopper.common.config.Configuration;
import treechopper.common.handler.TreeHandler;

public class ServerProxy extends CommonProxy {

//  public void interactWithTree(PlayerInteractEvent.LeftClickBlock interactEvent) {
//
//    if (playerNames.containsKey(interactEvent.getPlayer().getUniqueID()) && playerNames.get(interactEvent.getPlayer().getUniqueID())) {
//      interactEvent.getPlayer().sendMessage(new TranslationTextComponent("Block: " + interactEvent.getWorld().getBlockState(interactEvent.getPos()).getBlock().getTranslationKey()), interactEvent.getPlayer().getUniqueID());
//      interactEvent.getPlayer().sendMessage(new TranslationTextComponent("Main hand item: " + interactEvent.getPlayer().getHeldItemMainhand().getTranslationKey()), interactEvent.getPlayer().getUniqueID());
//    }
//
//    int logCount;
//    boolean shifting = true;
//
//    if (!Configuration.common.disableShift.get()) {
//      if (interactEvent.getPlayer().isSneaking() && !Configuration.common.reverseShift.get()) {
//        shifting = false;
//      }
//
//      if (!interactEvent.getPlayer().isSneaking() && Configuration.common.reverseShift.get()) {
//        shifting = false;
//      }
//    }
//
//    if (checkWoodenBlock(interactEvent.getWorld(), interactEvent.getPos()) && checkItemInHand(interactEvent.getPlayer()) && shifting) {
//
//      int axeDurability = interactEvent.getPlayer().getHeldItemMainhand().getMaxDamage() - interactEvent.getPlayer().getHeldItemMainhand().getDamage();
//
//      if (playerData.containsKey(interactEvent.getPlayer().getUniqueID()) &&
//              playerData.get(interactEvent.getPlayer().getUniqueID()).blockPos.equals(interactEvent.getPos()) &&
//              playerData.get(interactEvent.getPlayer().getUniqueID()).axeDurability == axeDurability) {
//        return;
//      }
//
//      treeHandler = new TreeHandler();
//      logCount = treeHandler.analyzeTree(interactEvent.getWorld(), interactEvent.getPos(), interactEvent.getPlayer());
//
//      if (interactEvent.getPlayer().getHeldItemMainhand().isDamageable() && axeDurability < logCount) {
//        playerData.remove(interactEvent.getPlayer().getUniqueID());
//        return;
//      }
//
//      if (logCount > 1) {
//        playerData.put(interactEvent.getPlayer().getUniqueID(), new PlayerInteract(interactEvent.getPos(), logCount, axeDurability));
//      }
//    } else {
//      playerData.remove(interactEvent.getPlayer().getUniqueID());
//    }
//  }
}
