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

public class TAConditions {
    private static void registerDamageCondition(ConditionFactory<Pair<DamageSource, Float>> conditionFactory) {
        Registry.register(ModRegistries.DAMAGE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

    private static void register(ConditionFactory<LivingEntity> conditionFactory) {
        Registry.register(ModRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

    public static void initialization() {
        registerDamageCondition(new ConditionFactory<>(new Identifier(TheArchitect.MODID, "is_magic"), new SerializableData(),
                (data, dmg) -> dmg.getLeft().getMagic()));

        register(new ConditionFactory<>(new Identifier(TheArchitect.MODID, "max_health"), new SerializableData()
                .add("comparison", SerializableDataType.COMPARISON)
                .add("compare_to", SerializableDataType.FLOAT),
                (data, entity) -> ((Comparison) data.get("comparison")).compare(entity.getMaxHealth(), data.getFloat("compare_to"))));

        register(new ConditionFactory<>(new Identifier(TheArchitect.MODID, "touching_gold"), new SerializableData(),
                (instance, playerEntity) -> GoldHelper.goldContact(playerEntity)));
    }
}
