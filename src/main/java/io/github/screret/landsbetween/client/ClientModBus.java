package io.github.screret.landsbetween.client;

import io.github.screret.landsbetween.LandsBetween;
import io.github.screret.landsbetween.registry.ModItems;
import io.github.screret.landsbetween.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LandsBetween.MOD_ID)
public class ClientModBus {

    @SubscribeEvent
    public void registerBlockColors(final ColorHandlerEvent.Block event){
        event.getBlockColors().register((state, tintGetter, pos, biomeTint) -> 0xd3c573, ModRegistry.ERDTREE_LEAVES.get());
    }

    @SubscribeEvent
    public void registerItemColors(final ColorHandlerEvent.Item event){
        event.getItemColors().register((itemStack, tintIndex) -> {
            BlockState blockstate = ((BlockItem)itemStack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, (BlockAndTintGetter)null, (BlockPos)null, tintIndex);
        }, ModItems.ERDTREE_LEAVES.get());
    }

}
