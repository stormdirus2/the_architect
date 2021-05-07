package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.*;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TAPowers {

    public static final PowerType<Power> NONFLAMMABLE = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "nonflammable"));
    public static final PowerType<Power> ARCHITECTS_FOCUS = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "architects_focus"));
    public static final PowerType<Power> AUROPHOBIA = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "aurophobia"));
    public static final PowerType<Power> UNNATURAL_COMPOSITION = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "unnatural_composition"));

    private static void create(PowerFactory<Power> serializer) {
        Registry.register(ModRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }


    public static void initialization() {
        create(new PowerFactory<>(new Identifier("thearchitect", "pocket_dimension"),
                new SerializableData()
                        .add("name", SerializableDataType.STRING, "container.inventory")
                        .add("drop_on_death", SerializableDataType.BOOLEAN, false)
                        .add("storage_space", SerializableDataType.INT, 27)
                        .add("drop_on_death_filter", SerializableDataType.ITEM_CONDITION, null)
                        .add("key", SerializableDataType.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                data ->
                        (type, player) -> {
                            InventoryPower power = new PocketDimension(type, player, data.getString("name"), data.getInt("storage_space"),
                                    data.getBoolean("drop_on_death"),
                                    data.isPresent("drop_on_death_filter") ? (ConditionFactory<ItemStack>.Instance) data.get("drop_on_death_filter") :
                                            itemStack -> true) {
                            };
                            power.setKey((Active.Key) data.get("key"));
                            return power;
                        }).allowCondition());
    }

}
