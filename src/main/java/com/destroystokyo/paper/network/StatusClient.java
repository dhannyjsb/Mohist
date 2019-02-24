package com.destroystokyo.paper.network;

public interface StatusClient extends NetworkClient {

    default boolean isLegacy() {
        return false;
    }
}
