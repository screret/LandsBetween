package io.github.screret.landsbetween.mixin;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer")
public class MixinTrunkPlacer {

    @Inject(at = @At("HEAD"), method = "trunkPlacerParts(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P3;", cancellable = true)
    private static <P extends TrunkPlacer> void trunkPlacerParts(RecordCodecBuilder.Instance<P> pInstance, CallbackInfoReturnable<Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer>> cir) {
        cir.setReturnValue(pInstance.group(Codec.intRange(0, 128).fieldOf("base_height").forGetter((placer) -> {
            return placer.baseHeight;
        }), Codec.intRange(0, 64).fieldOf("height_rand_a").forGetter((placer) -> {
            return placer.heightRandA;
        }), Codec.intRange(0, 64).fieldOf("height_rand_b").forGetter((placer) -> {
            return placer.heightRandB;
        })));
    }
}
