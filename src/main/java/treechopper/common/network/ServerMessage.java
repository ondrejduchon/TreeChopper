package treechopper.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import treechopper.common.config.ConfigHandler;

/**
 * Created by Duchy on 8/25/2016.
 */

public class ServerMessage implements IMessage {
    private int breakSpeed;
    private boolean ignoreDur;

    public ServerMessage() {
    }

    public ServerMessage(int breakSpeed, boolean ignoreDur) {
        this.breakSpeed = breakSpeed;
        this.ignoreDur = ignoreDur;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            breakSpeed = buf.readInt();
            ignoreDur = buf.readBoolean();
        } catch (IndexOutOfBoundsException e1) {
            System.out.println("There is a problem by FMLIndexedMessageCodec - significantly difference between client and server forge version..");
        } catch (Exception e2) {
            System.out.println("Other problem.." + e2);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(breakSpeed);
        buf.writeBoolean(ignoreDur);
    }

    public static class Handler implements IMessageHandler<ServerMessage, IMessage> {
        @Override
        public IMessage onMessage(ServerMessage message, MessageContext ctx) {

            ConfigHandler.breakSpeed = message.breakSpeed;
            ConfigHandler.ignoreDurability = message.ignoreDur;

            return null;
        }
    }
}
