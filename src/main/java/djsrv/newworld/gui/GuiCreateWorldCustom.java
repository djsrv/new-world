package djsrv.newworld.gui;

import djsrv.newworld.handler.ConfigHandler;
import djsrv.newworld.lib.LibGameMode;
import djsrv.newworld.lib.LibObfuscation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class GuiCreateWorldCustom extends GuiCreateWorld {

    private GuiButton btnGameMode;
    private GuiButton btnMapFeatures;
    private GuiButton btnBonusItems;
    private GuiButton btnWorldType;
    private GuiButton btnAllowCommands;
    private GuiButton btnCustomizeType;
    private GuiTextField worldSeedField;

    private final Method updateDisplayState = findMethod(LibObfuscation.UPDATE_DISPLAY_STATE);

    public GuiCreateWorldCustom(GuiScreen parentScreen) {
        super(parentScreen);
        setDefaults();
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
        updateGameMode();
        lockButtons();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                List<String> blackList = Arrays.asList(ConfigHandler.gameModeBlackList);
                String gameMode = getPrivateValue(LibObfuscation.GAME_MODE);

                int oldId = LibGameMode.GAME_MODES.indexOf(gameMode);
                int id = (oldId + 1) % LibGameMode.GAME_MODES.size();
                while (blackList.contains(LibGameMode.GAME_MODES.get(id)) && id != oldId) {
                    id = (id + 1) % LibGameMode.GAME_MODES.size();
                }
                setPrivateValue(LibObfuscation.GAME_MODE, LibGameMode.GAME_MODES.get(id));
                updateGameMode();
                updateDisplayState();
            } else if (button.id == 5) {
                int oldId = getPrivateValue(LibObfuscation.WORLD_TYPE);
                int id = (oldId + 1) % WorldType.WORLD_TYPES.length;
                while (!canSelectWorldType(id) && id != oldId) {
                    id = (id + 1) % WorldType.WORLD_TYPES.length;
                }
                setPrivateValue(LibObfuscation.WORLD_TYPE, id);
                updateDisplayState();
            } else {
                super.actionPerformed(button);
            }
            lockButtons();
        }
    }

    private boolean canSelectWorldType(int id) {
        List<String> blackList = Arrays.asList(ConfigHandler.worldTypeBlackList);
        WorldType worldtype = WorldType.WORLD_TYPES[id];
        return worldtype != null && worldtype.getCanBeCreated() && !blackList.contains(worldtype.getWorldTypeName()) ? (worldtype == WorldType.DEBUG_WORLD ? isShiftKeyDown() : true) : false;
    }

    private void updateDisplayState() {
        try {
            updateDisplayState.invoke(this);
        } catch (Exception e) {}
    }

    private void updateGameMode() {
        String gameMode = getPrivateValue(LibObfuscation.GAME_MODE);
        boolean hardCoreMode = gameMode.equals("hardcore");
        boolean allowCheatsWasSetByUser = getPrivateValue(LibObfuscation.ALLOW_CHEATS_WAS_SET_BY_USER);

        setPrivateValue(LibObfuscation.HARDCORE_MODE, hardCoreMode);

        if (!allowCheatsWasSetByUser) {
            setPrivateValue(LibObfuscation.ALLOW_CHEATS, gameMode.equals("creative"));
        }
        btnAllowCommands.enabled = !hardCoreMode;
        btnBonusItems.enabled = !hardCoreMode;
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

    private Method findMethod(String[] strings) {
        return ReflectionHelper.findMethod(GuiCreateWorld.class, this, strings);
    }

}
