package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBrewingStand extends BlockContainer
{
    private Random field_149961_a = new Random();
    @SideOnly(Side.CLIENT)
    private IIcon iconBrewingStandBase;
    private static final String __OBFID = "CL_00000207";

    public BlockBrewingStand()
    {
        super(Material.iron);
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
        return 25;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBrewingStand();
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider)
    {
        this.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBoundsForItemRender();
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBrewingStand tileentitybrewingstand = (TileEntityBrewingStand)worldIn.getTileEntity(x, y, z);

            if (tileentitybrewingstand != null)
            {
                player.func_146098_a(tileentitybrewingstand);
            }

            return true;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        if (itemIn.hasDisplayName())
        {
            ((TileEntityBrewingStand)worldIn.getTileEntity(x, y, z)).func_145937_a(itemIn.getDisplayName());
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntity tileentity = worldIn.getTileEntity(x, y, z);

        if (tileentity instanceof TileEntityBrewingStand)
        {
            TileEntityBrewingStand tileentitybrewingstand = (TileEntityBrewingStand)tileentity;

            for (int i1 = 0; i1 < tileentitybrewingstand.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileentitybrewingstand.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.field_149961_a.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.field_149961_a.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.field_149961_a.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = this.field_149961_a.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(worldIn, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.field_149961_a.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.field_149961_a.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.field_149961_a.nextGaussian() * f3);
                        worldIn.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.brewing_stand;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        double d0 = (double)((float)x + 0.4F + random.nextFloat() * 0.2F);
        double d1 = (double)((float)y + 0.7F + random.nextFloat() * 0.3F);
        double d2 = (double)((float)z + 0.4F + random.nextFloat() * 0.2F);
        worldIn.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.brewing_stand;
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
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        super.registerBlockIcons(reg);
        this.iconBrewingStandBase = reg.registerIcon(this.getTextureName() + "_base");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconBrewingStandBase()
    {
        return this.iconBrewingStandBase;
    }
}