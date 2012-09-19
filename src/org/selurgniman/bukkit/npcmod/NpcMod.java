/**
 * 
 */
package org.selurgniman.bukkit.npcmod;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author <a href="mailto:selurgniman@selurgniman.org">Selurgniman</a>
 * 
 */
public class NpcMod extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");
	private static boolean isDebug = true;
	private NpcModModel model = null;

	@Override
	public void onDisable() {
		log("disabled.");
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();

		isDebug = getConfig().getBoolean("debug");
		Message.populateMessages(getConfig());
		
		model = new NpcModModel(this);
		registerEvents();
		registerCommands();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		log(pdfFile.getName() + "version " + pdfFile.getVersion() + "is enabled!");
	}

	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new NpcModListener(this), this);

		log("registered event listener.");
	}
	
	public void registerCommands(){
//		MobModCommandExecutor commandExecutor = new MobModCommandExecutor();
//		this.getCommand("mm").setExecutor(commandExecutor);
//		this.getCommand("mmr").setExecutor(commandExecutor);
		
		log("registered commands.");
	}
	
	public NpcModModel getModel(){
		return this.model;
	}

	public static void messagePlayer(Player player, String message) {
		player.sendMessage(Message.PREFIX + " " + message);
	}
	
	public static void log(String message) {
		log.info(Message.PREFIX + " " + message);
	}
	
	public void debug(String message) {
		if (isDebug) {
			for (World world : this.getServer().getWorlds()) {
				for (Player player : world.getPlayers()) {
					messagePlayer(player, message);
				}
			}
			log("DEBUG: " + message);
		}
	}
	
	@Override
	public String toString(){
		return "NpcModModel";
	}
}
