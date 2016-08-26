package treechopper.core;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Duchy on 8/25/2016.
 */
public class ServerMessage implements IMessage {
    private boolean ignoreDurability, plantSapling, decayLeaves;
    private double breakSpeed;

    public ServerMessage() {
    }

    public ServerMessage(boolean ignoreDurability, boolean plantSapling, boolean decayLeaves, double breakSpeed) {
        this.ignoreDurability = ignoreDurability;
        this.plantSapling = plantSapling;
        this.decayLeaves = decayLeaves;
        this.breakSpeed = breakSpeed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ignoreDurability = buf.readBoolean();
        plantSapling = buf.readBoolean();
        decayLeaves = buf.readBoolean();
        breakSpeed = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(ignoreDurability);
        buf.writeBoolean(plantSapling);
        buf.writeBoolean(decayLeaves);
        buf.writeDouble(breakSpeed);
    }

    public static class Handler implements IMessageHandler<ServerMessage, IMessage> {
        @Override
        public IMessage onMessage(ServerMessage message, MessageContext ctx) {
            TreeChopper.breakSpeed = message.breakSpeed;
            TreeChopper.ignoreDurability = message.ignoreDurability;
            TreeChopper.decayLeaves = message.decayLeaves;
            TreeChopper.plantSapling = message.plantSapling;

            return null;
        }
    }
}
