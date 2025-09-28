package com.mrbysco.ghastcow;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "ghastcow";
	public static final String MOD_NAME = "Ghast Cow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation modLoc(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}