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


    public static void initialization() { }

}
