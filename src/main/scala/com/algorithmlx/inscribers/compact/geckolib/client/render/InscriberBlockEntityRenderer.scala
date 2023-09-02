package com.algorithmlx.inscribers.compact.geckolib.client.render

import com.algorithmlx.inscribers.compact.geckolib.block.GeckoInscriberBlockEntity
import com.algorithmlx.inscribers.compact.geckolib.client.model.InscriberBlockEntityModel
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer

class InscriberBlockEntityRenderer(ctx: TileEntityRendererDispatcher) extends GeoBlockRenderer[GeckoInscriberBlockEntity](ctx, new InscriberBlockEntityModel())
