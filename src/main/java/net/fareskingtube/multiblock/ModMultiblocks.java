package net.fareskingtube.multiblock;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class ModMultiblocks {

    public static final IMultiblock REVIVAL_ALTAR_MULTIBLOCK = registerMultiBlock("revival_altar_multiblock", PatchouliAPI.get().makeMultiblock(
            new String[][]{
                    {
                            "___________",
                            "___________",
                            "____SeS____",
                            "___SBBBS___",
                            "__SB   BS__",
                            "__sB   Bn__",
                            "__SB   BS__",
                            "___SBBBS___",
                            "____SwS____",
                            "___________",
                            "___________",
                    },
                    {
                            "___________",
                            "___________",
                            "___eBSBe___",
                            "__sBBRBBn__",
                            "__BB   BB__",
                            "__SR   RS__",
                            "__BB   BB__",
                            "__sBBRBBn__",
                            "___wBSBw___",
                            "___________",
                            "___________",
                    },
                    {
                            "___________",
                            "___________",
                            "___W___W___",
                            "__WBTTTBW__",
                            "___T   T___",
                            "___T   T___",
                            "___T   T___",
                            "__WBTTTBW__",
                            "___W___W___",
                            "___________",
                            "___________",
                    },
                    {
                            "___________",
                            "___________",
                            "___F___F___",
                            "__FB___BF__",
                            "____   ____",
                            "____   ____",
                            "____   ____",
                            "__FB___BF__",
                            "___F___F___",
                            "___________",
                            "___________",
                    },
                    {
                            "___________",
                            "___________",
                            "___W___W___",
                            "__WB___BW__",
                            "____   ____",
                            "____   ____",
                            "____   ____",
                            "__WB___BW__",
                            "___W___W___",
                            "___________",
                            "___________",
                    },
                    {
                            "___________",
                            "___________",
                            "___2___2___",
                            "__4B___B1__",
                            "____   ____",
                            "____ A ____",
                            "____   ____",
                            "__4B___B1__",
                            "___3___3___",
                            "___________",
                            "___________",
                    },
                    {
                            "__SSeeeSS__",
                            "_SBBBPBBBS_",
                            "SBBBPCPBBBS",
                            "SBBBSPSBBBS",
                            "sBPSbGbSPBn",
                            "sPCPG0GPCPn",
                            "sBPSbGbSPBn",
                            "SBBBSPSBBBS",
                            "SBBBPCPBBBS",
                            "_SBBBPBBBS_",
                            "__SSwwwSS__",
                    },

            },
            'A', ModBlocks.REVIVAL_ALTAR,
            'S', Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,
            'n', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH),
            'e', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.EAST),
            'w', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.WEST),
            's', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH),
            'B', Blocks.POLISHED_BLACKSTONE_BRICKS,
            'P', Blocks.POLISHED_BLACKSTONE,
            'C', Blocks.CHISELED_POLISHED_BLACKSTONE,
            'G', Blocks.GOLD_BLOCK,
            'b', ModBlocks.BLOOD_BLOCK,
            '1', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.BLOCK_HALF, BlockHalf.TOP),
            '2', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.EAST).with(Properties.BLOCK_HALF, BlockHalf.TOP),
            '3', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.WEST).with(Properties.BLOCK_HALF, BlockHalf.TOP),
            '4', Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH).with(Properties.BLOCK_HALF, BlockHalf.TOP),
            'W', Blocks.POLISHED_BLACKSTONE_BRICK_WALL,
            'F', Blocks.NETHER_BRICK_FENCE,
            'T', Blocks.POLISHED_BLACKSTONE_BRICK_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP),
            'R', Blocks.REDSTONE_BLOCK,
            '0', ModBlocks.BLOOD_BLOCK
    ));

    public static IMultiblock registerMultiBlock(String name, IMultiblock multiblock) {
        return PatchouliAPI.get().registerMultiblock(Identifier.of(HardcoreRevived.MOD_ID, name), multiblock);
    }

    public static void registerModMultiBlocks() {
        HardcoreRevived.LOGGER.info("Registering Mod Multiblocks for " + HardcoreRevived.MOD_ID);
    }
}
