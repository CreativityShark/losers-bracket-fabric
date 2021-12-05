package net.creativityshark.losers_bracket.entity.glare;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class GlareEntityRenderer extends GeoEntityRenderer<GlareEntity> {

    public GlareEntityRenderer(EntityRendererFactory.Context ctx, AnimatedGeoModel<GlareEntity> modelProvider) {
        super(ctx, modelProvider);
        addLayer(new GlareEmissiveLayerRenderer(this));
        this.shadowRadius = 0.4F;
    }
}
