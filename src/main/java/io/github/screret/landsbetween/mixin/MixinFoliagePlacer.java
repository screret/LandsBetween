package io.github.screret.landsbetween.mixin;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer")
public class MixinFoliagePlacer {

    @Inject(at = @At("HEAD"), method = "foliagePlacerParts(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P2;", cancellable = true)
    private static <P extends FoliagePlacer> void foliagePlacerParts(RecordCodecBuilder.Instance<P> pInstance, CallbackInfoReturnable<Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider>> cir) {
        cir.setReturnValue(pInstance.group(IntProvider.codec(0, 128).fieldOf("radius").forGetter((p_161449_) -> {
            return p_161449_.radius;
        }), IntProvider.codec(0, 128).fieldOf("offset").forGetter((p_161447_) -> {
            return p_161447_.offset;
        })));
    }
}
