package treechopper.common.tree;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class Tree {

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

    private Set<BlockPos> m_Wood;
    private Set<BlockPos> m_Leaves;
}
