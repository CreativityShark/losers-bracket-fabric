package net.creativityshark.losers_bracket;

import net.creativityshark.losers_bracket.entity.ModEntities;
import net.creativityshark.losers_bracket.entity.glare.GlareEntityModel;
import net.creativityshark.losers_bracket.entity.glare.GlareEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;

@Environment(EnvType.CLIENT)
public class LosersBracketClient implements ClientModInitializer {
    public void onInitializeClient() {

        ModEntities.registerModEntities();

        EntityRendererRegistryImpl.register(ModEntities.GLARE,
                ctx -> new GlareEntityRenderer(ctx, new GlareEntityModel()));
    }
}
