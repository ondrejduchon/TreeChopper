package treechopper.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import treechopper.common.config.ConfigHandler;
import treechopper.core.StaticHandler;

/**
 * Created by Duchy on 2/24/2017.
 */
public class ClientMessage implements IMessage { // From client to server
    private boolean reverseShift;
    private int playerID;

    public ClientMessage() {
    }

    public ClientMessage(boolean reverseShift, int playerID) {
        this.reverseShift = reverseShift;
        this.playerID = playerID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            reverseShift = buf.readBoolean();
            playerID = buf.readInt();
        } catch (IndexOutOfBoundsException e1) {
            System.out.println("There is a problem by FMLIndexedMessageCodec - significantly difference between client and server forge version..");
        } catch (Exception e2) {
            System.out.println("Other problem.." + e2);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(reverseShift);
        buf.writeInt(playerID);
    }

    public static class Handler implements IMessageHandler<ClientMessage, IMessage> {
        @Override
        public IMessage onMessage(ClientMessage message, MessageContext ctx) {

            StaticHandler.playerReverseShift.put(message.playerID, message.reverseShift);

            return null;
        }
    }
}