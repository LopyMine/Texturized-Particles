package net.lopymine.pe.mixin;

import net.minecraft.entity.effect.*;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.*;

import net.lopymine.pe.utils.PEStatusEffect;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements PEStatusEffect {

	@Unique
	private ParticleEffect particleEffect;

	@Override
	public void particleEffects$setParticleEffect(ParticleEffect particleEffect) {
		this.particleEffect = particleEffect;
	}

	public ParticleEffect particleEffects$getParticleEffect() {
		return this.particleEffect;
	}
}
