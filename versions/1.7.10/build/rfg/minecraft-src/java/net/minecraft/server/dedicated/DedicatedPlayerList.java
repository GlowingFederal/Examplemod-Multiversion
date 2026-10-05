package net.minecraft.server.dedicated;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.SERVER)
public class DedicatedPlayerList extends ServerConfigurationManager
{
    private static final Logger field_164439_d = LogManager.getLogger();
    private static final String __OBFID = "CL_00001783";

    public DedicatedPlayerList(DedicatedServer server)
    {
        super(server);
        this.func_152611_a(server.getIntProperty("view-distance", 10));
        this.maxPlayers = server.getIntProperty("max-players", 20);
        this.setWhiteListEnabled(server.getBooleanProperty("white-list", false));

        if (!server.isSinglePlayer())
        {
            this.func_152608_h().func_152686_a(true);
            this.getBannedIPs().func_152686_a(true);
        }

        this.func_152620_y();
        this.func_152617_w();
        this.func_152619_x();
        this.func_152618_v();
        this.loadOpsList();
        this.readWhiteList();
        this.saveOpsList();

        if (!this.func_152599_k().func_152691_c().exists())
        {
            this.saveWhiteList();
        }
    }

    public void setWhiteListEnabled(boolean whitelistEnabled)
    {
        super.setWhiteListEnabled(whitelistEnabled);
        this.getServerInstance().setProperty("white-list", Boolean.valueOf(whitelistEnabled));
        this.getServerInstance().saveProperties();
    }

    public void func_152605_a(GameProfile profile)
    {
        super.func_152605_a(profile);
        this.saveOpsList();
    }

    public void func_152610_b(GameProfile profile)
    {
        super.func_152610_b(profile);
        this.saveOpsList();
    }

    public void func_152597_c(GameProfile profile)
    {
        super.func_152597_c(profile);
        this.saveWhiteList();
    }

    public void func_152601_d(GameProfile profile)
    {
        super.func_152601_d(profile);
        this.saveWhiteList();
    }

    /**
     * Either does nothing, or calls readWhiteList.
     */
    public void loadWhiteList()
    {
        this.readWhiteList();
    }

    private void func_152618_v()
    {
        try
        {
            this.getBannedIPs().func_152678_f();
        }
        catch (IOException ioexception)
        {
            field_164439_d.warn("Failed to save ip banlist: ", ioexception);
        }
    }

    private void func_152617_w()
    {
        try
        {
            this.func_152608_h().func_152678_f();
        }
        catch (IOException ioexception)
        {
            field_164439_d.warn("Failed to save user banlist: ", ioexception);
        }
    }

    private void func_152619_x()
    {
        try
        {
            this.getBannedIPs().func_152679_g();
        }
        catch (IOException ioexception)
        {
            field_164439_d.warn("Failed to load ip banlist: ", ioexception);
        }
    }

    private void func_152620_y()
    {
        try
        {
            this.func_152608_h().func_152679_g();
        }
        catch (IOException ioexception)
        {
            field_164439_d.warn("Failed to load user banlist: ", ioexception);
        }
    }

    private void loadOpsList()
    {
        try
        {
            this.func_152603_m().func_152679_g();
        }
        catch (Exception exception)
        {
            field_164439_d.warn("Failed to load operators list: ", exception);
        }
    }

    private void saveOpsList()
    {
        try
        {
            this.func_152603_m().func_152678_f();
        }
        catch (Exception exception)
        {
            field_164439_d.warn("Failed to save operators list: ", exception);
        }
    }

    private void readWhiteList()
    {
        try
        {
            this.func_152599_k().func_152679_g();
        }
        catch (Exception exception)
        {
            field_164439_d.warn("Failed to load white-list: ", exception);
        }
    }

    private void saveWhiteList()
    {
        try
        {
            this.func_152599_k().func_152678_f();
        }
        catch (Exception exception)
        {
            field_164439_d.warn("Failed to save white-list: ", exception);
        }
    }

    public boolean func_152607_e(GameProfile profile)
    {
        return !this.isWhiteListEnabled() || this.func_152596_g(profile) || this.func_152599_k().func_152705_a(profile);
    }

    public DedicatedServer getServerInstance()
    {
        return (DedicatedServer)super.getServerInstance();
    }
}