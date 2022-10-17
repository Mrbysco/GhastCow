package com.mrbysco.ghastcow.config;

import com.mrbysco.ghastcow.Constants;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class GhowConfig {
	public static class Common {
		public final BooleanValue requireNamed;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Summon settings")
					.push("summon");

			requireNamed = builder
					.comment("When enabled requires the cow to be renamed to \"ghast\" before getting killed by a ghast")
					.define("requireNamed", false);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
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
