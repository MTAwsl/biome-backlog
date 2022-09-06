package com.minecraftabnormals.biome_vote_losers.register;

import com.minecraftabnormals.biome_vote_losers.BiomeVoteLosers;
import com.minecraftabnormals.biome_vote_losers.world.level.entity.Meerkat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BiomeVoteLosers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BiomeVoteLosers.MODID);

	public static final RegistryObject<EntityType<Meerkat>> MEERKAT = ENTITIES.register("meerkat", () -> EntityType.Builder.of(Meerkat::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build(prefix("meerkat")));

	private static String prefix(String path) {
		return BiomeVoteLosers.MODID + "." + path;
	}

	public static void spawnPlacementSetup() {
		SpawnPlacements.register(MEERKAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
	}

	@SubscribeEvent
	public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
		event.put(MEERKAT.get(), Meerkat.createAttributes().build());
	}
}
