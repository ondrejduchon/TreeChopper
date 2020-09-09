package treechopper.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class InfoCommand {
    private static final String COMMAND_NAME = "info";

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(COMMAND_NAME)
                    .executes(source ->
                            execute(source.getSource())
                );
    }

    private static int execute(CommandSource source){
        source.sendFeedback(new TranslationTextComponent("command.info", COMMAND_NAME), true);
        return 1;
    }
}
