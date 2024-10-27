package net.lopymine.tp.manager;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.effect.*;
import net.minecraft.particle.*;
import net.minecraft.potion.Potion;
import net.minecraft.registry.*;

import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.lopymine.tp.TexturizedParticles;
import net.lopymine.tp.client.TexturizedParticlesClient;
import net.lopymine.tp.particle.TexturizedParticle;
import net.lopymine.tp.utils.*;
import java.util.*;
import java.util.stream.*;
import net.minecraft.world.World;

//? >=1.21
import net.minecraft.registry.entry.RegistryEntry;

import org.jetbrains.annotations.Nullable;

public class TexturizedParticleManager {

	private static final List<ParticleEffect> REGISTERED_PARTICLE_TYPES = new ArrayList<>();
	private static final Map<Integer, List<ParticleEffect>> COLOR_TO_PARTICLES_MAP = new HashMap<>();
	private static final HashMap<ParticleEffect, StatusEffect> MINECRAFT_EFFECTS_WITH_TEXTURIZED_PARTICLE = getMinecraftEffectWidthTexturizedParticles();

	@Nullable
	public static List<ParticleEffect> getParticleEffects(Integer i) {
		return COLOR_TO_PARTICLES_MAP.get(i);
	}

	private static ParticleEffect registerParticleTypeForEffect(StatusEffect statusEffect, Identifier effectId) {
		// CREATE PARTICLE TYPE
		ParticleEffect type = Registry.register(
				Registries.PARTICLE_TYPE,
				getModEffectId(statusEffect, effectId), // WE NEED IT TO AVOID ISSUE WITH VANILLA TEXTURED PARTICLES
				FabricParticleTypes.simple()
		);

		// ADD TO REGISTERED PARTICLES TO REGISTER THEIR FACTORY AT CLIENT LAYER
		REGISTERED_PARTICLE_TYPES.add(type);
		return type;
	}

	private static Identifier getModEffectId(StatusEffect statusEffect, Identifier effectId) {
		boolean bl = MINECRAFT_EFFECTS_WITH_TEXTURIZED_PARTICLE.containsValue(statusEffect);
		return TexturizedParticles.id(effectId.getPath() + (bl ? "_new" : ""));
	}

	public static void onInitialize() {
		//-----------------------------------------------------//
		// SWAP OLD PARTICLE TYPE OF STATUS EFFECTS TO NEW ONE //
		//-----------------------------------------------------//
		for (Reference<StatusEffect> reference : Registries.STATUS_EFFECT.streamEntries().toList()) {
			StatusEffect statusEffect = reference.value();
			Identifier id = reference.registryKey().getValue();
			if (!id.getNamespace().equals("minecraft")) {
				continue;
			}

			// REGISTER NEW PARTICLE TYPE (AND EFFECT)
			ParticleEffect type = TexturizedParticleManager.registerParticleTypeForEffect(statusEffect, id);

			// SWAP PARTICLE TYPES
			StatusEffectUtils.swapParticle(statusEffect, type);
		}

		//---------------------------------------------------//
		// REGISTER EACH POTION COLOR TO LIST POTION EFFECTS //
		//        POTION COLOR = MIXED COLORS OF EFFECTS     //
		//---------------------------------------------------//
		for (Reference<Potion> reference : Registries.POTION.streamEntries().toList()) {
			Potion potion = reference.value();
			Identifier id = reference.registryKey().getValue();
			if (!id.getNamespace().equals("minecraft")) {
				continue;
			}

			List<StatusEffectInstance> effects = potion.getEffects();

			//? =1.20.1 {
			/*int color = ArgbUtils.getColorWithoutAlpha(net.minecraft.potion.PotionUtil.getColor(effects));

			List<ParticleEffect> particleEffects = effects.stream()
					.map(StatusEffectInstance::getEffectType)
					.flatMap((effect) -> {
						ParticleEffect particleEffect = ((TPStatusEffect) effect).texturizedParticles$getParticleEffect();
						if (particleEffect == null) {
							TexturizedParticlesClient.LOGGER.error("[DEV/Potion Registration] Looks like {} effect (from potion with color {}) doesn't have textured particle, this shouldn't happen! Skipping it registration.", color, effect.getName().getString());
							return Stream.empty();
						}
						return Stream.of(particleEffect);
					})
					.toList();

			*///?} else {
			OptionalInt optional = net.minecraft.component.type.PotionContentsComponent.mixColors(effects);
			if (optional.isEmpty()) {
				continue;
			}

			int color = ArgbUtils.getColorWithoutAlpha(optional.getAsInt());

			List<ParticleEffect> particleEffects = effects.stream()
					.map(StatusEffectInstance::getEffectType)
					.map(RegistryEntry::value)
					.flatMap((effect) -> {
						ParticleEffect particleEffect = ((TPStatusEffect) effect).texturizedParticles$getParticleEffect();
						if (particleEffect == null) {
							TexturizedParticlesClient.LOGGER.error("[DEV/Potion Registration] Looks like {} effect with color {} doesn't have textured particle, this shouldn't happen! Skipping it registration.", color, effect.getName().getString());
							return Stream.empty();
						}
						return Stream.of(particleEffect);
					})
					.toList();
			//?}

			COLOR_TO_PARTICLES_MAP.put(color, particleEffects);
		}

		//------------------------------------------------------//
		// NOT ALL EFFECTS CAN BE FOUND IN POTIONS, SO, WE ALSO //
		//  NEED TO REGISTER EACH EFFECT COLOR TO THEIR EFFECT  //
		//------------------------------------------------------//
		for (Reference<StatusEffect> reference : Registries.STATUS_EFFECT.streamEntries().toList()) {
			StatusEffect statusEffect = reference.value();
			Identifier id = reference.registryKey().getValue();
			if (!id.getNamespace().equals("minecraft")) {
				continue;
			}

			int color = ArgbUtils.getColorWithoutAlpha(statusEffect.getColor());

			ParticleEffect particleEffect = ((TPStatusEffect) statusEffect).texturizedParticles$getParticleEffect();
			// WE SET PARTICLE EFFECT(AND TYPE) AT FIRST PHASE
			// WHEN WE REGISTERED NEW PARTICLE TYPE

			if (particleEffect == null) {
				TexturizedParticlesClient.LOGGER.error("[DEV/Effect Registration] Looks like {} effect with color {} doesn't have textured particle, this shouldn't happen! Skipping it registration.", color, statusEffect.getName().getString());
				continue;
			}

			List<ParticleEffect> effects = COLOR_TO_PARTICLES_MAP.get(color);
			if (effects != null) {
				TexturizedParticlesClient.LOGGER.warn("[DEV/Effect Registration] Found registered effects for color {} from {} effect, skipping it registration. If you just mod user, ignore it.", color, statusEffect.getName().getString());
			} else {
				COLOR_TO_PARTICLES_MAP.put(color, List.of(particleEffect));
			}
		}
	}

	public static void onInitializeClient() {
		for (ParticleEffect type : REGISTERED_PARTICLE_TYPES) {
			ParticleFactoryRegistry.getInstance().register((/*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/) type, TexturizedParticle.BasedFactory::new);
		}
	}

	private static HashMap<ParticleEffect, StatusEffect> getMinecraftEffectWidthTexturizedParticles() {
		//? =1.20.1 {
		/*return new HashMap<>();
		 *///?} else {
		HashMap<ParticleEffect, StatusEffect> map = new HashMap<>();

		map.put(ParticleTypes.ITEM_SLIME, StatusEffects.OOZING.value());
		map.put(ParticleTypes.ITEM_COBWEB, StatusEffects.WEAVING.value());
		map.put(ParticleTypes.INFESTED, StatusEffects.INFESTED.value());
		map.put(ParticleTypes.TRIAL_OMEN, StatusEffects.TRIAL_OMEN.value());
		map.put(ParticleTypes.RAID_OMEN, StatusEffects.RAID_OMEN.value());
		map.put(ParticleTypes.SMALL_GUST, StatusEffects.WIND_CHARGED.value());

		return map;
		//?}
	}

	public static StatusEffect getVanillaStatusEffectByStatusEffect(ParticleEffect parameters) {
		return MINECRAFT_EFFECTS_WITH_TEXTURIZED_PARTICLE.get(parameters);
	}

	public static void processSplashPotionStageOne(LocalRef<List<ParticleEffect>> localParticleEffects, int color) {
		localParticleEffects.set(null);

		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
			return;
		}

		List<ParticleEffect> list = TexturizedParticleManager.getParticleEffects(ArgbUtils.getColorWithoutAlpha(color));
		if (list == null) {
			return;
		}

		localParticleEffects.set(list);
	}

	public static Particle processSplashPotionStageTwo(@Nullable World world, WorldRenderer instance, ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Particle> original, LocalRef<List<ParticleEffect>> localParticleEffects, int color) {
		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
			return original.call(instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
		}

		List<ParticleEffect> list = localParticleEffects.get();
		if (list == null || world == null) {
			return original.call(instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
		}
		ParticleEffect particleEffect = ListUtils.getRandomElement(list, world.getRandom());
		if (particleEffect == null) {
			return original.call(instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
		}
		//? =1.20.1 {
		/*((TPType) particleEffect).texturizedParticles$setColor(-1);
		 *///?} else {
		((TPType) particleEffect).texturizedParticles$setColor(color);
		//?}
		return original.call(instance, particleEffect, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
	}
}
