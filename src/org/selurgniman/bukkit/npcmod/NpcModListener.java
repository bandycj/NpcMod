/**case " * 
 */
package org.selurgniman.bukkit.npcmod;

import java.util.List;

import net.minecraft.server.EntityVillager;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.selurgniman.bukkit.npcmod.NpcModModel.MaterialValue;

/**
 * @author <a href="mailto:e83800@wnco.com">Chris Bandy</a> Created on: Sep 7,
 *         2012
 */
public class NpcModListener implements Listener {
	
	private NpcMod plugin = null;
	
	public NpcModListener(NpcMod plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!event.isCancelled()) {
			Entity entity = event.getRightClicked();
			CraftItemStack tradeItem = (CraftItemStack) event.getPlayer().getItemInHand();
			if (entity instanceof CraftVillager && tradeItem != null) {
				CraftVillager villager = (CraftVillager) entity;
				EntityVillager evillager = ((CraftVillager) villager).getHandle();
				NBTTagCompound nbt = new NBTTagCompound();

				Material material = tradeItem.getType();
				int tradeItemValue = plugin.getModel().getItemValue(tradeItem);
				
				if (tradeItemValue > 0){
					MerchantRecipeList offers = new MerchantRecipeList();
					List<MaterialValue> similarMaterials = plugin.getModel().getSimilarPriced(material);
					for (MaterialValue offer : similarMaterials) {
						int tradeAmount = 0;
						int offerAmount = 0;
	
						if (tradeItemValue > offer.getValue()) {
							tradeAmount = 1;
							offerAmount = tradeItemValue / offer.getValue();
						} else if (tradeItemValue < offer.getValue()) {
							tradeAmount = offer.getValue() / tradeItemValue;
							offerAmount = 1;
						} else {
							tradeAmount = 1;
							offerAmount = 1;
						}
						CraftItemStack offerItemStack = new CraftItemStack(offer.getMaterial(), offerAmount);
						CraftItemStack tradeItemStack = new CraftItemStack(material, tradeAmount);
						offers.a(new MerchantRecipe(offerItemStack.getHandle(), tradeItemStack.getHandle()));
					}
	
					nbt.setCompound("Offers", offers.a());
					evillager.a(nbt);
				}
			}
		}
	}
}
