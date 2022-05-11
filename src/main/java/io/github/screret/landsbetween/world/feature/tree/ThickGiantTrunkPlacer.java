package io.github.screret.landsbetween.world.feature.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.screret.landsbetween.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction8;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class ThickGiantTrunkPlacer extends TrunkPlacer {

    public static final Codec<ThickGiantTrunkPlacer> CODEC = RecordCodecBuilder.create((builder) -> {
        return trunkPlacerParts(builder).apply(builder, ThickGiantTrunkPlacer::new);
    });

    private final boolean placeRoots;
    private final int rootHeight;
    private final IntProvider rootLength;
    private final int rootWidth;

    public ThickGiantTrunkPlacer(int baseHeight, int pHeightRandA, int heightRandB) {
        super(baseHeight, pHeightRandA, heightRandB);
        this.placeRoots = true;
        this.rootHeight = 8;
        this.rootLength = UniformInt.of(4, 6);
        this.rootWidth = 4;
    }

    public ThickGiantTrunkPlacer(int baseHeight, int pHeightRandA, int heightRandB, boolean placeRoots, int rootHeight, IntProvider rootLength, int rootWidth) {
        super(baseHeight, pHeightRandA, heightRandB);
        this.placeRoots = placeRoots;
        this.rootHeight = rootHeight;
        this.rootLength = rootLength;
        this.rootWidth = rootWidth;
    }

    public TrunkPlacerType<?> type() {
        return ModRegistry.THICK_GIANT_TRUNK_PLACER.get();
    }

    public List<FoliagePlacer.FoliageAttachment> placeTrunkPillar(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        BlockPos blockpos = pPos.below();
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos, pConfig);
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos.east(), pConfig);
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos.south(), pConfig);
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos.south().east(), pConfig);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int y = 0; y < pFreeTreeHeight; ++y) {
            placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 1, y, 1);
            if (y < pFreeTreeHeight - 1) {
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 0, y, 0);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 0, y, 1);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 0, y, 2);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 1, y, 0);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 2, y, 0);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 1, y, 2);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 2, y, 2);
                placeLogIfFreeWithOffset(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig, pPos, 2, y, 1);
            }
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pPos.above(pFreeTreeHeight), 0, true));
    }

    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        list.addAll(this.placeTrunkPillar(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, pConfig));

        if(this.placeRoots) {
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.NORTH, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.NORTH_EAST, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.NORTH_WEST, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.EAST, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.SOUTH_EAST, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.SOUTH, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.SOUTH_WEST, pPos, pConfig));
            list.addAll(this.placeRoot(pLevel, pBlockSetter, pRandom, Direction8.WEST, pPos, pConfig));
        }

        for(int y = pFreeTreeHeight - 2 - pRandom.nextInt(4); y > pFreeTreeHeight / 2; y -= 2 + pRandom.nextInt(4)) {
            float rot = pRandom.nextFloat() * ((float)Math.PI * 2F);
            int x = 0;
            int z = 0;

            for(int y1 = 0; y1 < 5; ++y1) {
                x = (int)(1.5F + Mth.cos(rot) * (float)y1);
                z = (int)(1.5F + Mth.sin(rot) * (float)y1);
                BlockPos blockpos = pPos.offset(x, y - 3 + y1 / 2, z);
                placeLog(pLevel, pBlockSetter, pRandom, blockpos, pConfig);
            }

            list.add(new FoliagePlacer.FoliageAttachment(pPos.offset(x, y, z), -2, false));
        }

        return list;
    }

    private List<FoliagePlacer.FoliageAttachment> placeRoot(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, Direction8 direction, BlockPos pPos, TreeConfiguration pConfig){
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();

        for(int y = this.rootHeight; y >= 0; --y) {
            if (y - 1 <= this.rootHeight - pRandom.nextInt(2)) {
                ThickGiantTrunkPlacer.moveToDirection8(blockpos$mutableblockpos, direction);
            }

            if (TreeFeature.validTreePos(pLevel, blockpos$mutableblockpos)) {
                placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig);
            }

            if (y <= 0) {
                list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos.immutable(), 0, false));
            }

            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        int length = this.rootLength.sample(pRandom);

        for(int loop = 0; loop <= length; ++loop) {
            if (TreeFeature.validTreePos(pLevel, blockpos$mutableblockpos)) {
                placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig);
            }

            list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos.immutable(), 0, false));
            ThickGiantTrunkPlacer.moveToDirection8(blockpos$mutableblockpos, direction);
        }

        return list;
    }

    private static void moveToDirection8(BlockPos.MutableBlockPos mutableBlockPos, Direction8 direction){
        switch (direction){
            case NORTH -> mutableBlockPos.move(0, 0, -1);
            case NORTH_WEST -> mutableBlockPos.move(-1, 0, -1);
            case WEST -> mutableBlockPos.move(-1, 0, 0);
            case SOUTH_WEST -> mutableBlockPos.move(-1, 0, 1);
            case SOUTH -> mutableBlockPos.move(0, 0, 1);
            case SOUTH_EAST -> mutableBlockPos.move(1, 0, 1);
            case EAST -> mutableBlockPos.move(1, 0, 0);
            case NORTH_EAST -> mutableBlockPos.move(1, 0, -1);
        }
    }

    private static void placeLogIfFreeWithOffset(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, BlockPos.MutableBlockPos pPos, TreeConfiguration pConfig, BlockPos pOffsetPos, int pOffsetX, int pOffsetY, int pOffsetZ) {
        pPos.setWithOffset(pOffsetPos, pOffsetX, pOffsetY, pOffsetZ);
        placeLogIfFree(pLevel, pBlockSetter, pRandom, pPos, pConfig);
    }

    protected static <P extends TrunkPlacer> Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer> trunkPlacerParts(RecordCodecBuilder.Instance<P> pInstance) {
        return pInstance.group(Codec.intRange(0, 128).fieldOf("base_height").forGetter((placer) -> {
            return placer.baseHeight;
        }), Codec.intRange(0, 64).fieldOf("height_rand_a").forGetter((placer) -> {
            return placer.heightRandA;
        }), Codec.intRange(0, 64).fieldOf("height_rand_b").forGetter((placer) -> {
            return placer.heightRandB;
        }));
    }
}