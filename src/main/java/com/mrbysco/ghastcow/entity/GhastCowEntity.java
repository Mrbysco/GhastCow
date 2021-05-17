package com.mrbysco.ghastcow.entity;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class GhastCowEntity extends FlyingMonster implements IChargeableMob, IRangedAttackMob {
	private static final DataParameter<Boolean> ATTACKING = EntityDataManager.defineId(GhastCowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.defineId(GhastCowEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> TARGET = EntityDataManager.defineId(GhastCowEntity.class, DataSerializers.INT);
	private int nextUpdate = 0;
	private int idleUpdate = 0;

	private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS)).setCreateWorldFog(true);
	private static final Predicate<LivingEntity> NOT_UNDEAD = (creatureAttribute) -> creatureAttribute.getMobType() != CreatureAttribute.UNDEAD && !(creatureAttribute.getEntity() instanceof CowEntity) && creatureAttribute.attackable();
	private static final EntityPredicate ENEMY_CONDITION = (new EntityPredicate()).range(20.0D).selector(NOT_UNDEAD);

	public GhastCowEntity(EntityType<? extends FlyingMonster> type, World level) {
		super(type, level);
		this.setHealth(this.getMaxHealth());
		this.getNavigation().setCanFloat(true);
		this.xpReward = 50;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new GhastCowEntity.DoNothingGoal());
		this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, MobEntity.class, 0, false, false, NOT_UNDEAD));
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, (double)0.6F).add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.ARMOR, 4.0D);
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

	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setInvulTime(compound.getInt("Invul"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public void aiStep() {
		Vector3d vector3d = this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
		if (!this.level.isClientSide && this.getTargetID() > 0) {
			Entity entity = this.level.getEntity(this.getTargetID());
			if (entity != null) {
				double d0 = vector3d.y;
				if (this.getY() < entity.getY() || !this.isPowered() && this.getY() < entity.getY() + 5.0D) {
					d0 = Math.max(0.0D, d0);
					d0 = d0 + (0.3D - d0 * (double)0.6F);
				}

				vector3d = new Vector3d(vector3d.x, d0, vector3d.z);
				Vector3d vector3d1 = new Vector3d(entity.getX() - this.getX(), 0.0D, entity.getZ() - this.getZ());
				if (getHorizontalDistanceSqr(vector3d1) > 9.0D) {
					Vector3d vector3d2 = vector3d1.normalize();
					vector3d = vector3d.add(vector3d2.x * 0.3D - vector3d.x * 0.6D, 0.0D, vector3d2.z * 0.3D - vector3d.z * 0.6D);
				}
			}
		}

		this.setDeltaMovement(vector3d);
		if (getHorizontalDistanceSqr(vector3d) > 0.05D) {
			this.yRot = (float) MathHelper.atan2(vector3d.z, vector3d.x) * (180F / (float)Math.PI) - 90.0F;
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
				Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
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
						double d0 = MathHelper.nextDouble(this.random, this.getX() - f, this.getX() + f);
						double d1 = MathHelper.nextDouble(this.random, this.getY() - f1, this.getY() + f1);
						double d2 = MathHelper.nextDouble(this.random, this.getZ() - f, this.getZ() + f);
						this.launchFireballToCoords(d0, d1, d2, true);
						this.idleUpdate = 0;
					}
				}

				int targetID = this.getTargetID();
				if (targetID > 0) {
					Entity entity = this.level.getEntity(targetID);
					if (entity != null && entity.isAlive() && !(this.distanceToSqr(entity) > 900.0D) && this.canSee(entity)) {
						if (entity instanceof PlayerEntity && ((PlayerEntity)entity).abilities.invulnerable) {
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
						if (livingentity != this && livingentity.isAlive() && this.canSee(livingentity)) {
							if (livingentity instanceof PlayerEntity) {
								if (!((PlayerEntity)livingentity).abilities.invulnerable) {
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

			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}

	public void startSeenByPlayer(ServerPlayerEntity player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	public void stopSeenByPlayer(ServerPlayerEntity player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
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

	public static boolean canSpawnHere(EntityType<GhastCowEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
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
		} else if (source != DamageSource.DROWN && !(source.getEntity() instanceof GhastCowEntity)) {
			if (this.getInvulTime() > 0 && source != DamageSource.OUT_OF_WORLD) {
				return false;
			} else {
				if (this.isPowered()) {
					Entity entity = source.getDirectEntity();
					if (entity instanceof AbstractArrowEntity) {
						return false;
					}
				}

				Entity entity1 = source.getEntity();
				if (entity1 != null && !(entity1 instanceof PlayerEntity) && entity1 instanceof LivingEntity && ((LivingEntity)entity1).getMobType() == this.getMobType()) {
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
		setAttacking(true);
		if (!this.isSilent()) {
			this.level.levelEvent((PlayerEntity)null, 1024, this.blockPosition(), 0);
		}

		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		double d3 = x - d0;
		double d4 = y - d1;
		double d5 = z - d2;

		FireballEntity fireballentity = new FireballEntity(level, this, d3, d4, d5);
		fireballentity.setOwner(this);
		fireballentity.explosionPower = 1;

		fireballentity.setPosRaw(d0, d1, d2);
		this.level.addFreshEntity(fireballentity);
		setAttacking(false);
	}

	@Override
	public boolean isPowered() {
		return this.getHealth() <= this.getMaxHealth() / 2.0F;
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEAD;
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
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
			return GhastCowEntity.this.getInvulTime() > 0;
		}
	}
}
