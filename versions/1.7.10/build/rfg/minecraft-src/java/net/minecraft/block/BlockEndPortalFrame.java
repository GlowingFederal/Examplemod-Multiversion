package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEndPortalFrame extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon iconEndPortalFrameTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconEndPortalFrameEye;
    private static final String __OBFID = "CL_00000237";

    public BlockEndPortalFrame()
    {
        super(Material.rock);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconEndPortalFrameTop : (side == 0 ? Blocks.end_stone.getBlockTextureFromSide(side) : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.iconEndPortalFrameTop = reg.registerIcon(this.getTextureName() + "_top");
        this.iconEndPortalFrameEye = reg.registerIcon(this.getTextureName() + "_eye");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconEndPortalFrameEye()
    {
        return this.iconEndPortalFrameEye;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 26;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        int l = worldIn.getBlockMetadata(x, y, z);

        if (isEnderEyeInserted(l))
        {
            this.setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }

        this.setBlockBoundsForItemRender();
    }

    /**
     * checks if an ender eye has been inserted into the frame block. parameters: metadata
     */
    public static boolean isEnderEyeInserted(int p_150020_0_)
    {
        return (p_150020_0_ & 4) != 0;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int l = ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
        worldIn.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        int i1 = worldIn.getBlockMetadata(x, y, z);
        return isEnderEyeInserted(i1) ? 15 : 0;
    }
}