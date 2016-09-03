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
        return "/treechop <ignoreDur|plantSap|decayLeaves|breakSpeed> value";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List getCommandAliases() {
        return Lists.newArrayList("tch");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new WrongUsageException("/treechop <ignoredur|plantsap|decayleaves|breakspeed> [value]");

        else if (args[0].equals("ignoredur")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop ignoredur [0,1]");

            ConfigHandler.setIgnoreDurability(parseBoolean(args[1]));
            if (ConfigHandler.ignoreDurability)
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Ignore axe durability has been switched " + TextFormatting.GREEN + "ON"));
            else
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Ignore axe durability has been switched " + TextFormatting.RED + "OFF"));

        } else if (args[0].equals("plantsap")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop plantsap [0,1]");

            ConfigHandler.setPlantSapling(parseBoolean(args[1]));
            if (ConfigHandler.plantSapling)
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Auto sapling has been switched " + TextFormatting.GREEN + "ON"));
            else
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Auto sapling has been switched " + TextFormatting.RED + "OFF"));

        } else if (args[0].equals("decayleaves")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop decayleaves [0,1]");

            ConfigHandler.setDecayLeaves(parseBoolean(args[1]));
            sender.addChatMessage(new TextComponentTranslation("Decay leaves set to " + ConfigHandler.decayLeaves));
            if (ConfigHandler.decayLeaves)
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Decay leaves has been switched " + TextFormatting.GREEN + "ON"));
            else
                sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Decay leaves has been switched " + TextFormatting.RED + "OFF"));

        } else if (args[0].equals("breakspeed")) {
            if (args.length != 2)
                throw new WrongUsageException("/treechop breakspeed [value]");

            ConfigHandler.setBreakSpeed(parseInt(args[1], 1, 1000));
            sender.addChatMessage(new TextComponentTranslation("[" + TextFormatting.GOLD + "TCH" + TextFormatting.RESET + "]Break speed has been set to " + ConfigHandler.breakSpeed + ", [DEFAULT: 10]"));

            TreeChopper.network.sendToAll(new ServerMessage(ConfigHandler.breakSpeed));
        } else
            throw new WrongUsageException("/treechop <ignoredur|plantsap|decayleaves|breakspeed> [value]");

        ConfigHandler.writeConfig(ConfigHandler.decayLeaves, ConfigHandler.plantSapling, ConfigHandler.ignoreDurability, ConfigHandler.breakSpeed);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "ignoredur", "plantsap", "decayleaves", "breakspeed");

        return null;
    }
}
