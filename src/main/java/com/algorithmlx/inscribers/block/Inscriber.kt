package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.api.helper.VoxelHelper
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.ToolType
import net.minecraftforge.fml.network.NetworkHooks

@Suppress("override_deprecation")
class Inscriber: Block(
    Properties.of(Material.METAL)
//        .noOcclusion()
        .harvestLevel(3)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
), IInscriber {
    init {
        this.registerDefaultState(this.stateDefinition.any().setValue(IInscriber.InscriberStates.standardVariant, Direction.NORTH))
    }

    private val xVoxel = VoxelHelper.builder()
        .qb(4.0, 0.0, 4.0, 12.0, 1.0, 12.0)
        .qb(7.0, 0.0, 14.0, 9.0, 15.0, 16.0)
        .qb(2.0, 1.0, 2.0, 14.0, 4.0, 14.0)
        .qb(12.0, 4.0, 12.2, 12.5, 5.0, 12.7)
        .qb(3.0, 4.3, 3.2, 12.9, 4.8, 12.925)
        .qb(3.5, 4.0, 12.025, 4.0, 5.0, 12.525)
        .qb(12.0, 4.0, 3.5, 12.5, 5.0, 4.0)
        .qb(3.525, 4.0, 3.5, 4.025, 5.0, 4.0)
        .qb(2.0, 14.5, 12.0, 14.0, 15.0, 11.775)
        .qb(2.0, 12.5, 11.273, 14.0, 15.0, 11.775)
        .qb(2.0, 12.5, 11.775, 14.0, 15.0, 12.275)
        .qb(7.0, 15.0, 11.275, 9.0, 15.45, 14.0)
        .qb(7.0, 12.5, 16.0, 9.0, 15.0, 16.8)
        .qb(7.0, 12.05, 11.275, 9.0, 12.5, 14.0)
        .qb(7.0, 12.6, 12.276, 9.0, 16.0, 14.0)
        .qb(6.275, 12.5, 12.245, 7.0, 15.0, 16.8)
        .qb(13.5, 13.0, 11.275, 14.0, 14.5, 11.775)
        .qb(2.0, 13.0, 11.275, 2.5, 14.5, 11.775)
        .qb(9.0, 12.5, 12.275, 9.725, 15.0, 16.8)
        .qb(7.5, 12.1, 4.05, 8.5, 13.1, 5.05)
        .qb(7.0, 10.85, 3.55, 9.0, 12.85, 5.55)
        .qb(7.75, 9.85, 4.275, 8.25, 11.85, 4.775)
        .qb(7.25, 13.0, 3.275, 8.75, 14.5, 11.775)
        .of()

    override fun use(
        pState: BlockState,
        pLevel: World,
        pPos: BlockPos,
        pPlayer: PlayerEntity,
        pHand: Hand,
        pHit: BlockRayTraceResult
    ): ActionResultType {
        if (!pLevel.isClientSide) {
            val blockEntity = pLevel.getBlockEntity(pPos)

            if (blockEntity is InscriberBlockEntity) {
                NetworkHooks.openGui(pPlayer as ServerPlayerEntity, blockEntity as INamedContainerProvider, pPos)
            }
        }

        return ActionResultType.SUCCESS
    }

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? =
        InscriberBlockEntity()

    override fun createBlockStateDefinition(pBuilder: StateContainer.Builder<Block, BlockState>) {
        pBuilder.add(IInscriber.InscriberStates.standardVariant)
    }

    override fun getStateForPlacement(pContext: BlockItemUseContext): BlockState? = defaultBlockState().setValue(
        IInscriber.InscriberStates.standardVariant,
        pContext.horizontalDirection.clockWise
    )

    override fun onRemove(
        pState: BlockState,
        pLevel: World,
        pPos: BlockPos,
        pNewState: BlockState,
        pIsMoving: Boolean
    ) {
        val blockEntity = pLevel.getBlockEntity(pPos)

        if (blockEntity is InscriberBlockEntity) {
            InventoryHelper.dropContents(pLevel, pPos, blockEntity.getInv().getStackList())
        }
    }

    override fun getShape(
        pState: BlockState,
        pLevel: IBlockReader,
        pPos: BlockPos,
        pContext: ISelectionContext
    ): VoxelShape {
        return xVoxel
    }

    override fun getSize(): Int = 36

    override fun getType(): IInscriber.InscriberType = IInscriber.InscriberType.STANDARD_INSCRIBER
}