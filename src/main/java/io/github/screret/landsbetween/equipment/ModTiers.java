package io.github.screret.landsbetween.equipment;

import io.github.screret.landsbetween.registry.ModItems;
import io.github.screret.landsbetween.registry.ModRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier STEEL = new ForgeTier(3, 836, 2.5f,
            2.25f, 12, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.STEEL_INGOT.get()));

}
