package net.creativityshark.losers_bracket.entity.item;

import net.creativityshark.losers_bracket.LosersBracketMod;
import net.creativityshark.losers_bracket.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item GLARE_SPAWN_EGG = registerItem("glare_spawn_egg",
            new SpawnEggItem(ModEntities.GLARE, 7377453, 15572735,
                    new FabricItemSettings().group(ItemGroup.MISC)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(LosersBracketMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        System.out.println("Registering mod items for Loser's Bracket");
    }
}
