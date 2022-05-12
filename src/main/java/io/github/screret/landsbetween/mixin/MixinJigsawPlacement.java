package io.github.screret.landsbetween.mixin;

import com.google.common.collect.Lists;
import io.github.screret.landsbetween.LandsBetween;
import io.github.screret.landsbetween.registry.ModRegistry;
import net.minecraft.core.*;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(JigsawPlacement.class)
public class MixinJigsawPlacement {

    @Inject(at = @At("HEAD"), cancellable = true, method = "addPieces(Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceFactory;Lnet/minecraft/core/BlockPos;ZZ)Ljava/util/Optional;")
    private static void mixinAddPieces(PieceGeneratorSupplier.Context<JigsawConfiguration> supplier, JigsawPlacement.PieceFactory factory, BlockPos pos, boolean villageBoundsFix, boolean placeAtHeightmap, CallbackInfoReturnable<Optional<PieceGenerator<JigsawConfiguration>>> cir) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(supplier.seed(), supplier.chunkPos().x, supplier.chunkPos().z);
        RegistryAccess registryaccess = supplier.registryAccess();
        JigsawConfiguration jigsawconfiguration = supplier.config();
        ChunkGenerator chunkgenerator = supplier.chunkGenerator();
        StructureManager structuremanager = supplier.structureManager();
        LevelHeightAccessor level = supplier.heightAccessor();
        Predicate<Holder<Biome>> predicate = supplier.validBiome();
        StructureFeature.bootstrap();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = jigsawconfiguration.startPool().value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            cir.setReturnValue(Optional.empty());
        } else {
            PoolElementStructurePiece poolelementstructurepiece = factory.create(structuremanager, structurepoolelement, pos, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuremanager, pos, rotation));
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int centerX = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int centerZ = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int centerY;
            if (placeAtHeightmap) {
                centerY = pos.getY() + chunkgenerator.getFirstFreeHeight(centerX, centerZ, Heightmap.Types.WORLD_SURFACE_WG, level);
            } else {
                centerY = pos.getY();
            }

            if (!predicate.test(chunkgenerator.getNoiseBiome(QuartPos.fromBlock(centerX), QuartPos.fromBlock(centerY), QuartPos.fromBlock(centerZ)))) {
                cir.setReturnValue(Optional.empty());
            } else {
                int terrainLevel = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
                poolelementstructurepiece.move(0, centerY - terrainLevel, 0);
                cir.setReturnValue(Optional.of((builder, p_210283_) -> {
                    List<PoolElementStructurePiece> list = Lists.newArrayList();
                    list.add(poolelementstructurepiece);
                    if (jigsawconfiguration.maxDepth() > 0) {
                        AABB safeBounds = new AABB(centerX - ModRegistry.MAX_JIGSAW_SIZE_ONEDIR, centerY - ModRegistry.MAX_JIGSAW_SIZE_ONEDIR * 4, centerZ - ModRegistry.MAX_JIGSAW_SIZE_ONEDIR, centerX + ModRegistry.MAX_JIGSAW_SIZE_ONEDIR + 1, centerY + ModRegistry.MAX_JIGSAW_SIZE_ONEDIR * 4 + 1, centerZ + ModRegistry.MAX_JIGSAW_SIZE_ONEDIR + 1);
                        LandsBetween.LOGGER.error(safeBounds.toString());
                        JigsawPlacement.Placer jigsawPlacer = new JigsawPlacement.Placer(registry, jigsawconfiguration.maxDepth(), factory, chunkgenerator, structuremanager, list, worldgenrandom);
                        jigsawPlacer.placing.addLast(new JigsawPlacement.PieceState(poolelementstructurepiece, new MutableObject<>(Shapes.join(Shapes.create(safeBounds), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST)), 0));

                        while(!jigsawPlacer.placing.isEmpty()) {
                            JigsawPlacement.PieceState jigsawPieceState = jigsawPlacer.placing.removeFirst();
                            jigsawPlacer.tryPlacingChildren(jigsawPieceState.piece, jigsawPieceState.free, jigsawPieceState.depth, villageBoundsFix, level);
                        }

                        list.forEach(builder::addPiece);
                    }
                }));
            }
        }
        return;
    }
}
