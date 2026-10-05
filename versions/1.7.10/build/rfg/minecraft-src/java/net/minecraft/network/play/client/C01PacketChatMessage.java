package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C01PacketChatMessage extends Packet
{
    private String field_149440_a;
    private static final String __OBFID = "CL_00001347";

    public C01PacketChatMessage() {}

    public C01PacketChatMessage(String messageIn)
    {
        if (messageIn.length() > 100)
        {
            messageIn = messageIn.substring(0, 100);
        }

        this.field_149440_a = messageIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149440_a = data.readStringFromBuffer(100);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149440_a);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processChatMessage(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("message=\'%s\'", new Object[] {this.field_149440_a});
    }

    public String func_149439_c()
    {
        return this.field_149440_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}