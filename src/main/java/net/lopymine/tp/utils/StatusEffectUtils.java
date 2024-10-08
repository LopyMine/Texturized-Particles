package net.lopymine.tp.utils;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleEffect;

public class StatusEffectUtils {

	public static void swapParticle(StatusEffect statusEffect, ParticleEffect particleEffect) {
		((TPStatusEffect) statusEffect).texturizedParticles$setParticleEffect(particleEffect);
	}

}
