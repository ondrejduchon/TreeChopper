package treechopper.common.command;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.console.ConsoleFormatter;
import treechopper.client.gui.TCGuiConfig;
import treechopper.core.ConfigHandler;

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
            throw new WrongUsageException("/treechop <ignoreDur|plantSapp|decayLeaves|breakSpeed> [value]");

        else if (args[0].equals("ignoreDur")) {
            if (args.length != 2) {
                throw new WrongUsageException("/treechop ignoreDur [0,1]");
            }

            ConfigHandler.setIgnoreDurability(parseBoolean(args[1]));
            sender.addChatMessage(new TextComponentTranslation("Ignore axe durability set to " + ConfigHandler.ignoreDurability));

        } else if (args[0].equals("plantSap")) {
            if (args.length != 2) {
                throw new WrongUsageException("/treechop plantSap [0,1]");
            }

            ConfigHandler.setPlantSapling(parseBoolean(args[1]));
            sender.addChatMessage(new TextComponentTranslation("Plant sapling automatically set to " + ConfigHandler.plantSapling));

        } else if (args[0].equals("decayLeaves")) {
            if (args.length != 2) {
                throw new WrongUsageException("/treechop decayLeaves [0,1]");
            }

            ConfigHandler.setDecayLeaves(parseBoolean(args[1]));
            sender.addChatMessage(new TextComponentTranslation("Decay leaves set to " + ConfigHandler.decayLeaves));

        } else if (args[0].equals("breakSpeed")) {
            if (args.length != 2) {
                throw new WrongUsageException("/treechop breakSpeed [value]");
            }

            ConfigHandler.setBreakSpeed(parseDouble(args[1], 0, 100));
            sender.addChatMessage(new TextComponentTranslation("Break speed [DEFAULT: 1] set to " + ConfigHandler.breakSpeed));
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "ignoreDur", "plantSap", "decayLeaves", "breakSpeed");

        return null;
    }
}
