package net.fenrir.thearchitect.common;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.fenrir.thearchitect.TheArchitect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class TAEntityActions {

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    public static void initialization() {
        register(new ActionFactory<>(new Identifier(TheArchitect.MODID, "increase_resistance"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) entity;
                        StatusEffectInstance resistance = living.getStatusEffect(StatusEffects.RESISTANCE);
                        if (resistance != null) {
                            int newDuration = resistance.getDuration() + 60 * 20;
                            if (resistance.getAmplifier() < 2 && newDuration >= 5 * 60 * 20) {
                                resistance = new StatusEffectInstance(
                                        StatusEffects.RESISTANCE,
                                        60 * 20,
                                        resistance.getAmplifier() + 1
                                );
                            } else {
                                resistance = new StatusEffectInstance(
                                        StatusEffects.RESISTANCE,
                                        newDuration,
                                        resistance.getAmplifier()
                                );
                            }
                        } else {
                            resistance = new StatusEffectInstance(
                                    StatusEffects.RESISTANCE,
                                    60 * 20,
                                    0
                            );
                        }
                        living.addStatusEffect(resistance);
                    }
                }));
        register(new ActionFactory<>(new Identifier(TheArchitect.MODID, "use_curse"), new SerializableData().add("range", SerializableDataType.INT).add("cost", SerializableDataType.INT, 3),
                (data, entity) -> {
                    PlayerEntity playerEntity = (PlayerEntity) entity;
                    PlayerInterface playerInterface = (PlayerInterface) playerEntity;
                    int cost = data.getInt("cost");
                    int totalCurses = playerInterface.getCurses();
                    int levels = playerEntity.experienceLevel;
                    if (totalCurses > 0) {
                        if (levels < cost) {
                            return;
                        }
                    }
                    int range = data.getInt("range");
                    Vec3d startPoint = playerEntity.getCameraPosVec(1);
                    Vec3d lookVec = playerEntity.getRotationVec(1);
                    Vec3d endPoint = startPoint.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
                    EntityHitResult result = RayHelper.raycast(playerEntity, startPoint, endPoint, new Box(playerEntity.getBlockPos()).expand(range), Entity::isLiving, range * range);
                    Entity victim = null;
                    if (result != null) {
                        victim = result.getEntity();
                    }
                    if (victim != null) {
                        int targetCurses = playerInterface.getCursesTarget(victim);
                        if (targetCurses < 6) {
                            if (levels < cost * targetCurses) {
                                return;
                            }
                            ShulkerBulletEntity shulk = new ShulkerBulletEntity(playerEntity.world, playerEntity, victim, playerEntity.getMovementDirection().getAxis());
                            shulk.setCustomName(Text.of("Architect's Curse"));
                            shulk.setCustomNameVisible(false);
                            shulk.setPos(shulk.getX(), shulk.getY() + 2, shulk.getZ());
                            boolean success = playerEntity.world.spawnEntity(shulk);
                            shulk.setOwner(playerEntity);
                            if (success) {
                                if (totalCurses > 0) {
                                    playerEntity.addExperienceLevels(-Math.max(cost * targetCurses, cost));
                                }
                                playerInterface.addCurse(shulk);
                            }
                        }
                    }
                }
        ));
    }

}
