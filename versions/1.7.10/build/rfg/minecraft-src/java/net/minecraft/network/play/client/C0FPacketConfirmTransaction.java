package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction extends Packet
{
    private int field_149536_a;
    private short field_149534_b;
    private boolean field_149535_c;
    private static final String __OBFID = "CL_00001351";

    public C0FPacketConfirmTransaction() {}

    @SideOnly(Side.CLIENT)
    public C0FPacketConfirmTransaction(int p_i45244_1_, short p_i45244_2_, boolean p_i45244_3_)
    {
        this.field_149536_a = p_i45244_1_;
        this.field_149534_b = p_i45244_2_;
        this.field_149535_c = p_i45244_3_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149536_a = data.readByte();
        this.field_149534_b = data.readShort();
        this.field_149535_c = data.readByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_149536_a);
        data.writeShort(this.field_149534_b);
        data.writeByte(this.field_149535_c ? 1 : 0);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, uid=%d, accepted=%b", new Object[] {Integer.valueOf(this.field_149536_a), Short.valueOf(this.field_149534_b), Boolean.valueOf(this.field_149535_c)});
    }

    public int func_149532_c()
    {
        return this.field_149536_a;
    }

    public short func_149533_d()
    {
        return this.field_149534_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}