package com.mrbysco.ghastcow.datagen;

import com.mrbysco.ghastcow.datagen.client.ModLanguageProvider;
import com.mrbysco.ghastcow.datagen.data.ModLootProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDatagenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();

		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModLootProvider::new);
	}
}