package treechopper.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import treechopper.common.config.ConfigurationHandler;
import treechopper.core.TreeChopper;

public class GuiTCHFactory extends DefaultGuiFactory {

  public GuiTCHFactory() {
    super(TreeChopper.MOD_ID, TreeChopper.MOD_NAME);
  }

  @Override
  public GuiScreen createConfigGui(GuiScreen guiScreen) {
    return new GuiConfig(guiScreen, new ConfigElement(ConfigurationHandler.config.getCategory("Settings")).getChildElements(), modid, false, false, title);
  }
}