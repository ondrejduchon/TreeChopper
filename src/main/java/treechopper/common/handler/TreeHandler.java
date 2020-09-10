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

import java.util.*;

/**
 * Handles most things related to the brains of the mod. Most of the business logic
 * is handled within this class.
 */
public class TreeHandler {
  private static Map<UUID, Tree> treeMap = new HashMap<>();
  private Tree tree;

  /**
   * Get all related metadata about the tree and build a Tree object.
   *
   * @param world Minecraft world
   * @param treePos BlockPos: x,y,z coordinates of the block being destroyed
   * @param entityPlayer PlayerEntity: the player who is destroying the block
   * @return Tree
   */
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

  /**
   * Check out the area directly surrounding the block in question
   *
   * x x x
   * x O x
   * x x x
   *
   * @param originBlock Block to look around at all adjacent blocks
   * @param currentPos Where the block currently is x,y,z
   * @param world Minecraft world object
   * @param checkedBlocks Other blocks that have been checked
   * @return Queue<BlockPos>
   */
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

  /**
   * This will:
   *   Destroy log and leaf blocks, getting drops
   *   Plant a sapling if enabled
   *   Decay leaves if enabled, sets all leaf blocks to air
   *
   * @param world Minecraft world
   * @param entityPlayer Player who broke the tree
   */
  public void destroyTree(World world, PlayerEntity entityPlayer) {
    if (treeMap.containsKey(entityPlayer.getUniqueID())) {
      Tree tmpTree = treeMap.get(entityPlayer.getUniqueID());

      for (BlockPos logPos : tmpTree.getLogs()) {
        world.destroyBlock(logPos, true);
        world.setBlockState(logPos, Blocks.AIR.getDefaultState());
      }

      if (Configuration.common.plantSapling.get() && !tmpTree.getLeaves().isEmpty()) {
        BlockPos tmpPosition = tmpTree.getLeaves().get(tmpTree.getLeavesCount() - 1);
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

  /**
   * Plant a sapling after the tree is broken.
   *
   * TODO:
   *  Check if block has dirt underneath it (valid block)
   *  Search for a valid block around the tree.
   *
   * For some reason, you can't place the sapling where the original tree was.
   *
   * @param world Minecraft world
   * @param blockPos Block position x,y,z
   * @param originPos First block that was broken on the tree
   * @return boolean Was the block placed successfully or not
   */
  private boolean plantSapling(World world, BlockPos blockPos, BlockPos originPos) {
    ItemStack sapling = null;

    // Count should be the blocks in the tree, which could be something in the core Minecraft code
    // But if it is in there, it has not been mapped yet. 100 is a safe ceiling for the time being.
    int counter = 0;
    while (sapling == null && counter <= 100) {
      List<ItemStack> drops = Block.getDrops(world.getBlockState(blockPos), ((ServerWorld) world).getWorldServer(), blockPos, null);
      for (ItemStack item: drops) {
        // Only get sapling drops. There is no sapling tag so any mods using special saplings will
        // have to include sapling in their item/block name
        if (item.getItem().getTranslationKey().contains("sapling")) {
          sapling = item;
        }
      }
      counter++;
    }

    if (sapling == null) {
      return false;
    }

    return world.setBlockState(originPos.add(1, 0, 1), SaplingBlock.getBlockFromItem(sapling.getItem()).getDefaultState());
  }
}
