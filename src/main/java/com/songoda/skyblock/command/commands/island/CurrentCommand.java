package com.songoda.skyblock.command.commands.island;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.playerdata.PlayerData;
import com.songoda.skyblock.playerdata.PlayerDataManager;
import com.songoda.skyblock.sound.SoundManager;
import com.songoda.skyblock.utils.player.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class CurrentCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        MessageManager messageManager = plugin.getMessageManager();
        SoundManager soundManager = plugin.getSoundManager();

        Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (args.length > 0) {
            if (!args[0].equalsIgnoreCase(player.getName())) {
                if (args.length == 1) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);

                    if (targetPlayer == null) {
                        messageManager.sendMessage(player,
                                configLoad.getString("Command.Island.Current.Offline.Message"));
                        soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);

                        return;
                    }

                    if (!targetPlayer.getName().equals(player.getName())) {
                        PlayerData playerData = playerDataManager.getPlayerData(targetPlayer);

                        if (playerData.getIsland() == null) {
                            messageManager.sendMessage(player,
                                    configLoad.getString("Command.Island.Current.Island.None.Other.Message"));
                        } else {
                            String targetPlayerName = targetPlayer.getName(), ownerPlayerName;
                            targetPlayer = Bukkit.getServer().getPlayer(playerData.getIsland());

                            if (targetPlayer == null) {
                                ownerPlayerName = new OfflinePlayer(playerData.getIsland()).getName();
                            } else {
                                ownerPlayerName = targetPlayer.getName();
                            }

                            messageManager.sendMessage(player,
                                    configLoad.getString("Command.Island.Current.Island.Owner.Other.Message")
                                            .replace("%target", targetPlayerName)
                                            .replace("%owner", ownerPlayerName));
                        }

                        soundManager.playSound(player, CompatibleSound.ENTITY_VILLAGER_YES.getSound(), 1.0F, 1.0F);

                        return;
                    }
                } else if (args.length > 1) {
                    messageManager.sendMessage(player,
                            configLoad.getString("Command.Island.Current.Invalid.Message"));
                    soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);

                    return;
                }
            }
        }

        PlayerData playerData = playerDataManager.getPlayerData(player);

        if (playerData.getIsland() == null) {
            messageManager.sendMessage(player,
                    configLoad.getString("Command.Island.Current.Island.None.Yourself.Message"));
        } else {
            Player targetPlayer = Bukkit.getServer().getPlayer(playerData.getIsland());
            String targetPlayerName;

            if (targetPlayer == null) {
                targetPlayerName = new OfflinePlayer(playerData.getIsland()).getName();
            } else {
                targetPlayerName = targetPlayer.getName();
            }

            messageManager.sendMessage(player,
                    configLoad.getString("Command.Island.Current.Island.Owner.Yourself.Message").replace("%player",
                            targetPlayerName));
        }

        soundManager.playSound(player, CompatibleSound.ENTITY_VILLAGER_YES.getSound(), 1.0F, 1.0F);
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "current";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Island.Current.Info.Message";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
