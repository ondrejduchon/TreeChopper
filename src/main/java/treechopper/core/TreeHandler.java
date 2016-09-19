package treechopper.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import java.util.*;

/**
 * Created by Duchy on 8/22/2016.
 */

public class TreeHandler {
    private Set<BlockPos> tree;
    private static Set<BlockPos> leaves = new HashSet<BlockPos>();
    private static Set<BlockPos> leavesTmp = new HashSet<BlockPos>();
    private String leafVariant;
    private int leafCount;

    public float treeAnalyze(World world, BlockPos position) {
        Block logType = world.getBlockState(position).getBlock();
        Queue<BlockPos> logsToCheck = new LinkedList<BlockPos>();
        tree = new HashSet<BlockPos>();
        BlockPos curBlock = position;
        boolean isAnyNeighbour;

        leavesTmp.clear();
        leaves.clear();

        tree.add(position);

        do {
            for (int i = curBlock.getY() - 1; i <= curBlock.getY() + 3; i++) {
                if (logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ())), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ()));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ())), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ()));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1)), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() + 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1)), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ() - 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1)), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() + 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1)), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() - 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1)), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1));
                    tree.add(new BlockPos(curBlock.getX() + 1, i, curBlock.getZ() - 1));
                }

                if (logAnalyze(logType, (new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1)), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1));
                    tree.add(new BlockPos(curBlock.getX() - 1, i, curBlock.getZ() + 1));
                }
            }

            for (int i = curBlock.getY() + 1; i < curBlock.getY() + 4; i++)
                if (logAnalyze(logType, (new BlockPos(curBlock.getX(), i, curBlock.getZ())), position, world, tree)) {
                    logsToCheck.add(new BlockPos(curBlock.getX(), i, curBlock.getZ()));
                    tree.add(new BlockPos(curBlock.getX(), i, curBlock.getZ()));
                }

            if (logAnalyze(logType, (new BlockPos(curBlock.getX(), curBlock.getY() - 1, curBlock.getZ())), position, world, tree)) {
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

    private boolean logAnalyze(Block logType, BlockPos position, BlockPos originPos, World world, Set<BlockPos> visitedLogs) {
        if (world.getBlockState(position).getBlock() != logType) {
            if (world.getBlockState(position).getBlock().isLeaves(world.getBlockState(position), world, position))
                leaves.add(position);
            return false;
        }

        if (visitedLogs.contains(position))
            return false;

        if (position.getY() < originPos.getY())
            return false;

        if (world.getBlockState(position).getPropertyNames().toString().contains("variant"))
            return ((world.getBlockState(position).getValue(logType.getBlockState().getProperty("variant")) == world.getBlockState(originPos).getValue(logType.getBlockState().getProperty("variant"))));

        return true; // Ignoring log variant - doesnt have one..
    }

    private void lookAround(BlockPos position, World world, Set<BlockPos> newLeaves) {

        if (world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ())).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ())), world, position))
            if (!leaves.contains(new BlockPos(position.getX() + 1, position.getY(), position.getZ())))
                newLeaves.add(new BlockPos(position.getX() + 1, position.getY(), position.getZ()));

        if (world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ())).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ())), world, position))
            if (!leaves.contains(new BlockPos(position.getX() - 1, position.getY(), position.getZ())))
                newLeaves.add(new BlockPos(position.getX() - 1, position.getY(), position.getZ()));

        if (world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() + 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() + 1)), world, position))
            if (!leaves.contains(new BlockPos(position.getX(), position.getY(), position.getZ() + 1)))
                newLeaves.add(new BlockPos(position.getX(), position.getY(), position.getZ() + 1));

        if (world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ() - 1)), world, position))
            if (!leaves.contains(new BlockPos(position.getX(), position.getY(), position.getZ() - 1)))
                newLeaves.add(new BlockPos(position.getX(), position.getY(), position.getZ() - 1));

        if (world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1)), world, position))
            if (!leaves.contains(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1)))
                newLeaves.add(new BlockPos(position.getX() + 1, position.getY(), position.getZ() + 1));

        if (world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1)), world, position))
            if (!leaves.contains(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1)))
                newLeaves.add(new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1));

        if (world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1)), world, position))
            if (!leaves.contains(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1)))
                newLeaves.add(new BlockPos(position.getX() + 1, position.getY(), position.getZ() - 1));

        if (world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1)).getBlock().isLeaves(world.getBlockState(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1)), world, position))
            if (!leaves.contains(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1)))
                newLeaves.add(new BlockPos(position.getX() - 1, position.getY(), position.getZ() + 1));

    }

    public int treeDestroy(BlockEvent.BreakEvent event) {
        int logCount = tree.size();
        boolean destruction;
        Map<String, Integer> leafVariantCount = new HashMap<String, Integer>();

        for (BlockPos blockPos : tree) {

            if (blockPos.getX() != event.getPos().getX() || blockPos.getY() != event.getPos().getY() || blockPos.getZ() != event.getPos().getZ()) {
                destruction = event.getWorld().destroyBlock(blockPos, true);
                if (!destruction)
                    System.out.println("Problem with block.. " + blockPos);
                event.getWorld().setBlockToAir(blockPos);
            }
        }

        if (!ConfigHandler.decayLeaves) {
            leafCount = leaves.size();
            leavesTmp.clear();
            leaves.clear();
            return logCount;
        }

        for (BlockPos blockPos : leaves) {
            lookAround(blockPos, event.getWorld(), leavesTmp);
        }

        for (BlockPos blockPos : leavesTmp) {
            lookAround(blockPos, event.getWorld(), leaves);
            leaves.add(blockPos);
        }

        for (BlockPos blockPos : leaves) {
            if (event.getWorld().getBlockState(blockPos).getPropertyNames().toString().contains("variant"))
                leafVariant = event.getWorld().getBlockState(blockPos).getValue(event.getWorld().getBlockState(blockPos).getBlock().getBlockState().getProperty("variant")).toString().toUpperCase();
            else
                leafVariant = "notKnown";

            destruction = event.getWorld().destroyBlock(blockPos, true);
            if (!destruction)
                System.out.println("Problem with block.. " + blockPos);
            event.getWorld().setBlockToAir(blockPos);

            if (leafVariantCount.containsKey(leafVariant)) {
                int tmpCount = leafVariantCount.get(leafVariant);
                leafVariantCount.put(leafVariant, ++tmpCount);
            } else
                leafVariantCount.put(leafVariant, 1);
        }

        int maxValue = 0;
        for (Map.Entry<String, Integer> entry : leafVariantCount.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                leafVariant = entry.getKey();
            }
        }

        if (leaves.size() == 0 || logCount < 2) // Tree without leaves
            leafVariant = null;

        leafCount = leaves.size();
        leavesTmp.clear();
        leaves.clear();
        leafVariantCount.clear();
        return logCount;
    }

    public boolean plantSapling(World world, BlockPos position) {
        BlockPos position1 = new BlockPos(position.getX() - 1, position.getY(), position.getZ() - 1), position2 = new BlockPos(position.getX(), position.getY(), position.getZ() + 1);
        int positionsClear = 0;
        boolean planted;

        if (leafVariant == null)
            return false;

        if (world.getBlockState(new BlockPos(position1.getX(), position1.getY() - 1, position1.getZ())).isFullBlock() && !world.getBlockState(position1).isFullBlock())
            positionsClear++;

        if (world.getBlockState(new BlockPos(position2.getX(), position2.getY() - 1, position2.getZ())).isFullBlock() && !world.getBlockState(position2).isFullBlock() && leafCount > 3)
            positionsClear += 2;

        if (ConfigHandler.plantSaplingTree && world.getBlockState(new BlockPos(position2.getX(), position2.getY() - 1, position2.getZ())).isFullBlock() && !world.getBlockState(new BlockPos(position2.getX(), position2.getY() - 1, position2.getZ())).getBlock().isWood(world, new BlockPos(position2.getX(), position2.getY() - 1, position2.getZ()))) { // Plant sapling on tree position
            positionsClear = 1;
            position1 = new BlockPos(position.getX(), position.getY(), position.getZ());
        }

        if (positionsClear == 0)
            return false;

        try {
            switch (positionsClear) {
                case 1:
                    world.setBlockState(position1, Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.valueOf(leafVariant)));
                    break;
                case 2:
                    world.setBlockState(position2, Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.valueOf(leafVariant)));
                    break;
                case 3:
                    world.setBlockState(position1, Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.valueOf(leafVariant)));
                    world.setBlockState(position2, Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.valueOf(leafVariant)));
                    break;
            }
            planted = true;
        } catch (Exception e) {
            planted = false;
        }

        if (!planted && TreeChopper.BoPPresent)
            try {
                switch (positionsClear) {
                    case 1:
                        BOPAPIHandler.plantBOPSapling(world, position1, leafVariant);
                        break;
                    case 2:
                        BOPAPIHandler.plantBOPSapling(world, position2, leafVariant);
                        break;
                    case 3:
                        BOPAPIHandler.plantBOPSapling(world, position1, leafVariant);
                        BOPAPIHandler.plantBOPSapling(world, position2, leafVariant);
                        break;
                }
                planted = true;
            } catch (Exception e) {
                planted = false;
            }

        if (!planted && TreeChopper.ForestryPresent) {
            try {
                switch (positionsClear) {
                    case 1:
                        ForestryHandler.plantForestrySapling(world, position1, leafVariant);
                        break;
                    case 2:
                        ForestryHandler.plantForestrySapling(world, position2, leafVariant);
                        break;
                    case 3:
                        ForestryHandler.plantForestrySapling(world, position1, leafVariant);
                        ForestryHandler.plantForestrySapling(world, position2, leafVariant);
                        break;
                }
                planted = true;
            } catch (Exception e) {
                planted = false;
            }
        }

        if (!planted)
            System.out.println("Leaf variant not recognized..");

        leafVariant = null;
        return false;
    }
}
