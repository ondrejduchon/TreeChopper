package treechopper.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duchy on 8/25/2016.
 */
public class TCGuiConfig extends GuiConfig {

    public TCGuiConfig(GuiScreen guiScreen) {
        super(guiScreen, TCGuiConfig.configElements(), "treechop", false, false, "");
    }

    private static List<IConfigElement> configElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        return list;
    }
}
