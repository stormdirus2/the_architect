package net.fenrir.thearchitect.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class GoldHelper {

    public static boolean isGold(Item item) {
        if (item instanceof ToolItem) {
            if (((ToolItem) item).getMaterial() == ToolMaterials.GOLD) {
                return true;
            }
        } else if (item instanceof ArmorItem) {
            if (((ArmorItem) item).getMaterial() == ArmorMaterials.GOLD) {
                return true;
            }
        }
        return item.isIn(ItemTags.PIGLIN_LOVED);
    }

    public static boolean handlingGold(LivingEntity entity) {
        Iterable<ItemStack> iterable = entity.getItemsEquipped();

        for (ItemStack itemStack : iterable) {
            if (!itemStack.isEmpty()) {
                if (isGold(itemStack.getItem())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean goldContact(LivingEntity entity) {
        if (handlingGold(entity)) { return true; }

        Iterator<VoxelShape> blockList = entity.world.getBlockCollisions(entity, entity.getBoundingBox().expand(0.1)).iterator();

        while (blockList.hasNext()) {
            VoxelShape Shape = blockList.next();
            Vec3d pos = Shape.getBoundingBox().getCenter();
            BlockPos blockPos = new BlockPos(pos.x,pos.y,pos.z);
            if (entity.world.getBlockState(blockPos).getBlock().asItem().isIn(ItemTags.PIGLIN_LOVED)) {
                return true;
            }
        }

        return false;
    }
}
