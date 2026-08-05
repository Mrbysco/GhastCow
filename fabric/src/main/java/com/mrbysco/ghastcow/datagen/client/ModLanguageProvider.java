package com.mrbysco.ghastcow.datagen.client;

import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.registration.ModEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
	public ModLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(dataOutput, completableFuture);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
		builder.add(ModEntities.GHAST_COW.get(), "Ghast Cow");

		//Config
		addConfig(builder, "summon", "Summon", "Summon Settings");
		addConfig(builder, "requireNamed", "Require Named", "When enabled requires the cow to be renamed to \"ghast\" before getting killed by a ghast");

	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param builder     The translation builder
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(TranslationBuilder builder, String path, String name, @Nullable String description) {
		builder.add(Constants.MOD_ID + ".configuration." + path, name);
		if (description != null && !description.isEmpty()) {
			builder.add(Constants.MOD_ID + ".configuration." + path + ".tooltip", description);
		}
	}
}
