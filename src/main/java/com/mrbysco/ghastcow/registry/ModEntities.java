package com.mrbysco.ghastcow.registry;

import com.mrbysco.ghastcow.GhastCowMod;
import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, GhastCowMod.MOD_ID);

	public static final RegistryObject<EntityType<GhastCow>> GHAST_COW = ENTITIES.register("ghast_cow", () ->
			register("ghast_cow", EntityType.Builder.<GhastCow>of(GhastCow::new, MobCategory.MONSTER)
					.sized(2.5F, 1.5F).clientTrackingRange(10)));

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}

	public static void registerSpawnPlacement() {
		SpawnPlacements.register(ModEntities.GHAST_COW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhastCow::canSpawnHere);
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.GHAST_COW.get(), GhastCow.generateAttributes().build());
	}
}
