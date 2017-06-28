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
import treechopper.common.config.ConfigurationHandler;
import treechopper.proxy.CommonProxy;

import javax.annotation.Nullable;
import java.util.List;

public class TCHCommand extends CommandBase {

    private static final String m_ErrorMessage = "Type \"/tch help\" for help";

    @Override
    public String getName() {
        return "treechopper";
    }

    @Override
    public List<String> getAliases() {
        return Lists.newArrayList("tch");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return m_ErrorMessage;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length > 0 && args[0].equals("printName")) {
            if (args.length != 2) {
                throw new WrongUsageException("/tch printName 0/1");
            }

            CommonProxy.m_PrintNames = parseBoolean(args[1]);
            return;
        }

        if (sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName())) {
            if (args.length < 1) {
                throw new WrongUsageException(m_ErrorMessage);

            } else if (args[0].equals("help")) {
                GetUsage(sender);

            } else if (args[0].equals("info")) {
                GetInfo(sender);

            } else if (args[0].equals("decLeaves")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch decLeaves 0/1");
                }

                ConfigurationHandler.decayLeaves = parseBoolean(args[1]);
                if (ConfigurationHandler.decayLeaves) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Decay leaves has been switched " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Decay leaves has been switched " + TextFormatting.RED + "OFF"));
                }

            } else if (args[0].equals("plantSap")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch plantSap 0/1");
                }

                ConfigurationHandler.plantSapling = parseBoolean(args[1]);
                if (ConfigurationHandler.plantSapling) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Auto planting has been switched " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Auto planting has been switched " + TextFormatting.RED + "OFF"));
                }

            } else if (args[0].equals("revShift")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch revShift 0/1");
                }

                ConfigurationHandler.reverseShift = parseBoolean(args[1]);
                if (ConfigurationHandler.reverseShift) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Reverse function has been switched " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] Reverse function has been switched " + TextFormatting.RED + "OFF"));
                }
            } else {
                throw new WrongUsageException(m_ErrorMessage);
            }
        } else {
            sender.sendMessage(new TextComponentTranslation(TextFormatting.RED + "You do not have permission to use this command"));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "info", "plantSap", "decLeaves", "revShift", "help", "printName");
        }

        return null;
    }

    private void GetUsage(ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GOLD + "Format: /tch <argument> [value]"));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GRAY + "Arguments:"));

        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "info" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + " Print info about server settings"));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "plantSap" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + " Auto plant sapling, around his trunk."));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "decLeaves" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + " Decay leaves with tree fall."));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "revShift" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + " Reverse shift function"));
    }

    private void GetInfo(ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GOLD + "Tree Chopper"));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GRAY + "Settings:"));

        sender.sendMessage(new TextComponentTranslation("Leaves decay: " + TextFormatting.ITALIC + ConfigurationHandler.decayLeaves));
        sender.sendMessage(new TextComponentTranslation("Automatic sapling plant: " + TextFormatting.ITALIC + ConfigurationHandler.plantSapling));
        sender.sendMessage(new TextComponentTranslation("Reverse func: " + TextFormatting.ITALIC + ConfigurationHandler.reverseShift));
    }
}
