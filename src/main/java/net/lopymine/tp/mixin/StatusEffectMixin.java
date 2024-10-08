package net.lopymine.tp.mixin;

import net.minecraft.entity.effect.*;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.*;

import net.lopymine.tp.utils.TPStatusEffect;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements TPStatusEffect {

	@Unique
	private ParticleEffect particleEffect;

	@Override
	public void texturizedParticles$setParticleEffect(ParticleEffect particleEffect) {
		this.particleEffect = particleEffect;
	}

	public ParticleEffect texturizedParticles$getParticleEffect() {
		return this.particleEffect;
	}
}
