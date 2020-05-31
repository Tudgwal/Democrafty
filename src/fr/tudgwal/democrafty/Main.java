package fr.tudgwal.democrafty;

import fr.tudgwal.democrafty.command.CommandMessages;
import fr.tudgwal.democrafty.command.Commands;
import fr.tudgwal.democrafty.listener.SignListener;
import fr.tudgwal.democrafty.sqlite.SQLite;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    public static Main plugin = null;
    private FileConfiguration language;
    private File lang;
    public static SQLite db = null;

    public static Main getInstance(){
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Files.createDir(this);
        lang = Files.findLang(this);
        language = Files.setupLang(lang);
        Vault v = new Vault();
        v.setupPermissions();
        db = new SQLite(this);
        db.createMainTable();
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        Utils.log("Enabled", Level.INFO);
    }

    @Override
    public void onDisable(){
        Utils.log("Disable", Level.INFO);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        String commandName = cmd.getName().toLowerCase();
        if (!commandName.equalsIgnoreCase("democrafty") && !commandName.equalsIgnoreCase("dc"))
            return false;
        if (args.length == 0) {
            CommandMessages.helpCommand(sender);
            return false;
        }
        if (Vault.getPermission().has(sender, "democrafty.admin"))
            selectPermCommand(sender, args);
        else
            selectCommand(sender, args);
        return true;
    }

    public void selectCommand(CommandSender sender, String args[]){
        switch (args[0]) {
            case "list":
                Commands.list(sender);
                break;
            case "edit":
                Commands.result(sender, args);
                break;
            case "info":
                Commands.info(sender, args);
                break;
            default:
                CommandMessages.helpCommand(sender);
                break;
        }
    }

    public void selectPermCommand(CommandSender sender, String args[]) {
        switch (args[0]){
            case "create":
                Commands.create(sender, args);
                break;
            case "edit":
                Commands.edit(sender, args);
                break;
            case "info":
                Commands.info(sender, args);
                break;
            case "result":
                Commands.result(sender, args);
                break;
            case "remove":
                Commands.remove(sender, args);
                break;
            case "panel":
                Commands.panel(sender, args);
                break;
            case "list":
                Commands.list(sender);
                break;
            default:
                CommandMessages.helpCommand(sender);
                break;
        }
    }

    public FileConfiguration getLang() {
        return language;
    }
}