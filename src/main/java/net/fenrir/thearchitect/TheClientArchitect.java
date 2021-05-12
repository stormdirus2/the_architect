package net.fenrir.thearchitect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fenrir.thearchitect.common.ArchitectsParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class TheClientArchitect implements ClientModInitializer {

    public Random random = new Random();

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(TheArchitect.ARCHITECTS_PARTICLE, ArchitectsParticle.Factory::new);
        ClientPlayNetworking.registerGlobalReceiver(TheArchitect.ARCHITECT_RIFT, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            client.execute(() -> {
                World world = client.world;
                if (world != null) {
                    world.playSound(client.player, pos, SoundEvents.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    for (int a = 0; a < 128; ++a) {
                        world.addParticle((ParticleEffect) TheArchitect.ARCHITECTS_PARTICLE, true, pos.getX() + 0.5, pos.getY() + random.nextDouble() * 2.0D, pos.getZ() + 0.5, random.nextGaussian(), 0.0D, random.nextGaussian());
                    }
                }
            });
        });
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
