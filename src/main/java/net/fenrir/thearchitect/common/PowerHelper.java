package net.fenrir.thearchitect.common;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeRegistry;
import io.github.apace100.origins.power.VariableIntPower;
import io.github.apace100.origins.registry.ModComponents;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class PowerHelper {

    public final static Identifier DIMENSIONAL_DISPLACEMENT_ONOFF = new Identifier(TheArchitect.MODID, "dimensional_displacement_onoff");
    public final static Identifier SPATIAL_STRIDE_ONOFF = new Identifier(TheArchitect.MODID, "spatial_stride_onoff");

    public static boolean isDimensionallyDisplacing(Entity entity) {
        if (TAPowers.DIMENSIONAL_DISPLACEMENT.isActive(entity)) {
            OriginComponent component = ModComponents.ORIGIN.get(entity);
            PowerType<?> powerType = PowerTypeRegistry.get(DIMENSIONAL_DISPLACEMENT_ONOFF);
            Power p = component.getPower(powerType);
            if (p instanceof VariableIntPower) {
                int resourceValue = ((VariableIntPower) p).getValue();
                return resourceValue == 1;
            }
        }
        return false;
    }

    public static boolean isSpatialStriding(LivingEntity entity) {
        if (TAPowers.SPATIAL_STRIDE.isActive(entity)) {
            if (entity.getMainHandStack().getItem() == TheArchitect.ARCHITECTS_FOCUS) {
                return true;
            }
            OriginComponent component = ModComponents.ORIGIN.get(entity);
            PowerType<?> powerType = PowerTypeRegistry.get(SPATIAL_STRIDE_ONOFF);
            Power p = component.getPower(powerType);
            if (p instanceof VariableIntPower) {
                int resourceValue = ((VariableIntPower) p).getValue();
                return resourceValue >= entity.getY();
            }
        }
        return false;
    }
}
