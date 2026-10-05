package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C08PacketPlayerBlockPlacement extends Packet
{
    private int field_149583_a;
    private int field_149581_b;
    private int field_149582_c;
    private int field_149579_d;
    private ItemStack field_149580_e;
    private float field_149577_f;
    private float field_149578_g;
    private float field_149584_h;
    private static final String __OBFID = "CL_00001371";

    public C08PacketPlayerBlockPlacement() {}

    @SideOnly(Side.CLIENT)
    public C08PacketPlayerBlockPlacement(int p_i45265_1_, int p_i45265_2_, int p_i45265_3_, int p_i45265_4_, ItemStack p_i45265_5_, float p_i45265_6_, float p_i45265_7_, float p_i45265_8_)
    {
        this.field_149583_a = p_i45265_1_;
        this.field_149581_b = p_i45265_2_;
        this.field_149582_c = p_i45265_3_;
        this.field_149579_d = p_i45265_4_;
        this.field_149580_e = p_i45265_5_ != null ? p_i45265_5_.copy() : null;
        this.field_149577_f = p_i45265_6_;
        this.field_149578_g = p_i45265_7_;
        this.field_149584_h = p_i45265_8_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149583_a = data.readInt();
        this.field_149581_b = data.readUnsignedByte();
        this.field_149582_c = data.readInt();
        this.field_149579_d = data.readUnsignedByte();
        this.field_149580_e = data.readItemStackFromBuffer();
        this.field_149577_f = (float)data.readUnsignedByte() / 16.0F;
        this.field_149578_g = (float)data.readUnsignedByte() / 16.0F;
        this.field_149584_h = (float)data.readUnsignedByte() / 16.0F;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149583_a);
        data.writeByte(this.field_149581_b);
        data.writeInt(this.field_149582_c);
        data.writeByte(this.field_149579_d);
        data.writeItemStackToBuffer(this.field_149580_e);
        data.writeByte((int)(this.field_149577_f * 16.0F));
        data.writeByte((int)(this.field_149578_g * 16.0F));
        data.writeByte((int)(this.field_149584_h * 16.0F));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerBlockPlacement(this);
    }

    public int func_149576_c()
    {
        return this.field_149583_a;
    }

    public int func_149571_d()
    {
        return this.field_149581_b;
    }

    public int func_149570_e()
    {
        return this.field_149582_c;
    }

    public int func_149568_f()
    {
        return this.field_149579_d;
    }

    public ItemStack func_149574_g()
    {
        return this.field_149580_e;
    }

    public float func_149573_h()
    {
        return this.field_149577_f;
    }

    public float func_149569_i()
    {
        return this.field_149578_g;
    }

    public float func_149575_j()
    {
        return this.field_149584_h;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}