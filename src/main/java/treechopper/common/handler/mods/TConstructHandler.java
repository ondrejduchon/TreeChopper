package treechopper.common.handler.mods;

import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duchy on 9/23/2016.
 */

@Optional.Interface(iface = "", modid = "forestry")
public class TConstructHandler {
    public static List<String> tcAxes = new ArrayList<String>() {{
        add("item.tconstruct.mattock");
        add("item.tconstruct.lumberaxe");
    }};
}
