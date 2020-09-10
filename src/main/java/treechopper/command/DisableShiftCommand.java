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
 * Disable Shift toggle command.
 * When enabled, pressing the shift key will not do anything. When disabled, the shift key will
 * change how the mod does or does not chop trees.
 */
public class DisableShiftCommand {
    private static final String COMMAND_NAME = "disable_shift";
    private static final String COMMAND_SWITCH_KEY = "command.disable_shift_switch";

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
     * What the Disable Shift command actually does.
     *
     * Change the mod configuration on the server and client, let all players know the mod config changed.
     * @param source CommandSource: Event's command source
     * @param choice boolean: Toggle (true/false) turns on or off the mod configuration
     * @return int: Anything >= 0 is considered a successful run of the command
     */
    private static int execute(CommandSource source, boolean choice){
        if (source.hasPermissionLevel(1)) {
            Configuration.common.disableShift.set(choice);

            for(ServerPlayerEntity playerEntity : source.getServer().getPlayerList().getPlayers()) {
                playerEntity.sendMessage(new TranslationTextComponent(COMMAND_SWITCH_KEY, choice).mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY), Util.DUMMY_UUID);
            }
            source.sendFeedback(new TranslationTextComponent("command.choice", COMMAND_NAME, choice), true);
        }
        else {
            source.sendFeedback(new TranslationTextComponent("command.permissions", COMMAND_NAME, choice), true);
        }
        return 1;
    }
}
