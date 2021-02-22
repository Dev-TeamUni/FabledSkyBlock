package com.songoda.skyblock.command.commands.island;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.invite.Invite;
import com.songoda.skyblock.invite.InviteManager;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.island.IslandRole;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.sound.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class CancelCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        MessageManager messageManager = plugin.getMessageManager();
        IslandManager islandManager = plugin.getIslandManager();
        InviteManager inviteManager = plugin.getInviteManager();
        SoundManager soundManager = plugin.getSoundManager();

        Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (args.length == 1) {
            Island island = islandManager.getIsland(player);

            if (island == null) {
                messageManager.sendMessage(player, configLoad.getString("Command.Island.Cancel.Owner.Message"));
                soundManager.playSound(player,  CompatibleSound.ENTITY_VILLAGER_NO.getSound(), 1.0F, 1.0F);
            } else if (island.hasRole(IslandRole.Owner, player.getUniqueId())
                    || island.hasRole(IslandRole.Operator, player.getUniqueId())) {
                String playerName = args[0];
                Player targetPlayer = Bukkit.getServer().getPlayer(playerName);

                if (targetPlayer == null) {
                    messageManager.sendMessage(player, configLoad.getString("Command.Island.Cancel.Offline.Message"));
                    soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
                } else if (island.hasRole(IslandRole.Member, targetPlayer.getUniqueId())
                        || island.hasRole(IslandRole.Operator, targetPlayer.getUniqueId())
                        || island.hasRole(IslandRole.Owner, targetPlayer.getUniqueId())) {
                    messageManager.sendMessage(player, configLoad.getString("Command.Island.Cancel.Member.Message"));
                    soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
                } else if (inviteManager.hasInvite(targetPlayer.getUniqueId())) {
                    Invite invite = inviteManager.getInvite(targetPlayer.getUniqueId());

                    if (invite.getOwnerUUID().equals(island.getOwnerUUID())) {
                        inviteManager.removeInvite(targetPlayer.getUniqueId());

                        messageManager.sendMessage(player,
                                configLoad.getString("Command.Island.Cancel.Cancelled.Message").replace("%player",
                                        targetPlayer.getName()));
                        soundManager.playSound(player, CompatibleSound.ENTITY_GENERIC_EXPLODE.getSound(), 10.0F, 10.0F);
                    } else {
                        messageManager.sendMessage(player,
                                configLoad.getString("Command.Island.Cancel.Invited.Message"));
                        soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
                    }
                } else {
                    messageManager.sendMessage(player, configLoad.getString("Command.Island.Cancel.Invited.Message"));
                    soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
                }
            } else {
                messageManager.sendMessage(player, configLoad.getString("Command.Island.Cancel.Permission.Message"));
                soundManager.playSound(player,  CompatibleSound.ENTITY_VILLAGER_NO.getSound(), 1.0F, 1.0F);
            }
        } else {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.Cancel.Invalid.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        }
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "cancel";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Island.Cancel.Info.Message";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
