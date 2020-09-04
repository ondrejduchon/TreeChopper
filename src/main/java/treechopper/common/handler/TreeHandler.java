package treechopper.common.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import treechopper.common.config.Configuration;
import treechopper.common.tree.Tree;
import treechopper.core.TreeChopper;

import java.util.*;

public class TreeHandler {

  private static Map<UUID, Tree> treeMap = new HashMap<>();
  private Tree tree;

  //TODO: remove
  private static <T> T getLastElement(final Iterable<T> elements) {
    final Iterator<T> itr = elements.iterator();
    T lastElement = itr.next();
    while (itr.hasNext()) {
      lastElement = itr.next();
    }

    return lastElement;
  }

  public Tree analyzeTree(World world, BlockPos treePos, PlayerEntity entityPlayer) {
    Queue<BlockPos> queuedBlocks = new LinkedList<>();
    Set<BlockPos> tempAdjacentBlocks = new HashSet<>();
    Set<BlockPos> checkedBlocks = new HashSet<>();

    BlockPos currentPos;
    Block logBlock = world.getBlockState(treePos).getBlock();
    tree = new Tree();

    queuedBlocks.add(treePos);
    tree.insertLog(treePos);

    while (!queuedBlocks.isEmpty()) {

      currentPos = queuedBlocks.remove();
      checkedBlocks.add(currentPos);

      tempAdjacentBlocks.addAll(lookAroundBlock(logBlock, currentPos, world, checkedBlocks));
      queuedBlocks.addAll(tempAdjacentBlocks);
      // TODO: smells bad but inf loops
      checkedBlocks.addAll(tempAdjacentBlocks);
      tempAdjacentBlocks.clear();
    }

    Set<BlockPos> tmpLeaves = new HashSet<>(tree.getLeaves());

    for (BlockPos currentLeaf : tmpLeaves) {
      checkedBlocks.add(currentLeaf);
      lookAroundBlock(null, currentLeaf, world, checkedBlocks);
    }

    tree.setInitialBlockPosition(treePos);
    treeMap.put(entityPlayer.getUniqueID(), tree);

    return tree;
  }

  private Queue<BlockPos> lookAroundBlock(Block originBlock, BlockPos currentPos, World world, Set<BlockPos> checkedBlocks) {

    Queue<BlockPos> queuedBlocks = new LinkedList<>();
    BlockPos tmpPos;

    // Looks at all blocks surrounding the current position block
    // 3 x 3 x 3
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
          tmpPos = new BlockPos(currentPos.getX() + i, currentPos.getY() + j, currentPos.getZ() + k);
          if (checkBlockType(world, tmpPos, checkedBlocks, originBlock)) {
            queuedBlocks.add(tmpPos);
          }
        }
      }
    }

    return queuedBlocks;
  }

  private boolean checkBlockType(World world, BlockPos blockPos, Set<BlockPos> checkedBlocks, Block originBlock) {
    if (checkedBlocks.contains(blockPos)) {
      return false;
    }

    if (world.getBlockState(blockPos).getBlock() != originBlock) {
      if (Configuration.common.plantSapling.get() && world.getBlockState(blockPos).getBlock().getMaterialColor() == MaterialColor.FOLIAGE && tree.getLeaves().isEmpty()) {
        tree.insertLeaf(blockPos);
      }

      if (Configuration.common.decayLeaves.get() && world.getBlockState(blockPos).getBlock().getMaterialColor() == MaterialColor.FOLIAGE) {
        tree.insertLeaf(blockPos);
        return false;
      }
      else{
        return false;
      }
    }

    tree.insertLog(blockPos);

    return true;
  }

  public void destroyTree(World world, PlayerEntity entityPlayer) {
    if (treeMap.containsKey(entityPlayer.getUniqueID())) {
      Tree tmpTree = treeMap.get(entityPlayer.getUniqueID());

      for (BlockPos logPos : tmpTree.getLogs()) {
        world.destroyBlock(logPos, true);
        world.setBlockState(logPos, Blocks.AIR.getDefaultState());
      }

      if (Configuration.common.plantSapling.get() && !tmpTree.getLeaves().isEmpty()) {
        //TODO: rewrite to use getLeavesCount
        BlockPos tmpPosition = getLastElement(tmpTree.getLeaves());
        plantSapling(world, tmpPosition, tmpTree.getInitialBlockPosition());
      }

      if (Configuration.common.decayLeaves.get()) {
        for (BlockPos leafPos : tmpTree.getLeaves()) {
          world.destroyBlock(leafPos, true);
          world.setBlockState(leafPos, Blocks.AIR.getDefaultState());
        }
      }
    }
  }

  private boolean plantSapling(World world, BlockPos blockPos, BlockPos originPos) {
    ItemStack sapling = null;

    // TODO: Examine what this does
    int counter = 0;
    while (sapling == null && counter <= 100) {
      List<ItemStack> drops = Block.getDrops(world.getBlockState(blockPos), ((ServerWorld) world).getWorldServer(), blockPos, null);
      for (ItemStack item: drops) {
        if (item.getItem().getTranslationKey().contains("sapling")) {
          sapling = item;
        }
      }
      counter++;
    }

    if (sapling == null) {
      return false;
    }

    boolean result = world.setBlockState(originPos.add(1, 0, 1), SaplingBlock.getBlockFromItem(sapling.getItem()).getDefaultState());
    TreeChopper.LOGGER.info("Placed sapling block: " + result);
    return result;
  }
}
