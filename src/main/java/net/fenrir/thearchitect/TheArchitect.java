package net.fenrir.thearchitect;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.fabricmc.api.ModInitializer;
import net.fenrir.thearchitect.common.ArchitectsFocus;
import net.fenrir.thearchitect.common.TAConditions;
import net.fenrir.thearchitect.common.TAItemConditions;
import net.fenrir.thearchitect.common.TAPowers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class TheArchitect implements ModInitializer {
    public static final String MODID = "thearchitect";
    public static final String MODNAME = "The Architect";

    public static final Item ARCHITECTS_FOCUS = new ArchitectsFocus(new Item.Settings().maxCount(1).fireproof().rarity(Rarity.EPIC).group(ItemGroup.MISC));

    public TheArchitect() {
        TAConditions.initialization();
        TAPowers.initialization();
        TAItemConditions.initialization();
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MODID, "architects_focus"), ARCHITECTS_FOCUS);
        Logger.getLogger(MODID).log(Level.INFO, "[The Architect] Initialized");
    }
}
