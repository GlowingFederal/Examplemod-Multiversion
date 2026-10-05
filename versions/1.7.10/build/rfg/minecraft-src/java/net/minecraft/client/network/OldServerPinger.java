package net.minecraft.client.network;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class OldServerPinger
{
    private static final Splitter field_147230_a = Splitter.on('\u0000').limit(6);
    private static final Logger logger = LogManager.getLogger();
    private final List field_147229_c = Collections.synchronizedList(new ArrayList());
    private static final String __OBFID = "CL_00000892";


    public void func_147224_a(final ServerData server) throws UnknownHostException
    {
        ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
        final NetworkManager networkmanager = NetworkManager.provideLanClient(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort());
        this.field_147229_c.add(networkmanager);
        server.serverMOTD = "Pinging...";
        server.pingToServer = -1L;
        server.field_147412_i = null;
        networkmanager.setNetHandler(new INetHandlerStatusClient()
        {
            private boolean field_147403_d = false;
            private static final String __OBFID = "CL_00000893";
            public void handleServerInfo(S00PacketServerInfo packetIn)
            {
                ServerStatusResponse serverstatusresponse = packetIn.func_149294_c();

                if (serverstatusresponse.func_151317_a() != null)
                {
                    server.serverMOTD = serverstatusresponse.func_151317_a().getFormattedText();
                }
                else
                {
                    server.serverMOTD = "";
                }

                if (serverstatusresponse.func_151322_c() != null)
                {
                    server.gameVersion = serverstatusresponse.func_151322_c().func_151303_a();
                    server.field_82821_f = serverstatusresponse.func_151322_c().func_151304_b();
                }
                else
                {
                    server.gameVersion = "Old";
                    server.field_82821_f = 0;
                }

                if (serverstatusresponse.func_151318_b() != null)
                {
                    server.populationInfo = EnumChatFormatting.GRAY + "" + serverstatusresponse.func_151318_b().func_151333_b() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + serverstatusresponse.func_151318_b().func_151332_a();

                    if (ArrayUtils.isNotEmpty(serverstatusresponse.func_151318_b().func_151331_c()))
                    {
                        StringBuilder stringbuilder = new StringBuilder();
                        GameProfile[] agameprofile = serverstatusresponse.func_151318_b().func_151331_c();
                        int i = agameprofile.length;

                        for (int j = 0; j < i; ++j)
                        {
                            GameProfile gameprofile = agameprofile[j];

                            if (stringbuilder.length() > 0)
                            {
                                stringbuilder.append("\n");
                            }

                            stringbuilder.append(gameprofile.getName());
                        }

                        if (serverstatusresponse.func_151318_b().func_151331_c().length < serverstatusresponse.func_151318_b().func_151333_b())
                        {
                            if (stringbuilder.length() > 0)
                            {
                                stringbuilder.append("\n");
                            }

                            stringbuilder.append("... and ").append(serverstatusresponse.func_151318_b().func_151333_b() - serverstatusresponse.func_151318_b().func_151331_c().length).append(" more ...");
                        }

                        server.field_147412_i = stringbuilder.toString();
                    }
                }
                else
                {
                    server.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                }

                if (serverstatusresponse.func_151316_d() != null)
                {
                    String s = serverstatusresponse.func_151316_d();

                    if (s.startsWith("data:image/png;base64,"))
                    {
                        server.func_147407_a(s.substring("data:image/png;base64,".length()));
                    }
                    else
                    {
                        OldServerPinger.logger.error("Invalid server icon (unknown format)");
                    }
                }
                else
                {
                    server.func_147407_a((String)null);
                }

                FMLClientHandler.instance().bindServerListData(server, serverstatusresponse);
                networkmanager.scheduleOutboundPacket(new C01PacketPing(Minecraft.getSystemTime()), new GenericFutureListener[0]);
                this.field_147403_d = true;
            }
            public void handlePong(S01PacketPong packetIn)
            {
                long i = packetIn.func_149292_c();
                long j = Minecraft.getSystemTime();
                server.pingToServer = j - i;
                networkmanager.closeChannel(new ChatComponentText("Finished"));
            }
            /**
             * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
             */
            public void onDisconnect(IChatComponent reason)
            {
                if (!this.field_147403_d)
                {
                    OldServerPinger.logger.error("Can\'t ping " + server.serverIP + ": " + reason.getUnformattedText());
                    server.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    server.populationInfo = "";
                    OldServerPinger.this.func_147225_b(server);
                }
            }
            /**
             * Allows validation of the connection state transition. Parameters: from, to (connection state). Typically
             * throws IllegalStateException or UnsupportedOperationException if validation fails
             */
            public void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState)
            {
                if (newState != EnumConnectionState.STATUS)
                {
                    throw new UnsupportedOperationException("Unexpected change in protocol to " + newState);
                }
            }
            /**
             * For scheduled network tasks. Used in NetHandlerPlayServer to send keep-alive packets and in
             * NetHandlerLoginServer for a login-timeout
             */
            public void onNetworkTick() {}
        });

        try
        {
            networkmanager.scheduleOutboundPacket(new C00Handshake(5, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS), new GenericFutureListener[0]);
            networkmanager.scheduleOutboundPacket(new C00PacketServerQuery(), new GenericFutureListener[0]);
        }
        catch (Throwable throwable)
        {
            logger.error(throwable);
        }
    }

    private void func_147225_b(final ServerData server)
    {
        final ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(NetworkManager.eventLoops)).handler(new ChannelInitializer()
        {
            private static final String __OBFID = "CL_00000894";
            protected void initChannel(Channel p_initChannel_1_)
            {
                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
                }
                catch (ChannelException channelexception1)
                {
                    ;
                }

                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
                }
                catch (ChannelException channelexception)
                {
                    ;
                }

                p_initChannel_1_.pipeline().addLast(new ChannelHandler[] {new SimpleChannelInboundHandler()
                {
                    private static final String __OBFID = "CL_00000895";
                    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception
                    {
                        super.channelActive(p_channelActive_1_);
                        ByteBuf bytebuf = Unpooled.buffer();

                        try
                        {
                            bytebuf.writeByte(254);
                            bytebuf.writeByte(1);
                            bytebuf.writeByte(250);
                            char[] achar = "MC|PingHost".toCharArray();
                            bytebuf.writeShort(achar.length);
                            char[] achar1 = achar;
                            int i = achar.length;
                            int j;
                            char c0;

                            for (j = 0; j < i; ++j)
                            {
                                c0 = achar1[j];
                                bytebuf.writeChar(c0);
                            }

                            bytebuf.writeShort(7 + 2 * serveraddress.getIP().length());
                            bytebuf.writeByte(127);
                            achar = serveraddress.getIP().toCharArray();
                            bytebuf.writeShort(achar.length);
                            achar1 = achar;
                            i = achar.length;

                            for (j = 0; j < i; ++j)
                            {
                                c0 = achar1[j];
                                bytebuf.writeChar(c0);
                            }

                            bytebuf.writeInt(serveraddress.getPort());
                            p_channelActive_1_.channel().writeAndFlush(bytebuf).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        }
                        finally
                        {
                            bytebuf.release();
                        }
                    }
                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_)
                    {
                        short short1 = p_channelRead0_2_.readUnsignedByte();

                        if (short1 == 255)
                        {
                            String s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
                            String[] astring = (String[])Iterables.toArray(OldServerPinger.field_147230_a.split(s), String.class);

                            if ("\u00a71".equals(astring[0]))
                            {
                                int i = MathHelper.parseIntWithDefault(astring[1], 0);
                                String s1 = astring[2];
                                String s2 = astring[3];
                                int j = MathHelper.parseIntWithDefault(astring[4], -1);
                                int k = MathHelper.parseIntWithDefault(astring[5], -1);
                                server.field_82821_f = -1;
                                server.gameVersion = s1;
                                server.serverMOTD = s2;
                                server.populationInfo = EnumChatFormatting.GRAY + "" + j + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + k;
                            }
                        }

                        p_channelRead0_1_.close();
                    }
                    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_)
                    {
                        p_exceptionCaught_1_.close();
                    }
                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_)
                    {
                        this.channelRead0(p_channelRead0_1_, (ByteBuf)p_channelRead0_2_);
                    }
                }
                                                                         });
            }
        })).channel(NioSocketChannel.class)).connect(serveraddress.getIP(), serveraddress.getPort());
    }

    public void func_147223_a()
    {
        List list = this.field_147229_c;

        synchronized (this.field_147229_c)
        {
            Iterator iterator = this.field_147229_c.iterator();

            while (iterator.hasNext())
            {
                NetworkManager networkmanager = (NetworkManager)iterator.next();

                if (networkmanager.isChannelOpen())
                {
                    networkmanager.processReceivedPackets();
                }
                else
                {
                    iterator.remove();

                    if (networkmanager.getExitMessage() != null)
                    {
                        networkmanager.getNetHandler().onDisconnect(networkmanager.getExitMessage());
                    }
                }
            }
        }
    }

    public void func_147226_b()
    {
        List list = this.field_147229_c;

        synchronized (this.field_147229_c)
        {
            Iterator iterator = this.field_147229_c.iterator();

            while (iterator.hasNext())
            {
                NetworkManager networkmanager = (NetworkManager)iterator.next();

                if (networkmanager.isChannelOpen())
                {
                    iterator.remove();
                    networkmanager.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
    }
}