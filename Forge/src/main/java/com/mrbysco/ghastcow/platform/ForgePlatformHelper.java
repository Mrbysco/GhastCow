package com.mrbysco.ghastcow.platform;

import com.mrbysco.ghastcow.config.GhowConfig;
import com.mrbysco.ghastcow.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ForgePlatformHelper implements IPlatformHelper {


	@Override
	public boolean cancelMobGriefing(Level level, Entity entity) {
		return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(level, entity);
	}

	@Override
	public float getBlockFriction(Level level, BlockPos ground, Entity entity) {
		return level.getBlockState(ground).getFriction(level, ground, entity);
	}

	@Override
	public boolean requiresName() {
		return GhowConfig.COMMON.requireNamed.get();
	}
}
