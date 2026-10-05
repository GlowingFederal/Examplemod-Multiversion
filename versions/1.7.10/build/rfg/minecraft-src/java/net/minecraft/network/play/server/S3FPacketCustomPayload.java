package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3FPacketCustomPayload extends Packet
{
    private String field_149172_a;
    private byte[] field_149171_b;
    private static final String __OBFID = "CL_00001297";

    public S3FPacketCustomPayload() {}

    public S3FPacketCustomPayload(String channelName, ByteBuf dataIn)
    {
        this(channelName, dataIn.array());
    }

    public S3FPacketCustomPayload(String channelName, byte[] dataIn)
    {
        this.field_149172_a = channelName;
        this.field_149171_b = dataIn;

        //TODO: Remove this when FML protocol is re-written. To restore vanilla compatibility.
        if (dataIn.length > 0x1FFF9A) // Max size of ANY MC packet is 0x1FFFFF minus max size of this packet (101)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097050 bytes");
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149172_a = data.readStringFromBuffer(20);
        this.field_149171_b = new byte[cpw.mods.fml.common.network.ByteBufUtils.readVarShort(data)];
        data.readBytes(this.field_149171_b);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149172_a);
        cpw.mods.fml.common.network.ByteBufUtils.writeVarShort(data, this.field_149171_b.length);
        data.writeBytes(this.field_149171_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCustomPayload(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public String func_149169_c()
    {
        return this.field_149172_a;
    }

    @SideOnly(Side.CLIENT)
    public byte[] func_149168_d()
    {
        return this.field_149171_b;
    }
}