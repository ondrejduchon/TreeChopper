package treechopper.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import treechopper.common.config.ConfigurationHandler;

public class ClientSettingsMessage implements IMessage {

    private boolean m_ReverseShift;

    public ClientSettingsMessage(boolean reverseShift) {
        m_ReverseShift = reverseShift;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        m_ReverseShift = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(m_ReverseShift);
    }

    public static class MsgHandler implements IMessageHandler<ClientSettingsMessage, IMessage> {

        @Override
        public IMessage onMessage(ClientSettingsMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    ConfigurationHandler.reverseShift = message.m_ReverseShift;
                }
            });
            return null;
        }
    }
}
