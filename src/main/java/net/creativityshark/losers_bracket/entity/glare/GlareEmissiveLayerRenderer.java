package net.creativityshark.losers_bracket.entity.glare;

import net.creativityshark.losers_bracket.LosersBracketMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GlareEmissiveLayerRenderer extends GeoLayerRenderer<GlareEntity> {
    public static final Identifier TEXTURE = new Identifier(LosersBracketMod.MOD_ID, "textures/entity/glare/glare_eyes.png");

    public GlareEmissiveLayerRenderer(IGeoRenderer<GlareEntity> entityRenderIn) {
        super(entityRenderIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, GlareEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderModel(getEntityModel(), TEXTURE, matrixStackIn, bufferIn, 0xF000F0, entitylivingbaseIn, partialTicks, 1, 1, 1);
    }
}
