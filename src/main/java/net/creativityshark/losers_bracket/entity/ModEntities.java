package net.creativityshark.losers_bracket.entity;

import net.creativityshark.losers_bracket.LosersBracketMod;
import net.creativityshark.losers_bracket.entity.glare.GlareEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    public static final EntityType GLARE = registerEntity("glare",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GlareEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.25f)).build());

    private static EntityType registerEntity(String name, EntityType entityType) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(LosersBracketMod.MOD_ID, name), entityType);
    }

    public static void registerModEntities() {
        System.out.println("Registering mod entities for " + LosersBracketMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(GLARE, GlareEntity.createGlareAttributes());
    }
}
