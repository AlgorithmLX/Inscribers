package com.algorithmlx.inscribers.compact.geckolib.block

import com.algorithmlx.inscribers.block.InscriberBlockEntity
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.{IAnimatable, PlayState}
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.{AnimationData, AnimationFactory}
import software.bernie.geckolib3.util.GeckoLibUtil

class GeckoInscriberBlockEntity(`type`: TileEntityType[TileEntity])
  extends InscriberBlockEntity(`type`)
    with IAnimatable {
  private val animation = new AnimationBuilder().addAnimation("works")
  private val factory = GeckoLibUtil.createFactory(this)

  override def registerControllers(data: AnimationData): Unit = {
    data.addAnimationController(new AnimationController(this, "Animations", 0, evt => animations(evt)))
  }

  protected def animations(event: AnimationEvent[GeckoInscriberBlockEntity]): PlayState = {
    if (this.isWorking) {
      event.getController.setAnimation(animation)
      return PlayState.CONTINUE
    }

    PlayState.STOP
  }

  override def getFactory: AnimationFactory = this.factory
}
