package fr.tudgwal.democrafty;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;


public class Vault {
    private static Permission permission = null;
    private static Economy economy = null;
    private static Chat chat = null;
    private static Boolean vaultFound = Boolean.valueOf(false);
    private static Server server = Bukkit.getServer();
    private static final String nullString = null;

    public Vault() {
        if(server.getPluginManager().isPluginEnabled("Vault")) {
            vaultFound = Boolean.valueOf(true);
        } else {
            Utils.log("Vault not found!!", Level.SEVERE);
        }

    }

    public static Permission getPermission() {
        return permission != null && !permission.getName().equals("SuperPerms")?permission:null;
    }

    public static Boolean isVaultFound() {
        return vaultFound;
    }

    public static String getVersion() {
        return isVaultFound().booleanValue()?server.getPluginManager().getPlugin("Vault").getDescription().getVersion():"N/A";
    }

    public Boolean setupPermissions() {
        if(!isVaultFound().booleanValue()) {
            return Boolean.valueOf(false);
        } else {
            RegisteredServiceProvider permissionProvider = server.getServicesManager().getRegistration(Permission.class);
            if(permissionProvider != null) {
                permission = (Permission)permissionProvider.getProvider();
            }

            return Boolean.valueOf(getPermission() != null);
        }
    }

    public static boolean removeGroupAnyWorld(Player player, String group) {
        Permission perm = getPermission();
        return perm != null && perm.hasGroupSupport()?perm.playerRemoveGroup(nullString, player, group) || perm.playerRemoveGroup(player, group):false;
    }

    public static boolean addGroupAnyWorld(Player player, String group) {
        Permission perm = getPermission();
        return perm != null && perm.hasGroupSupport()?perm.playerAddGroup(nullString, player, group) || perm.playerAddGroup(player, group):false;
    }

    public static boolean playerInGroupAnyWorld(Player player, String group) {
        Permission perm = getPermission();
        return perm != null && perm.hasGroupSupport()?playerInGlobalGroup(player, group) || perm.playerInGroup(player, group):false;
    }

    public static boolean playerInGlobalGroup(Player player, String group) {
        Permission perm = getPermission();
        return perm != null && perm.hasGroupSupport()?perm.playerInGroup(nullString, player, group):false;
    }

    public static String getGlobalPrimaryGroup(Player player) {
        Permission perm = getPermission();
        return perm != null && perm.hasGroupSupport()?perm.getPrimaryGroup(nullString, player):null;
    }

    public static String[] getGlobalGroups(Player player) {
        Permission perm = getPermission();
        return perm != null && perm.hasGroupSupport()?perm.getPlayerGroups(nullString, player):null;
    }
}

