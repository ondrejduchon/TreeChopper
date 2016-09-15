package treechopper.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import treechopper.core.ConfigHandler;
import treechopper.core.ServerMessage;
import treechopper.core.StaticHandler;
import treechopper.core.TreeChopper;

import java.util.List;

/**
 * Created by Duchy on 8/31/2016.
 */

public class Commands extends CommandBase {

    @Override
    public String getCommandName() {
        return "treechop";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Type \"/treechop help\" for help";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List getCommandAliases() {
        return Lists.newArrayList("tch");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender.canCommandSenderUseCommand(2, this.getCommandName())) {
            if (args.length < 1)
                throw new WrongUsageException("Type \"/treechop help\" for help");

            else if (args[0].equals("ignoredur")) {
                if (args.length != 2)
                    throw new WrongUsageException("/treechop ignoredur [0,1]");

                ConfigHandler.setIgnoreDurability(parseBoolean(args[1]));
                if (ConfigHandler.ignoreDurability)
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Ignore axe durability has been switched " + TextFormatting.GREEN + "ON"));
                else
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Ignore axe durability has been switched " + TextFormatting.RED + "OFF"));

            } else if (args[0].equals("plantsap")) {
                if (args.length != 2)
                    throw new WrongUsageException("/treechop plantsap [0,1]");

                ConfigHandler.setPlantSapling(parseBoolean(args[1]));
                if (ConfigHandler.plantSapling)
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Auto sapling has been switched " + TextFormatting.GREEN + "ON"));
                else
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Auto sapling has been switched " + TextFormatting.RED + "OFF"));

            } else if (args[0].equals("decayleaves")) {
                if (args.length != 2)
                    throw new WrongUsageException("/treechop decayleaves [0,1]");

                ConfigHandler.setDecayLeaves(parseBoolean(args[1]));
                if (ConfigHandler.decayLeaves)
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Decay leaves has been switched " + TextFormatting.GREEN + "ON"));
                else
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Decay leaves has been switched " + TextFormatting.RED + "OFF"));

            } else if (args[0].equals("breakspeed")) {
                if (args.length != 2)
                    throw new WrongUsageException("/treechop breakspeed [value]");

                ConfigHandler.setBreakSpeed(parseInt(args[1], 1, 1000));
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Break speed has been set to " + ConfigHandler.breakSpeed + ", [DEFAULT: 10]"));

                TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed));

            } else if (args[0].equals("info") || args[0].equals("?")) {
                printSettings(sender);

            } else if (args[0].equals("printname")) {

            } else if (args[0].equals("help")) {
                usage(sender, 2);

            } else if (args[0].equals("plantsaptree")) {
                if (args.length != 2)
                    throw new WrongUsageException("/treechop plantsaptree [value]");

                ConfigHandler.setPlantSaplingTree(parseBoolean(args[1]));
                if (ConfigHandler.plantSaplingTree)
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Planting on tree position has been switched " + TextFormatting.GREEN + "ON"));
                else
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Planting on tree position has been switched " + TextFormatting.RED + "OFF"));

            } else
                throw new WrongUsageException("Type \"/treechop help\" for help");
        } else {
            if (args.length < 1)
                throw new WrongUsageException("Type \"/treechop help\" for help");

            else if (args[0].equals("info") || args[0].equals("?")) {
                printSettings(sender);

            } else if (args[0].equals("printname")) {

            } else if (args[0].equals("help")) {
                usage(sender, 0);

            } else
                throw new WrongUsageException("Type \"/treechop help\" for help");
        }

        if (args[0].equals("printname")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop printname [value]");

            printName(parseBoolean(args[1]), sender);
        }

        ConfigHandler.writeConfig(ConfigHandler.decayLeaves, ConfigHandler.plantSapling, ConfigHandler.ignoreDurability, ConfigHandler.breakSpeed, ConfigHandler.plantSaplingTree);
    }

    private void printSettings(ICommandSender sender) {
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GOLD + "Tree Chopper" + TextFormatting.RESET + " settings: \n--------------------"));
        sender.addChatMessage(new TextComponentTranslation("Break speed: " + TextFormatting.GOLD + ConfigHandler.breakSpeed));

        if (ConfigHandler.decayLeaves)
            sender.addChatMessage(new TextComponentTranslation("Leaves decay: " + TextFormatting.GREEN + "ON"));
        else
            sender.addChatMessage(new TextComponentTranslation("Leaves decay: " + TextFormatting.RED + "OFF"));

        if (ConfigHandler.plantSapling)
            sender.addChatMessage(new TextComponentTranslation("Automatic sapling plant: " + TextFormatting.GREEN + "ON"));
        else
            sender.addChatMessage(new TextComponentTranslation("Automatic sapling plant: " + TextFormatting.RED + "OFF"));

        if (ConfigHandler.plantSaplingTree)
            sender.addChatMessage(new TextComponentTranslation("Planting on tree position: " + TextFormatting.GREEN + "ON"));
        else
            sender.addChatMessage(new TextComponentTranslation("Planting on tree position: " + TextFormatting.RED + "OFF"));

        if (ConfigHandler.ignoreDurability)
            sender.addChatMessage(new TextComponentTranslation("Ignore durability: " + TextFormatting.GREEN + "ON"));
        else
            sender.addChatMessage(new TextComponentTranslation("Ignore durability: " + TextFormatting.RED + "OFF"));
    }

    private void printName(boolean print, ICommandSender sender) {
        if (print)
            sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Printing UnlocalizedNames has been switched " + TextFormatting.GREEN + "ON"));
        else
            sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Printing UnlocalizedNames has been switched " + TextFormatting.RED + "OFF"));

        if (print)
            try {
                StaticHandler.playerPrintUnName.add(sender.getCommandSenderEntity().getEntityId());
            } catch (Exception e) {
                sender.addChatMessage(new TextComponentTranslation("You are not a player.."));
            }
        else
            try {
                StaticHandler.playerPrintUnName.remove(sender.getCommandSenderEntity().getEntityId());
            } catch (Exception e) {
                sender.addChatMessage(new TextComponentTranslation("Not in there"));
            }

        //StaticHandler.printName = print;
    }

    private void usage(ICommandSender sender, int permissins) {
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GOLD + "Format: /treechop <argument> [value]"));
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GRAY + "Arguments:"));

        switch (permissins) {
            case 0:
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "?" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Print info about server settings"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "printname" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Logging UnlocalizedName of target block and main hand item, on mouse click"));
                break;
            case 2:
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "?" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Print info about server settings"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "ignoredur" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Ignoring duration of axe."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "plantsap" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Auto plant sapling, around his trunk."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "plantsaptree" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Set plant position on tree position."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "decayleaves" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Decay leaves with tree fall."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "breakspeed" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Set speed of breaking tree. Default: 10"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "printname" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Logging UnlocalizedName of target block and main hand item, on mouse click"));
                break;
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "ignoredur", "plantsap", "plantsaptree", "decayleaves", "breakspeed", "info", "printname");

        return null;
    }
}
