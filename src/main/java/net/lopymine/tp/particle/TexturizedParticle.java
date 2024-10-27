package net.lopymine.tp.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle./*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/;

import net.lopymine.tp.utils.*;

public class TexturizedParticle extends SpellParticle {

	protected TexturizedParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
		super(clientWorld, d, e, f, g, h, i, spriteProvider);
		super.setSprite(spriteProvider);
	}

	@Override
	public void setColor(float red, float green, float blue) {

	}

	@Override
	public void setSpriteForAge(SpriteProvider spriteProvider) {

	}

	public static class BasedFactory implements ParticleFactory</*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/> {

		private final SpriteProvider spriteProvider;

		public BasedFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(/*? >=1.21 {*/SimpleParticleType/*?} else {*//*DefaultParticleType*//*?}*/ effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			TexturizedParticle texturizedParticle = new TexturizedParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
			int color = ((TPType) effect).texturizedParticles$getColor();
			texturizedParticle.setAlpha((float) ArgbUtils.getAlpha(color) / 255F);
			return texturizedParticle;
		}
	}
}
