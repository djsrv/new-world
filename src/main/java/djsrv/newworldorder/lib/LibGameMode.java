package djsrv.newworldorder.lib;

import java.util.ArrayList;
import java.util.List;

public class LibGameMode {

    public static final List<String> GAME_MODES = new ArrayList<>();

    static {
        GAME_MODES.add("survival");
        GAME_MODES.add("hardcore");
        GAME_MODES.add("creative");
    }

}
