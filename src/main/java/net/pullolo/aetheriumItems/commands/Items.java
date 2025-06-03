package net.pullolo.aetheriumItems.commands;

import net.pullolo.aetheriumItems.gui.MainMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Items extends Command{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!cmd.getName().equalsIgnoreCase("items")){
            return false;
        }
        if (!(sender instanceof Player p)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        new MainMenu(p).open();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String s, @NotNull String @NotNull [] args) {
        return new ArrayList<>();
    }
}

