package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ItemNameTag extends Item
{
    private static final String __OBFID = "CL_00000052";

    public ItemNameTag()
    {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target)
    {
        if (!stack.hasDisplayName())
        {
            return false;
        }
        else if (target instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)target;
            entityliving.setCustomNameTag(stack.getDisplayName());
            entityliving.func_110163_bv();
            --stack.stackSize;
            return true;
        }
        else
        {
            return super.itemInteractionForEntity(stack, player, target);
        }
    }
}