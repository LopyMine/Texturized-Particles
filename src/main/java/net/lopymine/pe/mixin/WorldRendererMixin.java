package net.lopymine.pe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;

import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.*;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.pe.ParticleEffects;
import net.lopymine.pe.manager.ParticleEffectsManager;
import net.lopymine.pe.utils.*;
import java.util.List;
import org.jetbrains.annotations.Nullable;

//? >=1.21
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? =1.20.1
/*import net.minecraft.util.math.ColorHelper.Argb;*/

@Debug(export = true)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	@Nullable
	private ClientWorld world;

	//? <=1.21.1 {
	/*// SPLASH POTION
	@Inject(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;ofBottomCenter(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/math/Vec3d;"))
	private void modifyParticleEffect(int eventId, BlockPos pos, int data, CallbackInfo ci, @Share("tp_effects") LocalRef<List<ParticleEffect>> localParticleEffects) {
		ParticleEffectsManager.processSplashPotionStageOne(localParticleEffects, data);
	}

	// SPLASH POTION
	@WrapOperation(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)Lnet/minecraft/client/particle/Particle;", ordinal = 0))
	private Particle swapParticles(WorldRenderer instance, ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Particle> original, @Share("tp_effects") LocalRef<List<ParticleEffect>> localParticleEffects, @Local(argsOnly = true, ordinal = 1) int color) {
		return ParticleEffectsManager.processSplashPotionStageTwo(this.world, instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ, original, localParticleEffects, color);
	}
	*///?}

	// ENTITY PARTICLES
	@WrapOperation(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;"))
	private Particle swapParticle(WorldRenderer instance, ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Particle> original) {
		if (!ParticleEffects.getConfig().isModEnabled()) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}
		//? =1.20.1 {
		/*boolean bl = parameters.equals(ParticleTypes.ENTITY_EFFECT);
		boolean bl2 = parameters.equals(ParticleTypes.AMBIENT_ENTITY_EFFECT);
		if (!bl && !bl2) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}

		int color = Argb.getArgb(bl2 ? 38 : 255, (int) (velocityX * 255), (int) (velocityY * 255), (int) (velocityZ * 255));
		*///?} else {
		int color;

		if (parameters instanceof EntityEffectParticleEffect effect) { // RECEIVES IN SINGLEPLAYER AND IN MULTIPLAYER
			color = effect.color;
		} else {
			StatusEffect statusEffect = ParticleEffectsManager.getVanillaStatusEffectByStatusEffect(parameters);
			color = statusEffect == null ? 0 : ArgbUtils.getColorWithoutAlpha(statusEffect.getColor());
		}

		if (color == 0) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}
		//?}

		List<ParticleEffect> list = ParticleEffectsManager.getParticleEffects(ArgbUtils.getColorWithoutAlpha(color));
		if (list == null || this.world == null) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}

		ParticleEffect particleEffect = ListUtils.getRandomElement(list, this.world.getRandom());
		if (particleEffect == null) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}

		((PEType) particleEffect).particleEffects$setColor(color);
		return original.call(instance, particleEffect, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
	}
}
