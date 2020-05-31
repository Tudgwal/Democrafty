package fr.tudgwal.democrafty.command;

import com.mysql.fabric.xmlrpc.base.Value;
import com.mysql.jdbc.Util;
import fr.tudgwal.democrafty.Main;
import fr.tudgwal.democrafty.Utils;
import fr.tudgwal.democrafty.Vault;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CommandMessages {
    private static String demo = ChatColor.GREEN + "[" + ChatColor.WHITE + "Democrafty" + ChatColor.GREEN + "] ";

    private static String addLine(String message, String key, String target){
        message += ChatColor.GREEN + key + ChatColor.WHITE;
        message += (String) Main.getInstance().getLang().get(target) + "\n";
        return message;
    }

    public static String formatMessage(String path, Map<String, String> replace) {
        String toret = (String) Main.getInstance().getLang().get(path) + "\n";
        if (replace != null) {
            for (Map.Entry<String, String> ent : replace.entrySet()) {
                toret = addLine(toret, ent.getKey(), ent.getValue());
            }
        }
        return demo + toret;
    }

    public static String displayInfo(List<String> info){
        String msg = "\n" + (String) Main.getInstance().getLang().get("name") + " : " + info.get(0) + "\n";
        msg += (String) Main.getInstance().getLang().get("begin_date") + " : " + info.get(1) + "\n";
        msg += (String) Main.getInstance().getLang().get("end_date") + " : " + info.get(2) + "\n";
        return demo + msg;
    }

    public static String formatMessage(String path) {
        return formatMessage(path, null);
    }


    public static void helpCommand(CommandSender sender){
        Map<String, String> help = new HashMap<String, String>();
        help.put("/help", "help_help");
        help.put("/democrafty info <vote> : ", "help_info");
        help.put("/democrafty result <name> : ", "help_result");
        help.put("/democrafty list", "help_list");
        if (Vault.getPermission().has(sender, "democrafty.admin")) {
            help.put("/democrafty create <name> <begin date> <end date> : ", "help_create");
            help.put("/democrafty edit <name> <begin date> <end date> : ", "help_edit");
            help.put("/democrafty remove <name> : ", "help_remove");
            help.put("/democrafty panel remove : ", "help_panel_remove");
        }
        sender.sendMessage(formatMessage("help", help));
    }

    public static void displayResult(CommandSender sender, String table, String result) {
        String msg = table + " : " +(String) Main.getInstance().getLang().get("result") + " : \n";
        msg += result;
        sender.sendMessage(demo + msg);
    }

    public static String displayListt(List<String> votes) {
        String msg = "";
        for (int i = 0; i != votes.size() ; i++)
            msg += "- " + votes.get(i) + "\n";
        return demo + "\n" +msg;
    }
}
