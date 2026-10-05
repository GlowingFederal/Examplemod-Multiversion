package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S39PacketPlayerAbilities extends Packet
{
    private boolean field_149119_a;
    private boolean field_149117_b;
    private boolean field_149118_c;
    private boolean field_149115_d;
    private float field_149116_e;
    private float field_149114_f;
    private static final String __OBFID = "CL_00001317";

    public S39PacketPlayerAbilities() {}

    public S39PacketPlayerAbilities(PlayerCapabilities capabilities)
    {
        this.func_149108_a(capabilities.disableDamage);
        this.func_149102_b(capabilities.isFlying);
        this.func_149109_c(capabilities.allowFlying);
        this.func_149111_d(capabilities.isCreativeMode);
        this.func_149104_a(capabilities.getFlySpeed());
        this.func_149110_b(capabilities.getWalkSpeed());
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        byte b0 = data.readByte();
        this.func_149108_a((b0 & 1) > 0);
        this.func_149102_b((b0 & 2) > 0);
        this.func_149109_c((b0 & 4) > 0);
        this.func_149111_d((b0 & 8) > 0);
        this.func_149104_a(data.readFloat());
        this.func_149110_b(data.readFloat());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        byte b0 = 0;

        if (this.func_149112_c())
        {
            b0 = (byte)(b0 | 1);
        }

        if (this.func_149106_d())
        {
            b0 = (byte)(b0 | 2);
        }

        if (this.func_149105_e())
        {
            b0 = (byte)(b0 | 4);
        }

        if (this.func_149103_f())
        {
            b0 = (byte)(b0 | 8);
        }

        data.writeByte(b0);
        data.writeFloat(this.field_149116_e);
        data.writeFloat(this.field_149114_f);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlayerAbilities(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("invuln=%b, flying=%b, canfly=%b, instabuild=%b, flyspeed=%.4f, walkspped=%.4f", new Object[] {Boolean.valueOf(this.func_149112_c()), Boolean.valueOf(this.func_149106_d()), Boolean.valueOf(this.func_149105_e()), Boolean.valueOf(this.func_149103_f()), Float.valueOf(this.func_149101_g()), Float.valueOf(this.func_149107_h())});
    }

    public boolean func_149112_c()
    {
        return this.field_149119_a;
    }

    public void func_149108_a(boolean isInvulnerable)
    {
        this.field_149119_a = isInvulnerable;
    }

    public boolean func_149106_d()
    {
        return this.field_149117_b;
    }

    public void func_149102_b(boolean isFlying)
    {
        this.field_149117_b = isFlying;
    }

    public boolean func_149105_e()
    {
        return this.field_149118_c;
    }

    public void func_149109_c(boolean isAllowFlying)
    {
        this.field_149118_c = isAllowFlying;
    }

    public boolean func_149103_f()
    {
        return this.field_149115_d;
    }

    public void func_149111_d(boolean isCreativeMode)
    {
        this.field_149115_d = isCreativeMode;
    }

    public float func_149101_g()
    {
        return this.field_149116_e;
    }

    public void func_149104_a(float flySpeedIn)
    {
        this.field_149116_e = flySpeedIn;
    }

    public float func_149107_h()
    {
        return this.field_149114_f;
    }

    public void func_149110_b(float walkSpeedIn)
    {
        this.field_149114_f = walkSpeedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}