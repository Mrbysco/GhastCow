package com.mrbysco.ghastcow.platform.services;

import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public interface IPlatformHelper {

	/**
	 * If mob griefing should be canceled
	 */
	boolean cancelMobGriefing(Level level, Entity entity);

	/**
	 * Get the friction of the block
	 */
	float getBlockFriction(Level level, BlockPos ground, Entity entity);

	/**
	 * If the cow needs to be called 'ghast'
	 *
	 * @return the configured option for `requireNamed`
	 */
	boolean requiresName();

	/**
	 * @return the Ghast Cow EntityType
	 */
	EntityType<GhastCow> getEntityType();
}
