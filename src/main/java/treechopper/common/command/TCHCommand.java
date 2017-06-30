package treechopper.common.command;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import treechopper.common.config.ConfigurationHandler;
import treechopper.common.network.ClientSettingsMessage;
import treechopper.core.TreeChopper;
import treechopper.proxy.CommonProxy;

import javax.annotation.Nullable;
import java.util.List;

public class TCHCommand extends CommandBase {

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
        return I18n.format("command.errorMessage");
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

            try {
                CommonProxy.m_PlayerPrintNames.put(sender.getCommandSenderEntity().getPersistentID(), parseBoolean(args[1]));
            } catch (Exception e) {
                System.out.printf("Not a player");
            }
            return;
        }

        if (sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName())) {
            if (args.length < 1) {
                throw new WrongUsageException(I18n.format("command.errorMessage"));

            } else if (args[0].equals("help")) {
                GetUsage(sender);

            } else if (args[0].equals("info")) {
                GetInfo(sender);

            } else if (args[0].equals("decLeaves")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch decLeaves 0/1");
                }

                ConfigurationHandler.setDecayLea(parseBoolean(args[1]));
                if (ConfigurationHandler.decayLeaves) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + I18n.format("command.decayLeavesSwitch") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + I18n.format("command.decayLeavesSwitch") + " " + TextFormatting.RED + "OFF"));
                }

            } else if (args[0].equals("plantSap")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch plantSap 0/1");
                }

                ConfigurationHandler.setPlantSap(parseBoolean(args[1]));
                if (ConfigurationHandler.plantSapling) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + I18n.format("command.plantSaplingSwitch") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + I18n.format("command.plantSaplingSwitch") + " " + TextFormatting.RED + "OFF"));
                }

            } else if (args[0].equals("revShift")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch revShift 0/1");
                }

                ConfigurationHandler.setReverseShi(parseBoolean(args[1]));
                if (ConfigurationHandler.reverseShift) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + I18n.format("command.reverseShiftSwitch") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + I18n.format("command.reverseShiftSwitch") + " " + TextFormatting.RED + "OFF"));
                }

                TreeChopper.m_Network.sendToAll(new ClientSettingsMessage(ConfigurationHandler.reverseShift));
            } else {
                throw new WrongUsageException(I18n.format("command.errorMessage"));
            }
        } else {
            sender.sendMessage(new TextComponentTranslation(TextFormatting.RED + I18n.format("command.permissions")));
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
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GOLD + "Tree Chopper"));

        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch info" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + I18n.format("command.infoInfo")));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch plantSap" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + I18n.format("command.plantSaplingInfo")));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch decLeaves" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + I18n.format("command.decayLeavesInfo")));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch revShift" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + I18n.format("command.reverseShiftInfo")));
    }

    private void GetInfo(ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GOLD + "Tree Chopper"));

        sender.sendMessage(new TextComponentTranslation(I18n.format("command.decayLeaves") + " " + TextFormatting.ITALIC + ConfigurationHandler.decayLeaves));
        sender.sendMessage(new TextComponentTranslation(I18n.format("command.plantSapling") + " " + TextFormatting.ITALIC + ConfigurationHandler.plantSapling));
        sender.sendMessage(new TextComponentTranslation(I18n.format("command.reverseShift") + " " + TextFormatting.ITALIC + ConfigurationHandler.reverseShift));
    }
}
