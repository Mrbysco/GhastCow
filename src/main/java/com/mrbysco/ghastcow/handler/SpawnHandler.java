package com.mrbysco.ghastcow.handler;

import com.mrbysco.ghastcow.config.GhowConfig;
import com.mrbysco.ghastcow.entity.GhastCow;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Locale;

public class SpawnHandler {
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		Level level = livingEntity.getCommandSenderWorld();
		if (level != null && !level.isClientSide) {
			DamageSource source = event.getSource();
			Entity directEntity = source.getDirectEntity();
			if (directEntity instanceof LargeFireball fireball) {
				if (fireball.getOwner() instanceof Ghast && livingEntity instanceof Cow cowEntity) {
					if (!GhowConfig.COMMON.requireNamed.get() ||
							(cowEntity.hasCustomName() && cowEntity.getCustomName() != null && cowEntity.getCustomName().getString().toLowerCase(Locale.ROOT).equals("ghast"))) {
						BlockPos blockpos = cowEntity.blockPosition();
						GhastCow ghastCowEntity = ModEntities.GHAST_COW.get().create(level);
						if (ghastCowEntity != null) {
							ghastCowEntity.moveTo((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 1.55D, (double) blockpos.getZ() + 0.5D, cowEntity.getYRot(), cowEntity.getXRot());
							ghastCowEntity.yBodyRot = cowEntity.yBodyRot;

							level.addFreshEntity(ghastCowEntity);
						}
					}
				}
			}
		}
	}
}
