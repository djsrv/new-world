package djsrv.newworld.handler;

import djsrv.newworld.NewWorld;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;

public class ConfigHandler {

    public static Configuration config;

    public static String defaultGameMode = "survival";
    public static String defaultSeed = "";
    public static boolean defaultGenerateStructures = true;
    public static WorldType defaultWorldType = WorldType.DEFAULT;
    public static boolean defaultAllowCheats = false;
    public static boolean defaultBonusChest = false;
    public static String defaultWorldPreset = "";

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        config.load();
    }

    public static void handleConfig() {
        defaultGameMode = getDefaultGameMode();
        defaultSeed = getString("defaults", "seed.default", "Seed", defaultSeed);
        defaultGenerateStructures = getBoolean("defaults", "generateStructures.default", "Generate Structures", defaultGenerateStructures);
        defaultWorldType = getDefaultWorldType();
        defaultAllowCheats = getBoolean("defaults", "allowCheats.default", "Allow Cheats", defaultAllowCheats);
        defaultBonusChest = getBoolean("defaults", "bonusChest.default", "Bonus Chest", defaultBonusChest);
        defaultWorldPreset = getString("defaults", "worldPreset.default", "World Preset", defaultWorldPreset);

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

    private static String getDefaultGameMode () {
        ArrayList<String> validGameModes = getValidGameModes();

        String comment = "Game Mode\nValid values: ";
        comment += String.join(", ", validGameModes);

        String gameMode = getString("defaults", "gameMode.default", comment, defaultGameMode);
        if (!validGameModes.contains(gameMode)) {
            NewWorld.logger.log(Level.ERROR, "Invalid game mode: " + gameMode);
            return "survival";
        }
        return gameMode;
    }

    private static ArrayList<String> getValidGameModes () {
        ArrayList<String> gameModes = new ArrayList<String>();
        for (GameType gameMode : GameType.values()) {
            if (gameMode != GameType.NOT_SET) gameModes.add(gameMode.getName());
        }
        return gameModes;
    }

    private static WorldType getDefaultWorldType () {
        String comment = "World Type\nValid values: ";
        comment += String.join(", ", getValidWorldTypes());

        String name = getString("defaults", "worldType.default", comment, defaultWorldType.getWorldTypeName());
        WorldType worldType = WorldType.parseWorldType(name);
        if (worldType == null) {
            NewWorld.logger.log(Level.ERROR, "Invalid world type: " + name);
            return WorldType.DEFAULT;
        }
        return worldType;
    }

    private static ArrayList<String> getValidWorldTypes () {
        ArrayList<String> worldTypes = new ArrayList<String>();
        for (WorldType worldType : WorldType.WORLD_TYPES) {
            if (worldType != null) worldTypes.add(worldType.getWorldTypeName());
        }
        return worldTypes;
    }

}
