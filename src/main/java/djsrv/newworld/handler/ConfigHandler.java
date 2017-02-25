package djsrv.newworld.handler;

import djsrv.newworld.NewWorld;
import djsrv.newworld.lib.LibGameMode;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    public static Configuration config;

    public static String gameModeDefault = "survival";
    public static String worldSeedDefault = "";
    public static boolean generateStructuresDefault = true;
    public static WorldType worldTypeDefault = WorldType.DEFAULT;
    public static boolean allowCheatsDefault = false;
    public static boolean bonusChestDefault = false;
    public static String worldPresetDefault = "";

    public static boolean gameModeLocked = false;
    public static boolean worldSeedLocked = false;
    public static boolean generateStructuresLocked = false;
    public static boolean worldTypeLocked = false;
    public static boolean allowCheatsLocked = false;
    public static boolean bonusChestLocked = false;
    public static boolean customizeLocked = false;

    public static String[] gameModeBlackList = {};
    public static String[] worldTypeBlackList = {};

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        config.load();
    }

    public static void handleConfig() {
        gameModeDefault = getDefaultGameMode();
        worldSeedDefault = config.get(ConfigCategory.WORLD_SEED, ConfigKey.DEFAULT, worldSeedDefault).getString();
        generateStructuresDefault = config.get(ConfigCategory.GENERATE_STRUCTURES, ConfigKey.DEFAULT, generateStructuresDefault).getBoolean();
        worldTypeDefault = getDefaultWorldType();
        allowCheatsDefault = config.get(ConfigCategory.ALLOW_CHEATS, ConfigKey.DEFAULT, allowCheatsDefault).getBoolean();
        bonusChestDefault = config.get(ConfigCategory.BONUS_CHEST, ConfigKey.DEFAULT, bonusChestDefault).getBoolean();
        worldPresetDefault = config.get(ConfigCategory.CUSTOMIZE, ConfigKey.DEFAULT_PRESET, worldPresetDefault).getString();

        gameModeLocked = config.get(ConfigCategory.GAME_MODE, ConfigKey.LOCKED, gameModeLocked).getBoolean();
        worldSeedLocked = config.get(ConfigCategory.WORLD_SEED, ConfigKey.LOCKED, worldSeedLocked).getBoolean();
        generateStructuresLocked = config.get(ConfigCategory.GENERATE_STRUCTURES, ConfigKey.LOCKED, generateStructuresLocked).getBoolean();
        worldTypeLocked = config.get(ConfigCategory.WORLD_TYPE, ConfigKey.LOCKED, worldTypeLocked).getBoolean();
        allowCheatsLocked = config.get(ConfigCategory.ALLOW_CHEATS, ConfigKey.LOCKED, allowCheatsLocked).getBoolean();
        bonusChestLocked = config.get(ConfigCategory.BONUS_CHEST, ConfigKey.LOCKED, bonusChestLocked).getBoolean();
        customizeLocked = config.get(ConfigCategory.CUSTOMIZE, ConfigKey.LOCKED, customizeLocked).getBoolean();

        gameModeBlackList = config.get(ConfigCategory.GAME_MODE, ConfigKey.BLACK_LIST, gameModeBlackList, getGameModeComment()).getStringList();
        worldTypeBlackList = config.get(ConfigCategory.WORLD_TYPE, ConfigKey.BLACK_LIST, worldTypeBlackList, getWorldTypeComment()).getStringList();

        if (config.hasChanged()) config.save();
    }

    private static String getDefaultGameMode() {
        String gameMode = config.get(ConfigCategory.GAME_MODE, ConfigKey.DEFAULT, gameModeDefault).getString();
        if (!LibGameMode.GAME_MODES.contains(gameMode)) {
            NewWorld.logger.log(Level.ERROR, "Invalid game mode: " + gameMode);
            return "survival";
        }
        return gameMode;
    }

    private static WorldType getDefaultWorldType() {
        String name = config.get(ConfigCategory.WORLD_TYPE, ConfigKey.DEFAULT, worldTypeDefault.getWorldTypeName()).getString();
        WorldType worldType = WorldType.parseWorldType(name);
        if (worldType == null) {
            NewWorld.logger.log(Level.ERROR, "Invalid world type: " + name);
            return WorldType.DEFAULT;
        }
        return worldType;
    }

    private static String getGameModeComment() {
        String comment = "Valid game modes: ";
        comment += String.join(", ", LibGameMode.GAME_MODES);
        return comment;
    }

    private static String getWorldTypeComment() {
        String comment = "Valid world types: ";
        comment += String.join(", ", getValidWorldTypes());
        return comment;
    }

    private static List<String> getValidWorldTypes() {
        List<String> worldTypes = new ArrayList<>();
        for (WorldType worldType : WorldType.WORLD_TYPES) {
            if (worldType != null && worldType.getCanBeCreated()) worldTypes.add(worldType.getWorldTypeName());
        }
        return worldTypes;
    }

    private class ConfigCategory {
        public static final String GAME_MODE = "game mode";
        public static final String WORLD_SEED = "world seed";
        public static final String GENERATE_STRUCTURES = "generate structures";
        public static final String WORLD_TYPE = "world type";
        public static final String ALLOW_CHEATS = "allow cheats";
        public static final String BONUS_CHEST = "bonus chest";
        public static final String CUSTOMIZE = "customize";
    }

    private class ConfigKey {
        public static final String DEFAULT = "default";
        public static final String DEFAULT_PRESET = "default preset";
        public static final String LOCKED = "locked";
        public static final String BLACK_LIST = "blacklist";
    }

}
