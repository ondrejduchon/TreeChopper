package treechopper.common.tree;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Tree {

  private ArrayList<BlockPos> logs;
  private ArrayList<BlockPos> leaves;
  private BlockPos initialBlockPosition;

  public Tree() {
    logs = new ArrayList<>();
    leaves = new ArrayList<>();
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

  public ArrayList<BlockPos> getLogs() {
    return logs;
  }

  public ArrayList<BlockPos> getLeaves() {
    return leaves;
  }

  public BlockPos getInitialBlockPosition() {
    return initialBlockPosition;
  }

  public void setInitialBlockPosition(BlockPos newPosition) {
    this.initialBlockPosition = newPosition;
  }
}
