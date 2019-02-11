package com.destroystokyo.paper.network;

import com.sun.istack.internal.Nullable;
import net.minecraft.network.NetworkManager;

import java.net.InetSocketAddress;

public class PaperNetworkClient implements NetworkClient
{
    private final NetworkManager networkManager;

    PaperNetworkClient(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress)this.networkManager.getSocketAddress();
    }

    @Override
    public int getProtocolVersion() {
        return this.networkManager.protocolVersion;
    }

    @Nullable
    @Override
    public InetSocketAddress getVirtualHost() {
        return this.networkManager.virtualHost;
    }

    public static InetSocketAddress prepareVirtualHost(String host, int port) {
        int len = host.length();
        final int pos = host.indexOf(0);
        if (pos >= 0) {
            len = pos;
        }
        if (len > 0 && host.charAt(len - 1) == '.') {
            --len;
        }
        return InetSocketAddress.createUnresolved(host.substring(0, len), port);
    }
}
