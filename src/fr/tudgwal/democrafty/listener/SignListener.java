package fr.tudgwal.democrafty.listener;

import fr.tudgwal.democrafty.Main;
import fr.tudgwal.democrafty.Utils;
import fr.tudgwal.democrafty.Vault;
import fr.tudgwal.democrafty.command.CommandMessages;
import fr.tudgwal.democrafty.command.Commands;
import fr.tudgwal.democrafty.sqlite.SQLite;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class SignListener implements Listener {
    public static SignListener instance;

    public SignListener(){
        instance = this;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e){
        Block b = e.getBlock();
        if (Utils.isWallSign(b.getType())) {
            Sign sign = (Sign) b.getState();
            if (sign.getLine(0).equalsIgnoreCase("[Democrafty]")) {
                List<MetadataValue> value = e.getPlayer().getMetadata("democraftyPanelDelete");
                if (value == null || value.isEmpty() || !value.get(0).asBoolean()) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(CommandMessages.formatMessage("error_perm_panel"));
                    return;
                } else if (value != null && !value.isEmpty() && value.get(0).asBoolean()){
                    e.getPlayer().removeMetadata("democraftyPanelDelete", Main.plugin);
                    b.breakNaturally();
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Block b = e.getClickedBlock();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            if (Utils.isWallSign(b.getType())) {
                Sign sign = (Sign) b.getState();
                List<MetadataValue> value = e.getPlayer().getMetadata("democraftyPanelDelete");
                if (sign.getLine(0).equalsIgnoreCase("[Democrafty]") && (value == null|| value.isEmpty() || !value.get(0).asBoolean())) {
                    if (Vault.getPermission().has(e.getPlayer(), "democrafty.player") || Vault.getPermission().has(e.getPlayer(), "democrafty.admin")) {
                        if (!sign.getLine(1).isEmpty() && !sign.getLine(2).isEmpty()) {
                            String vote = sign.getLine(1);
                            String choice = sign.getLine(2);
                            if (SQLite.tableExist(vote).equalsIgnoreCase(vote))
                                Commands.addVote(vote, choice, e.getPlayer());
                        }
                    } else
                        e.getPlayer().sendMessage(CommandMessages.formatMessage("error_perm_user"));
                }
            }
        }
    }
}