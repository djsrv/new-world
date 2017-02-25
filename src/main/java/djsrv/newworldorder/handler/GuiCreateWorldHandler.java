package djsrv.newworldorder.handler;

import djsrv.newworldorder.gui.GuiCreateWorldCustom;
import djsrv.newworldorder.lib.LibObfuscation;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class GuiCreateWorldHandler {

    @SubscribeEvent
    public static void handleGuiCreateWorld(GuiOpenEvent event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiCreateWorld && !(gui instanceof GuiCreateWorldCustom)) {
            GuiScreen parentScreen = ReflectionHelper.getPrivateValue(GuiCreateWorld.class, (GuiCreateWorld) gui, LibObfuscation.PARENT_SCREEN);
            event.setGui(new GuiCreateWorldCustom(parentScreen));
        }
    }

}
