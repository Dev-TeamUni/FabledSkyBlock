package com.songoda.skyblock.gui.permissions;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.skyblock.SkyBlock;
import com.songoda.skyblock.config.FileManager;
import com.songoda.skyblock.island.IslandRole;
import com.songoda.skyblock.permission.BasicPermission;
import com.songoda.skyblock.permission.PermissionManager;
import com.songoda.skyblock.permission.PermissionType;
import com.songoda.skyblock.sound.SoundManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GuiAdminPermissions extends Gui {

    private final PermissionManager permissionManager;
    private final SoundManager soundManager;
    private final IslandRole role;
    private final FileConfiguration configLoad;
    private final FileManager.Config settingsConfig;
    private final FileConfiguration settingsConfigLoad;
    private final Gui returnGui;

    public GuiAdminPermissions(SkyBlock plugin, IslandRole role, Gui returnGui) {
        super(6, returnGui);
        this.permissionManager = plugin.getPermissionManager();
        this.soundManager = plugin.getSoundManager();
        this.role = role;
        this.returnGui = returnGui;
        this.configLoad = plugin.getFileManager()
                .getConfig(new File(plugin.getDataFolder(), "language.yml")).getFileConfiguration();
        settingsConfig = plugin.getFileManager()
                .getConfig(new File(plugin.getDataFolder(), "settings.yml"));
        settingsConfigLoad = settingsConfig.getFileConfiguration();
        setTitle(TextUtils.formatText(configLoad.getString("Menu.Settings." + role.name() + ".Title")));
        setDefaultItem(null);
        paint();
    }

    public void paint() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 0, 5, 9, null);

        setButton(0, GuiUtils.createButtonItem(CompatibleMaterial.OAK_FENCE_GATE,
                TextUtils.formatText(configLoad.getString("Menu.Settings.Categories.Item.Exit.Displayname"))), (event) -> {
            soundManager.playSound(event.player, CompatibleSound.BLOCK_CHEST_CLOSE.getSound(), 1f, 1f);
            guiManager.showGUI(event.player, returnGui);
        });

        setButton(8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_FENCE_GATE,
                TextUtils.formatText(configLoad.getString("Menu.Settings.Categories.Item.Exit.Displayname"))), (event) -> {
            soundManager.playSound(event.player, CompatibleSound.BLOCK_CHEST_CLOSE.getSound(), 1f, 1f);
            guiManager.showGUI(event.player, returnGui);
        });

        List<BasicPermission> permissions = permissionManager.getPermissions().stream()
                .filter(p -> p.getType() == getType(role) &&
                    SkyBlock.getInstance().getSettings().getBoolean("Settings." + role.name() + "." + p.getName() + ".Enabled", true))
                .collect(Collectors.toList());
        double itemCount = permissions.size();
        this.pages = (int) Math.max(1, Math.ceil(itemCount / 36));

        if (page != 1)
            setButton(5, 2, GuiUtils.createButtonItem(CompatibleMaterial.ARROW,
                    TextUtils.formatText(configLoad.getString("Menu.Settings.Categories.Item.Last.Displayname"))),
                    (event) -> {
                        page--;
                        paint();
                    });

        if (page != pages)
            setButton(5, 6, GuiUtils.createButtonItem(CompatibleMaterial.ARROW,
                    TextUtils.formatText(configLoad.getString("Menu.Settings.Categories.Item.Next.Displayname"))),
                    (event) -> {
                        page++;
                        paint();
                    });

        for (int i = 9; i < 45; i++) {
            int current = ((page - 1) * 36) - 9;
            if (current + i >= permissions.size()) {
                setItem(i, null);
                continue;
            }
            BasicPermission permission = permissions.get(current + i);
            if (permission == null) continue;

            final String path = "Settings." + role.name() + "." + permission.getName();
            boolean setting = settingsConfigLoad.getBoolean(path);
            setButton(i, permission.getItem(setting, role), (event) -> {
                settingsConfigLoad.set(path, !setting);
                try {
                    settingsConfigLoad.save(settingsConfig.getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                paint();
            });
        }
    }

    public PermissionType getType(IslandRole role) {
        switch (role) {
            default:
            case Visitor:
            case Member:
            case Coop:
                return PermissionType.GENERIC;
            case Operator:
                return PermissionType.OPERATOR;
            case Owner:
                return PermissionType.ISLAND;
        }
    }
}
