package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0APacketAnimation extends Packet
{
    private int field_149424_a;
    private int field_149423_b;
    private static final String __OBFID = "CL_00001345";

    public C0APacketAnimation() {}

    @SideOnly(Side.CLIENT)
    public C0APacketAnimation(Entity p_i45238_1_, int p_i45238_2_)
    {
        this.field_149424_a = p_i45238_1_.getEntityId();
        this.field_149423_b = p_i45238_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149424_a = data.readInt();
        this.field_149423_b = data.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149424_a);
        data.writeByte(this.field_149423_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processAnimation(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, type=%d", new Object[] {Integer.valueOf(this.field_149424_a), Integer.valueOf(this.field_149423_b)});
    }

    public int func_149421_d()
    {
        return this.field_149423_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}