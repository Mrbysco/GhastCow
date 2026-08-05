package com.mrbysco.ghastcow.datagen.client;

import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.registration.ModEntities;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(PackOutput packOutput) {
		super(packOutput, Constants.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addEntityType(ModEntities.GHAST_COW, "Ghast Cow");

		//Config
		addConfig("summon", "Summon", "Summon Settings");
		addConfig("requireNamed", "Require Named", "When enabled requires the cow to be renamed to \"ghast\" before getting killed by a ghast");

	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add(Constants.MOD_ID + ".configuration." + path, name);
		if (description != null && !description.isEmpty()) {
			this.add(Constants.MOD_ID + ".configuration." + path + ".tooltip", description);
		}
	}
}
