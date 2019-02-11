package com.destroystokyo.paper;

import net.minecraft.util.ResourceLocation;

public abstract interface KeyedObject
{
    public abstract ResourceLocation getResourceLocation();

    public default String getResourceLocationString()
    {
        return getResourceLocation().toString();
    }
}
