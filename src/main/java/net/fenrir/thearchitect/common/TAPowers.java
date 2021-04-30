package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class TAPowers {

    public static final PowerType<Power> NONFLAMMABLE = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "nonflammable"));
    public static final PowerType<Power> ARCHITECTS_FOCUS = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "architects_focus"));
    public static final PowerType<Power> AUROPHOBIA = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "aurophobia"));
    public static final PowerType<Power> UNNATURAL_COMPOSITION = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "unnatural_composition"));
    private static final Map<PowerFactory<?>, Identifier> FACTORY = new LinkedHashMap<>();

    private static <T extends Power> PowerFactory<T> create(PowerFactory<T> factory) {
        FACTORY.put(factory, factory.getSerializerId());
        return factory;
    }

    public static void initialization() {
        FACTORY.keySet().forEach(powerType -> Registry.register(ModRegistries.POWER_FACTORY, FACTORY.get(powerType), powerType));
    }

}
