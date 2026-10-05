package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;

public class BanList extends UserList
{
    private static final String __OBFID = "CL_00001396";

    public BanList(File bansFile)
    {
        super(bansFile);
    }

    protected UserListEntry func_152682_a(JsonObject entryData)
    {
        return new IPBanEntry(entryData);
    }

    public boolean func_152708_a(SocketAddress address)
    {
        String s = this.func_152707_c(address);
        return this.func_152692_d(s);
    }

    public IPBanEntry func_152709_b(SocketAddress address)
    {
        String s = this.func_152707_c(address);
        return (IPBanEntry)this.func_152683_b(s);
    }

    private String func_152707_c(SocketAddress address)
    {
        String s = address.toString();

        if (s.contains("/"))
        {
            s = s.substring(s.indexOf(47) + 1);
        }

        if (s.contains(":"))
        {
            s = s.substring(0, s.indexOf(58));
        }

        return s;
    }
}