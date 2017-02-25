package treechopper.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import treechopper.common.config.ConfigHandler;
import treechopper.common.network.ServerMessage;
import treechopper.common.network.sendReverseToClient;
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
    public List<String> getCommandAliases() {
        return Lists.newArrayList("tch");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if ((server.isSinglePlayer()) && server.getServerOwner().equals(sender.getName()) || sender.canCommandSenderUseCommand(2, this.getCommandName())) {
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

                TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed, ConfigHandler.ignoreDurability));

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

                TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed, ConfigHandler.ignoreDurability));

            } else if (args[0].equals("info") || args[0].equals("?")) {
                printSettings(sender);

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

            } else if (args[0].equals("reset")) {

                ConfigHandler.setPlantSaplingTree(false);
                ConfigHandler.setPlantSapling(false);
                ConfigHandler.setBreakSpeed(10);
                ConfigHandler.setDecayLeaves(true);
                ConfigHandler.setIgnoreDurability(false);
                ConfigHandler.setRoots(false);
                ConfigHandler.setReverseShift(false);

                TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed, ConfigHandler.ignoreDurability));

                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Settings has been reset"));
            } else if (args[0].equals("roots")) {
                if (args.length != 2)
                    throw new WrongUsageException("/treechop roots [0,1]");

                ConfigHandler.setRoots(parseBoolean(args[1]));
                if (ConfigHandler.roots)
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Breaking roots has been switched " + TextFormatting.GREEN + "ON"));
                else
                    sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Breaking roots has been switched " + TextFormatting.RED + "OFF"));
            } else if (!args[0].equals("printname") && !args[0].equals("reverse"))
                throw new WrongUsageException("Type \"/treechop help\" for help");
        } else {
            if (args.length < 1)
                throw new WrongUsageException("Type \"/treechop help\" for help");

            else if (args[0].equals("info") || args[0].equals("?")) {
                printSettings(sender);

            } else if (args[0].equals("help")) {
                usage(sender, 0);

            } else if (!args[0].equals("printname") && !args[0].equals("reverse"))
                throw new WrongUsageException("Type \"/treechop help\" for help");
        }

        if (args[0].equals("printname")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop printname [value]");

            printName(parseBoolean(args[1]), sender);
        } else if (args[0].equals("reverse")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop reverse [0,1]");

            boolean reverseShift = parseBoolean(args[1]);

            if (reverseShift)
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Reverse function has been switched " + TextFormatting.GREEN + "ON"));
            else
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Reverse function has been switched " + TextFormatting.RED + "OFF"));

            try {
                StaticHandler.playerReverseShift.put(sender.getCommandSenderEntity().getEntityId(), reverseShift);
                ConfigHandler.setReverseShift(reverseShift);

                TreeChopper.network.sendTo(new sendReverseToClient(reverseShift), (EntityPlayerMP) sender);
            } catch (Exception e) {
                sender.addChatMessage(new TextComponentTranslation("You are not a player.."));
            }
        }

        ConfigHandler.writeConfig(ConfigHandler.decayLeaves, ConfigHandler.plantSapling, ConfigHandler.ignoreDurability, ConfigHandler.breakSpeed, ConfigHandler.plantSaplingTree, ConfigHandler.roots, ConfigHandler.reverseShift);
    }

    private void printSettings(ICommandSender sender) {
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GOLD + "Tree Chopper"));
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GRAY + "Settings:"));

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

        if (ConfigHandler.roots)
            sender.addChatMessage(new TextComponentTranslation("Breaking roots: " + TextFormatting.GREEN + "ON"));
        else
            sender.addChatMessage(new TextComponentTranslation("Breaking roots: " + TextFormatting.RED + "OFF"));

        try {
            if (StaticHandler.playerReverseShift.get(sender.getCommandSenderEntity().getEntityId()))
                sender.addChatMessage(new TextComponentTranslation("Reverse func: " + TextFormatting.GREEN + "ON"));
            else
                sender.addChatMessage(new TextComponentTranslation("Reverse func: " + TextFormatting.RED + "OFF"));
        } catch (Exception e) {
            sender.addChatMessage(new TextComponentTranslation("Reverse func: " + TextFormatting.RED + "-"));
        }

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
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Printing UnlocalizedNames has been switched " + TextFormatting.RED + "OFF"));
            }
        else
            try {
                StaticHandler.playerPrintUnName.remove(sender.getCommandSenderEntity().getEntityId());
            } catch (Exception e) {
                sender.addChatMessage(new TextComponentTranslation("No effect.. (Not been switched on)"));
            }
    }

    private void usage(ICommandSender sender, int permissins) {
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GOLD + "Format: /treechop <argument> [value]"));
        sender.addChatMessage(new TextComponentTranslation(TextFormatting.GRAY + "Arguments:"));

        switch (permissins) {
            case 0:
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "?" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Print info about server settings"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "printname" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Logging UnlocalizedName of target block and main hand item, on mouse click"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "reverse" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Reverse shift function"));

                break;
            case 2:
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "?" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Print info about server settings"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "ignoredur" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Ignoring duration of axe."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "plantsap" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Auto plant sapling, around his trunk."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "plantsaptree" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Set plant position on tree position."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "decayleaves" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Decay leaves with tree fall."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "breakspeed" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Set speed of breaking tree. Default: 10"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "printname" + TextFormatting.RESET + " [value] -" + TextFormatting.ITALIC + " Logging UnlocalizedName of target block and main hand item, on mouse click"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "reset" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Reset settings to default."));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "roots" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Break roots - dig 3 blocks under ground"));
                sender.addChatMessage(new TextComponentTranslation(TextFormatting.AQUA + "reverse" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Reverse shift function"));

                break;
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "ignoredur", "plantsap", "plantsaptree", "decayleaves", "breakspeed", "info", "printname", "help", "reset", "roots", "reverse");

        return null;
    }
}
