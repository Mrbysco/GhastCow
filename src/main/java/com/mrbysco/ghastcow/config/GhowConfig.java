package com.mrbysco.ghastcow.config;

import com.mrbysco.ghastcow.GhastCowMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class GhowConfig {
	public static class Common {
		public final BooleanValue requireNamed;
		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Summon settings")
					.push("summon");

			requireNamed = builder
					.comment("When enabled requires the cow to be renamed to \"ghast\" before getting killed by a ghast")
					.define("showPercentage", false);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec serverSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		serverSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		GhastCowMod.LOGGER.debug("Loaded The Ghast Cow's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		GhastCowMod.LOGGER.debug("The Ghast Cow's config just got changed on the file system!");
	}
}
