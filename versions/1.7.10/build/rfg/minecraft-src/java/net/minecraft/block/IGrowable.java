package net.minecraft.block;

import java.util.Random;
import net.minecraft.world.World;

public interface IGrowable
{
    boolean func_149851_a(World worldIn, int x, int y, int z, boolean isClient);

    boolean func_149852_a(World worldIn, Random random, int x, int y, int z);

    void func_149853_b(World worldIn, Random random, int x, int y, int z);
}