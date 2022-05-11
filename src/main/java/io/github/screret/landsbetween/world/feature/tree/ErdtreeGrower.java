package io.github.screret.landsbetween.world.feature.tree;

import io.github.screret.landsbetween.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Random;

public class ErdtreeGrower extends AbstractTreeGrower {

    @org.jetbrains.annotations.Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random pRandom, boolean pLargeHive) {
        return ModConfiguredFeatures.MINOR_ERDTREE;
    }
}
