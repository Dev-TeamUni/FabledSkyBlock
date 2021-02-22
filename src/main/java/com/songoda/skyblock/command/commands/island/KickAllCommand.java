package com.songoda.skyblock.command.commands.island;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.skyblock.api.event.island.IslandKickEvent;
import com.songoda.skyblock.api.utils.APIUtil;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.island.IslandRole;
import com.songoda.skyblock.island.IslandStatus;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.sound.SoundManager;
import com.songoda.skyblock.utils.world.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;
import java.util.UUID;

public class KickAllCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        MessageManager messageManager = plugin.getMessageManager();
        IslandManager islandManager = plugin.getIslandManager();
        SoundManager soundManager = plugin.getSoundManager();

        Config config = plugin.getFileManager().getConfig(new File(plugin.getDataFolder(), "language.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        Island island = islandManager.getIsland(player);

        if (island == null) {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Owner.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        } else if (island.hasRole(IslandRole.Owner, player.getUniqueId())
                || (island.hasRole(IslandRole.Operator, player.getUniqueId())
                && plugin.getPermissionManager().hasPermission(island, "Kick", IslandRole.Operator))) {
            if (!island.getStatus().equals(IslandStatus.CLOSED)) {
                Set<UUID> islandVisitors = islandManager.getVisitorsAtIsland(island);

                if (islandVisitors.size() == 0) {
                    messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Visitors.Message"));
                    soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
                } else {
                    for (UUID islandVisitorList : islandVisitors) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(islandVisitorList);

                        if (targetPlayer != null &&
                                (targetPlayer.hasPermission("fabledskyblock.bypass.kick")))
                            continue;

                        IslandKickEvent islandKickEvent = new IslandKickEvent(island.getAPIWrapper(),
                                APIUtil.fromImplementation(IslandRole.Visitor),
                                Bukkit.getServer().getOfflinePlayer(islandVisitorList), player);
                        Bukkit.getServer().getPluginManager().callEvent(islandKickEvent);

                        if (!islandKickEvent.isCancelled()) {
                            LocationUtil.teleportPlayerToSpawn(targetPlayer);

                            messageManager.sendMessage(targetPlayer,
                                    configLoad.getString("Command.Island.KickAll.Kicked.Target.Message")
                                            .replace("%player", player.getName()));
                            soundManager.playSound(targetPlayer, CompatibleSound.ENTITY_IRON_GOLEM_ATTACK.getSound(), 1.0F, 1.0F);
                        }
                    }

                    messageManager.sendMessage(player,
                            configLoad.getString("Command.Island.KickAll.Kicked.Sender.Message").replace("%visitors",
                                    "" + islandVisitors.size()));
                    soundManager.playSound(player, CompatibleSound.ENTITY_IRON_GOLEM_ATTACK.getSound(), 1.0F, 1.0F);
                }
            } else {
                messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Closed.Message"));
                soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
            }
        } else {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.KickAll.Role.Message"));
            soundManager.playSound(player,  CompatibleSound.ENTITY_VILLAGER_NO.getSound(), 1.0F, 1.0F);
        }
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "expel";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Island.KickAll.Info.Message";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
