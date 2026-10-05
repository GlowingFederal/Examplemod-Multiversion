package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class PositionedSoundRecord extends PositionedSound
{
    private static final String __OBFID = "CL_00001120";

    public static PositionedSoundRecord func_147674_a(ResourceLocation soundResource, float pitch)
    {
        return new PositionedSoundRecord(soundResource, 0.25F, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord func_147673_a(ResourceLocation soundResource)
    {
        return new PositionedSoundRecord(soundResource, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord func_147675_a(ResourceLocation soundResource, float xPosition, float yPosition, float zPosition)
    {
        return new PositionedSoundRecord(soundResource, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
    }

    public PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, float xPosition, float yPosition, float zPosition)
    {
        this(soundResource, volume, pitch, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
    }

    private PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, boolean repeat, int repeatDelay, ISound.AttenuationType attenuationType, float xPosition, float yPosition, float zPosition)
    {
        super(soundResource);
        this.volume = volume;
        this.field_147663_c = pitch;
        this.xPosF = xPosition;
        this.yPosF = yPosition;
        this.zPosF = zPosition;
        this.repeat = repeat;
        this.field_147665_h = repeatDelay;
        this.field_147666_i = attenuationType;
    }
}