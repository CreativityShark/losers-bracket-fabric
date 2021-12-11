package net.creativityshark.losers_bracket.entity.glare;

import net.creativityshark.losers_bracket.LosersBracketClient;
import net.creativityshark.losers_bracket.LosersBracketMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GlareEntityRenderer extends MobEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(LosersBracketMod.MOD_ID, "textures/entity/glare/glare.png");

    public GlareEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GlareEntityModel(context.getPart(LosersBracketClient.GLARE_MODEL_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }
}
