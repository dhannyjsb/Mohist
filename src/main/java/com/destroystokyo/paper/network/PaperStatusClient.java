package com.destroystokyo.paper.network;

import net.minecraft.network.NetworkManager;

class PaperStatusClient extends PaperNetworkClient implements StatusClient
{
    PaperStatusClient(final NetworkManager networkManager) {
        super(networkManager);
    }
}
