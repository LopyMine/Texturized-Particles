package net.lopymine.pe.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.pe.ParticleEffects;
import net.lopymine.pe.client.ParticleEffectsClient;
import net.lopymine.pe.manager.ParticleEffectsManager;
import net.lopymine.pe.utils.*;

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
		if (!ParticleEffects.getConfig().isModEnabled()) {
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

		List<ParticleEffect> list = ParticleEffectsManager.getParticleEffects(ArgbUtils.getColorWithoutAlpha(color));
		if (list == null || list.isEmpty()) {
			return original;
		}

		ParticleEffect particleEffect = ListUtils.getRandomElement(list, this.getWorld().getRandom());
		if (particleEffect == null) {
			return original;
		}

		//? =1.20.1 {
		/*((PEType) particleEffect).particleEffects$setColor(-1);
		// The color doesn't support alpha at 1.20.1, so we set it to -1 (aka 255)
		*///?} else {
		((PEType) particleEffect).particleEffects$setColor(color);
		//?}

		return particleEffect;
	}
}
