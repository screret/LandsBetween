package io.github.screret.landsbetween.world.feature;

import io.github.screret.landsbetween.LandsBetween;
import io.github.screret.landsbetween.registry.ModRegistry;
import io.github.screret.landsbetween.world.feature.tree.ThickGiantTrunkPlacer;
import io.github.screret.landsbetween.world.feature.tree.UnlockedRandomSpreadFoliagePlacer;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {

    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> MINOR_ERDTREE =
            FeatureUtils.register(LandsBetween.MOD_ID + ":minor_erdtree", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(Blocks.OAK_LOG),
                    new ThickGiantTrunkPlacer(96, 2, 16, true, 8, UniformInt.of(4, 6), 4),
                    BlockStateProvider.simple(ModRegistry.ERDTREE_LEAVES.get()),
                    new UnlockedRandomSpreadFoliagePlacer(ConstantInt.of(32), ConstantInt.of(16), ConstantInt.of(32), 4096),
                    new ThreeLayersFeatureSize(1, 2, 1, 2, 1, OptionalInt.empty())).ignoreVines().build());

    public static final Holder<PlacedFeature> ERDTREE_CHECKED = PlacementUtils.register(LandsBetween.MOD_ID + ":erdtree_checked", MINOR_ERDTREE, PlacementUtils.filteredByBlockSurvival(ModRegistry.ERDTREE_SAPLING.get()));

    public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> ERDTREE_SPAWN =
            FeatureUtils.register(LandsBetween.MOD_ID + ":erdtree_spawn", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ERDTREE_CHECKED,
                            0.05F)), ERDTREE_CHECKED));


    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> ERDLEAF_FLOWER =
            FeatureUtils.register(LandsBetween.MOD_ID + ":flower_erdleaf", Feature.FLOWER,
                    new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(ModRegistry.ERDLEAF_FLOWER.get())))));
}
