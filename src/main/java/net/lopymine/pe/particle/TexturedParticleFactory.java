package net.lopymine.pe.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle./*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/;

import net.lopymine.pe.utils.*;

public class TexturedParticleFactory implements ParticleFactory</*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/> {

	private final SpriteProvider spriteProvider;

	public TexturedParticleFactory(SpriteProvider spriteProvider) {
		this.spriteProvider = spriteProvider;
	}

	@Override
	public Particle createParticle(/*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/ effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		TexturedParticle texturedParticle = new TexturedParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		int color = ((PEType) effect).particleEffects$getColor();
		texturedParticle.setAlpha((float) ArgbUtils.getAlpha(color) / 255F);
		return texturedParticle;
	}
}
