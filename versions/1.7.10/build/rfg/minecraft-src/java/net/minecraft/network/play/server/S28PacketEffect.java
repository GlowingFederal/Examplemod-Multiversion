package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S28PacketEffect extends Packet
{
    private int field_149251_a;
    private int field_149249_b;
    private int field_149250_c;
    private int field_149247_d;
    private int field_149248_e;
    private boolean field_149246_f;
    private static final String __OBFID = "CL_00001307";

    public S28PacketEffect() {}

    public S28PacketEffect(int p_i45198_1_, int p_i45198_2_, int p_i45198_3_, int p_i45198_4_, int p_i45198_5_, boolean p_i45198_6_)
    {
        this.field_149251_a = p_i45198_1_;
        this.field_149250_c = p_i45198_2_;
        this.field_149247_d = p_i45198_3_;
        this.field_149248_e = p_i45198_4_;
        this.field_149249_b = p_i45198_5_;
        this.field_149246_f = p_i45198_6_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149251_a = data.readInt();
        this.field_149250_c = data.readInt();
        this.field_149247_d = data.readByte() & 255;
        this.field_149248_e = data.readInt();
        this.field_149249_b = data.readInt();
        this.field_149246_f = data.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149251_a);
        data.writeInt(this.field_149250_c);
        data.writeByte(this.field_149247_d & 255);
        data.writeInt(this.field_149248_e);
        data.writeInt(this.field_149249_b);
        data.writeBoolean(this.field_149246_f);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEffect(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149244_c()
    {
        return this.field_149246_f;
    }

    @SideOnly(Side.CLIENT)
    public int func_149242_d()
    {
        return this.field_149251_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_149241_e()
    {
        return this.field_149249_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149240_f()
    {
        return this.field_149250_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_149243_g()
    {
        return this.field_149247_d;
    }

    @SideOnly(Side.CLIENT)
    public int func_149239_h()
    {
        return this.field_149248_e;
    }
}