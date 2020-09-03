package treechopper.common.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import treechopper.common.config.Configuration;
import treechopper.common.tree.Tree;

import java.util.*;

public class TreeHandler {

  private static Map<UUID, Tree> m_Trees = new HashMap<>();
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

  public int AnalyzeTree(World world, BlockPos treePos, PlayerEntity entityPlayer) {
    Queue<BlockPos> queuedBlocks = new LinkedList<>();
    Set<BlockPos> tmpBlocks = new HashSet<>();
    Set<BlockPos> checkedBlocks = new HashSet<>();
    
    BlockPos currentPos;
    Block logBlock = world.getBlockState(treePos).getBlock();
    tree = new Tree();

    queuedBlocks.add(treePos);
    tree.InsertWood(treePos);

    while (!queuedBlocks.isEmpty()) {

      currentPos = queuedBlocks.remove();
      checkedBlocks.add(currentPos);

      tmpBlocks.addAll(LookAroundBlock(logBlock, currentPos, world, checkedBlocks));
      queuedBlocks.addAll(tmpBlocks);
      // TODO: smells bad but inf loops
      checkedBlocks.addAll(tmpBlocks);
      tmpBlocks.clear();
    }

    Set<BlockPos> tmpLeaves = new HashSet<>();
    tmpLeaves.addAll(tree.GetM_Leaves());

    for (BlockPos currentLeaf : tmpLeaves) {
      checkedBlocks.add(currentLeaf);
      LookAroundBlock(null, currentLeaf, world, checkedBlocks);
    }

    tree.setM_Position(treePos);
    m_Trees.put(entityPlayer.getUniqueID(), tree);

    return tree.GetLogCount();
  }

  private Queue<BlockPos> LookAroundBlock(Block originBlock, BlockPos currentPos, World world, Set<BlockPos> checkedBlocks) {

    Queue<BlockPos> queuedBlocks = new LinkedList<>();
    BlockPos tmpPos;

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
          tmpPos = new BlockPos(currentPos.getX() + i, currentPos.getY() + j, currentPos.getZ() + k);
          if (CheckBlock(world, tmpPos, checkedBlocks, originBlock)) {
            queuedBlocks.add(tmpPos);
          }
        }
      }
    }

    return queuedBlocks;
  }

  // TODO: check block for what?
  private boolean CheckBlock(World world, BlockPos blockPos, Set<BlockPos> checkedBlocks, Block originBlock) {
    if (checkedBlocks.contains(blockPos)) {
      return false;
    }

    if (world.getBlockState(blockPos).getBlock() != originBlock) {
      if (Configuration.COMMON.plantSapling.get() && world.getBlockState(blockPos).getBlock().getMaterialColor() == MaterialColor.FOLIAGE && tree.GetM_Leaves().isEmpty()) {
        tree.InsertLeaf(blockPos);
      }

      if (Configuration.COMMON.decayLeaves.get() && world.getBlockState(blockPos).getBlock().getMaterialColor() == MaterialColor.FOLIAGE) {
        tree.InsertLeaf(blockPos);
        return false;
      }
    }

    tree.InsertWood(blockPos);

    return true;
  }

  public void DestroyTree(World world, PlayerEntity entityPlayer) {
    if (m_Trees.containsKey(entityPlayer.getUniqueID())) {
      Tree tmpTree = m_Trees.get(entityPlayer.getUniqueID());

      for (BlockPos logPos : tmpTree.GetM_Wood()) {
        world.destroyBlock(logPos, true);
        world.setBlockState(logPos, Blocks.AIR.getDefaultState());
      }

      if (Configuration.COMMON.plantSapling.get() && !tmpTree.GetM_Leaves().isEmpty()) {
        //TODO: rewrite to use GetLeavesCount
        BlockPos tmpPosition = getLastElement(tmpTree.GetM_Leaves());
        PlantSapling(world, tmpPosition, tmpTree.getM_Position());
      }

      if (Configuration.COMMON.decayLeaves.get()) {
        for (BlockPos leafPos : tmpTree.GetM_Leaves()) {
          world.destroyBlock(leafPos, true);
          world.setBlockState(leafPos, Blocks.AIR.getDefaultState());
        }
      }
    }
  }

  private void PlantSapling(World world, BlockPos blockPos, BlockPos originPos) {
    FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((ServerWorld) world);

    Set<ItemStack> leafDrop = new HashSet<>();
    BlockPos plantPos1 = new BlockPos(originPos.getX() - 1, originPos.getY(), originPos.getZ() - 1);
    int counter = 0;

    while (leafDrop.isEmpty() && counter <= 100) {
      NonNullList<ItemStack> tmpList = NonNullList.create();
      world.getBlockState(blockPos).getBlock().harvestBlock(world, fakePlayer, blockPos, world.getBlockState(blockPos), null, new ItemStack(Blocks.OAK_WOOD));
      leafDrop.addAll(tmpList);

      counter++;
    }

    if (leafDrop.isEmpty()) {
      return;
    }

    fakePlayer.setHeldItem(Hand.MAIN_HAND, leafDrop.iterator().next());

    for (ItemStack itemStack : leafDrop) {
      itemStack.onItemUse(new ItemUseContext(fakePlayer, Hand.MAIN_HAND, new BlockRayTraceResult(Vector3d.ZERO, Direction.UP, blockPos, false)));
    }
  }
}
