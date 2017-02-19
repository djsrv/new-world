package djsrv.newworld.handler;

import djsrv.newworld.gui.GuiCreateWorldCustom;
import djsrv.newworld.lib.LibObfuscation;
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
