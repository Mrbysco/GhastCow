package com.mrbysco.ghastcow.registry;

import com.mrbysco.ghastcow.entity.GhastCow;
import com.mrbysco.ghastcow.registration.ModEntities;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class ModSetup {

	public static void registerSpawnPlacements(final RegisterSpawnPlacementsEvent event) {
		event.register(ModEntities.GHAST_COW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhastCow::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.OR);
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.GHAST_COW.get(), GhastCow.generateAttributes().build());
	}
}
