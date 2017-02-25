package treechopper.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import treechopper.common.config.ConfigHandler;

/**
 * Created by Duchy on 2/25/2017.
 */
public class sendReverseToClient implements IMessage {
    private boolean reverseShift;

    public sendReverseToClient() {
    }

    public sendReverseToClient(boolean reverseShift) {
        this.reverseShift = reverseShift;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            reverseShift = buf.readBoolean();
        } catch (IndexOutOfBoundsException e1) {
            System.out.println("There is a problem by FMLIndexedMessageCodec - significantly difference between client and server forge version..");
        } catch (Exception e2) {
            System.out.println("Other problem.." + e2);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(reverseShift);
    }

    public static class Handler implements IMessageHandler<sendReverseToClient, IMessage> {
        @Override
        public IMessage onMessage(sendReverseToClient message, MessageContext ctx) {

            ConfigHandler.setReverseShift(message.reverseShift);

            ConfigHandler.writeConfig(ConfigHandler.decayLeaves, ConfigHandler.plantSapling, ConfigHandler.ignoreDurability, ConfigHandler.breakSpeed, ConfigHandler.plantSaplingTree, ConfigHandler.roots, ConfigHandler.reverseShift);

            return null;
        }
    }
}
