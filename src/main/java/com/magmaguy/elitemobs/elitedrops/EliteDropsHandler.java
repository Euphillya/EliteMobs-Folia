/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmaguy.elitemobs.elitedrops;

import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.LootCustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.magmaguy.elitemobs.ChatColorConverter.chatColorConverter;
import static org.bukkit.Bukkit.getLogger;

/**
 * Created by MagmaGuy on 29/11/2016.
 */
public class EliteDropsHandler implements Listener {

    public static List<ItemStack> lootList = new ArrayList();
    public static HashMap<ItemStack, List<PotionEffect>> potionEffectItemList = new HashMap();
    public static HashMap<Integer, List<ItemStack>> rankedItemStacks = new HashMap<>();

    private LootCustomConfig lootCustomConfig = new LootCustomConfig();

    public void superDropParser() {

        //TODO: use ItemRankHandler.guessItemRank(material, enchantmentTotal)
        //TODO: split class up

        List<String> lootCount = lootCounter();

        for (String lootEntry : lootCount) {

            StringBuilder path = new StringBuilder();
            path.append(lootEntry);

            String previousPath = path.toString();


            String itemType = itemTypeHandler(previousPath);
            Bukkit.getLogger().info("Adding: " + previousPath);
            String itemName = itemNameHandler(previousPath);

            List itemEnchantments = itemEnchantmentHandler(previousPath);
            List potionEffects = itemPotionEffectHandler(previousPath);

            ItemStack itemStack = new ItemStack(Material.getMaterial(itemType), 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemName);

            int enchantmentCount = 0;

            if (itemEnchantments != null) {

                for (Object object : itemEnchantments) {

                    String string = object.toString();

                    String[] parsedString = string.split(",");

                    String enchantmentName = parsedString[0];
                    Enchantment enchantmentType = Enchantment.getByName(enchantmentName);

                    int enchantmentLevel = Integer.parseInt(parsedString[1]);

                    enchantmentCount += enchantmentLevel;

                    itemMeta.addEnchant(enchantmentType, enchantmentLevel, true);

                }

            }


            List<PotionEffect> parsedPotionEffect = new ArrayList();

            //Add potion effects to a separate list to reduce i/o operations
            if (potionEffects != null) {

                for (Object object : potionEffects) {

                    String string = object.toString();

                    String[] parsedString = string.split(",");

                    //this is a really bad way of doing things, two wrongs make a right
                    if (parsedString.length % 2 != 0) {

                        getLogger().info("Your item " + itemName + " has a problematic potions effect entry.");

                    }

                    int potionEffectAmplifier = Integer.parseInt(parsedString[1]);
                    enchantmentCount += potionEffectAmplifier * 15;

                }

            }

            int itemRank = ItemRankHandler.guessItemRank(itemStack.getType(), enchantmentCount);

            List itemLore = itemLoreHandler(previousPath, itemRank);
            itemMeta.setLore(itemLore);

            if (ConfigValues.defaultConfig.getBoolean("Use MMORPG colors for custom items") && ChatColor.stripColor(itemName).equals(itemName)) {

                double rankProgression = ConfigValues.defaultConfig.getDouble("Increase MMORPG color rank every X levels X=");

                if (Math.floor(itemRank / rankProgression) >= 5) {

                    itemMeta.setDisplayName(ChatColor.GOLD + itemMeta.getDisplayName());

                    List list = new ArrayList();

                    for (String string : itemMeta.getLore()) {

                        String coloredString = ChatColor.GOLD + "" + ChatColor.ITALIC + string;
                        list.add(coloredString);

                    }

                    itemMeta.setLore(list);

                } else if (Math.floor(itemRank / rankProgression) == 4) {

                    itemMeta.setDisplayName(ChatColor.DARK_PURPLE + itemMeta.getDisplayName());

                    List list = new ArrayList();

                    for (String string : itemMeta.getLore()) {

                        String coloredString = ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + string;
                        list.add(coloredString);

                    }

                    itemMeta.setLore(list);

                } else if (Math.floor(itemRank / rankProgression) == 3) {

                    itemMeta.setDisplayName(ChatColor.BLUE + itemMeta.getDisplayName());

                    List list = new ArrayList();

                    for (String string : itemMeta.getLore()) {

                        String coloredString = ChatColor.BLUE + "" + ChatColor.ITALIC + string;
                        list.add(coloredString);

                    }

                    itemMeta.setLore(list);

                } else if (Math.floor(itemRank / rankProgression) == 2) {

                    itemMeta.setDisplayName(ChatColor.GREEN + itemMeta.getDisplayName());

                    List list = new ArrayList();

                    for (String string : itemMeta.getLore()) {

                        String coloredString = ChatColor.GREEN + "" + ChatColor.ITALIC + string;
                        list.add(coloredString);

                    }

                    itemMeta.setLore(list);

                } else if (Math.floor(itemRank / rankProgression) == 1) {

                    itemMeta.setDisplayName(ChatColor.WHITE + itemMeta.getDisplayName());

                    List list = new ArrayList();

                    for (String string : itemMeta.getLore()) {

                        String coloredString = ChatColor.WHITE + "" + ChatColor.ITALIC + string;
                        list.add(coloredString);

                    }

                    itemMeta.setLore(list);

                } else if (Math.floor(itemRank / rankProgression) == 0) {

                    itemMeta.setDisplayName(ChatColor.GRAY + itemMeta.getDisplayName());

                    List list = new ArrayList();

                    for (String string : itemMeta.getLore()) {

                        String coloredString = ChatColor.GRAY + "" + ChatColor.ITALIC + string;
                        list.add(coloredString);

                    }

                    itemMeta.setLore(list);

                }

            }

            itemStack.setItemMeta(itemMeta);

            if (potionEffects != null) {

                for (Object object : potionEffects) {

                    String string = object.toString();

                    String[] parsedString = string.split(",");

                    String potionEffectTypeString = parsedString[0];
                    PotionEffectType potionEffectType = PotionEffectType.getByName(potionEffectTypeString);

                    //this is a really bad way of doing things, two wrongs make a right
                    if (parsedString.length % 2 != 0) {

                        getLogger().info("Your item " + itemName + " has a problematic potions effect entry.");

                    }

                    int potionEffectAmplifier = Integer.parseInt(parsedString[1]);

                    PotionEffect potionEffect = new PotionEffect(potionEffectType, 40, potionEffectAmplifier);

                    parsedPotionEffect.add(potionEffect);

                }

                potionEffectItemList.put(itemStack, parsedPotionEffect);

            }

            lootList.add(itemStack);

            rankedItemMapCreator(itemRank, itemStack);

        }

    }


    public List<String> lootCounter() {

        List<String> lootCount = new ArrayList();

        for (String configIterator : lootCustomConfig.getLootConfig().getKeys(true)) {

            int dotCount = 0;

            if (configIterator.contains("Loot.")) {

                for (int i = 0; i < configIterator.length(); i++) {

                    if (configIterator.charAt(i) == '.') {

                        dotCount++;

                    }

                }

                if (dotCount == 1) {

                    lootCount.add(configIterator);


                }

            }

        }

        return lootCount;

    }

    public String automatedStringBuilder(String previousPath, String append) {

        StringBuilder automatedStringBuilder = new StringBuilder();

        automatedStringBuilder.append(previousPath);
        automatedStringBuilder.append(".");
        automatedStringBuilder.append(append);

        String path = automatedStringBuilder.toString();

        return path;

    }

    public String itemTypeHandler(String previousPath) {

        String path = automatedStringBuilder(previousPath, "Item Type");

        String itemType = lootCustomConfig.getLootConfig().getString(path);

        return itemType;

    }

    public String itemNameHandler(String previousPath) {

        String path = automatedStringBuilder(previousPath, "Item Name");

        String itemName = chatColorConverter(lootCustomConfig.getLootConfig().getString(path));

        return itemName;

    }

    public List itemLoreHandler(String previousPath, int itemRank) {

        String path = automatedStringBuilder(previousPath, "Item Lore");

        List<String> itemLore = (List<String>) lootCustomConfig.getLootConfig().getList(path);

        if (!ConfigValues.defaultConfig.getBoolean("Show item rank on custom item drops") && (itemLore == null || itemLore.isEmpty()) &&
                !ConfigValues.economyConfig.getBoolean("Enable economy")) {

            return itemLore;

        }

        List<String> newList = new ArrayList<>();

        if (ConfigValues.defaultConfig.getBoolean("Show item rank on custom item drops")) {

            newList.add("Rank " + itemRank + " Elite Mob Drop");

        }

        if (itemLore != null && !itemLore.isEmpty()) {

            for (String string : itemLore) {

                if (string != null && !string.isEmpty()) {

                    newList.add(chatColorConverter(string));

                }

            }

        }

        if (ConfigValues.economyConfig.getBoolean("Enable economy")) {

            String lore3;

            lore3 = "Worth " + itemRank * ConfigValues.economyConfig.getDouble("Tier price progression") + " " +
                    ConfigValues.economyConfig.getString("Currency name");

            newList.add(lore3);

        }

        if (newList == null || newList.isEmpty()) {

            return itemLore;

        }

        return newList;

    }

    public List itemEnchantmentHandler(String previousPath) {

        String path = automatedStringBuilder(previousPath, "Enchantments");

        List enchantments = lootCustomConfig.getLootConfig().getList(path);

        return enchantments;

    }

    public List itemPotionEffectHandler(String previousPath) {

        String path = automatedStringBuilder(previousPath, "Potion Effects");

        List potionEffects = lootCustomConfig.getLootConfig().getList(path);

        return potionEffects;

    }

    public void rankedItemMapCreator(int itemPower, ItemStack itemStack) {

        if (rankedItemStacks.get(itemPower) == null) {

            List<ItemStack> list = new ArrayList<>();

            list.add(itemStack);

            rankedItemStacks.put(itemPower, list);

        } else {

            List<ItemStack> list = rankedItemStacks.get(itemPower);

            list.add(itemStack);

            rankedItemStacks.put(itemPower, list);

        }

    }

}
