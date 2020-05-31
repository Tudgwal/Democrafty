package fr.tudgwal.democrafty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.bukkit.Material;

public class Utils {
    private static final Logger logger = Logger.getLogger("Minecraft");

    public static boolean isWallSign(Material m) {
        Material[] wallSign = {Material.ACACIA_WALL_SIGN,
                Material.SPRUCE_WALL_SIGN,
                Material.BIRCH_WALL_SIGN,
                Material.OAK_WALL_SIGN,
                Material.JUNGLE_WALL_SIGN,
                Material.DARK_OAK_WALL_SIGN};
        return Arrays.stream(wallSign).anyMatch(ws -> ws == m);
    }

    public static void log(String message, Level level) {
        if(!message.isEmpty())
            logger.log(level,("[Democrafty] " + message));
    }

    public static boolean validDate(String toValid, String format){
        if(toValid == null){
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(toValid);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
