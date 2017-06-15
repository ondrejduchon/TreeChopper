package treechopper.common.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import treechopper.common.tree.Tree;

import java.util.*;

public class TreeHandler {

    public static final int AREA_LOOK = 3;

    private Tree tree;

    public void AnalyzeTree(World world, BlockPos blockPos, EntityPlayer entityPlayer) {

        Queue<BlockPos> queuedBlocks = new LinkedList<BlockPos>();
        Set<BlockPos> checkedBlocks = new HashSet<BlockPos>();
        BlockPos currentPos, tmpPos;
        Block logBlock = world.getBlockState(blockPos).getBlock();
        tree = new Tree();

        queuedBlocks.add(blockPos);

        while (!queuedBlocks.isEmpty()) {

            currentPos = queuedBlocks.remove();
            checkedBlocks.add(currentPos);

            tmpPos = new BlockPos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX() - 1, currentPos.getY(), currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY() - 1, currentPos.getZ());
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }

            tmpPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ() - 1);
            if (CheckBlock(world, tmpPos, checkedBlocks, logBlock)) {
                queuedBlocks.add(tmpPos);
            }
        }

        m_Trees.put(entityPlayer.getUniqueID(), tree);
    }

    private boolean CheckBlock(World world, BlockPos blockPos, Set<BlockPos> checkedBlocks, Block originBlock) {

        if (checkedBlocks.contains(blockPos)) {
            return false;
        }

        if (world.getBlockState(blockPos).getBlock() != originBlock) {
            return false;
        }

        tree.InsertWood(blockPos);

        return true;
    }

    public void DestroyTree(World world, EntityPlayer entityPlayer) {

    }

    private static Map<UUID, Tree> m_Trees;
}
