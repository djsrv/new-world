package djsrv.newworld;

import djsrv.newworld.handler.ConfigHandler;
import djsrv.newworld.handler.GuiCreateWorldHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NewWorld.MOD_ID, version = NewWorld.VERSION, clientSideOnly = true)
public class NewWorld {

    public static final String MOD_ID = "newworld";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(GuiCreateWorldHandler.class);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ConfigHandler.handleConfig();
    }

}
