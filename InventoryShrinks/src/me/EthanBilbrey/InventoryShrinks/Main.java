package me.EthanBilbrey.InventoryShrinks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements CommandExecutor, Listener
{
	public boolean isStarted;
	/*TODO:
	 * When player dies, make sure when they spawn back in, they still have panes blocking theirs slots.
	 */
	@Override
	public void onEnable() 
	{
		isStarted = false;
		this.getCommand("isstart").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new ChallengeInventoryManager(), this);
	}
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
    	if(sender instanceof Player) 
    	{
    		if(command.getName().equals("isstart")) 
    		{
    			isStarted = true;
    			sender.sendMessage(ChatColor.BOLD + "Challenge Started!");
    			for(Player p : Bukkit.getOnlinePlayers()) 
    			{
    				ChallengeInventoryManager cim = new ChallengeInventoryManager(p);
    			}
    		}
    	}
        return true;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) 
    {
    	if(isStarted) 
    	{
    		ChallengeInventoryManager cim = new ChallengeInventoryManager(e.getPlayer());
    	}
    }
    @EventHandler
    public void playerInventoryInteract(InventoryClickEvent e) 
    {
    	try 
    	{
    		if(e.getWhoClicked().getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().equals(" ") && 
        			e.getWhoClicked().getInventory().getItem(e.getSlot()).getType().equals(Material.GRAY_STAINED_GLASS_PANE))
        	{
        		e.setCancelled(true);
        	}
    	}
    	catch(NullPointerException exc) {}
    	
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) 
    {
    	ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
    	if(e.getDrops().contains(item)) 
    	{
    		e.getDrops().remove(item);
    	}
    }
}
