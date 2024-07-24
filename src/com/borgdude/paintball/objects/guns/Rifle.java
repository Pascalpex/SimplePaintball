package com.borgdude.paintball.objects.guns;

import com.borgdude.paintball.managers.PaintballManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.borgdude.paintball.Main;
import com.borgdude.paintball.objects.Gun;
import com.borgdude.paintball.utils.MathUtil;

public class Rifle extends Gun {

    private Main plugin;
    private PaintballManager paintballManager;

    public Rifle(Main p) {
        this.plugin = p;
        this.paintballManager = p.getPaintballManager();
    }

    @Override
    public ItemStack getLobbyItem() {
        ItemStack is = new ItemStack(Material.RED_WOOL);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Rifle");
        is.setItemMeta(im);
        return is;
    }

    @Override
    public ItemStack getInGameItem() {
        ItemStack is = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RED + "PaintBall Rifle");
        is.setItemMeta(im);
        return is;
    }

    @Override
    public int getCooldown() {
        return 4;
    }

    @Override
    public void fire(Player player) {

        float accuracy = 0.1F;

        final Snowball snowball = player.launchProjectile(Snowball.class);
        final Vector velocity = player.getLocation().getDirection().multiply(1.5).add(MathUtil.getRandomVector(accuracy));// set the velocity variable
        snowball.setVelocity(velocity);

        paintballManager.getProjectiles().put(snowball.getEntityId(), new BukkitRunnable() {

            @Override
            public void run() {
                snowball.setVelocity(velocity);
            }
        }.runTaskTimer(plugin, 1, 1));

        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2, 0.5f);

    }

    @Override
    public void onHit(Player player, Snowball ball, Block block, Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName() {
        return "Rifle";
    }

}
