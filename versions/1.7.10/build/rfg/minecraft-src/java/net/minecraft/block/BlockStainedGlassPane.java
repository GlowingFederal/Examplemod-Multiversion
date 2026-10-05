package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStainedGlassPane extends BlockPane
{
    private static final IIcon[] field_150106_a = new IIcon[16];
    private static final IIcon[] field_150105_b = new IIcon[16];
    private static final String __OBFID = "CL_00000313";

    public BlockStainedGlassPane()
    {
        super("glass", "glass_pane_top", Material.glass, false);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149735_b(int side, int meta)
    {
        return field_150106_a[meta % field_150106_a.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_150104_b(int p_150104_1_)
    {
        return field_150105_b[~p_150104_1_ & 15];
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return this.func_149735_b(side, ~meta & 15);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta;
    }

    @SideOnly(Side.CLIENT)
    public static int func_150103_c(int p_150103_0_)
    {
        return p_150103_0_ & 15;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<net.minecraft.item.ItemStack> list)
    {
        for (int i = 0; i < field_150106_a.length; ++i)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        super.registerBlockIcons(reg);

        for (int i = 0; i < field_150106_a.length; ++i)
        {
            field_150106_a[i] = reg.registerIcon(this.getTextureName() + "_" + ItemDye.field_150921_b[func_150103_c(i)]);
            field_150105_b[i] = reg.registerIcon(this.getTextureName() + "_pane_top_" + ItemDye.field_150921_b[func_150103_c(i)]);
        }
    }
}