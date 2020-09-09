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

public class ReverseShiftCommand {
    private static final String COMMAND_NAME = "reverse_shift";
    private static final String COMMAND_SWITCH_KEY = "command.decay_leaves_switch";

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(COMMAND_NAME)
                .then(
                        Commands.argument("toggle", BoolArgumentType.bool())
                                .executes(source ->
                                        execute(source.getSource(), BoolArgumentType.getBool(source, "toggle"))
                                )
                );
    }

    private static int execute(CommandSource source, boolean choice){
        Configuration.common.reverseShift.set(choice);

        for(ServerPlayerEntity playerEntity : source.getServer().getPlayerList().getPlayers()) {
            playerEntity.sendMessage(new TranslationTextComponent(COMMAND_SWITCH_KEY, choice).mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY), Util.DUMMY_UUID);
        }
        source.sendFeedback(new TranslationTextComponent("command.choice", COMMAND_NAME, choice), true);
        return 1;
    }
}
