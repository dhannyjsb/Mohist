package red.mohist;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.cauldron.entity.CraftCustomEntity;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import red.mohist.api.ItemAPI;
import red.mohist.api.ServerAPI;
import red.mohist.i18n.Message;

import java.util.Map;

public class ForgeInjectBukkit {

    public static void init(){
        itemtoMaterials();
        blocktoMaterials();
        enchantment();
        potion();
        entity();
    }

    public static void itemtoMaterials(){
        for (Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation key = entry.getKey();
            if(!key.getResourceDomain().equals("minecraft")) {
                String materialName = Material.normalizeName(key.toString());
                Item item = entry.getValue();
                // inject item materials into Bukkit for FML
                Material material = Material.addMaterial(Item.getIdFromItem(item), item.getItemStackLimit(), materialName);
                if (material != null) {
                    ServerAPI.injectmaterials.put(material.name(), material.getId());
                    Mohist.LOGGER.debug("Save: " + Message.getFormatString("ingected.item", new Object[]{material.name(), String.valueOf(material.getId()), String.valueOf(ItemAPI.getBukkit(material).getDurability())}));
                }
            }
        }
    }

    public static void blocktoMaterials(){
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation key = entry.getKey();
            if(!key.getResourceDomain().equals("minecraft")) {
                String materialName = Material.normalizeName(key.toString());
                Block block = entry.getValue();
                // inject block materials into Bukkit for FML
                Material material = Material.addMaterial(Block.getIdFromBlock(block), materialName);
                if (material != null) {
                    ServerAPI.injectblock.put(material.name(), material.getId());
                    Mohist.LOGGER.debug("Save: " + Message.getFormatString("ingected.block", new Object[]{material.name(), String.valueOf(material.getId())}));
                }
            }
        }
    }

    public static void enchantment() {
        // Enchantment
        for (Object enchantment : Enchantment.REGISTRY) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment((Enchantment) enchantment));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void potion() {
        // Points
        for (Object effect : Potion.REGISTRY) {
            PotionEffectType.registerPotionEffectType(new CraftPotionEffectType((Potion) effect));
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void biome() {
        b:
        for (Map.Entry<ResourceLocation, net.minecraft.world.biome.Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getKey().getResourcePath().toUpperCase(java.util.Locale.ENGLISH);
            for (Biome biome : Biome.values()) {
                if (biome.toString().equals(biomeName)) {
                    continue b;
                }
                EnumHelper.addEnum(Biome.class, biomeName, new Class[0], new Object[0]);
            }
        }
    }

    public static World.Environment addBukkitEnvironment(int id, String name) {
        return (World.Environment)EnumHelper.addEnum(World.Environment.class,name,new Class[]{Integer.TYPE}, new Object[]{id});
    }

    public static WorldType addBukkitWorldType(String name)
    {

        WorldType worldType = EnumHelper.addEnum(WorldType.class, name, new Class [] { String.class }, new Object[] { name });
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void entity() {
        Map<String, EntityType> NAME_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<String, Class<? extends Entity>> entity : EntityRegistry.entityClassMap.entrySet()) {
            String name = entity.getKey();
            String entityType = name.replace("-", "_").toUpperCase();
            int typeId = GameData.getEntityRegistry().getID(EntityRegistry.getEntry(entity.getValue()));
            EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[] { String.class, Class.class, Integer.TYPE, Boolean.TYPE }, new Object[] { name, CraftCustomEntity.class, typeId, false });

            NAME_MAP.put(name.toLowerCase(), bukkitType);
            ID_MAP.put((short)typeId, bukkitType);
        }
    }
}
