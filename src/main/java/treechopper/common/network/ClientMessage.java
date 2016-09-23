package treechopper.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import treechopper.core.StaticHandler;

/**
 * Created by Duchy on 9/23/2016.
 */
public class ClientMessage implements IMessage {
    private int logCount;

    public ClientMessage() {
    }

    public ClientMessage(int logCount) {
        this.logCount = logCount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            logCount = buf.readInt();
        } catch (IndexOutOfBoundsException e1) {
            System.out.println("There is a problem by FMLIndexedMessageCodec - significantly difference between client and server forge version..");
        } catch (Exception e2) {
            System.out.println("Other problem.." + e2);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(logCount);
    }

    public static class Handler implements IMessageHandler<ClientMessage, IMessage> {
        @Override
        public IMessage onMessage(ClientMessage message, MessageContext ctx) {

            StaticHandler.playersLogCount = message.logCount;

            return null;
        }
    }
}