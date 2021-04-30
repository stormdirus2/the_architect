package net.fenrir.thearchitect.mixin;

import net.fenrir.thearchitect.common.ArchitectsCurseAccessor;
import net.fenrir.thearchitect.common.PlayerInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashSet;

@Mixin(PlayerEntity.class)
public class Player implements PlayerInterface {

    public HashSet<ShulkerBulletEntity> curses = new HashSet<>();

    @Override
    public int getCurses() {
        return (int) curses.stream().filter(curse -> curse.isAlive() && !curse.removed && curse.getOwner() == ((Object) this)).count();
    }

    @Override
    public void addCurse(ShulkerBulletEntity entity) {
        curses.add(entity);
    }

    @Override
    public int getCursesTarget(Entity entity) {
        return (int) curses.stream().filter(curse -> curse.isAlive() && !curse.removed && curse.getOwner() == ((Object) this) && entity == ((ArchitectsCurseAccessor) curse).getTarget()).count();
    }
}
