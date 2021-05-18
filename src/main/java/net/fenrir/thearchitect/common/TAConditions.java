package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.Comparison;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class TAConditions {
    private static final Map<ConditionFactory<LivingEntity>, Identifier> CONDITIONS = new LinkedHashMap<>();
    public static final ConditionFactory<LivingEntity> TOUCHING_GOLD = create(new ConditionFactory<>(new Identifier(TheArchitect.MODID, "touching_gold"), new SerializableData(), (instance, playerEntity) -> GoldHelper.goldContact(playerEntity)));
    public static final ConditionFactory<LivingEntity> MAX_HEALTH = create(new ConditionFactory<>(new Identifier(TheArchitect.MODID, "max_health"), new SerializableData()
            .add("comparison", SerializableDataType.COMPARISON)
            .add("compare_to", SerializableDataType.FLOAT),

            (data, entity) -> ((Comparison) data.get("comparison")).compare(entity.getMaxHealth(), data.getFloat("compare_to"))));
    private static final Map<ConditionFactory<Pair<DamageSource, Float>>, Identifier> CONDITIONS2 = new LinkedHashMap<>();
    public static final ConditionFactory<Pair<DamageSource, Float>> IS_MAGIC = create2(new ConditionFactory<>(new Identifier(TheArchitect.MODID, "is_magic"), new SerializableData(), (data, dmg) -> dmg.getLeft().getMagic()));

    private static ConditionFactory<LivingEntity> create(ConditionFactory<LivingEntity> factory) {
        CONDITIONS.put(factory, factory.getSerializerId());
        return factory;
    }

    private static ConditionFactory<Pair<DamageSource, Float>> create2(ConditionFactory<Pair<DamageSource, Float>> factory) {
        CONDITIONS2.put(factory, factory.getSerializerId());
        return factory;
    }

    public static void initialization() {
        CONDITIONS.keySet().forEach(condition -> Registry.register(ModRegistries.ENTITY_CONDITION, CONDITIONS.get(condition), condition));
        CONDITIONS2.keySet().forEach(condition -> Registry.register(ModRegistries.DAMAGE_CONDITION, CONDITIONS2.get(condition), condition));
    }
}
