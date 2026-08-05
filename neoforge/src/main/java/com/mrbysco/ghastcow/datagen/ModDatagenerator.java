package com.mrbysco.ghastcow.datagen;

import com.mrbysco.ghastcow.datagen.client.ModLanguageProvider;
import com.mrbysco.ghastcow.datagen.data.ModLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class ModDatagenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new ModLanguageProvider(packOutput));
		generator.addProvider(true, new ModLootProvider(packOutput, lookupProvider));
	}
}