package treechopper.common.tree;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class Tree {

  private Set<BlockPos> logs;
  private Set<BlockPos> leaves;
  private BlockPos initialBlockPosition;

  public Tree() {
    logs = new HashSet<>();
    leaves = new HashSet<>();
  }

  public void insertLog(BlockPos blockPos) {
    logs.add(blockPos);
  }

  public void insertLeaf(BlockPos blockPos) {
    leaves.add(blockPos);
  }

  public int getLogCount() {
    return logs.size();
  }

  public int getLeavesCount() {
    return leaves.size();
  }

  public Set<BlockPos> getLogs() {
    return logs;
  }

  public Set<BlockPos> getLeaves() {
    return leaves;
  }

  public BlockPos getInitialBlockPosition() {
    return initialBlockPosition;
  }

  public void setInitialBlockPosition(BlockPos newPosition) {
    this.initialBlockPosition = newPosition;
  }
}
