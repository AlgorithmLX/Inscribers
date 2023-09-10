package com.algorithmlx.inscribers.api.block

import com.algorithmlx.inscribers.api.translate
import com.algorithmlx.inscribers.init.config.InscribersConfig
import net.minecraft.state.DirectionProperty
import net.minecraft.util.Direction
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.sqrt

interface IInscriber {
    /**
     * Sets container size.
     *
     * Default: 2
     * @return [Int]
     */
    fun getSize(): Int = 2
    /**
     * Sets Inscriber Type
     * @return [InscriberType]
     */
    fun getType(): InscriberType

    /**
     * Sets energy capacity
     * @return [Int]
     */
    fun getEnergy(): Int = InscribersConfig.inscriberCapacity.get()

    private val sqrtSize: Int
        get() = sqrt(this.getSize().toFloat()).toInt()

    fun getXSize(): Int = this.sqrtSize

    fun getYSize(): Int = this.sqrtSize

    enum class InscriberType {
        STANDARD_INSCRIBER;

        fun getTranslationName(): TranslationTextComponent = translate("api.type", this.name.lowercase()) as TranslationTextComponent
    }

    object InscriberStates {
        val standardVariant = direction("standard_model")

        private fun direction(id: String): DirectionProperty = DirectionProperty.create(
            id,
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST,
            Direction.UP,
            Direction.DOWN
        )
    }
}