package com.mrbysco.ghastcow.registration;

import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * Example class for item registration
 */
public class ModEntities {
	public static final RegistrationProvider<EntityType<?>> ENTITY_TYPES = RegistrationProvider.get(BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);


	public static final RegistryObject<EntityType<GhastCow>> GHAST_COW = ENTITY_TYPES.register("ghast_cow", () ->
			EntityType.Builder.<GhastCow>of(GhastCow::new, MobCategory.MONSTER)
					.sized(2.5F, 1.5F).clientTrackingRange(10).build("ghast_cow"));


	// Called in the mod initializer / constructor in order to make sure that items are registered
	public static void loadClass() {
	}
}
