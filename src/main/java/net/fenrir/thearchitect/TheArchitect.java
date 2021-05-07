package net.fenrir.thearchitect;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fenrir.thearchitect.common.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TheArchitect implements ModInitializer {
    public static final String MODID = "thearchitect";
    public static final String MODNAME = "The Architect";

    public static final Item ARCHITECTS_FOCUS = new ArchitectsFocus(new Item.Settings().maxCount(1).fireproof().rarity(Rarity.RARE).group(ItemGroup.MISC));

    public static final Tag<Item> GOLD = TagRegistry.item(new Identifier(MODID, "gold"));

    public TheArchitect() {
        TAConditions.initialization();
        TAPowers.initialization();
        TAItemConditions.initialization();
        TAEntityActions.initialization();
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MODID, "architects_focus"), ARCHITECTS_FOCUS);
        Logger.getLogger(MODID).log(Level.INFO, "[The Architect] Initialized");
    }
}
