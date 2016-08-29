package treechopper.core;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPSapling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Duchy on 8/22/2016.
 */
public class TreeHandler {
    private Set<BlockPos> tree;
    private static Set<BlockPos> leaves = new HashSet<BlockPos>();
    private static Set<BlockPos> leavesTmp = new HashSet<BlockPos>();
    private String leafVariant;

    public float treeAnalyze(PlayerInteractEvent event, BlockPos position) {
        Block logType = event.getWorld().getBlockState(event.getPos()).getBlock();
        Queue<BlockPos> logsToCheck = new LinkedList<BlockPos>();
        tree = new HashSet<BlockPos>();
        BlockPos curBlock = position;
        boolean isAnyNeighbour;

        tree.add(position);

        do {
            for (int i = curBlock.getY() - 1; i <= curBlock.getY() + 2; i++) {
                if (logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ())), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ()));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ())), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ()));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1));
                }
            }

            if (logAnalyze(logType, (new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ())), event, tree)) {
                logsToCheck.add(new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ()));
                tree.add(new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ()));
            }

            if (logAnalyze(logType, (new BlockPos(curBlock.getX(), curBlock.getY() - 1, curBlock.getZ())), event, tree)) {
                logsToCheck.add(new BlockPos(curBlock.getX(), curBlock.getY() - 1, curBlock.getZ()));
                tree.add(new BlockPos(curBlock.getX(), curBlock.getY() - 1, curBlock.getZ()));
            }

            if (!logsToCheck.isEmpty()) {
                curBlock = logsToCheck.remove();
                tree.add(curBlock);
                isAnyNeighbour = true;
            } else
                isAnyNeighbour = false;

        } while (isAnyNeighbour);

        return tree.size();
    }

    private boolean logAnalyze(Block logType, BlockPos position, PlayerInteractEvent event, Set<BlockPos> visitedLogs) {
        if (event.getWorld().getBlockState(position).getBlock() != logType) {
            if (event.getWorld().getBlockState(position).getBlock().isLeaves(event.getWorld().getBlockState(position), event.getWorld(), position)) {
                leaves.add(position);
                lookAround(position, event.getWorld());
            }
            return false;
        }

        if (visitedLogs.contains(position))
            return false;

        if (position.getY() < event.getPos().getY())
            return false;

        if (event.getWorld().getBlockState(position).getPropertyNames().toString().contains("variant"))
            return ((event.getWorld().getBlockState(position).getValue(logType.getBlockState().getProperty("variant")) == event.getWorld().getBlockState(event.getPos()).getValue(logType.getBlockState().getProperty("variant"))));

        return true; // Ignoring log variant - doesnt have one..
    }

    private void lookAround(BlockPos position, World world) {

        if (world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ())).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ())), world, position))
            leavesTmp.add(new BlockPos(position.getX() + 1, position.getY(), position.getZ()));

        if (world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ())).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ())), world, position))
            leavesTmp.add(new BlockPos(position.getX() - 1, position.getY(), position.getZ()));

        if (world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() + 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() + 1)), world, position))
            leavesTmp.add(new BlockPos(position.getX(), position.getY(), position.getZ() + 1));

        if (world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 1)), world, position))
            leavesTmp.add(new BlockPos(position.getX(), position.getY(), position.getZ() - 1));

        if (world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1)), world, position))
            leavesTmp.add(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1));

        if (world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1)), world, position))
            leavesTmp.add(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1));

        if (world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1)), world, position))
            leavesTmp.add(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1));

        if (world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1)), world, position))
            leavesTmp.add(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1));

    }

    public int treeDestroy(BlockEvent.BreakEvent event) {
        int logCount = tree.size();

        for (BlockPos blockPos : tree) {

            if (blockPos.getX() != event.getPos().getX() || blockPos.getY() != event.getPos().getY() || blockPos.getZ() != event.getPos().getZ())
                event.getWorld().destroyBlock(blockPos, true);
        }

        for (BlockPos blockPos : leavesTmp)
            leaves.add(blockPos);

        leavesTmp.clear();

        for (BlockPos blockPos : leaves)
            lookAround(blockPos, event.getWorld());

        for (BlockPos blockPos : leavesTmp)
            leaves.add(blockPos);

        for (BlockPos blockPos : leaves) {
            if (event.getWorld().getBlockState(blockPos).getPropertyNames().toString().contains("variant"))
                leafVariant = event.getWorld().getBlockState(blockPos).getValue(event.getWorld().getBlockState(blockPos).getBlock().getBlockState().getProperty("variant")).toString().toUpperCase();
            else
                leafVariant = null;
            event.getWorld().destroyBlock(blockPos, true);
        }

        if (leaves.size() == 0) // Tree without leaves
            leafVariant = null;

        leavesTmp.clear();
        leaves.clear();
        return logCount;
    }

    public boolean plantSapling(World world, BlockPos position) {
        Block logType = world.getBlockState(position).getBlock(), sapling;

        if (leafVariant == null)
            return false;

        if (logType == Blocks.LOG || logType == Blocks.LOG2) {
            sapling = Blocks.SAPLING;
            world.setBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 2), sapling.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.valueOf(leafVariant)));
        } else if (logType == BOPBlocks.log_0 || logType == BOPBlocks.log_1 || logType == BOPBlocks.log_2 || logType == BOPBlocks.log_3 || logType == BOPBlocks.log_4) // Biomes O Plenty
            world.setBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 2), BlockBOPSapling.paging.getVariantState(BOPTrees.valueOf(leafVariant)));

        return false;
    }
}
