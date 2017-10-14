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
        return GetTranslatedText(sender, "command.errorMessage", "Type \"/tch help\" for help");
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
                throw new WrongUsageException(GetTranslatedText(sender, "command.errorMessage", "Type \"/tch help\" for help"));

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
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.decayLeavesSwitch", "Decay leaves has been switched") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.decayLeavesSwitch", "Decay leaves has been switched") + " " + TextFormatting.RED + "OFF"));
                }

            } else if (args[0].equals("plantSap")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch plantSap 0/1");
                }

                ConfigurationHandler.setPlantSap(parseBoolean(args[1]));
                if (ConfigurationHandler.plantSapling) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.plantSaplingSwitch", "Auto planting has been switched") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.plantSaplingSwitch", "Auto planting has been switched") + " " + TextFormatting.RED + "OFF"));
                }

            } else if (args[0].equals("revShift")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch revShift 0/1");
                }

                ConfigurationHandler.setReverseShi(parseBoolean(args[1]));
                if (ConfigurationHandler.reverseShift) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.reverseShiftSwitch", "Reverse function has been switched") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.reverseShiftSwitch", "Reverse function has been switched") + " " + TextFormatting.RED + "OFF"));
                }

                TreeChopper.m_Network.sendToAll(new ClientSettingsMessage(ConfigurationHandler.reverseShift, ConfigurationHandler.disableShift));
            } else if (args[0].equals("disShift")) {
                if (args.length != 2) {
                    throw new WrongUsageException("/tch disShift 0/1");
                }

                ConfigurationHandler.setDisableShi(parseBoolean(args[1]));
                if (ConfigurationHandler.disableShift) {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.disableShiftSwitch", "Disable shift function has been switched") + " " + TextFormatting.GREEN + "ON"));
                } else {
                    sender.sendMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "] " + GetTranslatedText(sender, "command.disableShiftSwitch", "Disable shift function has been switched") + " " + TextFormatting.RED + "OFF"));
                }

                TreeChopper.m_Network.sendToAll(new ClientSettingsMessage(ConfigurationHandler.disableShift, ConfigurationHandler.disableShift));
            } else {
                throw new WrongUsageException(GetTranslatedText(sender, "command.errorMessage", "Type \"/tch help\" for help"));
            }
        } else {
            sender.sendMessage(new TextComponentTranslation(TextFormatting.RED + GetTranslatedText(sender, "command.permissions", "You do not have permission to use this command")));
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

        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch info" + TextFormatting.RESET + " -" + TextFormatting.ITALIC + GetTranslatedText(sender, "command.infoInfo", " Print info about server settings")));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch plantSap" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + GetTranslatedText(sender, "command.plantSaplingInfo", " Auto plant sapling, around his trunk")));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch decLeaves" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + GetTranslatedText(sender, "command.decayLeavesInfo", " Decay leaves with tree fall")));
        sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA + "/tch revShift" + TextFormatting.RESET + " 0/1 -" + TextFormatting.ITALIC + GetTranslatedText(sender, "command.reverseShiftInfo", " Reverse shift function")));
    }

    private void GetInfo(ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation(TextFormatting.GOLD + "Tree Chopper"));

        sender.sendMessage(new TextComponentTranslation(GetTranslatedText(sender, "command.decayLeaves", "Leaves decay:") + " " + TextFormatting.ITALIC + ConfigurationHandler.decayLeaves));
        sender.sendMessage(new TextComponentTranslation(GetTranslatedText(sender, "command.plantSapling", "Automatic sapling plant:") + " " + TextFormatting.ITALIC + ConfigurationHandler.plantSapling));
        sender.sendMessage(new TextComponentTranslation(GetTranslatedText(sender, "command.reverseShift", "Reverse func:") + " " + TextFormatting.ITALIC + ConfigurationHandler.reverseShift));
        sender.sendMessage(new TextComponentTranslation(GetTranslatedText(sender, "command.disableShift", "Disable shift:") + " " + TextFormatting.ITALIC + ConfigurationHandler.disableShift));
    }

    private String GetTranslatedText(ICommandSender sender, String translateKey, String text) {

        if (sender.getServer() != null && sender.getServer().isSinglePlayer()) {
            return I18n.format(translateKey);
        }

        return text;
    }
}
