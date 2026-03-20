package com.mrbysco.ghastcow;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "ghastcow";
	public static final String MOD_NAME = "Ghast Cow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier modLoc(String name) {
		return Identifier.fromNamespaceAndPath(MOD_ID, name);
	}
}