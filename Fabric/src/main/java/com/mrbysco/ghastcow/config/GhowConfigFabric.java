package com.mrbysco.ghastcow.config;

import com.mrbysco.ghastcow.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Constants.MOD_ID)
public class GhowConfigFabric implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject
	public General general = new General();

	public static class General {

		@Comment("When enabled requires the cow to be renamed to \"ghast\" before getting killed by a ghast")
		public boolean requireNamed = false;
	}
}