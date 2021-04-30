package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TAItemConditions {
    public static void initialization() {
        register(new ConditionFactory<ItemStack>(new Identifier(TheArchitect.MODID, "item"), new SerializableData()
                .add("item", SerializableDataType.ITEM),
                (data, stack) -> stack.getItem() == data.get("item")
        ));
    }

    private static void register(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ModRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
