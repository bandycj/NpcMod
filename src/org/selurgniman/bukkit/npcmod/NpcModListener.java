/**case " * 
 */
package org.selurgniman.bukkit.npcmod;

import net.minecraft.server.EntityVillager;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * @author <a href="mailto:e83800@wnco.com">Chris Bandy</a> Created on: Sep 7,
 *         2012
 */
public class NpcModListener implements Listener {

	private static NpcMod plugin = null;

	private static final String CONFIG_MAX_RADIUS_KEY = "maxRadius";
	private static final String CONFIG_ZONES_KEY = "zones";
	private static final String CONFIG_DAMAGE_KEY = CONFIG_ZONES_KEY + ".%1$s.damage";
	private static final String CONFIG_RADIUS_KEY = CONFIG_ZONES_KEY + ".%1$s.radius";
	private static final String CONFIG_LOOT_KEY = CONFIG_ZONES_KEY + ".%1$s.loot";
	private static final String CONFIG_COLOR_KEY = CONFIG_ZONES_KEY + ".%1$s.color";

	public NpcModListener(NpcMod plugin) {
		Configuration config = plugin.getConfig();
		NpcModListener.plugin = plugin;

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!event.isCancelled()) {
			Entity entity = event.getRightClicked();
			CraftItemStack itemStack = (CraftItemStack) event.getPlayer().getItemInHand();
			if (entity instanceof CraftVillager) {
				CraftVillager villager = (CraftVillager) entity;
				EntityVillager evillager = ((CraftVillager) villager).getHandle();
				NBTTagCompound nbt = new NBTTagCompound();
				MerchantRecipeList offers = new MerchantRecipeList();
				offers.a(new MerchantRecipe(itemStack.getHandle(), new net.minecraft.server.ItemStack(
						net.minecraft.server.Item.DIAMOND, 1)));
				offers.a(new MerchantRecipe(new net.minecraft.server.ItemStack(net.minecraft.server.Item.DIAMOND, 1),
						itemStack.getHandle()));
				nbt.setCompound("Offers", offers.a());
				evillager.a(nbt);

				System.out.println("NPC clicked");
			}
		}
	}
}
