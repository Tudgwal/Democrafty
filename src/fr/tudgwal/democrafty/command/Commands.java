package fr.tudgwal.democrafty.command;

import fr.tudgwal.democrafty.Main;
import fr.tudgwal.democrafty.Utils;
import fr.tudgwal.democrafty.sqlite.SQLite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Commands {
    private static String dateFormat = "HH:mm-dd/MM/yyyy";

    public static boolean validDates(String date1, String date2){
        if (Utils.validDate(date1, dateFormat) && Utils.validDate(date2, dateFormat)){
            return true;
        }
        return false;
    }

    public static boolean validArg(String args[]){
        if (args.length == 4){
            return true;
        }
        return false;
    }

    public static void create(CommandSender sender, String args[]){
        if (!validArg(args) || !validDates(args[2], args[3])) {
            sender.sendMessage(CommandMessages.formatMessage("error_date_format"));
        } else {
            SQLite.createNewVote(args);
            SQLite.createTable(args);
            sender.sendMessage((String) Main.getInstance().getLang().get("success_new_vote") + " " + args[1]);
        }
    }

    public static void edit(CommandSender sender, String args[]){
        if (SQLite.tableExist(args[1]).equalsIgnoreCase(args[1])) {
            if (!validArg(args) || !validDates(args[2], args[3])) {
                sender.sendMessage(CommandMessages.formatMessage("error_date_format"));
            } else {
                SQLite.updateVote(args);
                sender.sendMessage((String) Main.getInstance().getLang().get("success_update_vote") + " " + args[1]);
            }
        }
    }

    public static void info(CommandSender sender, String args[]){
        if (SQLite.tableExist(args[1]).equalsIgnoreCase(args[1])) {
            List<String> info = SQLite.getInfo(args[1]);
            if (info == null)
                CommandMessages.formatMessage("error_name");
            else {
                sender.sendMessage(CommandMessages.displayInfo(info));
            }
        }
    }

    public static void result(CommandSender sender, String args[]){
        String result;
        if (SQLite.tableExist(args[1]).equalsIgnoreCase(args[1])) {
            result = SQLite.getResult(args[1]);
            if (result.isEmpty())
                sender.sendMessage((String) Main.getInstance().getLang().get("no_vote"));
            else
                CommandMessages.displayResult(sender, args[1], result);
        } else
            sender.sendMessage((String) Main.getInstance().getLang().get("error_name"));
    }

    public static void remove(CommandSender sender, String args[]){
        if (args[1].equalsIgnoreCase("democraftyTable")) {
            sender.sendMessage((String) Main.getInstance().getLang().get("delete_unable"));
        } else {
            if (SQLite.tableExist(args[1]).equalsIgnoreCase(args[1])) {
                SQLite.deleteInMainTable(args[1]);
                SQLite.deleteTable(args[1]);
                sender.sendMessage((String) Main.getInstance().getLang().get("delete_table"));
            } else
                sender.sendMessage((String) Main.getInstance().getLang().get("error_name"));
        }
    }

    public static void list(CommandSender sender){
        List<String> votes = SQLite.getVotes();
        sender.sendMessage(CommandMessages.displayListt(votes));
    }

    public static void panel(CommandSender sender, String args[]){
        if (args[1].equalsIgnoreCase("remove")){
            sender.getName();
            getServer().getPlayer(sender.getName()).setMetadata("democraftyPanelDelete", new FixedMetadataValue(Main.plugin, true));
        }
    }

    public static LocalDateTime formatDate(String sdate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime date = LocalDateTime.parse(sdate, formatter);
        return date;
    }

    public static boolean checkDate(String table){
        LocalDateTime date = LocalDateTime.now();

        if (date.isAfter(formatDate(SQLite.getDate(table, "begin"))) && date.isBefore(formatDate(SQLite.getDate(table, "end"))))
            return true;
        else
            return false;
    }

    public static void  addVote(String table, String choice, Player player){
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern(dateFormat);
        String date = sdf.format(localDate);
        if (SQLite.tableExist(table).equalsIgnoreCase(table)){
            if (checkDate(table)) {
                if (!SQLite.checkName(table, player.getName()).equalsIgnoreCase(player.getName())){
                    SQLite.playerVote(table, player.getName(), choice, date);
                    player.sendMessage(CommandMessages.formatMessage("success_vote"));
                } else
                    player.sendMessage(CommandMessages.formatMessage("already_vote"));
            } else
                player.sendMessage(CommandMessages.formatMessage("error_date"));
        } else
            player.sendMessage(CommandMessages.formatMessage("error_name"));
    }
}
