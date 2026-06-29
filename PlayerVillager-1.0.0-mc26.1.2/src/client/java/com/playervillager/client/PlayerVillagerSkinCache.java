package com.playervillager.client;

import com.mojang.authlib.GameProfile;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public final class PlayerVillagerSkinCache {
	private static final Map<String, PlayerSkin> SKINS = new ConcurrentHashMap<>();
	private static final Map<String, Boolean> REQUESTS = new ConcurrentHashMap<>();

	private PlayerVillagerSkinCache() {
	}

	public static PlayerSkin requestSkin(String playerName) {
		String cacheKey = playerName.toLowerCase(Locale.ROOT);
		PlayerSkin cachedSkin = SKINS.get(cacheKey);
		if (cachedSkin != null) {
			return cachedSkin;
		}

		GameProfile profile = new GameProfile(
				UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)),
				playerName);
		PlayerSkin fallbackSkin = DefaultPlayerSkin.get(profile);

		if (REQUESTS.putIfAbsent(cacheKey, Boolean.TRUE) == null) {
			CompletableFuture
					.supplyAsync(() -> Minecraft.getInstance().services().profileResolver().fetchByName(playerName))
					.thenCompose(optionalProfile -> optionalProfile
							.map(resolvedProfile -> Minecraft.getInstance().getSkinManager().get(resolvedProfile))
							.orElseGet(() -> CompletableFuture.completedFuture(Optional.of(fallbackSkin))))
					.thenAccept(optionalSkin -> SKINS.put(cacheKey, optionalSkin.orElse(fallbackSkin)))
					.exceptionally(throwable -> {
						SKINS.put(cacheKey, fallbackSkin);
						return null;
					})
					.whenComplete((unused, throwable) -> REQUESTS.remove(cacheKey));
		}

		return fallbackSkin;
	}
}