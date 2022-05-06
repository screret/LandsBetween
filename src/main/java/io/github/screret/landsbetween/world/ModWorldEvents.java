package io.github.screret.landsbetween.world;

import io.github.screret.landsbetween.LandsBetween;
import io.github.screret.landsbetween.world.gen.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LandsBetween.MOD_ID)
public class ModWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModTreeGeneration.generateTrees(event);
        ModFlowerGeneration.generateFlowers(event);

        ModEntityGeneration.onEntitySpawn(event);
    }
}