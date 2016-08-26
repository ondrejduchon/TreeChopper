package treechopper.core;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Duchy on 8/23/2016.
 */
public class ClientMessage implements IMessage {
    private int isPressed = 0;

    public ClientMessage() {
    }

    public ClientMessage(int isPressed) {
        this.isPressed = isPressed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isPressed = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(isPressed);
    }

    public static class Handler implements IMessageHandler<ClientMessage, IMessage> {
        @Override
        public IMessage onMessage(ClientMessage message, MessageContext ctx) {

            switch (message.isPressed) {
                case 0:
                    StaticHandler.shiftPress = false;
                    break;
                case 1:
                    StaticHandler.shiftPress = true;
                    break;
            }

            return null;
        }
    }
}
