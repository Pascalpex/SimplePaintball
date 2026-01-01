package com.borgdude.paintball.utils;

import com.borgdude.paintball.Main;
import com.borgdude.paintball.objects.Arena;
import com.borgdude.paintball.objects.ArenaState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class JoinInventory {

    private static final int ARENA_SELECTOR_ROWS = 3;
    private static final String ARENA_SELECTOR_TITLE = "<gold><bold>Paintball Arenas";
    public static final List<Material> ARENA_MATERIALS = List.of(Material.GOLDEN_HOE, Material.IRON_SHOVEL, Material.DIAMOND_HOE);

    private Inventory arenaSelector;
    private final Main plugin;

    public JoinInventory(Main plugin) {
        this.plugin = plugin;
        buildArenaSelector();
    }

    public void buildArenaItem(Arena arena) {
        int arenaIndex = plugin.getArenaManager().getActivatedArenas().indexOf(arena);
        ItemStack arenaItem = new ItemStack(getArenaMaterial(arenaIndex), 1);
        ItemMeta arenaMeta = arenaItem.getItemMeta();
        arenaMeta.itemName(Component.text(arena.getTitle()).color(NamedTextColor.GREEN));
        arenaMeta.lore(List.of(
                Component.text("Klicke zum beitreten").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false),
                (arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS || arena.getArenaState() == ArenaState.STARTING) ? Component.text("Spieler: ").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false).append(Component.text(arena.getPlayers().size() + "/" + arena.getMaxPlayers()).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)) : Component.text("Spiel läuft").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        arenaItem.setItemMeta(arenaMeta);

        arenaSelector.setItem(arenaIndex + 10, arenaItem);
    }

    private Material getArenaMaterial(int index) {
        return ARENA_MATERIALS.get(index % ARENA_MATERIALS.size());
    }

    public void buildArenaSelector() {
        arenaSelector = Bukkit.createInventory(null, ARENA_SELECTOR_ROWS * 9, MiniMessage.miniMessage().deserialize(ARENA_SELECTOR_TITLE));

        ItemStack spacer = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta spacerMeta = spacer.getItemMeta();
        spacerMeta.itemName(Component.text("").color(NamedTextColor.GRAY));
        spacer.setItemMeta(spacerMeta);

        for(int i = 0; i < arenaSelector.getSize(); i++) {
            arenaSelector.setItem(i, spacer);
        }

        plugin.getArenaManager().getActivatedArenas().forEach(this::buildArenaItem);
    }

    public boolean isArenaSelector(Inventory inventory) {
        return arenaSelector == inventory;
    }

    public void openArenaSelector(Player player) {
        player.openInventory(arenaSelector);
    }

}
