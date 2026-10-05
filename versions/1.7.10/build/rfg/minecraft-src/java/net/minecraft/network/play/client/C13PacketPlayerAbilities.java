package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C13PacketPlayerAbilities extends Packet
{
    private boolean field_149500_a;
    private boolean field_149498_b;
    private boolean field_149499_c;
    private boolean field_149496_d;
    private float field_149497_e;
    private float field_149495_f;
    private static final String __OBFID = "CL_00001364";

    public C13PacketPlayerAbilities() {}

    public C13PacketPlayerAbilities(PlayerCapabilities capabilities)
    {
        this.func_149490_a(capabilities.disableDamage);
        this.func_149483_b(capabilities.isFlying);
        this.func_149491_c(capabilities.allowFlying);
        this.func_149493_d(capabilities.isCreativeMode);
        this.func_149485_a(capabilities.getFlySpeed());
        this.func_149492_b(capabilities.getWalkSpeed());
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        byte b0 = data.readByte();
        this.func_149490_a((b0 & 1) > 0);
        this.func_149483_b((b0 & 2) > 0);
        this.func_149491_c((b0 & 4) > 0);
        this.func_149493_d((b0 & 8) > 0);
        this.func_149485_a(data.readFloat());
        this.func_149492_b(data.readFloat());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        byte b0 = 0;

        if (this.func_149494_c())
        {
            b0 = (byte)(b0 | 1);
        }

        if (this.func_149488_d())
        {
            b0 = (byte)(b0 | 2);
        }

        if (this.func_149486_e())
        {
            b0 = (byte)(b0 | 4);
        }

        if (this.func_149484_f())
        {
            b0 = (byte)(b0 | 8);
        }

        data.writeByte(b0);
        data.writeFloat(this.field_149497_e);
        data.writeFloat(this.field_149495_f);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerAbilities(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("invuln=%b, flying=%b, canfly=%b, instabuild=%b, flyspeed=%.4f, walkspped=%.4f", new Object[] {Boolean.valueOf(this.func_149494_c()), Boolean.valueOf(this.func_149488_d()), Boolean.valueOf(this.func_149486_e()), Boolean.valueOf(this.func_149484_f()), Float.valueOf(this.func_149482_g()), Float.valueOf(this.func_149489_h())});
    }

    public boolean func_149494_c()
    {
        return this.field_149500_a;
    }

    public void func_149490_a(boolean isInvulnerable)
    {
        this.field_149500_a = isInvulnerable;
    }

    public boolean func_149488_d()
    {
        return this.field_149498_b;
    }

    public void func_149483_b(boolean isFlying)
    {
        this.field_149498_b = isFlying;
    }

    public boolean func_149486_e()
    {
        return this.field_149499_c;
    }

    public void func_149491_c(boolean isAllowFlying)
    {
        this.field_149499_c = isAllowFlying;
    }

    public boolean func_149484_f()
    {
        return this.field_149496_d;
    }

    public void func_149493_d(boolean isCreativeMode)
    {
        this.field_149496_d = isCreativeMode;
    }

    public float func_149482_g()
    {
        return this.field_149497_e;
    }

    public void func_149485_a(float flySpeedIn)
    {
        this.field_149497_e = flySpeedIn;
    }

    public float func_149489_h()
    {
        return this.field_149495_f;
    }

    public void func_149492_b(float walkSpeedIn)
    {
        this.field_149495_f = walkSpeedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}