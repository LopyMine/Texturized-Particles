package net.lopymine.tp.mixin;

import net.minecraft.particle./*? =1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/;
import org.spongepowered.asm.mixin.*;

import net.lopymine.tp.utils.TPType;

@Mixin(/*? =1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/.class)
public class SimpleParticleTypeMixin implements TPType {

	@Unique
	private int color;

	@Override
	public int texturizedParticles$getColor() {
		return this.color;
	}

	@Override
	public void texturizedParticles$setColor(int color) {
		this.color = color;
	}
}
