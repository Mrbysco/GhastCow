package com.mrbysco.ghastcow.config;

import com.mrbysco.ghastcow.Constants;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class GhowConfig {
	public static class Common {
		public final ModConfigSpec.BooleanValue requireNamed;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("Summon settings")
					.push("summon");

			requireNamed = builder
					.comment("When enabled requires the cow to be renamed to \"ghast\" before getting killed by a ghast")
					.define("requireNamed", false);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Constants.LOGGER.debug("Loaded The Ghast Cow's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Constants.LOGGER.debug("The Ghast Cow's config just got changed on the file system!");
	}
}
