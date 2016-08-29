package treechopper.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import treechopper.core.ConfigHandler;
import treechopper.core.TreeChopper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duchy on 8/25/2016.
 */
public class TCGuiConfig extends GuiConfig {

    public TCGuiConfig(GuiScreen guiScreen) {
        super(guiScreen, new ConfigElement(ConfigHandler.config.getCategory("Settings")).getChildElements(), TreeChopper.MODID, false, false, "");
    }
}
