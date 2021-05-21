package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.util.Identifier;

public class TAPowers {

    public static final PowerType<Power> NONFLAMMABLE = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "nonflammable"));
    public static final PowerType<Power> ARCHITECTS_FOCUS = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "architects_focus"));
    public static final PowerType<Power> AUROPHOBIA = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "aurophobia"));
    public static final PowerType<Power> UNNATURAL_COMPOSITION = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "unnatural_composition"));
    public static final PowerType<Power> DIMENSIONAL_DISPLACEMENT = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "dimensional_displacement"));
    public static final PowerType<Power> ARCHITECTS_PARTICLE = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "architects_particle"));
    public static final PowerType<Power> PERITIA_EFFICIENCY = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "peritia_efficiency"));
    public static final PowerType<Power> SPATIAL_STRIDE = new PowerTypeReference<>(new Identifier(TheArchitect.MODID, "spatial_stride"));


    public static void initialization() {
    }

}
