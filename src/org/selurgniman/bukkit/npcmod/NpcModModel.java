/**
 * 
 */
package org.selurgniman.bukkit.npcmod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 * @author <a href="mailto:e83800@wnco.com">Chris Bandy</a> Created on: Sep 17,
 *         2012
 */
public class NpcModModel {
	private final static String CONFIG_VALUES_KEY = "values";
	private final static Pattern TOOL_WEAPON_PATTERN = Pattern.compile(".+(SPADE|PICKAXE|AXE|SWORD|HOE)");
	
	private final NpcMod plugin;
	private final ArrayList<MaterialValue> materialValues;

	public NpcModModel(NpcMod plugin) {
		this.plugin = plugin;
		this.materialValues = new ArrayList<MaterialValue>();
		loadMap();
	}

	private void loadMap() {
		try {
			Configuration config = plugin.getConfig();
			Set<String> materials = config.getConfigurationSection(CONFIG_VALUES_KEY).getKeys(false);
			for (String materialName : materials) {
				try {
					Material material = Material.getMaterial(materialName);
					int value = config.getInt(CONFIG_VALUES_KEY + "." + materialName);
					NpcMod.log(material + ":" + value);
					materialValues.add(new MaterialValue(material,value));
				} catch (IllegalArgumentException ex) {
					// Item not found, swallow exception and proceed.
				}
			}

			for (Material material : Material.values()) {
				if (!hasMaterial(material)) {
					calculateCost(material);
				}
			}
			Collections.sort(materialValues, new Comparator<MaterialValue>(){
				@Override
				public int compare(MaterialValue o1, MaterialValue o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});
			
			NpcMod.log("loaded " + materialValues.size() + " material values.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculateCost(Material material) {
		List<Recipe> recipes = plugin.getServer().getRecipesFor(new ItemStack(material));
		for (Recipe recipe : recipes) {
			List<ItemStack> ingredients = new ArrayList<ItemStack>();

			if (recipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
				ingredients = new ArrayList<ItemStack>();
				for (String item : shapedRecipe.getShape()) {
					for (Character character : item.toCharArray()) {
						ItemStack itemStack = shapedRecipe.getIngredientMap().get(character);
						if (itemStack != null && itemStack.getType() != Material.FIRE) {
							ingredients.add(itemStack);
						}
					}
				}

			} else if (recipe instanceof ShapelessRecipe) {
				ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
				ingredients = shapelessRecipe.getIngredientList();
			}

			int total = 0;
			for (ItemStack ingredient : ingredients) {
				Material recipeItem = ingredient.getType();
				if (!hasMaterial(recipeItem)) {
					calculateCost(recipeItem);
				}
				
				try{
					total += getMaterialValue(recipeItem);
				} catch (NullPointerException ex){
					plugin.debug("Missing material: "+recipeItem);
				}
			}
			
			materialValues.add(new MaterialValue(material, total));
			break;
		}
	}

	public List<MaterialValue> getSimilarPriced(Material material) {
		List<MaterialValue> returnList = new ArrayList<MaterialValue>();

		int minOfferValue = getMaterialValue(material);
		if (minOfferValue > -1){
			int maxOfferValue = material.getMaxStackSize() * minOfferValue;
			for (MaterialValue materialValue : materialValues) {
				if (materialValue.getMaterial() != material) {
					int minTradeValue = materialValue.getValue();
					int maxTradeValue = materialValue.getMaterial().getMaxStackSize() * minTradeValue;
					if (maxOfferValue >= minTradeValue && minOfferValue <= maxTradeValue) {
						returnList.add(materialValue);
					}
				}
			}
		}
		
		return returnList;
	}
	
	private boolean hasMaterial(Material material){
		return (getMaterialValue(material) > -1);
	}
	
	private Integer getMaterialValue(Material material) {
		Integer value = -1;
		
		for (MaterialValue materialValue:materialValues){
			if (materialValue.getMaterial() == material){
				return materialValue.getValue();
			}
		}
		
		return value;
	}
	
	public Integer getItemValue(ItemStack item){
		Material material = item.getType();
		int amount = getMaterialValue(material);
		if (TOOL_WEAPON_PATTERN.matcher(material.toString()).matches()){
			float maxDurability = material.getMaxDurability();
			float itemDurability = item.getDurability();
			float percentDamaged = (maxDurability-itemDurability)/maxDurability;
			amount = (int)(amount * percentDamaged);
		}
		return amount;
	}
	
	public class MaterialValue {
		private final Material material;
		private final Integer value;
		
		public MaterialValue(Material material, Integer value){
			this.material = material;
			this.value = value;
		}

		/**
		 * @return the material
		 */
		public Material getMaterial() {
			return material;
		}

		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}
	}
}
