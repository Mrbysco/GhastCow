package com.mrbysco.ghastcow.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingDeathCallback {
	Event<LivingDeathCallback> EVENT = EventFactory.createArrayBacked(LivingDeathCallback.class,
			(listeners) -> (living, source) -> {
				for (LivingDeathCallback event : listeners) {
					InteractionResult result = event.die(living, source);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);

	InteractionResult die(LivingEntity livingEntity, DamageSource source);
}
