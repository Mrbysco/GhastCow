package com.mrbysco.ghastcow.registry;

import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);

	public static final RegistryObject<EntityType<GhastCow>> GHAST_COW = ENTITY_TYPES.register("ghast_cow", () ->
			EntityType.Builder.<GhastCow>of(GhastCow::new, MobCategory.MONSTER)
					.sized(2.5F, 1.5F).clientTrackingRange(10).build("ghast_cow"));

	public static void registerSpawnPlacements(final SpawnPlacementRegisterEvent event) {
		event.register(ModEntities.GHAST_COW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhastCow::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.GHAST_COW.get(), GhastCow.generateAttributes().build());
	}
}
