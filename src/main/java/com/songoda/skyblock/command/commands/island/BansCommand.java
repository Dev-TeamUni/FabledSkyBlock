package com.songoda.skyblock.command.commands.island;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandRole;
import com.songoda.skyblock.menus.Bans;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.sound.SoundManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class BansCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        MessageManager messageManager = plugin.getMessageManager();
        SoundManager soundManager = plugin.getSoundManager();

        Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        Island island = plugin.getIslandManager().getIsland(player);

        if (island == null) {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.Bans.Owner.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        } else if ((island.hasRole(IslandRole.Operator, player.getUniqueId())
                && plugin.getPermissionManager().hasPermission(island, "Unban", IslandRole.Operator))
                || island.hasRole(IslandRole.Owner, player.getUniqueId())) {
            if (island.getBan().getBans().size() == 0) {
                messageManager.sendMessage(player, configLoad.getString("Command.Island.Bans.Bans.Message"));
                soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
            } else {
                Bans.getInstance().open(player);
                soundManager.playSound(player, CompatibleSound.BLOCK_CHEST_OPEN.getSound(), 1.0F, 1.0F);
            }
        } else {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.Bans.Permission.Message"));
            soundManager.playSound(player,  CompatibleSound.ENTITY_VILLAGER_NO.getSound(), 1.0F, 1.0F);
        }
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "bans";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Island.Bans.Info.Message";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
