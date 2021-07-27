package com.mrbysco.ghastcow.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class GhastCow extends FlyingMonster implements PowerableMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(GhastCow.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> INVULNERABILITY_TIME = SynchedEntityData.defineId(GhastCow.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(GhastCow.class, EntityDataSerializers.INT);
	private int nextUpdate = 0;
	private int idleUpdate = 0;

	private final ServerBossEvent bossInfo = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setCreateWorldFog(true);
	private static final Predicate<LivingEntity> NOT_UNDEAD = (livingEntity) -> livingEntity.getMobType() != MobType.UNDEAD && !(livingEntity instanceof Cow) && livingEntity.attackable();
	private static final TargetingConditions ENEMY_CONDITION = TargetingConditions.forCombat().range(20.0D).selector(NOT_UNDEAD);

	public GhastCow(EntityType<? extends FlyingMonster> type, Level level) {
		super(type, level);
		this.setHealth(this.getMaxHealth());
		this.getNavigation().setCanFloat(true);
		this.xpReward = 50;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new GhastCow.DoNothingGoal());
		this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 0, false, false, NOT_UNDEAD));
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, (double)0.6F).add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.ARMOR, 4.0D);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ATTACKING, false);
		this.entityData.define(TARGET, 0);
		this.entityData.define(INVULNERABILITY_TIME, 0);
	}

	public boolean isAttacking() {
		return this.entityData.get(ATTACKING);
	}

	public void setAttacking(boolean attacking) {
		this.entityData.set(ATTACKING, attacking);
	}

	public int getInvulTime() {
		return this.entityData.get(INVULNERABILITY_TIME);
	}

	public void setInvulTime(int time) {
		this.entityData.set(INVULNERABILITY_TIME, time);
	}

	public int getTargetID() {
		return this.entityData.get(TARGET);
	}

	public void updateTargetID(int newId) {
		this.entityData.set(TARGET, newId);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setInvulTime(compound.getInt("Invul"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public void aiStep() {
		Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
		if (!this.level.isClientSide && this.getTargetID() > 0) {
			Entity entity = this.level.getEntity(this.getTargetID());
			if (entity != null) {
				double d0 = vec3.y;
				if (this.getY() < entity.getY() || !this.isPowered() && this.getY() < entity.getY() + 5.0D) {
					d0 = Math.max(0.0D, d0);
					d0 = d0 + (0.3D - d0 * (double)0.6F);
				}

				vec3 = new Vec3(vec3.x, d0, vec3.z);
				Vec3 vec31 = new Vec3(entity.getX() - this.getX(), 0.0D, entity.getZ() - this.getZ());
				if (vec31.horizontalDistanceSqr() > 9.0D) {
					Vec3 vec32 = vec31.normalize();
					vec3 = vec3.add(vec32.x * 0.3D - vec3.x * 0.6D, 0.0D, vec32.z * 0.3D - vec3.z * 0.6D);
				}
			}
		}

		this.setDeltaMovement(vec3);
		if (vec3.horizontalDistanceSqr() > 0.05D) {
			this.setYRot((float)Mth.atan2(vec3.z, vec3.x) * (180F / (float)Math.PI) - 90.0F);
		}

		super.aiStep();

		boolean flag = this.isPowered();

		double d8 = this.getX();
		double d10 = this.getY();
		double d2 = this.getZ();
		this.level.addParticle(ParticleTypes.POOF, d8 + this.random.nextGaussian() * (double)0.3F, d10 + this.random.nextGaussian() * (double)0.3F, d2 + this.random.nextGaussian() * (double)0.3F, 0.0D, 0.0D, 0.0D);
		if (flag && this.level.random.nextInt(4) == 0) {
			this.level.addParticle(ParticleTypes.ENTITY_EFFECT, d8 + this.random.nextGaussian() * (double)0.3F, d10 + this.random.nextGaussian() * (double)0.3F, d2 + this.random.nextGaussian() * (double)0.3F, (double)0.7F, (double)0.7F, 0.5D);
		}

		if (this.getInvulTime() > 0) {
			for(int i1 = 0; i1 < 3; ++i1) {
				this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), (double)0.7F, (double)0.7F, (double)0.9F);
			}
		}
	}

	@Override
	protected void customServerAiStep() {
		if (this.getInvulTime() > 0) {
			int j1 = this.getInvulTime() - 1;
			if (j1 <= 0) {
				Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
				this.level.explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, explosion$mode);
				if (!this.isSilent()) {
					this.level.playSound(null, this.blockPosition(), SoundEvents.GHAST_SCREAM, getSoundCategory(), getSoundVolume(), 1.0F);
				}
			}

			this.setInvulTime(j1);
			if (this.tickCount % 10 == 0) {
				this.heal(10.0F);
			}

		} else {
			super.customServerAiStep();

			if (this.tickCount >= nextUpdate) {
				this.nextUpdate = this.tickCount + 10 + this.random.nextInt(10);
				if (this.level.getDifficulty() == Difficulty.NORMAL || this.level.getDifficulty() == Difficulty.HARD) {
					int idleHeadUpdates = idleUpdate;
					idleUpdate = idleUpdate++;
					if (idleHeadUpdates > 15) {
						float f = 10.0F;
						float f1 = 5.0F;
						double d0 = Mth.nextDouble(this.random, this.getX() - f, this.getX() + f);
						double d1 = Mth.nextDouble(this.random, this.getY() - f1, this.getY() + f1);
						double d2 = Mth.nextDouble(this.random, this.getZ() - f, this.getZ() + f);
						this.launchFireballToCoords(d0, d1, d2, true);
						this.idleUpdate = 0;
					}
				}

				int targetID = this.getTargetID();
				if (targetID > 0) {
					Entity entity = this.level.getEntity(targetID);
					if (entity != null && entity.isAlive() && !(this.distanceToSqr(entity) > 900.0D) && this.hasLineOfSight(entity)) {
						if (entity instanceof Player && ((Player)entity).getAbilities().invulnerable) {
							this.updateTargetID(0);
						} else {
							this.launchFireball((LivingEntity)entity);
							this.nextUpdate = this.tickCount + 40 + this.random.nextInt(20);
							this.idleUpdate = 0;
						}
					} else {
						this.updateTargetID(0);
					}
				} else {
					List<LivingEntity> list = this.level.getNearbyEntities(LivingEntity.class, ENEMY_CONDITION, this, this.getBoundingBox().inflate(20.0D, 8.0D, 20.0D));

					for(int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2) {
						LivingEntity livingentity = list.get(this.random.nextInt(list.size()));
						if (livingentity != this && livingentity.isAlive() && this.hasLineOfSight(livingentity)) {
							if (livingentity instanceof Player) {
								if (!((Player)livingentity).getAbilities().invulnerable) {
									this.updateTargetID(livingentity.getId());
								}
							} else {
								this.updateTargetID(livingentity.getId());
							}
							break;
						}

						list.remove(livingentity);
					}
				}
			}

			if (this.getTarget() != null) {
				this.updateTargetID(this.getTarget().getId());
			} else {
				this.updateTargetID(0);
			}

			if (this.tickCount % 60 == 0) {
				this.heal(1.0F);
			}

			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	public SoundSource getSoundCategory() {
		return SoundSource.HOSTILE;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.COW_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.COW_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.COW_DEATH;
	}

	protected float getSoundVolume() {
		return 5.0F;
	}

	protected boolean canRide(Entity entityIn) {
		return false;
	}

	public boolean canChangeDimensions() {
		return false;
	}

	public static boolean canSpawnHere(EntityType<GhastCow> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(20) == 0 && checkMobSpawnRules(entityType, world, reason, pos, random);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		this.launchFireball(target);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (source != DamageSource.DROWN && !(source.getEntity() instanceof GhastCow)) {
			if (this.getInvulTime() > 0 && source != DamageSource.OUT_OF_WORLD) {
				return false;
			} else {
				if (this.isPowered()) {
					Entity entity = source.getDirectEntity();
					if (entity instanceof AbstractArrow) {
						return false;
					}
				}

				Entity entity1 = source.getEntity();
				if (entity1 != null && !(entity1 instanceof Player) && entity1 instanceof LivingEntity && ((LivingEntity)entity1).getMobType() == this.getMobType()) {
					return false;
				} else {
					this.idleUpdate += 3;

					return super.hurt(source, amount);
				}
			}
		} else {
			return false;
		}
	}

	private void launchFireball(LivingEntity target) {
		this.launchFireballToCoords(target.getX(), target.getY() + (double)target.getEyeHeight() * 0.5D, target.getZ(), this.random.nextFloat() < 0.001F);
	}

	private void launchFireballToCoords(double x, double y, double z, boolean invulnerable) {
		this.setAttacking(true);
		if (!this.isSilent()) {
			this.level.levelEvent((Player)null, 1024, this.blockPosition(), 0);
		}

		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		double d3 = x - d0;
		double d4 = y - d1;
		double d5 = z - d2;

		LargeFireball fireballentity = new LargeFireball(level, this, d3, d4, d5, 1);
		fireballentity.setOwner(this);

		fireballentity.setPosRaw(d0, d1, d2);
		this.level.addFreshEntity(fireballentity);
		this.setAttacking(false);
	}

	@Override
	public boolean isPowered() {
		return this.getHealth() <= this.getMaxHealth() / 2.0F;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		this.setInvulTime(220);
		this.setHealth(this.getMaxHealth() / 2.0F);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	class DoNothingGoal extends Goal {
		public DoNothingGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			return GhastCow.this.getInvulTime() > 0;
		}
	}
}
