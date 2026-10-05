package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C10PacketCreativeInventoryAction extends Packet
{
    private int field_149629_a;
    private ItemStack field_149628_b;
    private static final String __OBFID = "CL_00001369";

    public C10PacketCreativeInventoryAction() {}

    @SideOnly(Side.CLIENT)
    public C10PacketCreativeInventoryAction(int p_i45263_1_, ItemStack p_i45263_2_)
    {
        this.field_149629_a = p_i45263_1_;
        this.field_149628_b = p_i45263_2_ != null ? p_i45263_2_.copy() : null;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processCreativeInventoryAction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149629_a = data.readShort();
        this.field_149628_b = data.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeShort(this.field_149629_a);
        data.writeItemStackToBuffer(this.field_149628_b);
    }

    public int func_149627_c()
    {
        return this.field_149629_a;
    }

    public ItemStack func_149625_d()
    {
        return this.field_149628_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}