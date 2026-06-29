package com.playervillager.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.entity.EntityType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PlayerVillagerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(EntityType.VILLAGER, PlayervillagerRenderer::new);
	}
}