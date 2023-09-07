package com.algorithmlx.inscribers.api.helper

import com.algorithmlx.inscribers.Constant
import net.minecraft.util.text._

object MojTextHelper {
  def menu(context: String): ITextComponent = {
    translate("menu", context)
  }

  def translate(id: String, context: String, any: Any*): ITextComponent = {
    new TranslationTextComponent(basedText(id, context), any)
  }

  def keybind(id: String, context: String): ITextComponent = {
    new KeybindTextComponent(basedText(id, context))
  }

  def scoreText(id: String, context: String, unnamed: String): ITextComponent = {
    new ScoreTextComponent(basedText(id, context), unnamed)
  }

  def selectorText(id: String, context: String): ITextComponent = {
    new SelectorTextComponent(basedText(id, context))
  }

  def stringText(id: String, context: String): ITextComponent = {
    new StringTextComponent(basedText(id, context))
  }

  private def basedText(id: String, context: String): String = {
    s"$id.${Constant.ModId}.$context"
  }
}
