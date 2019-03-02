package com.destroystokyo.paper;

import net.minecraft.util.ResourceLocation;

public interface KeyedObject
{
    ResourceLocation getResourceLocation();

    default String getResourceLocationString()
    {
        return getResourceLocation().toString();
    }
}
