package net.lopymine.pe.mixin;

import net.minecraft.particle./*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/;
import org.spongepowered.asm.mixin.*;

import net.lopymine.pe.utils.PEType;

@Mixin(/*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/.class)
public class SimpleParticleTypeMixin implements PEType {

	@Unique
	private int color;

	@Override
	public int particleEffects$getColor() {
		return this.color;
	}

	@Override
	public void particleEffects$setColor(int color) {
		this.color = color;
	}
}
