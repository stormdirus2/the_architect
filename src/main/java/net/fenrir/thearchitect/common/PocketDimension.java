package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.InventoryPower;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;

import java.util.function.Predicate;

public abstract class PocketDimension extends InventoryPower {

    private final TranslatableText containerName;
    private final ScreenHandlerFactory factory;

    public PocketDimension(PowerType<?> type, PlayerEntity player, String containerName, int size, boolean shouldDropOnDeath, Predicate<ItemStack> dropOnDeathFilter) {
        super(type, player, containerName, size, shouldDropOnDeath, dropOnDeathFilter);
        this.containerName = new TranslatableText(containerName);
        this.factory = (i, playerInventory, playerEntity) -> GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, this);
    }

    @Override
    public void onUse() {
        if (!player.world.isClient && isActive()) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(factory, containerName));
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player == this.player;
    }
}
