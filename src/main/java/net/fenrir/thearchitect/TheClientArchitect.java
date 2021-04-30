package net.fenrir.thearchitect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Optional;

public class TheClientArchitect implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack.getItem() == TheArchitect.ARCHITECTS_FOCUS) {
                CompoundTag compoundTag = stack.getOrCreateSubTag(TheArchitect.MODID);
                Optional<RegistryKey<World>> world2 = World.CODEC.parse(NbtOps.INSTANCE, compoundTag.get("warp_world")).result();
                if (compoundTag.contains("warp_pos") && world2.isPresent()) {
                    RegistryKey<World> realWorld2 = world2.get();
                    BlockPos pos = NbtHelper.toBlockPos(compoundTag.getCompound("warp_pos"));
                    Text text = Text.of(pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " - " + realWorld2.getValue().getPath().toUpperCase());
                    lines.add(text);
                }
            }
        });
    }
}
