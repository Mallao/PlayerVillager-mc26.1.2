package com.playervillager.client;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;

public class PlayervillagerRenderState extends EntityRenderState {
	public final VillagerRenderState villagerState = new VillagerRenderState();
	public final AvatarRenderState avatarState = new AvatarRenderState();
	public boolean usePlayerModel;
	public String villagerName;
}