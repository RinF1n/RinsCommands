package com.myname.mymodid;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myname.mymodid.commands.CommandBack;
import com.myname.mymodid.commands.CommandHome;
import com.myname.mymodid.commands.CommandSetHome;
import com.myname.mymodid.helper.Home;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class MyMod {

    public static final Logger LOG = LogManager.getLogger(Tags.MODID);
    public static HashMap<EntityPlayer, Location> BackPositions = new HashMap<EntityPlayer, Location>();
    public static File worldDir;
    public static File modFolderDir; // it's inside the worldDir
    public static File HomesFile;
    @SidedProxy(clientSide = "com.myname.mymodid.ClientProxy", serverSide = "com.myname.mymodid.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        MinecraftForge.EVENT_BUS.register(new MyEventHandler());
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {

        worldDir = DimensionManager.getCurrentSaveRootDirectory();
        modFolderDir = new File(worldDir, "RinsCommands");
        if (!modFolderDir.exists()) modFolderDir.mkdir();

        HomesFile = new File(modFolderDir, "homes.txt");
        Home.homes = new ArrayList<Home>();

        if (!HomesFile.exists()) {
            try {
                HomesFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Home.readHomes(HomesFile);
        } ;

        event.registerServerCommand(new CommandBack());
        event.registerServerCommand(new CommandSetHome());
        event.registerServerCommand(new CommandHome());

        proxy.serverStarting(event);
    }

    @Mod.EventHandler

    public void serverStopping(FMLServerStoppingEvent event) {
        Home.writeHomes(HomesFile);
    }

}
