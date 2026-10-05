package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S35PacketUpdateTileEntity extends Packet
{
    private int field_148863_a;
    private int field_148861_b;
    private int field_148862_c;
    private int field_148859_d;
    private NBTTagCompound field_148860_e;
    private static final String __OBFID = "CL_00001285";

    public S35PacketUpdateTileEntity() {}

    public S35PacketUpdateTileEntity(int xcoord, int ycoord, int zcoord, int meta, NBTTagCompound nbtTag)
    {
        this.field_148863_a = xcoord;
        this.field_148861_b = ycoord;
        this.field_148862_c = zcoord;
        this.field_148859_d = meta;
        this.field_148860_e = nbtTag;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148863_a = data.readInt();
        this.field_148861_b = data.readShort();
        this.field_148862_c = data.readInt();
        this.field_148859_d = data.readUnsignedByte();
        this.field_148860_e = data.readNBTTagCompoundFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_148863_a);
        data.writeShort(this.field_148861_b);
        data.writeInt(this.field_148862_c);
        data.writeByte((byte)this.field_148859_d);
        data.writeNBTTagCompoundToBuffer(this.field_148860_e);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateTileEntity(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_148856_c()
    {
        return this.field_148863_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_148855_d()
    {
        return this.field_148861_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_148854_e()
    {
        return this.field_148862_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_148853_f()
    {
        return this.field_148859_d;
    }

    @SideOnly(Side.CLIENT)
    public NBTTagCompound func_148857_g()
    {
        return this.field_148860_e;
    }
}