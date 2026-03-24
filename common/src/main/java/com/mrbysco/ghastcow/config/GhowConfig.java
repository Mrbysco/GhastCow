package com.mrbysco.ghastcow.config;

import net.neoforged.neoforge.common.ModConfigSpec;
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
}
