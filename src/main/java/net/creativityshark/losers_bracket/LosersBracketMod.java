package net.creativityshark.losers_bracket;

import net.creativityshark.losers_bracket.entity.item.ModItems;
import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;

public class LosersBracketMod implements ModInitializer {
	public static final String MOD_ID = "losers_bracket";
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		GeckoLib.initialize();
		ModItems.registerModItems();
	}
}
