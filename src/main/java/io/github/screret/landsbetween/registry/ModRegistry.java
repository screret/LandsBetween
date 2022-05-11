package io.github.screret.landsbetween.registry;

import com.mojang.serialization.Codec;
import io.github.screret.landsbetween.LandsBetween;
import io.github.screret.landsbetween.world.feature.tree.ErdtreeGrower;
import io.github.screret.landsbetween.world.feature.tree.ThickGiantTrunkPlacer;
import io.github.screret.landsbetween.world.feature.tree.UnlockedRandomSpreadFoliagePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {

    public static final int MAX_JIGSAW_SIZE_ONEDIR = 256;


    //registries
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, LandsBetween.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LandsBetween.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, LandsBetween.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, LandsBetween.MOD_ID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registry.TRUNK_PLACER_TYPE_REGISTRY, LandsBetween.MOD_ID);
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, LandsBetween.MOD_ID);

    //blocks
    public static final RegistryObject<Block> ERDTREE_LEAVES = BLOCKS.register("erdtree_leaves", () -> leaves(SoundType.GRASS));
    public static final RegistryObject<Block> ERDTREE_LOG = BLOCKS.register("erdtree_log", () -> log(MaterialColor.SAND, MaterialColor.GOLD));
    public static final RegistryObject<Block> ERDTREE_WOOD = BLOCKS.register("erdtree_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_ERDTREE_LOG = BLOCKS.register("stripped_erdtree_log", () -> log(MaterialColor.SAND, MaterialColor.SAND));
    public static final RegistryObject<Block> STRIPPED_ERDTREE_WOOD = BLOCKS.register("stripped_erdtree_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> ERDTREE_PLANKS = BLOCKS.register("erdtree_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> ERDTREE_SAPLING = BLOCKS.register("erdtree_sapling", () -> new SaplingBlock(new ErdtreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noOcclusion()));
    public static final RegistryObject<Block> ERDLEAF_FLOWER = BLOCKS.register("erdleaf_flower", () -> new FlowerBlock(MobEffects.HEAL, 2, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));

    //trees
    public static final RegistryObject<TrunkPlacerType<ThickGiantTrunkPlacer>> THICK_GIANT_TRUNK_PLACER = TRUNK_PLACERS.register("thick_giant_trunk_placer", () -> registerTrunkPlacer("thick_giant_trunk_placer", ThickGiantTrunkPlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<UnlockedRandomSpreadFoliagePlacer>> UNLOCKED_RANDOM_SPREAD_FOLIAGE_PLACER = FOLIAGE_PLACERS.register("unlocked_random_spread_foliage_placer", () -> new FoliagePlacerType<>(UnlockedRandomSpreadFoliagePlacer.CODEC));


    private static LeavesBlock leaves(SoundType sound) {
        return new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(sound).noOcclusion().isValidSpawn((state, getter, pos, entityType) -> false).isSuffocating(ModRegistry::blockNever).isViewBlocking(ModRegistry::blockNever).lightLevel((state) -> 6));
    }

    private static RotatedPillarBlock log(MaterialColor pTopColor, MaterialColor pBarkColor) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? pTopColor : pBarkColor).strength(2.0F).sound(SoundType.WOOD));
    }

    private static boolean blockAlways(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    private static boolean blockNever(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    public static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunkPlacer(String pKey, Codec<P> type) {
        return Registry.register(Registry.TRUNK_PLACER_TYPES, pKey, new TrunkPlacerType<>(type));
    }
}
