package com.mrbysco.ghastcow.handler;

import com.mrbysco.ghastcow.entity.GhastCowEntity;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Locale;

public class SpawnHandler {
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		World level = livingEntity.getCommandSenderWorld();
		if(!level.isClientSide && level != null) {
			DamageSource source = event.getSource();
			Entity sourceEntity = source.getDirectEntity();
			if(sourceEntity instanceof FireballEntity) {
				FireballEntity fireball = (FireballEntity) sourceEntity;
				if(fireball.getOwner() instanceof GhastEntity && livingEntity instanceof CowEntity) {
					CowEntity cowEntity = (CowEntity) livingEntity;
					System.out.println(cowEntity);
					if(cowEntity.hasCustomName() && cowEntity.getCustomName() != null && cowEntity.getCustomName().getString().toLowerCase(Locale.ROOT).equals("ghast")) {
						System.out.println("Found custom name");
						BlockPos blockpos = cowEntity.blockPosition();
						GhastCowEntity ghastCowEntity = ModEntities.GHAST_COW.get().create(level);
						if(ghastCowEntity != null) {
							ghastCowEntity.moveTo((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 1.55D, (double)blockpos.getZ() + 0.5D, cowEntity.yRot, cowEntity.xRot);
							ghastCowEntity.yBodyRot = cowEntity.yBodyRot;

							level.addFreshEntity(ghastCowEntity);
						}
					}
				}
			}
		}
	}
}
