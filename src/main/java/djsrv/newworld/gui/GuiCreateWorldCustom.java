package djsrv.newworld.gui;

import djsrv.newworld.handler.ConfigHandler;
import djsrv.newworld.lib.LibObfuscation;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class GuiCreateWorldCustom extends GuiCreateWorld {

    public GuiCreateWorldCustom(GuiScreen parentScreen) {
        super(parentScreen);
        setDefaults();
    }

    private void setDefaults() {
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultGameMode, LibObfuscation.GAME_MODE);
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultSeed, LibObfuscation.WORLD_SEED);
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultGenerateStructures, LibObfuscation.GENERATE_STRUCTURES);
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultWorldType.getWorldTypeID(), LibObfuscation.WORLD_TYPE);
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultAllowCheats, LibObfuscation.ALLOW_CHEATS);
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultBonusChest, LibObfuscation.BONUS_CHEST_ENABLED);
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, ConfigHandler.defaultWorldPreset, LibObfuscation.WORLD_PRESET);
    }

}
