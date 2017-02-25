package djsrv.newworld.gui;

import djsrv.newworld.handler.ConfigHandler;
import djsrv.newworld.lib.LibObfuscation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.IOException;

public class GuiCreateWorldCustom extends GuiCreateWorld {

    private GuiButton btnGameMode;
    private GuiButton btnMapFeatures;
    private GuiButton btnBonusItems;
    private GuiButton btnWorldType;
    private GuiButton btnAllowCommands;
    private GuiButton btnCustomizeType;
    private GuiTextField worldSeedField;

    public GuiCreateWorldCustom(GuiScreen parentScreen) {
        super(parentScreen);
        setDefaults();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.btnGameMode = getPrivateValue(LibObfuscation.BTN_GAME_MODE);
        this.btnMapFeatures = getPrivateValue(LibObfuscation.BTN_MAP_FEATURES);
        this.btnWorldType = getPrivateValue(LibObfuscation.BTN_WORLD_TYPE);
        this.btnAllowCommands = getPrivateValue(LibObfuscation.BTN_ALLOW_COMMANDS);
        this.btnBonusItems = getPrivateValue(LibObfuscation.BTN_BONUS_ITEMS);
        this.btnCustomizeType = getPrivateValue(LibObfuscation.BTN_CUSTOMIZE_TYPE);
        this.worldSeedField = getPrivateValue(LibObfuscation.WORLD_SEED_FIELD);
        lockButtons();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        lockButtons();
    }

    private void setDefaults() {
        setPrivateValue(LibObfuscation.GAME_MODE, ConfigHandler.gameModeDefault);
        setPrivateValue(LibObfuscation.WORLD_SEED, ConfigHandler.worldSeedDefault);
        setPrivateValue(LibObfuscation.GENERATE_STRUCTURES, ConfigHandler.generateStructuresDefault);
        setPrivateValue(LibObfuscation.WORLD_TYPE, ConfigHandler.worldTypeDefault.getWorldTypeID());
        setPrivateValue(LibObfuscation.ALLOW_CHEATS, ConfigHandler.allowCheatsDefault);
        setPrivateValue(LibObfuscation.BONUS_CHEST_ENABLED, ConfigHandler.bonusChestDefault);
        setPrivateValue(LibObfuscation.WORLD_PRESET, ConfigHandler.worldPresetDefault);
    }

    private void lockButtons() {
        if (ConfigHandler.gameModeLocked) this.btnGameMode.enabled = false;
        if (ConfigHandler.generateStructuresLocked) this.btnMapFeatures.enabled = false;
        if (ConfigHandler.worldTypeLocked) this.btnWorldType.enabled = false;
        if (ConfigHandler.allowCheatsLocked) this.btnAllowCommands.enabled = false;
        if (ConfigHandler.bonusChestLocked) this.btnBonusItems.enabled = false;
        if (ConfigHandler.customizeLocked) this.btnCustomizeType.enabled = false;
        if (ConfigHandler.worldSeedLocked) this.worldSeedField.setEnabled(false);
    }

    private <T> T getPrivateValue(String[] strings) {
        return ReflectionHelper.getPrivateValue(GuiCreateWorld.class, this, strings);
    }

    private <T> void setPrivateValue(String[] strings, T value) {
        ReflectionHelper.setPrivateValue(GuiCreateWorld.class, this, value, strings);
    }

}
