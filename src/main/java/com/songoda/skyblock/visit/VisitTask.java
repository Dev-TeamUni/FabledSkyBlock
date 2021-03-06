package com.songoda.skyblock.visit;

import com.songoda.skyblock.playerdata.PlayerData;
import com.songoda.skyblock.playerdata.PlayerDataManager;
import org.bukkit.scheduler.BukkitRunnable;

public class VisitTask extends BukkitRunnable {

    private final PlayerDataManager playerDataManager;

    public VisitTask(PlayerDataManager playerManager) {
        this.playerDataManager = playerManager;
    }

    @Override
    public void run() {
        for (PlayerData playerData : playerDataManager.getPlayerData().values()) {
            if (playerData.getIsland() != null) {
                playerData.setVisitTime(playerData.getVisitTime() + 1);
            }
        }
    }
}
