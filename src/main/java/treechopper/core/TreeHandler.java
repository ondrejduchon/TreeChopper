package treechopper.core;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
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
    private String leafVariant;

    public float treeAnalyze(PlayerInteractEvent event, BlockPos position) {
        Block logType = event.getWorld().getBlockState(event.getPos()).getBlock();
        World world = event.getWorld();
        Queue<BlockPos> logsToCheck = new LinkedList<BlockPos>();
        tree = new HashSet<BlockPos>();
        BlockPos curBlock = position;
        boolean isAnyNeighbour;

        tree.add(position);

        do {
            for (int i = curBlock.getY() - 1; i <= curBlock.getY() + 2; i++) {
                if (world.getBlockState(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ())).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ())), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ()));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ())).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ())), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ()));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1)).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1)).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1)).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1)).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1)).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1));
                }

                if (world.getBlockState(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1)).getBlock() == logType &&
                        logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1)), event, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1));
                }
            }

            if (world.getBlockState(new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ())).getBlock() == logType &&
                    logAnalyze(logType, (new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ())), event, tree)) {
                logsToCheck.add(new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ()));
                tree.add(new BlockPos(curBlock.getX(), curBlock.getY() + 1, curBlock.getZ()));
            }

            if (world.getBlockState(new BlockPos(curBlock.getX(), curBlock.getY() - 1, curBlock.getZ())).getBlock() == logType &&
                    logAnalyze(logType, (new BlockPos(curBlock.getX(), curBlock.getY() - 1, curBlock.getZ())), event, tree)) {
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
        if (visitedLogs.contains(position))
            return false;

        if (position.getY() < event.getPos().getY())
            return false;

        if (event.getWorld().getBlockState(position).getPropertyNames().toString().contains("variant"))
            return ((event.getWorld().getBlockState(position).getValue(logType.getBlockState().getProperty("variant")) == event.getWorld().getBlockState(event.getPos()).getValue(logType.getBlockState().getProperty("variant"))));

        return true; // Ignoring log variant - doesnt have one..
    }

    public int treeDestroy(BlockEvent.BreakEvent event) {
        int logCount = tree.size();

        for (BlockPos blockPos : tree) {

            if (blockPos.getX() != event.getPos().getX() || blockPos.getY() != event.getPos().getY() || blockPos.getZ() != event.getPos().getZ())
                event.getWorld().destroyBlock(blockPos, true);
        }

        return logCount;
    }

    public boolean plantSapling(World world, BlockPos position) {
        Block logType = world.getBlockState(position).getBlock(), sapling;
        String logVariant;
        if (world.getBlockState(position).getPropertyNames().toString().contains("variant"))
            logVariant = world.getBlockState(position).getValue(logType.getBlockState().getProperty("variant")).toString().toUpperCase();
        else
            return false;

        if (logType == Blocks.LOG || logType == Blocks.LOG2) {
            sapling = Blocks.SAPLING;
            world.setBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 2), sapling.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.valueOf(logVariant)));
        } else if (logType == BOPBlocks.log_0 || logType == BOPBlocks.log_1 || logType == BOPBlocks.log_2 || logType == BOPBlocks.log_3 || logType == BOPBlocks.log_4) {
            //world.setBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 2), BlockBOPSapling.paging.getVariantState(BOPTrees.valueOf(" ")));
            System.out.println(logVariant);
        }

        return false;
    }
}
