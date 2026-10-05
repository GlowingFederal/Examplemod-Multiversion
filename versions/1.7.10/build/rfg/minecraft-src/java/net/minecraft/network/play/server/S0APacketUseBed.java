package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S0APacketUseBed extends Packet
{
    private int field_149097_a;
    private int field_149095_b;
    private int field_149096_c;
    private int field_149094_d;
    private static final String __OBFID = "CL_00001319";

    public S0APacketUseBed() {}

    public S0APacketUseBed(EntityPlayer player, int xPos, int yPos, int zPos)
    {
        this.field_149095_b = xPos;
        this.field_149096_c = yPos;
        this.field_149094_d = zPos;
        this.field_149097_a = player.getEntityId();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149097_a = data.readInt();
        this.field_149095_b = data.readInt();
        this.field_149096_c = data.readByte();
        this.field_149094_d = data.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149097_a);
        data.writeInt(this.field_149095_b);
        data.writeByte(this.field_149096_c);
        data.writeInt(this.field_149094_d);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUseBed(this);
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer func_149091_a(World p_149091_1_)
    {
        return (EntityPlayer)p_149091_1_.getEntityByID(this.field_149097_a);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149092_c()
    {
        return this.field_149095_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149090_d()
    {
        return this.field_149096_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_149089_e()
    {
        return this.field_149094_d;
    }
}