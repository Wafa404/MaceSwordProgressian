package com.osman.macesword;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MaceSwordProgression extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> swordLevels = new HashMap<>();
    private final Map<UUID, Integer> maceLevels = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("MaceSwordProgression enabled!");
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        Player killer = victim.getKiller();
        if (killer == null) return;

        ItemStack weapon = killer.getInventory().getItemInMainHand();
        Material type = weapon.getType();

        if (type.name().endsWith("_SWORD")) {
            upgrade(killer, weapon, true);
        } else if (type == Material.MACE) {
            upgrade(killer, weapon, false);
        }
    }

    private void upgrade(Player player, ItemStack weapon, boolean sword) {
        UUID id = player.getUniqueId();
        Map<UUID, Integer> levels = sword ? swordLevels : maceLevels;

        int level = levels.getOrDefault(id, 0) + 1;
        levels.put(id, level);

        weapon.addUnsafeEnchantment(
            Enchantment.SHARPNESS, Math.min(level, 10)
        );

        player.sendMessage("§6Weapon upgraded! §eLevel " + level);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        UUID id = event.getEntity().getUniqueId();
        swordLevels.remove(id);
        maceLevels.remove(id);
    }
            }
