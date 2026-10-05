package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0BPacketAnimation extends Packet
{
    private int field_148981_a;
    private int field_148980_b;
    private static final String __OBFID = "CL_00001282";

    public S0BPacketAnimation() {}

    public S0BPacketAnimation(Entity ent, int animationType)
    {
        this.field_148981_a = ent.getEntityId();
        this.field_148980_b = animationType;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148981_a = data.readVarIntFromBuffer();
        this.field_148980_b = data.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.field_148981_a);
        data.writeByte(this.field_148980_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleAnimation(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, type=%d", new Object[] {Integer.valueOf(this.field_148981_a), Integer.valueOf(this.field_148980_b)});
    }

    @SideOnly(Side.CLIENT)
    public int func_148978_c()
    {
        return this.field_148981_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_148977_d()
    {
        return this.field_148980_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}