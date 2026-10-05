package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockChest extends BlockContainer
{
    private final Random field_149955_b = new Random();
    public final int field_149956_a;
    private static final String __OBFID = "CL_00000214";

    protected BlockChest(int type)
    {
        super(Material.wood);
        this.field_149956_a = type;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 22;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        if (worldIn.getBlock(x, y, z - 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (worldIn.getBlock(x, y, z + 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        }
        else if (worldIn.getBlock(x - 1, y, z) == this)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (worldIn.getBlock(x + 1, y, z) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        }
        else
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        this.func_149954_e(worldIn, x, y, z);
        Block block = worldIn.getBlock(x, y, z - 1);
        Block block1 = worldIn.getBlock(x, y, z + 1);
        Block block2 = worldIn.getBlock(x - 1, y, z);
        Block block3 = worldIn.getBlock(x + 1, y, z);

        if (block == this)
        {
            this.func_149954_e(worldIn, x, y, z - 1);
        }

        if (block1 == this)
        {
            this.func_149954_e(worldIn, x, y, z + 1);
        }

        if (block2 == this)
        {
            this.func_149954_e(worldIn, x - 1, y, z);
        }

        if (block3 == this)
        {
            this.func_149954_e(worldIn, x + 1, y, z);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        Block block = worldIn.getBlock(x, y, z - 1);
        Block block1 = worldIn.getBlock(x, y, z + 1);
        Block block2 = worldIn.getBlock(x - 1, y, z);
        Block block3 = worldIn.getBlock(x + 1, y, z);
        byte b0 = 0;
        int l = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            b0 = 2;
        }

        if (l == 1)
        {
            b0 = 5;
        }

        if (l == 2)
        {
            b0 = 3;
        }

        if (l == 3)
        {
            b0 = 4;
        }

        if (block != this && block1 != this && block2 != this && block3 != this)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, b0, 3);
        }
        else
        {
            if ((block == this || block1 == this) && (b0 == 4 || b0 == 5))
            {
                if (block == this)
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z - 1, b0, 3);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z + 1, b0, 3);
                }

                worldIn.setBlockMetadataWithNotify(x, y, z, b0, 3);
            }

            if ((block2 == this || block3 == this) && (b0 == 2 || b0 == 3))
            {
                if (block2 == this)
                {
                    worldIn.setBlockMetadataWithNotify(x - 1, y, z, b0, 3);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x + 1, y, z, b0, 3);
                }

                worldIn.setBlockMetadataWithNotify(x, y, z, b0, 3);
            }
        }

        if (itemIn.hasDisplayName())
        {
            ((TileEntityChest)worldIn.getTileEntity(x, y, z)).func_145976_a(itemIn.getDisplayName());
        }
    }

    public void func_149954_e(World p_149954_1_, int p_149954_2_, int p_149954_3_, int p_149954_4_)
    {
        if (!p_149954_1_.isRemote)
        {
            Block block = p_149954_1_.getBlock(p_149954_2_, p_149954_3_, p_149954_4_ - 1);
            Block block1 = p_149954_1_.getBlock(p_149954_2_, p_149954_3_, p_149954_4_ + 1);
            Block block2 = p_149954_1_.getBlock(p_149954_2_ - 1, p_149954_3_, p_149954_4_);
            Block block3 = p_149954_1_.getBlock(p_149954_2_ + 1, p_149954_3_, p_149954_4_);
            boolean flag = true;
            int l;
            Block block4;
            int i1;
            Block block5;
            boolean flag1;
            byte b0;
            int j1;

            if (block != this && block1 != this)
            {
                if (block2 != this && block3 != this)
                {
                    b0 = 3;

                    if (block.func_149730_j() && !block1.func_149730_j())
                    {
                        b0 = 3;
                    }

                    if (block1.func_149730_j() && !block.func_149730_j())
                    {
                        b0 = 2;
                    }

                    if (block2.func_149730_j() && !block3.func_149730_j())
                    {
                        b0 = 5;
                    }

                    if (block3.func_149730_j() && !block2.func_149730_j())
                    {
                        b0 = 4;
                    }
                }
                else
                {
                    l = block2 == this ? p_149954_2_ - 1 : p_149954_2_ + 1;
                    block4 = p_149954_1_.getBlock(l, p_149954_3_, p_149954_4_ - 1);
                    i1 = block2 == this ? p_149954_2_ - 1 : p_149954_2_ + 1;
                    block5 = p_149954_1_.getBlock(i1, p_149954_3_, p_149954_4_ + 1);
                    b0 = 3;
                    flag1 = true;

                    if (block2 == this)
                    {
                        j1 = p_149954_1_.getBlockMetadata(p_149954_2_ - 1, p_149954_3_, p_149954_4_);
                    }
                    else
                    {
                        j1 = p_149954_1_.getBlockMetadata(p_149954_2_ + 1, p_149954_3_, p_149954_4_);
                    }

                    if (j1 == 2)
                    {
                        b0 = 2;
                    }

                    if ((block.func_149730_j() || block4.func_149730_j()) && !block1.func_149730_j() && !block5.func_149730_j())
                    {
                        b0 = 3;
                    }

                    if ((block1.func_149730_j() || block5.func_149730_j()) && !block.func_149730_j() && !block4.func_149730_j())
                    {
                        b0 = 2;
                    }
                }
            }
            else
            {
                l = block == this ? p_149954_4_ - 1 : p_149954_4_ + 1;
                block4 = p_149954_1_.getBlock(p_149954_2_ - 1, p_149954_3_, l);
                i1 = block == this ? p_149954_4_ - 1 : p_149954_4_ + 1;
                block5 = p_149954_1_.getBlock(p_149954_2_ + 1, p_149954_3_, i1);
                b0 = 5;
                flag1 = true;

                if (block == this)
                {
                    j1 = p_149954_1_.getBlockMetadata(p_149954_2_, p_149954_3_, p_149954_4_ - 1);
                }
                else
                {
                    j1 = p_149954_1_.getBlockMetadata(p_149954_2_, p_149954_3_, p_149954_4_ + 1);
                }

                if (j1 == 4)
                {
                    b0 = 4;
                }

                if ((block2.func_149730_j() || block4.func_149730_j()) && !block3.func_149730_j() && !block5.func_149730_j())
                {
                    b0 = 5;
                }

                if ((block3.func_149730_j() || block5.func_149730_j()) && !block2.func_149730_j() && !block4.func_149730_j())
                {
                    b0 = 4;
                }
            }

            p_149954_1_.setBlockMetadataWithNotify(p_149954_2_, p_149954_3_, p_149954_4_, b0, 3);
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        int l = 0;

        if (worldIn.getBlock(x - 1, y, z) == this)
        {
            ++l;
        }

        if (worldIn.getBlock(x + 1, y, z) == this)
        {
            ++l;
        }

        if (worldIn.getBlock(x, y, z - 1) == this)
        {
            ++l;
        }

        if (worldIn.getBlock(x, y, z + 1) == this)
        {
            ++l;
        }

        return l > 1 ? false : (this.func_149952_n(worldIn, x - 1, y, z) ? false : (this.func_149952_n(worldIn, x + 1, y, z) ? false : (this.func_149952_n(worldIn, x, y, z - 1) ? false : !this.func_149952_n(worldIn, x, y, z + 1))));
    }

    private boolean func_149952_n(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x, y, z) != this ? false : (worldIn.getBlock(x - 1, y, z) == this ? true : (worldIn.getBlock(x + 1, y, z) == this ? true : (worldIn.getBlock(x, y, z - 1) == this ? true : worldIn.getBlock(x, y, z + 1) == this)));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        TileEntityChest tileentitychest = (TileEntityChest)worldIn.getTileEntity(x, y, z);

        if (tileentitychest != null)
        {
            tileentitychest.updateContainingBlockInfo();
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntityChest tileentitychest = (TileEntityChest)worldIn.getTileEntity(x, y, z);

        if (tileentitychest != null)
        {
            for (int i1 = 0; i1 < tileentitychest.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileentitychest.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.field_149955_b.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.field_149955_b.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.field_149955_b.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; worldIn.spawnEntityInWorld(entityitem))
                    {
                        int j1 = this.field_149955_b.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(worldIn, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.field_149955_b.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.field_149955_b.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.field_149955_b.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            worldIn.func_147453_f(x, y, z, blockBroken);
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
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
            IInventory iinventory = this.func_149951_m(worldIn, x, y, z);

            if (iinventory != null)
            {
                player.displayGUIChest(iinventory);
            }

            return true;
        }
    }

    public IInventory func_149951_m(World p_149951_1_, int p_149951_2_, int p_149951_3_, int p_149951_4_)
    {
        Object object = (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_);

        if (object == null)
        {
            return null;
        }
        else if (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_, DOWN))
        {
            return null;
        }
        else if (func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this && (p_149951_1_.isSideSolid(p_149951_2_ - 1, p_149951_3_ + 1, p_149951_4_, DOWN) || func_149953_o(p_149951_1_, p_149951_2_ - 1, p_149951_3_, p_149951_4_)))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this && (p_149951_1_.isSideSolid(p_149951_2_ + 1, p_149951_3_ + 1, p_149951_4_, DOWN) || func_149953_o(p_149951_1_, p_149951_2_ + 1, p_149951_3_, p_149951_4_)))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this && (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_ - 1, DOWN) || func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ - 1)))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this && (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_ + 1, DOWN) || func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ + 1)))
        {
            return null;
        }
        else
        {
            if (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this)
            {
                object = new InventoryLargeChest("container.chestDouble", (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_ - 1, p_149951_3_, p_149951_4_), (IInventory)object);
            }

            if (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this)
            {
                object = new InventoryLargeChest("container.chestDouble", (IInventory)object, (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_ + 1, p_149951_3_, p_149951_4_));
            }

            if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this)
            {
                object = new InventoryLargeChest("container.chestDouble", (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_ - 1), (IInventory)object);
            }

            if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this)
            {
                object = new InventoryLargeChest("container.chestDouble", (IInventory)object, (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_ + 1));
            }

            return (IInventory)object;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        TileEntityChest tileentitychest = new TileEntityChest();
        return tileentitychest;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return this.field_149956_a == 1;
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (!this.canProvidePower())
        {
            return 0;
        }
        else
        {
            int i1 = ((TileEntityChest)worldIn.getTileEntity(x, y, z)).numPlayersUsing;
            return MathHelper.clamp_int(i1, 0, 15);
        }
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 1 ? this.isProvidingWeakPower(worldIn, x, y, z, side) : 0;
    }

    private static boolean func_149953_o(World p_149953_0_, int p_149953_1_, int p_149953_2_, int p_149953_3_)
    {
        Iterator iterator = p_149953_0_.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox((double)p_149953_1_, (double)(p_149953_2_ + 1), (double)p_149953_3_, (double)(p_149953_1_ + 1), (double)(p_149953_2_ + 2), (double)(p_149953_3_ + 1))).iterator();
        EntityOcelot entityocelot;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            Entity entity = (Entity)iterator.next();
            entityocelot = (EntityOcelot)entity;
        }
        while (!entityocelot.isSitting());

        return true;
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
        return Container.calcRedstoneFromInventory(this.func_149951_m(worldIn, x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("planks_oak");
    }
}