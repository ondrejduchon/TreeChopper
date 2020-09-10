package treechopper.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

/**
 * Top level parent command for the Tree Chopper mod.
 * All other sub-commands inherit from this main level command.
 */
public class TreeChopperCommand {
    private static final String COMMAND_NAME = "tch";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(COMMAND_NAME)
                        // List of all registered sub commands.
                    .then(DecayLeavesCommand.register())
                    .then(DisableShiftCommand.register())
                    .then(InfoCommand.register())
                    .then(PlantSaplingCommand.register())
                    .then(ReverseShiftCommand.register())
        );
    }
}
