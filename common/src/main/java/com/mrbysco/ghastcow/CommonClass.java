package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.entity.GhastCow;
import com.mrbysco.ghastcow.platform.Services;
import com.mrbysco.ghastcow.registration.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class CommonClass {
	public static void init() {
		ModEntities.loadClass();
	}

	public static void onDeath(LivingEntity livingEntity, DamageSource source) {
		final Level level = livingEntity.level();
		if (!level.isClientSide) {
			Entity directEntity = source.getDirectEntity();
			if (directEntity instanceof LargeFireball fireball) {
				if (fireball.getOwner() instanceof Ghast && livingEntity instanceof Cow cowEntity) {
					if (!Services.PLATFORM.requiresName() ||
							(cowEntity.hasCustomName() && cowEntity.getCustomName() != null && cowEntity.getCustomName().getString().toLowerCase(Locale.ROOT).equals("ghast"))) {
						BlockPos blockpos = cowEntity.blockPosition();
						GhastCow ghastCow = ModEntities.GHAST_COW.get().create(level);
						if (ghastCow != null) {
							ghastCow.moveTo((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 1.55D, (double) blockpos.getZ() + 0.5D, cowEntity.getYRot(), cowEntity.getXRot());
							ghastCow.yBodyRot = cowEntity.yBodyRot;

							level.addFreshEntity(ghastCow);
						}
					}
				}
			}
		}
	}
}