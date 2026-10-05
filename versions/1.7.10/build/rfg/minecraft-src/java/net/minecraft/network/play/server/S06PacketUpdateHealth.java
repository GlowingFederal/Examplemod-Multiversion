package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S06PacketUpdateHealth extends Packet
{
    private float field_149336_a;
    private int field_149334_b;
    private float field_149335_c;
    private static final String __OBFID = "CL_00001332";

    public S06PacketUpdateHealth() {}

    public S06PacketUpdateHealth(float healthIn, int foodLevelIn, float saturationIn)
    {
        this.field_149336_a = healthIn;
        this.field_149334_b = foodLevelIn;
        this.field_149335_c = saturationIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149336_a = data.readFloat();
        this.field_149334_b = data.readShort();
        this.field_149335_c = data.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeFloat(this.field_149336_a);
        data.writeShort(this.field_149334_b);
        data.writeFloat(this.field_149335_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateHealth(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public float func_149332_c()
    {
        return this.field_149336_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_149330_d()
    {
        return this.field_149334_b;
    }

    @SideOnly(Side.CLIENT)
    public float func_149331_e()
    {
        return this.field_149335_c;
    }
}