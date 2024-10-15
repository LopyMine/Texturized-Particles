package net.lopymine.tp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;


import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper.Argb;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.tp.client.TexturizedParticlesClient;
import net.lopymine.tp.manager.TexturizedParticleManager;
import net.lopymine.tp.utils.*;
import java.util.List;
import org.jetbrains.annotations.Nullable;

@Debug(export = true)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	@Nullable
	private ClientWorld world;

	// SPLASH POTION
	@Inject(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;ofBottomCenter(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/math/Vec3d;"))
	private void modifyParticleEffect(int eventId, BlockPos pos, int data, CallbackInfo ci, @Share("tp_effects") LocalRef<List<ParticleEffect>> localParticleEffects) {
		localParticleEffects.set(null);

		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
			return;
		}

		List<ParticleEffect> list = TexturizedParticleManager.getParticleEffects(ArgbUtils.getColorWithoutAlpha(data));
		if (list == null) {
			return;
		}

		localParticleEffects.set(list);
	}

	// SPLASH POTION
	@WrapOperation(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)Lnet/minecraft/client/particle/Particle;", ordinal = 0))
	private Particle swapParticles(WorldRenderer instance, ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Particle> original, @Share("tp_effects") LocalRef<List<ParticleEffect>> localParticleEffects, @Local(argsOnly = true, ordinal = 1) int color) {
		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
			return original.call(instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
		}

		List<ParticleEffect> list = localParticleEffects.get();
		if (list == null || this.world == null) {
			return original.call(instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
		}
		ParticleEffect particleEffect = ListUtils.getRandomElement(list, this.world.getRandom());

		//? =1.20.1 {
		/*((TPType) particleEffect).texturizedParticles$setColor(-1);
		*///?} else {
		((TPType) particleEffect).texturizedParticles$setColor(color);
		 //?}
		return original.call(instance, particleEffect, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
	}

	// ENTITY PARTICLES
	@WrapOperation(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;"))
	private Particle swapParticle(WorldRenderer instance, ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Particle> original) {
		if (!TexturizedParticlesClient.getConfig().isModEnabled()) {
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
			StatusEffect statusEffect = TexturizedParticleManager.getVanillaStatusEffectByStatusEffect(parameters);
			color = statusEffect == null ? 0 : ArgbUtils.getColorWithoutAlpha(statusEffect.getColor());
		}

		if (color == 0) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}
		//?}

		List<ParticleEffect> list = TexturizedParticleManager.getParticleEffects(ArgbUtils.getColorWithoutAlpha(color));
		if (list == null || this.world == null) {
			return original.call(instance, parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
		}
		ParticleEffect particleEffect = ListUtils.getRandomElement(list, this.world.getRandom());

		((TPType) particleEffect).texturizedParticles$setColor(color);
		return original.call(instance, particleEffect, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
	}
}