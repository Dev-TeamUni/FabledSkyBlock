package com.songoda.skyblock.command;

import com.songoda.skyblock.SkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.io.File;

public abstract class SubCommand {

    protected final SkyBlock plugin;
    protected final String info;
    protected final String[] aliases;

    public SubCommand() {
        this.plugin = SkyBlock.getInstance();
        this.info = this.plugin.formatText(this.plugin.getLanguage().getString(this.getInfoMessagePath()));
        this.aliases = this.plugin.getLanguage().getStringList(this.getAliasesPath()).toArray(new String[0]);
    }

    public abstract void onCommandByPlayer(Player player, String[] args);

    public abstract void onCommandByConsole(ConsoleCommandSender sender, String[] args);

    public abstract String getName();

    public abstract String getInfoMessagePath();

    public final String getAliasesPath() {
        return "Command.SubCommand."
            + (this.getClass().getName().contains("admin") ? "Admin." : "")
            + this.getClass().getSimpleName().replace("Command", "");
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract String[] getArguments();

    public String getInfo() {
        return this.info;
    }

    public boolean hasPermission(Permissible toCheck, boolean isAdmin) {
        if (toCheck.hasPermission("fabledskyblock.*"))
            return true;

        return isAdmin
                ? toCheck.hasPermission("fabledskyblock.admin.*") || toCheck.hasPermission("fabledskyblock.admin." + this.getName())
                : toCheck.hasPermission("fabledskyblock.island.*") || toCheck.hasPermission("fabledskyblock.island." + this.getName());
    }

}
