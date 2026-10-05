package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCauldron extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon field_150029_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150028_b;
    @SideOnly(Side.CLIENT)
    private IIcon field_150030_M;
    private static final String __OBFID = "CL_00000213";

    public BlockCauldron()
    {
        super(Material.iron);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_150028_b : (side == 0 ? this.field_150030_M : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.field_150029_a = reg.registerIcon(this.getTextureName() + "_" + "inner");
        this.field_150028_b = reg.registerIcon(this.getTextureName() + "_top");
        this.field_150030_M = reg.registerIcon(this.getTextureName() + "_" + "bottom");
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        float f = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBoundsForItemRender();
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getCauldronIcon(String iconName)
    {
        return iconName.equals("inner") ? Blocks.cauldron.field_150029_a : (iconName.equals("bottom") ? Blocks.cauldron.field_150030_M : null);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
        return 24;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        int l = func_150027_b(worldIn.getBlockMetadata(x, y, z));
        float f = (float)y + (6.0F + (float)(3 * l)) / 16.0F;

        if (!worldIn.isRemote && entityIn.isBurning() && l > 0 && entityIn.boundingBox.minY <= (double)f)
        {
            entityIn.extinguish();
            this.func_150024_a(worldIn, x, y, z, l - 1);
        }
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
            ItemStack itemstack = player.inventory.getCurrentItem();

            if (itemstack == null)
            {
                return true;
            }
            else
            {
                int i1 = worldIn.getBlockMetadata(x, y, z);
                int j1 = func_150027_b(i1);

                if (itemstack.getItem() == Items.water_bucket)
                {
                    if (j1 < 3)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }

                        this.func_150024_a(worldIn, x, y, z, 3);
                    }

                    return true;
                }
                else
                {
                    if (itemstack.getItem() == Items.glass_bottle)
                    {
                        if (j1 > 0)
                        {
                            if (!player.capabilities.isCreativeMode)
                            {
                                ItemStack itemstack1 = new ItemStack(Items.potionitem, 1, 0);

                                if (!player.inventory.addItemStackToInventory(itemstack1))
                                {
                                    worldIn.spawnEntityInWorld(new EntityItem(worldIn, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, itemstack1));
                                }
                                else if (player instanceof EntityPlayerMP)
                                {
                                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                                }

                                --itemstack.stackSize;

                                if (itemstack.stackSize <= 0)
                                {
                                    player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                                }
                            }

                            this.func_150024_a(worldIn, x, y, z, j1 - 1);
                        }
                    }
                    else if (j1 > 0 && itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.CLOTH)
                    {
                        ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                        itemarmor.removeColor(itemstack);
                        this.func_150024_a(worldIn, x, y, z, j1 - 1);
                        return true;
                    }

                    return false;
                }
            }
        }
    }

    public void func_150024_a(World worldIn, int x, int y, int z, int level)
    {
        worldIn.setBlockMetadataWithNotify(x, y, z, MathHelper.clamp_int(level, 0, 3), 2);
        worldIn.func_147453_f(x, y, z, this);
    }

    /**
     * currently only used by BlockCauldron to incrament meta-data during rain
     */
    public void fillWithRain(World worldIn, int x, int y, int z)
    {
        if (worldIn.rand.nextInt(20) == 1)
        {
            int l = worldIn.getBlockMetadata(x, y, z);

            if (l < 3)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, l + 1, 2);
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.cauldron;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.cauldron;
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
        return func_150027_b(i1);
    }

    public static int func_150027_b(int meta)
    {
        return meta;
    }

    @SideOnly(Side.CLIENT)
    public static float getRenderLiquidLevel(int meta)
    {
        int j = MathHelper.clamp_int(meta, 0, 3);
        return (float)(6 + 3 * j) / 16.0F;
    }
}