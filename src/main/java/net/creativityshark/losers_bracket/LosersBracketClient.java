package net.creativityshark.losers_bracket;

import net.creativityshark.losers_bracket.entity.ModEntities;
import net.creativityshark.losers_bracket.entity.glare.GlareEntityModel;
import net.creativityshark.losers_bracket.entity.glare.GlareEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class LosersBracketClient implements ClientModInitializer {
    public static final EntityModelLayer GLARE_MODEL_LAYER = new EntityModelLayer(new Identifier(LosersBracketMod.MOD_ID, "glare"), "main");
    public void onInitializeClient() {
        //register entities
        ModEntities.registerModEntities();

        EntityRendererRegistry.register(ModEntities.GLARE, GlareEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(GLARE_MODEL_LAYER, GlareEntityModel::getTexturedModelData);
    }
}
