package net.lopymine.tp.manager;

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

//? =1.21
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
					.map((effect) -> ((TPStatusEffect) effect).texturizedParticles$getParticleEffect())
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
					.map((effect) -> ((TPStatusEffect) effect).texturizedParticles$getParticleEffect())
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

			List<ParticleEffect> effects = COLOR_TO_PARTICLES_MAP.get(color);
			if (effects != null) {
				TexturizedParticlesClient.LOGGER.warn("[DEV] Found registered effects for color {} from {} effect, skipping it registration.", color, statusEffect.getName().getString());
			} else {
				COLOR_TO_PARTICLES_MAP.put(color, List.of(particleEffect));
			}
		}
	}

	public static void onInitializeClient() {
		for (ParticleEffect type : REGISTERED_PARTICLE_TYPES) {
			ParticleFactoryRegistry.getInstance().register((/*? =1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/) type, TexturizedParticle.BasedFactory::new);
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
}
