package net.fenrir.thearchitect.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;

public interface PlayerInterface {

    int getCurses();

    void addCurse(ShulkerBulletEntity entity);

    int getCursesTarget(Entity target);

}
