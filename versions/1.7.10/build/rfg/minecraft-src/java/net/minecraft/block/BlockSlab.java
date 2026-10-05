package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab extends Block
{
    protected final boolean field_150004_a;
    private static final String __OBFID = "CL_00000253";

    public BlockSlab(boolean p_i45410_1_, Material p_i45410_2_)
    {
        super(p_i45410_2_);
        this.field_150004_a = p_i45410_1_;

        if (p_i45410_1_)
        {
            this.opaque = true;
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }

        this.setLightOpacity(255);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        if (this.field_150004_a)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            boolean flag = (worldIn.getBlockMetadata(x, y, z) & 8) != 0;

            if (flag)
            {
                this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
            else
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            }
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        if (this.field_150004_a)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return this.field_150004_a;
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        return this.field_150004_a ? meta : (side != 0 && (side == 1 || (double)subY <= 0.5D) ? meta : meta | 8);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return this.field_150004_a ? 2 : 1;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta & 7;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return this.field_150004_a;
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (this.field_150004_a)
        {
            return super.shouldSideBeRendered(worldIn, x, y, z, side);
        }
        else if (side != 1 && side != 0 && !super.shouldSideBeRendered(worldIn, x, y, z, side))
        {
            return false;
        }
        else
        {
            int i1 = x + Facing.offsetsXForSide[Facing.oppositeSide[side]];
            int j1 = y + Facing.offsetsYForSide[Facing.oppositeSide[side]];
            int k1 = z + Facing.offsetsZForSide[Facing.oppositeSide[side]];
            boolean flag = (worldIn.getBlockMetadata(i1, j1, k1) & 8) != 0;
            return flag ? (side == 0 ? true : (side == 1 && super.shouldSideBeRendered(worldIn, x, y, z, side) ? true : !func_150003_a(worldIn.getBlock(x, y, z)) || (worldIn.getBlockMetadata(x, y, z) & 8) == 0)) : (side == 1 ? true : (side == 0 && super.shouldSideBeRendered(worldIn, x, y, z, side) ? true : !func_150003_a(worldIn.getBlock(x, y, z)) || (worldIn.getBlockMetadata(x, y, z) & 8) != 0));
        }
    }

    @SideOnly(Side.CLIENT)
    private static boolean func_150003_a(Block p_150003_0_)
    {
        return p_150003_0_ == Blocks.stone_slab || p_150003_0_ == Blocks.wooden_slab;
    }

    public abstract String func_150002_b(int p_150002_1_);

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        return super.getDamageValue(worldIn, x, y, z) & 7;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z)
    {
        return func_150003_a(this) ? Item.getItemFromBlock(this) : (this == Blocks.double_stone_slab ? Item.getItemFromBlock(Blocks.stone_slab) : (this == Blocks.double_wooden_slab ? Item.getItemFromBlock(Blocks.wooden_slab) : Item.getItemFromBlock(Blocks.stone_slab)));
    }
}