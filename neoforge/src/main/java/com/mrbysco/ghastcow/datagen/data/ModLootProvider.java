package com.mrbysco.ghastcow.datagen.data;

import com.mrbysco.ghastcow.registration.ModEntities;
import com.mrbysco.ghastcow.registration.RegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModLootProvider extends LootTableProvider {
	public ModLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Set.of(), List.of(
						new SubProviderEntry(ChocoEntityLoot::new, LootContextParamSets.ENTITY))
				, lookupProvider);
	}

	private static class ChocoEntityLoot extends EntityLootSubProvider {
		protected ChocoEntityLoot(HolderLookup.Provider provider) {
			super(FeatureFlags.REGISTRY.allFlags(), provider);
		}

		@Override
		public void generate() {
			this.add(ModEntities.GHAST_COW.get(), LootTable.lootTable()
					.withPool(LootPool.lootPool().name("leather").setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(Items.LEATHER)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 12.0F)))
									.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
							)
					)
					.withPool(LootPool.lootPool().name("beef").setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(Items.COOKED_BEEF)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 12.0F)))
									.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
							)
					)
					.withPool(LootPool.lootPool().name("gunpowder").setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(Items.GUNPOWDER)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 10.0F)))
									.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
							)
					)
			);
		}

		@Override
		protected Stream<EntityType<?>> getKnownEntityTypes() {
			return ModEntities.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
		}
	}
}