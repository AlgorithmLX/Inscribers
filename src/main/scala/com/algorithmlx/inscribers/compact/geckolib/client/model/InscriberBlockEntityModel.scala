package com.algorithmlx.inscribers.compact.geckolib.client.model

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.compact.geckolib.block.GeckoInscriberBlockEntity
import net.minecraft.util.ResourceLocation
import software.bernie.geckolib3.model.AnimatedGeoModel

class InscriberBlockEntityModel extends AnimatedGeoModel[GeckoInscriberBlockEntity] {
  override def getModelLocation(`object`: GeckoInscriberBlockEntity): ResourceLocation =
    reloc("geo/inscriber.geo.json")

  override def getTextureLocation(`object`: GeckoInscriberBlockEntity): ResourceLocation =
    reloc("textures/geckolib/insciber.png")

  override def getAnimationFileLocation(animatable: GeckoInscriberBlockEntity): ResourceLocation =
    reloc("animations/inscriber.animation.json")
}
