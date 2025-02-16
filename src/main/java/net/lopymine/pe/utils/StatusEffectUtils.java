package net.lopymine.pe.utils;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleEffect;

public class StatusEffectUtils {

	public static void swapParticle(StatusEffect statusEffect, ParticleEffect particleEffect) {
		((PEStatusEffect) statusEffect).particleEffects$setParticleEffect(particleEffect);
	}

}
