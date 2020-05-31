package fr.tudgwal.democrafty;

import java.io.File;
import java.util.logging.Level;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

public class Files {
    public static void createDir(Main demo) {
        if(!demo.getDataFolder().exists()) {
            if(!demo.getDataFolder().mkdir()) {
                Utils.log("Could not create plugin folder!", Level.SEVERE);
            }
        }
    }

    public static File findLang(Main demo){
        File languageF;
        try {
            if (!demo.getDataFolder().exists()) {
                demo.getDataFolder().mkdirs();
            }
            languageF = new File(demo.getDataFolder(), "french.yml");
            if (!languageF.exists()) {
                Utils.log("french.yml not found, creating!", Level.INFO);
                languageF.getParentFile().mkdirs();
                demo.saveResource("french.yml", false);
            } else {
                Utils.log("french.yml  found, loading!", Level.INFO);
            }
            return languageF;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static FileConfiguration setupLang(File languageF) {
        FileConfiguration language;
        language = new YamlConfiguration();
        try {
            language.load(languageF);
            return language;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
