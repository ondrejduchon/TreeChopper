package treechopper.common.tree;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class Tree {

    private Set<BlockPos> m_Wood;
    private Set<BlockPos> m_Leaves;
    private BlockPos m_Position;

    public Tree() {
        m_Wood = new HashSet<BlockPos>();
        m_Leaves = new HashSet<BlockPos>();
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

    public BlockPos getM_Position() {
        return m_Position;
    }

    public void setM_Position(BlockPos m_Position) {
        this.m_Position = m_Position;
    }
}
