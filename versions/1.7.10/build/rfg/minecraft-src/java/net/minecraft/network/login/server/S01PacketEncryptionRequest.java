package net.minecraft.network.login.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class S01PacketEncryptionRequest extends Packet
{
    private String field_149612_a;
    private PublicKey field_149610_b;
    private byte[] field_149611_c;
    private static final String __OBFID = "CL_00001376";

    public S01PacketEncryptionRequest() {}

    public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] p_i45268_3_)
    {
        this.field_149612_a = serverId;
        this.field_149610_b = key;
        this.field_149611_c = p_i45268_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149612_a = data.readStringFromBuffer(20);
        this.field_149610_b = CryptManager.decodePublicKey(readBlob(data));
        this.field_149611_c = readBlob(data);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149612_a);
        writeBlob(data, this.field_149610_b.getEncoded());
        writeBlob(data, this.field_149611_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleEncryptionRequest(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerLoginClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public String func_149609_c()
    {
        return this.field_149612_a;
    }

    @SideOnly(Side.CLIENT)
    public PublicKey func_149608_d()
    {
        return this.field_149610_b;
    }

    @SideOnly(Side.CLIENT)
    public byte[] func_149607_e()
    {
        return this.field_149611_c;
    }
}