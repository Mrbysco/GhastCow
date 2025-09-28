package com.mrbysco.ghastcow.platform;

import com.mrbysco.ghastcow.GhastCowFabric;
import com.mrbysco.ghastcow.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public boolean cancelMobGriefing(ServerLevel serverLevel, Entity entity) {
		return serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
	}

	@Override
	public float getBlockFriction(Level level, BlockPos ground, Entity entity) {
		return level.getBlockState(ground).getBlock().getFriction();
	}

	@Override
	public boolean requiresName() {
		return GhastCowFabric.config.get().general.requireNamed;
	}
}
