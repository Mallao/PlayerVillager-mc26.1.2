package com.playervillager.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.npc.villager.Villager;

public class PlayervillagerAvatarRenderer extends LivingEntityRenderer<Villager, AvatarRenderState, PlayerModel> {
	private final PlayerModel wideModel;
	private final PlayerModel slimModel;

	public PlayervillagerAvatarRenderer(EntityRendererProvider.Context context) {
		super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
		this.wideModel = this.model;
		this.slimModel = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
	}

	@Override
	public AvatarRenderState createRenderState() {
		return new AvatarRenderState();
	}

	@Override
	public void extractRenderState(Villager villager, AvatarRenderState renderState, float tickDelta) {
		super.extractRenderState(villager, renderState, tickDelta);
	}

	@Override
	public Identifier getTextureLocation(AvatarRenderState renderState) {
		PlayerSkin skin = renderState.skin;
		return skin != null ? skin.body().texturePath() : DefaultPlayerSkin.getDefaultTexture();
	}

	@Override
	public void submit(AvatarRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
			CameraRenderState cameraRenderState) {
		PlayerSkin skin = renderState.skin;
		this.model = skin != null && skin.model() == PlayerModelType.SLIM ? this.slimModel : this.wideModel;
		super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
	}
}