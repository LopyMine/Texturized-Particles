package net.lopymine.tp.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.tp.client.TexturizedParticlesClient;
import net.lopymine.tp.manager.TexturizedParticleManager;
import net.lopymine.tp.utils.*;

import java.util.List;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin extends Entity {

	//? =1.20.1
	/*@Shadow public abstract int getColor();*/

	public AreaEffectCloudEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	// LINGERING POTION
	@ModifyReturnValue(at = @At(value = "RETURN"), method = "getParticleType")
	private ParticleEffect swapParticleType(ParticleEffect original) {
		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
			return original;
		}

		//? =1.20.1 {
		/*int color = this.getColor();
		*///?} else {
		if (!(original instanceof EntityEffectParticleEffect effect)) {
			return original;
		}
		int color = effect.color;
		//?}

		List<ParticleEffect> list = TexturizedParticleManager.getParticleEffects(ArgbUtils.getColorWithoutAlpha(color));
		if (list == null || list.isEmpty()) {
			return original;
		}

		ParticleEffect particleEffect = ListUtils.getRandomElement(list, this.getWorld().getRandom());
		if (particleEffect == null) {
			return original;
		}

		//? =1.20.1 {
		/*((TPType) particleEffect).texturizedParticles$setColor(-1);
		// The color doesn't support alpha at 1.20.1, so we set it to -1 (aka 255)
		*///?} else {
		((TPType) particleEffect).texturizedParticles$setColor(color);
		//?}

		return particleEffect;
	}
}
