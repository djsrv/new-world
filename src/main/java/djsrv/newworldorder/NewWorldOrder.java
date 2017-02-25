package djsrv.newworldorder;

import djsrv.newworldorder.handler.ConfigHandler;
import djsrv.newworldorder.handler.GuiCreateWorldHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NewWorldOrder.MOD_ID, version = NewWorldOrder.VERSION, clientSideOnly = true)
public class NewWorldOrder {

    public static final String MOD_ID = "newworldorder";
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
