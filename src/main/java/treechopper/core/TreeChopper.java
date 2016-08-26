package treechopper.core;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duchy on 8/11/2016.
 */

@Mod(modid = "treechop", version = "1.0.2") //, guiFactory = "GuiFactory"
public class TreeChopper {
    public static SimpleNetworkWrapper network;

    // -------------------------- CONFIG STUFF -------------------------- //
    public static boolean ignoreDurability = false;
    public static boolean ignoreDurabilityVar;

    public static boolean plantSapling = false;
    public static boolean plantSaplingVar;

    public static boolean decayLeaves = false;
    public static boolean decayLeavesVar;

    public static double breakSpeed = 1.0;
    public static double breakSpeedVar;

    public static List<String> logTypes = new ArrayList<String>();
    public static List<String> axeTypes = new ArrayList<String>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Property property;

        Configuration config = new Configuration(new File("config/TreeChopper.cfg"));
        config.load();

        ignoreDurability = config.get("Settings", "Ignore axe durability", false).getBoolean();
        ignoreDurabilityVar = ignoreDurability;

        //plantSapling = config.get("Settings", "Plant sapling automatically", false).getBoolean();
        plantSaplingVar = plantSapling;

        //decayLeaves = config.get("Settings", "Decay leaves", false).getBoolean();
        decayLeavesVar = decayLeaves;

        breakSpeed = config.get("Settings", "Break speed [DEFAULT: 1]", 1.0).getDouble();
        breakSpeedVar = breakSpeed;

        property = config.get("Data", "Log Types", new String[]{"tile.log", "tile.log_0", "tile.log_1", "tile.log_2", "tile.log_3", "tile.log_4", "tile.pamCinnamon", "tile.pamPaperbark", "tile.pamMaple"}, "Put new log between < and >");
        logTypes = ImmutableList.copyOf(property.getStringList());

        property = config.get("Data", "Axe Types", new String[]{"item.hatchetWood", "item.hatchetStone", "item.hatchetIron", "item.hatchetGold", "item.hatchetDiamond", "item.mud_axe", "item.psi:psimetalAxe"}, "Put new axe between < and >");
        axeTypes = ImmutableList.copyOf(property.getStringList());

        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
        network.registerMessage(ClientMessage.Handler.class, ClientMessage.class, 0, Side.SERVER);
        network.registerMessage(ServerMessage.Handler.class, ServerMessage.class, 1, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
