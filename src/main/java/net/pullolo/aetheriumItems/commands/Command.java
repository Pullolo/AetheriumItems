package net.pullolo.aetheriumItems.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class Command implements CommandExecutor, TabCompleter {
    protected void addToCompletion(String arg, String userInput, List<String> completion){
        if (arg.regionMatches(true, 0, userInput, 0, userInput.length()) || userInput.length() == 0){
            completion.add(arg);
        }
    }
}
