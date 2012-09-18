/**
 * 
 */
package org.selurgniman.bukkit.npcmod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

/**
 * @author <a href="mailto:e83800@wnco.com">Chris Bandy</a> Created on: Sep 17,
 *         2012
 */
public class NpcModModel {
	private final static String CONFIG_VALUES_KEY = "values";
	private final NpcMod plugin;
	private final TreeMap<Material, Integer> itemMap = new TreeMap<Material, Integer>();

	public NpcModModel(NpcMod plugin) {
		this.plugin = plugin;
		loadMap();
	}

	private void loadMap() {
		Configuration config = plugin.getConfig();
		List<String> materials = config.getStringList(CONFIG_VALUES_KEY);
		for (String materialName : materials) {
			try {
				Material material = Material.getMaterial(materialName);
				itemMap.put(material, config.getInt(CONFIG_VALUES_KEY + "." + materialName));
			} catch (IllegalArgumentException ex) {
				// Item not found, swallow exception and proceed.
			}
		}
	}

	public Integer getItemValue(Material material) {
		return itemMap.get(material);
	}

	public List<ItemStack> getSimilarPriced(ItemStack itemStack) {
		ArrayList<ItemStack> returnList = new ArrayList<ItemStack>();

		int minValue = itemMap.get(itemStack.getType());
		int maxValue = itemStack.getMaxStackSize() * minValue;

		for (Entry<Material, Integer> entry : itemMap.entrySet()) {
			if (entry.getKey() != itemStack.getType()) {
				int materialMin = entry.getValue();
				int materialMax = entry.getKey().getMaxStackSize() * materialMin;
				if (materialMax <= maxValue && materialMin >= minValue) {
					
				}
			}
		}

		return returnList;
	}
}
