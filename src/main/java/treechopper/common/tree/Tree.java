package treechopper.common.tree;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class Tree {

    public Tree() {
        m_Wood = new HashSet<>();
        m_Leaves = new HashSet<>();
    }

    public void InsertWood(BlockPos blockPos) {
        m_Wood.add(blockPos);
    }

    public void InsertLeaf(BlockPos blockPos) {
        m_Leaves.add(blockPos);
    }

    public int GetLogCount() {
        return m_Wood.size();
    }

    public int GetLeavesCount() {
        return m_Leaves.size();
    }

    public Set<BlockPos> GetM_Wood() {
        return m_Wood;
    }

    public Set<BlockPos> GetM_Leaves() {
        return m_Leaves;
    }

    private Set<BlockPos> m_Wood;
    private Set<BlockPos> m_Leaves;
}
