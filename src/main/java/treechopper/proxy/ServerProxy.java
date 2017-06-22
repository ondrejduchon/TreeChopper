package treechopper.proxy;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ServerProxy extends CommonProxy {

    @Override
    public void BreakingBlock(PlayerEvent.BreakSpeed breakSpeed) {

        if (m_PlayerData.containsKey(breakSpeed.getEntityPlayer().getPersistentID())) {

            BlockPos blockPos = m_PlayerData.get(breakSpeed.getEntityPlayer().getPersistentID()).m_BlockPos;

            if (blockPos.equals(breakSpeed.getPos())) {
                breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (m_PlayerData.get(breakSpeed.getEntityPlayer().getPersistentID()).m_LogCount / 2.0f));
            } else {
                breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
            }
        }

    }
}
