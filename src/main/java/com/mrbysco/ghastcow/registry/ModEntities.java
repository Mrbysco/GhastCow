package com.mrbysco.ghastcow.registry;

import com.mrbysco.ghastcow.GhastCowMod;
import com.mrbysco.ghastcow.entity.GhastCowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, GhastCowMod.MOD_ID);

	public static final RegistryObject<EntityType<GhastCowEntity>> GHAST_COW = ENTITIES.register("ghast_cow", () ->
			register("fairy", EntityType.Builder.<GhastCowEntity>of(GhastCowEntity::new, EntityClassification.MONSTER)
					.sized(0.5F, 0.5F).clientTrackingRange(10)));

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}

	public static void registerSpawnPlacement() {
		EntitySpawnPlacementRegistry.register(ModEntities.GHAST_COW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhastCowEntity::canSpawnHere);
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.GHAST_COW.get(), GhastCowEntity.generateAttributes().build());
	}
}
