package io.github.screret.landsbetween.registry;

import io.github.screret.landsbetween.LandsBetween;
import io.github.screret.landsbetween.equipment.ModArmorMaterials;
import io.github.screret.landsbetween.equipment.ModTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LandsBetween.MOD_ID);

    //items
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().tab(LandsBetween.MOD_TAB)));

    public static final RegistryObject<Item> STEEL_HELMET = ITEMS.register("steel_helmet", () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.HEAD, new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate", () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.CHEST, new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> STEEL_LEGGINGS = ITEMS.register("steel_leggings", () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.LEGS, new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> STEEL_BOOTS = ITEMS.register("steel_boots", () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.FEET, new Item.Properties().tab(LandsBetween.MOD_TAB)));

    public static final RegistryObject<Item> STEEL_SWORD = ITEMS.register("steel_sword", () -> new SwordItem(ModTiers.STEEL,3, -2.4F, new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> STEEL_AXE = ITEMS.register("steel_axe", () -> new AxeItem(ModTiers.STEEL,5.0F, -3.0F, new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", () -> new PickaxeItem(ModTiers.STEEL,1, -2.8F, new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> STEEL_SHOVEL = ITEMS.register("steel_shovel", () -> new ShovelItem(ModTiers.STEEL,1.5F, -3.0F, new Item.Properties().tab(LandsBetween.MOD_TAB)));

    //blockItems
    public static final RegistryObject<Item> ERDTREE_LEAVES = ITEMS.register("erdtree_leaves", () -> new BlockItem(ModRegistry.ERDTREE_LEAVES.get(), new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> ERDTREE_LOG = ITEMS.register("erdtree_log", () -> new BlockItem(ModRegistry.ERDTREE_LOG.get(), new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> ERDTREE_PLANKS = ITEMS.register("erdtree_planks", () -> new BlockItem(ModRegistry.ERDTREE_PLANKS.get(), new Item.Properties().tab(LandsBetween.MOD_TAB)));

    public static final RegistryObject<Item> ERDTREE_SAPLING = ITEMS.register("erdtree_sapling", () -> new BlockItem(ModRegistry.ERDTREE_SAPLING.get(), new Item.Properties().tab(LandsBetween.MOD_TAB)));
    public static final RegistryObject<Item> ERDLEAF_FLOWER = ITEMS.register("erdleaf_flower", () -> new BlockItem(ModRegistry.ERDLEAF_FLOWER.get(), new Item.Properties().tab(LandsBetween.MOD_TAB)));

}
