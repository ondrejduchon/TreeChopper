package treechopper.common.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.tree.Tree;

import java.util.*;

public class TreeHandler {

    public int AnalyzeTree(World world, BlockPos blockPos, EntityPlayer entityPlayer) {

        Queue<BlockPos> queuedBlocks = new LinkedList<>();
        Set<BlockPos> checkedBlocks = new HashSet<>();
        BlockPos currentPos;
        Block logBlock = world.getBlockState(blockPos).getBlock();
        tree = new Tree();

        queuedBlocks.add(blockPos);

        while (!queuedBlocks.isEmpty()) {

            currentPos = queuedBlocks.remove();
            checkedBlocks.add(currentPos);

            queuedBlocks.addAll(LookAroundBlock(logBlock, currentPos, world, checkedBlocks));
        }

        m_Trees.put(entityPlayer.getPersistentID(), tree);

        return tree.GetLogCount();
    }

    private Queue<BlockPos> LookAroundBlock(Block logBlock, BlockPos currentPos, World world, Set<BlockPos> checkedBlocks) {

        Queue<BlockPos> queuedBlocks = new LinkedList<>();
        BlockPos tmpPos;

        for (int i = -1; i <= 1; i++) {
            tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY() + i, currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + i, currentPos.getZ() + 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY() + i, currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + i, currentPos.getZ() - 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY() + i, currentPos.getZ() + 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY() + i, currentPos.getZ() - 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY() + i, currentPos.getZ() + 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY() + i, currentPos.getZ() - 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + i, currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }
        }

        return queuedBlocks;
    }

    private boolean CheckBlock(World world, BlockPos blockPos, Set<BlockPos> checkedBlocks, Block originBlock) {

        if (checkedBlocks.contains(blockPos)) {
            return false;
        }

        if (world.getBlockState(blockPos).getBlock() != originBlock) {
            if (ConfigurationHandler.decayLeaves && world.getBlockState(blockPos).getBlock().isLeaves(world.getBlockState(blockPos), world, blockPos)) {
                tree.InsertLeaf(blockPos);

                return true;
            } else {
                return false;
            }
        }

        tree.InsertWood(blockPos);

        return true;
    }

    public void DestroyTree(World world, EntityPlayer entityPlayer) {

        int soundReduced = 0;

        if (m_Trees.containsKey(entityPlayer.getPersistentID())) {

            Tree tmpTree = m_Trees.get(entityPlayer.getPersistentID());

            for (BlockPos blockPos : tmpTree.GetM_Wood()) {

                if (soundReduced <= 1) {
                    world.destroyBlock(blockPos, true);
                } else {
                    world.getBlockState(blockPos).getBlock().dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), 0);
                }

                world.setBlockToAir(blockPos);

                soundReduced++;
            }
        }
    }

    private static Map<UUID, Tree> m_Trees = new HashMap<>();
    private Tree tree;
}
