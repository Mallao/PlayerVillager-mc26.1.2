package com.playervillager.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.npc.villager.Villager;

public class PlayervillagerRenderer extends EntityRenderer<Villager, PlayervillagerRenderState> {
	private final VillagerRenderer villagerRenderer;
	private final PlayervillagerAvatarRenderer playerRenderer;

	public PlayervillagerRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.villagerRenderer = new VillagerRenderer(context);
		this.playerRenderer = new PlayervillagerAvatarRenderer(context);
	}

	@Override
	public PlayervillagerRenderState createRenderState() {
		return new PlayervillagerRenderState();
	}

	@Override
	public void extractRenderState(Villager villager, PlayervillagerRenderState renderState, float tickDelta) {
		super.extractRenderState(villager, renderState, tickDelta);
		this.villagerRenderer.extractRenderState(villager, renderState.villagerState, tickDelta);
		renderState.villagerName = villager.hasCustomName() && villager.getCustomName() != null
				? villager.getCustomName().getString().trim()
				: "";
		renderState.usePlayerModel = !renderState.villagerName.isEmpty();
		if (renderState.usePlayerModel) {
			this.playerRenderer.extractRenderState(villager, renderState.avatarState, tickDelta);
			renderState.avatarState.skin = PlayerVillagerSkinCache.requestSkin(renderState.villagerName);
		}
	}

	@Override
	public void submit(PlayervillagerRenderState renderState, PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		if (renderState.usePlayerModel) {
			this.playerRenderer.submit(renderState.avatarState, poseStack, submitNodeCollector, cameraRenderState);
			return;
		}

		this.villagerRenderer.submit(renderState.villagerState, poseStack, submitNodeCollector, cameraRenderState);
	}
}