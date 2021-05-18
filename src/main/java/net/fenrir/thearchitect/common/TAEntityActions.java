package net.fenrir.thearchitect.common;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeRegistry;
import io.github.apace100.origins.power.VariableIntPower;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModComponents;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
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
        register(new ActionFactory<>(new Identifier(TheArchitect.MODID, "set_min"), new SerializableData().add("resource", SerializableDataType.IDENTIFIER),
                (data, entity) -> {
                    if (entity instanceof ServerPlayerEntity) {
                        OriginComponent component = ModComponents.ORIGIN.get(entity);
                        PowerType<?> powerType = PowerTypeRegistry.get(data.getId("resource"));
                        Power p = component.getPower(powerType);
                        if (p instanceof VariableIntPower) {
                            ((VariableIntPower) p).setValue(((VariableIntPower) p).getMin());
                        }
                    }
                }));
        register(new ActionFactory<>(new Identifier(TheArchitect.MODID, "set_resource_y_level"), new SerializableData().add("resource", SerializableDataType.IDENTIFIER),
                (data, entity) -> {
                    if (entity instanceof ServerPlayerEntity) {
                        OriginComponent component = ModComponents.ORIGIN.get(entity);
                        entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDER_EYE_DEATH, entity.getSoundCategory(), 1.0F, 1.0F);
                        PowerType<?> powerType = PowerTypeRegistry.get(data.getId("resource"));
                        Power p = component.getPower(powerType);
                        if (p instanceof VariableIntPower) {
                            ((VariableIntPower) p).setValue((int) Math.floor(entity.getY()));
                        }
                    }
                }));
    }

}
