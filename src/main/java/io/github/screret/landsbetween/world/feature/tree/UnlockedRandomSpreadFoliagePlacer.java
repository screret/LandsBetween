package io.github.screret.landsbetween.world.feature.tree;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.screret.landsbetween.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;

import java.util.Random;
import java.util.function.BiConsumer;

public class UnlockedRandomSpreadFoliagePlacer extends RandomSpreadFoliagePlacer {

    public static final Codec<UnlockedRandomSpreadFoliagePlacer> CODEC = RecordCodecBuilder.create((builder) -> {
        return foliagePlacerParts(builder).and(builder.group(IntProvider.codec(1, 512).fieldOf("foliage_height").forGetter((placer) -> {
            return placer.foliageHeight;
        }), Codec.intRange(0, 4096).fieldOf("leaf_placement_attempts").forGetter((placer) -> {
            return placer.leafPlacementAttempts;
        }))).apply(builder, UnlockedRandomSpreadFoliagePlacer::new);
    });
    public UnlockedRandomSpreadFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider foliageHeight, int leafPlacementAttempts) {
        super(radius, offset, foliageHeight, leafPlacementAttempts);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModRegistry.UNLOCKED_RANDOM_SPREAD_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliagePlacer.FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
        BlockPos blockpos = pAttachment.pos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable();

        for(int i = 0; i < this.leafPlacementAttempts; ++i) {
            blockpos$mutableblockpos.setWithOffset(blockpos, pRandom.nextInt(pFoliageRadius) - pRandom.nextInt(pFoliageRadius), pRandom.nextInt(pFoliageHeight) - pRandom.nextInt(pFoliageHeight), pRandom.nextInt(pFoliageRadius) - pRandom.nextInt(pFoliageRadius));
            tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, blockpos$mutableblockpos);
        }

    }

    @Override
    public int foliageHeight(Random pRandom, int pHeight, TreeConfiguration pConfig) {
        return this.foliageHeight.sample(pRandom);
    }

    /**
     * Skips certain positions based on the provided shape, such as rounding corners randomly.
     * The coordinates are passed in as absolute value, and should be within [0, {@code range}].
     */
    @Override
    protected boolean shouldSkipLocation(Random pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        return false;
    }

    protected static <P extends FoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider> foliagePlacerParts(RecordCodecBuilder.Instance<P> pInstance) {
        return pInstance.group(IntProvider.codec(0, 128).fieldOf("radius").forGetter((placer) -> {
            return placer.radius;
        }), IntProvider.codec(0, 128).fieldOf("offset").forGetter((placer) -> {
            return placer.offset;
        }));
    }
}
