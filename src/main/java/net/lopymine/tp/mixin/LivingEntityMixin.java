package net.lopymine.tp.mixin;
//? =1.20.1 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.tp.client.TexturizedParticlesClient;
import net.lopymine.tp.utils.ListUtils;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

@Shadow public abstract Map<StatusEffect, StatusEffectInstance> getActiveStatusEffects();

	@Shadow public abstract Random getRandom();

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), method = "tickStatusEffects")
	private void swapParticle(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> original) {
		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
			original.call(instance, parameters, x, y, z, velocityX, velocityY, velocityZ);
			return;
		}

		Set<StatusEffect> effects = this.getActiveStatusEffects().keySet();
		if (effects.isEmpty()) {
			original.call(instance, parameters, x, y, z, velocityX, velocityY, velocityZ);
			return;
		}

		StatusEffect statusEffect = ListUtils.getRandomElement(effects.stream().toList(), this.getRandom());
		int color = statusEffect.getColor();

		double red = Argb.getRed(color) / 255.0;
		double green = Argb.getGreen(color) / 255.0;
		double blue = Argb.getBlue(color) / 255.0;

		original.call(instance, parameters, x, y, z, red, green, blue);
	}


}
*///?}
