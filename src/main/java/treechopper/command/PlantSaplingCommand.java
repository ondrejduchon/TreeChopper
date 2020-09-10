package treechopper.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import treechopper.common.config.Configuration;

/**
 * Plant Sapling toggle command.
 * Turn on or off planting a sapling after chopping down a tree.
 */
public class PlantSaplingCommand {
    private static final String COMMAND_NAME = "plant_sapling";
    private static final String COMMAND_SWITCH_KEY = "command.plant_sapling_switch";

    /**
     * Registers the command to the dispatcher tree's command configuration
     * @return ArgumentBuilder: Attaches the command to the parent "/tch" command
     */
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(COMMAND_NAME)
                .then(
                        Commands.argument("toggle", BoolArgumentType.bool())
                                .executes(source ->
                                        execute(source.getSource(), BoolArgumentType.getBool(source, "toggle"))
                                )
                );
    }

    /**
     * What the Plant Sapling command actually does.
     *
     * Change the mod configuration on the server and client, let all players know the mod config changed.
     * @param source CommandSource: Event's command source
     * @param choice boolean: Toggle (true/false) turns on or off the mod configuration
     * @return int: Anything >= 0 is considered a successful run of the command
     */
    private static int execute(CommandSource source, boolean choice){
        if (source.hasPermissionLevel(1)) {
            Configuration.common.plantSapling.set(choice);

            // There may be a way to more efficiently broadcast a message to all players but I didn't see one
            // in the documentation or source code.
            for(ServerPlayerEntity playerEntity : source.getServer().getPlayerList().getPlayers()) {
                playerEntity.sendMessage(new TranslationTextComponent(COMMAND_SWITCH_KEY, choice).mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY), Util.DUMMY_UUID);
            }
            source.sendFeedback(new TranslationTextComponent("command.choice", COMMAND_NAME, choice), true);
        }
        else{
            source.sendFeedback(new TranslationTextComponent("command.permissions", COMMAND_NAME, choice), true);
        }
        return 1;
    }
}
