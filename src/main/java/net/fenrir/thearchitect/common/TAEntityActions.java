package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TAEntityActions {

    public static final TranslatableText CONTAINER_NAME = new TranslatableText("container.enderchest");

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    public static void initialization() {

        register(new ActionFactory<>(new Identifier(TheArchitect.MODID, "open_enderchest"), new SerializableData(),
                (data, entity) -> {
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    EnderChestInventory enderChestInventory = playerEntity.getEnderChestInventory();
                    playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity2) ->
                            GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChestInventory), CONTAINER_NAME));
                }));
        register(new ActionFactory<>(new Identifier(TheArchitect.MODID, "create_rift"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof ServerPlayerEntity) {
                        RiftHelper.createRift(entity.world, entity.getBlockPos());
                    }
                }));
    }

}
