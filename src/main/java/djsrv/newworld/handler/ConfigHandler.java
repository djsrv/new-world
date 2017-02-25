package djsrv.newworld.handler;

import djsrv.newworld.NewWorld;
import net.minecraft.world.GameType;
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

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        config.load();
    }

    public static void handleConfig() {
        gameModeDefault = getDefaultGameMode();
        worldSeedDefault = getString(ConfigCategory.WORLD_SEED, ConfigKey.DEFAULT, null, worldSeedDefault);
        generateStructuresDefault = getBoolean(ConfigCategory.GENERATE_STRUCTURES, ConfigKey.DEFAULT, null, generateStructuresDefault);
        worldTypeDefault = getDefaultWorldType();
        allowCheatsDefault = getBoolean(ConfigCategory.ALLOW_CHEATS, ConfigKey.DEFAULT, null, allowCheatsDefault);
        bonusChestDefault = getBoolean(ConfigCategory.BONUS_CHEST, ConfigKey.DEFAULT, null, bonusChestDefault);
        worldPresetDefault = getString(ConfigCategory.CUSTOMIZE, ConfigKey.DEFAULT_PRESET, null, worldPresetDefault);

        gameModeLocked = getBoolean(ConfigCategory.GAME_MODE, ConfigKey.LOCKED, null, gameModeLocked);
        worldSeedLocked = getBoolean(ConfigCategory.WORLD_SEED, ConfigKey.LOCKED, null, worldSeedLocked);
        generateStructuresLocked = getBoolean(ConfigCategory.GENERATE_STRUCTURES, ConfigKey.LOCKED, null, generateStructuresLocked);
        worldTypeLocked = getBoolean(ConfigCategory.WORLD_TYPE, ConfigKey.LOCKED, null, worldTypeLocked);
        allowCheatsLocked = getBoolean(ConfigCategory.ALLOW_CHEATS, ConfigKey.LOCKED, null, allowCheatsLocked);
        bonusChestLocked = getBoolean(ConfigCategory.BONUS_CHEST, ConfigKey.LOCKED, null, bonusChestLocked);
        customizeLocked = getBoolean(ConfigCategory.CUSTOMIZE, ConfigKey.LOCKED, null, customizeLocked);

        if (config.hasChanged()) config.save();
    }

    private static String getString (String category, String propName, String comment, String defaultValue) {
        Property prop = config.get(category, propName, defaultValue);
        if (comment != null) prop.setComment(comment);
        return prop.getString();
    }

    private static boolean getBoolean (String category, String propName, String comment, boolean defaultValue) {
        Property prop = config.get(category, propName, defaultValue);
        if (comment != null) prop.setComment(comment);
        return prop.getBoolean(defaultValue);
    }

    private static String getDefaultGameMode() {
        List<String> validGameModes = getValidGameModes();

        String comment = "Valid values: ";
        comment += String.join(", ", validGameModes);

        String gameMode = getString(ConfigCategory.GAME_MODE, ConfigKey.DEFAULT, comment, gameModeDefault);
        if (!validGameModes.contains(gameMode)) {
            NewWorld.logger.log(Level.ERROR, "Invalid game mode: " + gameMode);
            return "survival";
        }
        return gameMode;
    }

    private static List<String> getValidGameModes () {
        List<String> gameModes = new ArrayList<>();
        for (GameType gameMode : GameType.values()) {
            if (gameMode != GameType.NOT_SET) gameModes.add(gameMode.getName());
        }
        return gameModes;
    }

    private static WorldType getDefaultWorldType () {
        String comment = "Valid values: ";
        comment += String.join(", ", getValidWorldTypes());

        String name = getString(ConfigCategory.WORLD_TYPE, ConfigKey.DEFAULT, comment, worldTypeDefault.getWorldTypeName());
        WorldType worldType = WorldType.parseWorldType(name);
        if (worldType == null) {
            NewWorld.logger.log(Level.ERROR, "Invalid world type: " + name);
            return WorldType.DEFAULT;
        }
        return worldType;
    }

    private static List<String> getValidWorldTypes () {
        List<String> worldTypes = new ArrayList<>();
        for (WorldType worldType : WorldType.WORLD_TYPES) {
            if (worldType != null) worldTypes.add(worldType.getWorldTypeName());
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
    }

}
