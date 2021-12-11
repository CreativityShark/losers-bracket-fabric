package net.creativityshark.losers_bracket.entity.glare;

import net.creativityshark.losers_bracket.LosersBracketMod;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class GlareEntity extends AnimalEntity implements Flutterer {

    int ticksToFindShade;
    int ticksNapped = 20;
    boolean isNapping;
    BlockPos shade;
    GlareEntity.GlareMoveToShadeGoal glareMoveToShadeGoal;

    public GlareEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 0.5d));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new TemptGoal(this, 1d, Ingredient.ofItems(Items.BONE_MEAL), false));
        this.goalSelector.add(2, new GlareFindShadeGoal());
        //I have no idea why the move goal is all funky, but it is like that in the bee code, and I'm
        //too scared to change it
        this.glareMoveToShadeGoal = new GlareMoveToShadeGoal();
        this.goalSelector.add(2, glareMoveToShadeGoal);
        this.goalSelector.add(2, new GlareNapGoal());
        this.goalSelector.add(3, new GlareWanderGoal(this));
        this.goalSelector.add(4, new GlareLookAtEntityGoal(this, MobEntity.class,8));
        this.goalSelector.add(4, new GlareLookAtEntityGoal(this, PlayerEntity.class,8));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.hasShade()) {
            nbt.put("ShadePos", NbtHelper.fromBlockPos(this.shade));
        }
        nbt.putBoolean("IsNapping", this.isNapping);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.shade = null;
        if (nbt.contains("ShadePos")) {
            this.shade = NbtHelper.toBlockPos(nbt.getCompound("ShadePos"));
        }

        super.readCustomDataFromNbt(nbt);
        this.isNapping = nbt.getBoolean("IsNapping");
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public static DefaultAttributeContainer.Builder createGlareAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.45d)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35d)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48d);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public void setTicksNapped(int ticks) {
        this.ticksNapped = ticks;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(ticks);
        buf.writeInt(GlareEntity.this.getId());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) GlareEntity.this.world, GlareEntity.this.getBlockPos())) {
            ServerPlayNetworking.send(player, LosersBracketMod.GLARE_TICKS_NAPPING_PACKET_ID, buf);
        }
    }

    public void setNapping(boolean nap) {
        this.isNapping = nap;
    }

    public int getTicksNapped() {
        return this.ticksNapped;
    }

    @Override
    public boolean isInAir() {
        return true;
    }

    boolean isTooFar(BlockPos pos) {
        return !pos.isWithinDistance(this.getBlockPos(), 32d);
    }

    public boolean hasShade() {
        return GlareEntity.this.shade != null;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    /*
    The following method is used to find areas dark enough for mobs to spawn
    The method iterates through a 32x32x32 area around the Glare, looking for a block that meets the following criteria:
        -Light level less than or equal to 7
        -Is air
        -Has a non-air block below it
        -Is above y=0
    If multiple areas fit the criteria the method returns the closest darkest block
     */
    private BlockPos getNearbyShade() {
        BlockPos blockPos = GlareEntity.this.getBlockPos();
        BlockPos nearShad = null;
        for (int i = 0; i <= 64; i++) {
            for (int j = 0; j <= 64; j++) {
                for (int k = 0; k <= 64; k++) {
                    BlockPos currentBlock = blockPos.add(-32 + i, -32 + j, -32 + k);
                    if(
                            GlareEntity.this.world.getLightLevel(currentBlock) <= 0 &&
                            GlareEntity.this.world.getBlockState(currentBlock).isAir() &&
                            (GlareEntity.this.world.getBlockState(currentBlock.down()).isSolidBlock(GlareEntity.this.world, currentBlock.down())) &&
                            currentBlock.getY() >= 0 &&
                            (nearShad == null || GlareEntity.this.world.getLightLevel(currentBlock) <= GlareEntity.this.world.getLightLevel(nearShad)) &&
                            (nearShad == null || currentBlock.getManhattanDistance(blockPos) < nearShad.getManhattanDistance(blockPos))
                    ) {
                        nearShad = currentBlock;
                    }
                }
            }
        }
        return nearShad;
    }

    //LookAtEntityGoal modified to keep from looking at other entities when napping
    public class GlareLookAtEntityGoal extends LookAtEntityGoal {
        public GlareLookAtEntityGoal(MobEntity mob, Class<? extends LivingEntity> targetType, float range) {
            super(mob, targetType, range);
        }
        @Override
        public boolean canStart() {
            if (GlareEntity.this.isNapping) {
                return false;
            } else {
                return super.canStart();
            }
        }

        @Override
        public boolean shouldContinue() {
            if (GlareEntity.this.isNapping) {
                return false;
            } else {
                return super.shouldContinue();
            }
        }
    }

    //Wander when idle, adapted from Bee code
    private class GlareWanderGoal extends Goal {
        protected final PathAwareEntity mob;

        GlareWanderGoal(PathAwareEntity entity) {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
            this.mob = entity;
        }

        public boolean canStart() {
            return GlareEntity.this.navigation.isIdle() && GlareEntity.this.random.nextInt(15) == 0 && !GlareEntity.this.isNapping;
        }

        public boolean shouldContinue() {
            return GlareEntity.this.navigation.isFollowingPath() && !(GlareEntity.this.random.nextInt(10) == 0) && !GlareEntity.this.isNapping;
        }

        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                GlareEntity.this.navigation.startMovingAlong(GlareEntity.this.navigation.findPathTo(new BlockPos(vec3d), 3), 1.0D);
            }
        }

        public void stop() {
            this.mob.getNavigation().stop();
        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d3;
            vec3d3 = GlareEntity.this.getRotationVec(0.0F);
            Vec3d vec3d4 = AboveGroundTargeting.find(GlareEntity.this, 8, 7, vec3d3.x, vec3d3.z, 1.5707964F, 3, 1);
            return vec3d4 != null ? vec3d4 : NoPenaltySolidTargeting.find(GlareEntity.this, 8, 4, -2, vec3d3.x, vec3d3.z, 1.5707963705062866D);
        }
    }

    //Used to locate areas where mobs can spawn, adapted from Bee code
    class GlareFindShadeGoal extends Goal {
        GlareFindShadeGoal() {
            super();
        }

        @Override
        public boolean canStart() {
            return (GlareEntity.this.shade != null && GlareEntity.this.world.getLightLevel(GlareEntity.this.shade) >= 1) || GlareEntity.this.ticksToFindShade >= 0;
        }

        public boolean shouldContinue() {return false;}

        public void start() {
            GlareEntity.this.shade = GlareEntity.this.getNearbyShade();
        }

        public void stop() {}
    }

    //Nap when in area where mobs spawn
    class GlareNapGoal extends Goal {
        GlareNapGoal() {
            super();
        }

        public boolean canStart() {
            return GlareEntity.this.shade != null && GlareEntity.this.getBlockPos().getManhattanDistance(GlareEntity.this.shade) <= 0.5 && GlareEntity.this.getBlockPos().getY() <= GlareEntity.this.shade.getY();
        }

        public boolean shouldContinue() {
            return GlareEntity.this.world.getLightLevel(GlareEntity.this.getBlockPos()) <= 0 &&
                    GlareEntity.this.getBlockPos().getManhattanDistance(GlareEntity.this.shade) <= 1 &&
                    !GlareEntity.this.world.isAir(GlareEntity.this.getBlockPos().down(1));
        }

        public void start() {
            super.start();
            GlareEntity.this.navigation.stop();
            GlareEntity.this.getNavigation().stop();
            GlareEntity.this.setNapping(true);
        }

        public void stop() {
            GlareEntity.this.setNapping(false);
            GlareEntity.this.setTicksNapped(0);
        }

        public void tick() {
            GlareEntity.this.setTicksNapped(GlareEntity.this.getTicksNapped() + 1);
        }
    }

    //Move to area in which mobs spawn, adapted from Bee code
    class GlareMoveToShadeGoal extends Goal {
        int ticks;

        GlareMoveToShadeGoal() {
            super();
            this.ticks = GlareEntity.this.world.random.nextInt(10);
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        public boolean canStart() {
            return GlareEntity.this.shade != null &&

                    GlareEntity.this.world.getLightLevel(GlareEntity.this.getBlockPos()) >= 1 &&
                    GlareEntity.this.world.getLightLevel(GlareEntity.this.shade) <= 0;
        }

        public boolean shouldContinue() {return this.canStart();}

        public void start() {
            this.ticks = 0;
            GlareEntity.this.ticksToFindShade = 400;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            GlareEntity.this.navigation.stop();
            GlareEntity.this.navigation.resetRangeMultiplier();
        }

        public void tick() {
            if (GlareEntity.this.shade != null) {
                this.ticks++;
                if (this.ticks > 400) {
                    GlareEntity.this.shade = null;
                } else if (!GlareEntity.this.navigation.isFollowingPath()) {
                    if (GlareEntity.this.isTooFar(GlareEntity.this.shade)) {
                        GlareEntity.this.shade = null;
                    } else {
                        GlareEntity.this.navigation.startMovingTo(GlareEntity.this.shade.getX(),
                                GlareEntity.this.shade.getY(),
                                GlareEntity.this.shade.getZ(),
                                0.45d);
                    }
                }
                if (!isNapping) {
                    GlareEntity.this.ticksToFindShade--;
                }
            }
        }
    }
}

