package net.creativityshark.losers_bracket.entity.glare;

import net.creativityshark.losers_bracket.LosersBracketMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GlareEntityModel extends AnimatedGeoModel<GlareEntity>
{
    @Override
    public Identifier getModelLocation(GlareEntity object)
    {
        return new Identifier(LosersBracketMod.MOD_ID, "geo/entity/glare.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GlareEntity object)
    {
        return new Identifier(LosersBracketMod.MOD_ID, "textures/entity/glare/glare.png");
    }

    @Override
    public Identifier getAnimationFileLocation(GlareEntity object)
    {
        return new Identifier(LosersBracketMod.MOD_ID, "animations/glare/glare.animation.json");
    }
}