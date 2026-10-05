package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer extends Packet
{
    protected double field_149479_a;
    protected double field_149477_b;
    protected double field_149478_c;
    protected double field_149475_d;
    protected float field_149476_e;
    protected float field_149473_f;
    protected boolean field_149474_g;
    protected boolean field_149480_h;
    protected boolean field_149481_i;
    private static final String __OBFID = "CL_00001360";

    public C03PacketPlayer() {}

    @SideOnly(Side.CLIENT)
    public C03PacketPlayer(boolean p_i45256_1_)
    {
        this.field_149474_g = p_i45256_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayer(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149474_g = data.readUnsignedByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_149474_g ? 1 : 0);
    }

    public double func_149464_c()
    {
        return this.field_149479_a;
    }

    public double func_149467_d()
    {
        return this.field_149477_b;
    }

    public double func_149472_e()
    {
        return this.field_149478_c;
    }

    public double func_149471_f()
    {
        return this.field_149475_d;
    }

    public float func_149462_g()
    {
        return this.field_149476_e;
    }

    public float func_149470_h()
    {
        return this.field_149473_f;
    }

    public boolean func_149465_i()
    {
        return this.field_149474_g;
    }

    public boolean func_149466_j()
    {
        return this.field_149480_h;
    }

    public boolean func_149463_k()
    {
        return this.field_149481_i;
    }

    public void func_149469_a(boolean p_149469_1_)
    {
        this.field_149480_h = p_149469_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }

    public static class C04PacketPlayerPosition extends C03PacketPlayer
        {
            private static final String __OBFID = "CL_00001361";

            public C04PacketPlayerPosition()
            {
                this.field_149480_h = true;
            }

            @SideOnly(Side.CLIENT)
            public C04PacketPlayerPosition(double p_i45253_1_, double p_i45253_3_, double p_i45253_5_, double p_i45253_7_, boolean p_i45253_9_)
            {
                this.field_149479_a = p_i45253_1_;
                this.field_149477_b = p_i45253_3_;
                this.field_149475_d = p_i45253_5_;
                this.field_149478_c = p_i45253_7_;
                this.field_149474_g = p_i45253_9_;
                this.field_149480_h = true;
            }

            /**
             * Reads the raw packet data from the data stream.
             */
            public void readPacketData(PacketBuffer data) throws IOException
            {
                this.field_149479_a = data.readDouble();
                this.field_149477_b = data.readDouble();
                this.field_149475_d = data.readDouble();
                this.field_149478_c = data.readDouble();
                super.readPacketData(data);
            }

            /**
             * Writes the raw packet data to the data stream.
             */
            public void writePacketData(PacketBuffer data) throws IOException
            {
                data.writeDouble(this.field_149479_a);
                data.writeDouble(this.field_149477_b);
                data.writeDouble(this.field_149475_d);
                data.writeDouble(this.field_149478_c);
                super.writePacketData(data);
            }

            /**
             * Passes this Packet on to the NetHandler for processing.
             */
            public void processPacket(INetHandler handler)
            {
                super.processPacket((INetHandlerPlayServer)handler);
            }
        }

    public static class C05PacketPlayerLook extends C03PacketPlayer
        {
            private static final String __OBFID = "CL_00001363";

            public C05PacketPlayerLook()
            {
                this.field_149481_i = true;
            }

            @SideOnly(Side.CLIENT)
            public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_)
            {
                this.field_149476_e = p_i45255_1_;
                this.field_149473_f = p_i45255_2_;
                this.field_149474_g = p_i45255_3_;
                this.field_149481_i = true;
            }

            /**
             * Reads the raw packet data from the data stream.
             */
            public void readPacketData(PacketBuffer data) throws IOException
            {
                this.field_149476_e = data.readFloat();
                this.field_149473_f = data.readFloat();
                super.readPacketData(data);
            }

            /**
             * Writes the raw packet data to the data stream.
             */
            public void writePacketData(PacketBuffer data) throws IOException
            {
                data.writeFloat(this.field_149476_e);
                data.writeFloat(this.field_149473_f);
                super.writePacketData(data);
            }

            /**
             * Passes this Packet on to the NetHandler for processing.
             */
            public void processPacket(INetHandler handler)
            {
                super.processPacket((INetHandlerPlayServer)handler);
            }
        }

    public static class C06PacketPlayerPosLook extends C03PacketPlayer
        {
            private static final String __OBFID = "CL_00001362";

            public C06PacketPlayerPosLook()
            {
                this.field_149480_h = true;
                this.field_149481_i = true;
            }

            @SideOnly(Side.CLIENT)
            public C06PacketPlayerPosLook(double p_i45254_1_, double p_i45254_3_, double p_i45254_5_, double p_i45254_7_, float p_i45254_9_, float p_i45254_10_, boolean p_i45254_11_)
            {
                this.field_149479_a = p_i45254_1_;
                this.field_149477_b = p_i45254_3_;
                this.field_149475_d = p_i45254_5_;
                this.field_149478_c = p_i45254_7_;
                this.field_149476_e = p_i45254_9_;
                this.field_149473_f = p_i45254_10_;
                this.field_149474_g = p_i45254_11_;
                this.field_149481_i = true;
                this.field_149480_h = true;
            }

            /**
             * Reads the raw packet data from the data stream.
             */
            public void readPacketData(PacketBuffer data) throws IOException
            {
                this.field_149479_a = data.readDouble();
                this.field_149477_b = data.readDouble();
                this.field_149475_d = data.readDouble();
                this.field_149478_c = data.readDouble();
                this.field_149476_e = data.readFloat();
                this.field_149473_f = data.readFloat();
                super.readPacketData(data);
            }

            /**
             * Writes the raw packet data to the data stream.
             */
            public void writePacketData(PacketBuffer data) throws IOException
            {
                data.writeDouble(this.field_149479_a);
                data.writeDouble(this.field_149477_b);
                data.writeDouble(this.field_149475_d);
                data.writeDouble(this.field_149478_c);
                data.writeFloat(this.field_149476_e);
                data.writeFloat(this.field_149473_f);
                super.writePacketData(data);
            }

            /**
             * Passes this Packet on to the NetHandler for processing.
             */
            public void processPacket(INetHandler handler)
            {
                super.processPacket((INetHandlerPlayServer)handler);
            }
        }
}