package treechopper.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import treechopper.common.config.ConfigHandler;
import treechopper.core.TreeChopper;

/**
 * Created by Duchy on 8/25/2016.
 */

public class TCGuiConfig extends GuiConfig {

    public TCGuiConfig(GuiScreen guiScreen) {
        super(guiScreen, new ConfigElement(ConfigHandler.config.getCategory("Settings")).getChildElements(), TreeChopper.MODID, false, false, "");
    }
}
