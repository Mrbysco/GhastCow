package com.mrbysco.ghastcow.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface IPlatformHelper {

	/**
	 * If mob griefing should be canceled
	 */
	boolean cancelMobGriefing(ServerLevel serverLevel, Entity entity);

	/**
	 * Get the friction of the block
	 */
	float getBlockFriction(Level level, BlockPos ground, Entity entity);
}
