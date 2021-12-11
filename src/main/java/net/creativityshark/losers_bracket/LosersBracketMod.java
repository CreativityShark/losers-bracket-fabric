package net.creativityshark.losers_bracket;

import net.creativityshark.losers_bracket.entity.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class LosersBracketMod implements ModInitializer {
	public static final String MOD_ID = "losers_bracket";

	//Packet IDs
	public static final Identifier GLARE_TICKS_NAPPING_PACKET_ID = new Identifier(MOD_ID, "ticks_napping");
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItems.registerModItems();
	}
}
