package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.api.helper.VoxelHelper
import com.algorithmlx.inscribers.block.entity.InscriberBlockEntity
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
        .harvestLevel(3)
        .harvestTool(ToolType.PICKAXE)
        .requiresCorrectToolForDrops()
), IInscriber {
    init {
        this.registerDefaultState(this.stateDefinition.any().setValue(IInscriber.InscriberStates.standardVariant, Direction.NORTH))
    }

    private val south = VoxelHelper.builder()
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

    val west = VoxelHelper.builder()
        .qb(4, 0, 4, 12, 1, 12)
        .qb(0, 0, 7, 2, 15, 9)
        .qb(2, 1, 2, 14, 4, 14)
        .qb(12, 4, 12.2, 12.5, 5, 12.7)
        .qb(3, 4.3, 3.2, 12.9, 4.8, 12.925)
        .qb(3.5, 4, 12.025, 4, 5, 12.525)
        .qb(12, 4, 3.5, 12.5, 5, 4)
        .qb(3.525, 4, 3.5, 4.025, 5, 4)
        .qb(11, 12.1, 7.5, 12, 13.1, 8.5)
        .qb(10.5, 10.85, 7, 12.5, 12.85, 9)
        .qb(11.25, 9.85, 7.8, 11.75, 11.85, 8.3)
        .qb(4.225, 13, 7.225, 12.725, 14.5, 8.725)
        .qb(-0.8, 12.5, 9, 3.725, 15, 9.725)
        .qb(-0.8, 12.5, 6.275, 3.725, 15, 7)
        .qb(-0.8, 12.5, 7, -3.47, 15, 9)
        .qb(4.225, 14.5, 1.8, 4.725, 15, 13.8)
        .qb(4.225, 12.5, 1.775, 4.725, 13, 13.774)
        .qb(3.725, 12.5, 1.8, 4.225, 15, 13.8)
        .qb(2, 15, 7, 4.725, 15.45, 9)
        .qb(2, 12.05, 7, 4.725, 12.5, 9)
        .qb(2, 12.5, 7, 3.725, 15, 9)
        .qb(4.225, 13, 13.275, 4.725, 14.5, 13.775)
        .qb(4.225, 13, 1.775, 4.725, 14.5, 2.275)
        .of()

    val north = VoxelHelper.builder()
        .qb(4, 0, 4, 12, 1, 12)
        .qb(7, 0, 0, 9, 15, 2)
        .qb(2, 1, 2, 14, 4, 14)
        .qb(12, 4, 12.2, 12.5, 5, 12.7)
        .qb(3, 4.3, 3.2, 12.9, 4.8, 12.925)
        .qb(3.5, 4, 12.025, 4, 5, 12.525)
        .qb(12, 4, 3.5, 12.5, 5, 4)
        .qb(3.525, 4, 3.5, 4.025, 5, 4)
        .qb(2, 14.5, 4.225, 14, 15, 4.725)
        .qb(2, 12.5, 4.225, 14, 13, 4.725)
        .qb(2, 12.5, 3.725, 14, 15, 4.225)
        .qb(7, 15, 2, 9, 15.45, 4.725)
        .qb(7, 12.5, -0.8, 9, 15, 6.87)
        .qb(7, 12.05, 2, 9, 12.5, 4.725)
        .qb(7, 12.5, 2, 9, 15, 3.725)
        .qb(6.275, 12.5, -0.8, 7, 15, 3.725)
        .qb(13.5, 13, 4.225, 14, 14.5, 4.725)
        .qb(2, 13, 4.225, 2.5, 14.5, 4.725)
        .qb(9, 12.5, -0.8, 9.725, 15, 3.725)
        .qb(7.5, 12.1, 11.15, 8.5, 13.1, 12.15)
        .qb(7, 10.85, 10.65, 9, 12.85, 12.65)
        .qb(7.75, 9.85, 11.375, 8.25, 11.85, 11.875)
        .qb(7.25, 13, 4.225, 8.75, 14.5, 12.725)
        .of()

    val east = VoxelHelper.builder()
        .qb(4, 0, 4, 12, 1, 12)
        .qb(14, 0, 7, 16, 15, 9)
        .qb(2, 1, 2, 14, 4, 14)
        .qb(12, 4, 12.2, 12.5, 5, 12.7)
        .qb(3, 4.3, 3.2, 12.9, 4.8, 12.925)
        .qb(3.5, 4, 12.0245, 4, 5, 12.525)
        .qb(12, 4, 3.5, 12.5, 5, 4)
        .qb(3.525, 4, 3.5, 4.025, 5, 4)
        .qb(4, 12.1, 7.5, 5, 13.100000000000001, 8.5)
        .qb(3.5, 10.85, 7, 5.5, 12.85, 9)
        .qb(4.25, 9.85, 7.8, 4.75, 11.85, 8.3)
        .qb(3.275, 13, 7.225, 11.775, 14.5, 8.725)
        .qb(12.275, 12.45, 9, 16.8, 15, 9.725)
        .qb(12.275, 12.5, 6.275, 16.8, 15, 7)
        .qb(16, 12.5, 7, 16.8, 15, 9)
        .qb(11.275, 14.5, 1.8, 11.775, 15, 13.8)
        .qb(11.275, 12.5, 1.8, 11.775, 13, 13.8)
        .qb(11.775, 12.5, 1.8, 12.275, 15, 13.8)
        .qb(11.275, 15, 7, 14, 15.45, 9)
        .qb(11.275, 12.05, 7, 14, 12.5, 9)
        .qb(12.275, 12.5, 7, 14, 15, 9)
        .qb(11.275, 13, 13.3, 11.775, 14.5, 13.8)
        .qb(11.275, 13, 1.8, 11.775, 14.5, 2.3)
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
                NetworkHooks.openGui(pPlayer as ServerPlayerEntity, blockEntity, pPos)
            }
        }

        return ActionResultType.SUCCESS
    }

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity =
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
        if (pState.block != pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)

            if (blockEntity is InscriberBlockEntity)
                InventoryHelper.dropContents(pLevel, pPos, blockEntity.getInv().getStackList())
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

    override fun getShape(
        pState: BlockState,
        pLevel: IBlockReader,
        pPos: BlockPos,
        pContext: ISelectionContext
    ): VoxelShape {
        return when(val direction = pState.getValue(IInscriber.InscriberStates.standardVariant)) {
            Direction.WEST -> south
            Direction.NORTH -> west
            Direction.EAST -> north
            Direction.SOUTH -> east
            else -> throw UnsupportedOperationException("Enable to load voxel shape for direction $direction")
        }
    }

    override fun getSize(): Int = 36

    override fun getType(): IInscriber.InscriberType = IInscriber.InscriberType.STANDARD_INSCRIBER
}