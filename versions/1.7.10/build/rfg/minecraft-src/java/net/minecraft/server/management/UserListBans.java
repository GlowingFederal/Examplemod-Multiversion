package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList
{
    private static final String __OBFID = "CL_00001873";

    public UserListBans(File bansFile)
    {
        super(bansFile);
    }

    protected UserListEntry func_152682_a(JsonObject entryData)
    {
        return new UserListBansEntry(entryData);
    }

    public boolean func_152702_a(GameProfile profile)
    {
        return this.func_152692_d(profile);
    }

    public String[] func_152685_a()
    {
        String[] astring = new String[this.func_152688_e().size()];
        int i = 0;
        UserListBansEntry userlistbansentry;

        for (Iterator iterator = this.func_152688_e().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile)userlistbansentry.func_152640_f()).getName())
        {
            userlistbansentry = (UserListBansEntry)iterator.next();
        }

        return astring;
    }

    protected String func_152701_b(GameProfile profile)
    {
        return profile.getId().toString();
    }

    public GameProfile func_152703_a(String username)
    {
        Iterator iterator = this.func_152688_e().values().iterator();
        UserListBansEntry userlistbansentry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            userlistbansentry = (UserListBansEntry)iterator.next();
        }
        while (!username.equalsIgnoreCase(((GameProfile)userlistbansentry.func_152640_f()).getName()));

        return (GameProfile)userlistbansentry.func_152640_f();
    }

    protected String func_152681_a(Object obj)
    {
        return this.func_152701_b((GameProfile)obj);
    }
}