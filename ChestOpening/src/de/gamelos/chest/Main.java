package de.gamelos.chest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gamelos.jaylosapi.JaylosAPI;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		
		Bukkit.getPluginManager().registerEvents(this, this);
//		==================================================
		normal.put("Ice-Boots", glass(Material.LEATHER_BOOTS, ChatColor.AQUA+"Ice-Boots", Color.AQUA));
		normal.put("Fire-Boots", glass(Material.LEATHER_BOOTS, ChatColor.RED+"Fire-Boots", Color.RED));
		normal.put("Love-Boots", glass(Material.LEATHER_BOOTS, ChatColor.RED+"Love-Boots", Color.RED));
		normal.put("Ghost-Boots", glass(Material.LEATHER_BOOTS, ChatColor.RED+"Ghost-Boots", Color.WHITE));
		normal.put("Lava-Boots", glass(Material.LEATHER_BOOTS, ChatColor.RED+"Lava-Boots", Color.YELLOW));
		normal.put("Smoke-Boots", glass(Material.LEATHER_BOOTS, ChatColor.RED+"Smoke-Boots", Color.GRAY));
		gut.put("Schwein", glasss(Material.MONSTER_EGG, ChatColor.LIGHT_PURPLE+"Schwein", (short)90));
		gut.put("Huhn", glasss(Material.MONSTER_EGG, ChatColor.YELLOW+"Huhn", (short)93));
		gut.put("Zombie", glasss(Material.MONSTER_EGG, ChatColor.GREEN+"Zombie", (short)50));
		gut.put("Hexe", glasss(Material.MONSTER_EGG, ChatColor.DARK_PURPLE+"Hexe", (short)66));
		op.put("Rewinside", head(ChatColor.YELLOW+"Rewinside", "Rewinside"));
		op.put("GommeHD", head(ChatColor.RED+"GommeHD", "GommeHD"));
//		==================================================
		ConnectMySQL();
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		
		
		super.onDisable();
	}
	
	public static MySQL mysql;
	private void ConnectMySQL(){
		mysql = new MySQL(JaylosAPI.gethost(), JaylosAPI.getuser(), JaylosAPI.getdatabase(), JaylosAPI.getpassword());
		mysql.update("CREATE TABLE IF NOT EXISTS Chest(UUID varchar(64), Truhen int, Kit varchar(600));");
		mysql.update("CREATE TABLE IF NOT EXISTS Chestinfo(UUID varchar(64), Truhen int, Kit varchar(600));");
	}

	@EventHandler
	public static void onjoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		Calendar c = Calendar.getInstance();
		String datum = c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
		if(!SQLStats2.getlasttime(p.getUniqueId().toString(), p.getName()).equals(datum)){
			SQLStats2.setKits(p.getUniqueId().toString(), datum, p.getName());
			SQLStats.addTruhen(p.getUniqueId().toString(), 1, p.getName());
			p.sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.GREEN+"Du hast deine Tägliche belohnung erfolgreich erhalten!");
		}
	}
	

@EventHandler
public void onklick(PlayerInteractEvent e){
	if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
		if(e.getClickedBlock().getType() == Material.CHEST){
			e.setCancelled(true);
			if(SQLStats.getkitanzahl(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName())<12){
			Inventory inv = Bukkit.createInventory(null, 3*9, ChatColor.YELLOW+"ChestOpening");
			inv.setItem(13, amountitem(Material.CHEST, "Klicke um zu Starten ("+SQLStats.getkitanzahl(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName())+"/12)", SQLStats.getTruhen(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName())));
			e.getPlayer().openInventory(inv);
			}else{
				e.getPlayer().sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.RED+"Du hast alle im Moment verfügbaren Items!");
			}
			
		}
	}
}

public static HashMap<String,ItemStack> normal = new HashMap<>();
public static HashMap<String,ItemStack> gut = new HashMap<>();
public static HashMap<String,ItemStack> op = new HashMap<>();

@EventHandler
public void oni(InventoryClickEvent e){
	if(e.getClickedInventory().getTitle().equals(ChatColor.YELLOW+"ChestOpening")){
		Player p = (Player) e.getWhoClicked();
		e.setCancelled(true);
		if(e.getCurrentItem().getType() == Material.CHEST){
			if(e.getCurrentItem().getAmount()>0){
				SQLStats.removeTruhen(p.getUniqueId().toString(),1, p.getName());
			Inventory inv = Bukkit.createInventory(null, 5*9, ChatColor.YELLOW+"ChestOpening");
			inv.setItem(4, item(Material.NETHER_STAR, "Dein Item"));
//			===========================================================================
			List<String> n = new ArrayList<>();
			for(String s : normal.keySet()){
				n.add(s);
			}
			List<String> g = new ArrayList<>();
			for(String s : gut.keySet()){
				g.add(s);
			}
			List<String> o = new ArrayList<>();
			for(String s : op.keySet()){
				o.add(s);
			}
//			===========================================================================
			inv.setItem(4+4*9, item(Material.NETHER_STAR, "Dein Item"));
			p.openInventory(inv);
			onre(n, g, o, p, inv);
			}else{
				p.sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.RED+"Du hast keine Kisten mehr!");
			}
		}
	}
}
@SuppressWarnings({ "rawtypes", "unchecked" })
HashMap<Player,Integer> list = new HashMap();

int count = 0;
public void onre(List<String>normal,List<String>gut,List<String>op, Player p, Inventory inv){
	count = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
		Random r = new Random();
		int i = r.nextInt(10)+150;
		int i1 = 0;
		@Override
		public void run() {
			
			if(i>70){
				next(inv, normal, gut, op,p);
				p.playSound(p.getLocation(),Sound.ITEM_PICKUP, 1F, 1F);
			}else if(i>=30&&i<70){
				if(i1==2){
					i1=0;
				}	
				if(i1==0){
						next(inv, normal, gut, op,p);
						p.playSound(p.getLocation(),Sound.ITEM_PICKUP, 1F, 1F);
					}
					i1++;
			}else if(i<=0){
				if(list.containsKey(p)){
				Bukkit.getScheduler().cancelTask(list.get(p));
				list.remove(p);
				}
				win(inv,p);
				
			}else{
				if(i1==3){
					i1=0;
				}
				if(i1==0){
					next(inv, normal, gut, op,p);
					p.playSound(p.getLocation(),Sound.ITEM_PICKUP, 1F, 1F);
				}
				i1++;
			}
			
			i--;
			if(i>0){
			if(!list.containsKey(p)){
				list.put(p, count);
			}
			}
		}
	}, 1, 1);
	
}

public ItemStack randomitem(Inventory inv,List<String>normal,List<String>gut,List<String>op,List<String>has,Player p){
	ItemStack item = null;
	Random r = new Random();
	int i = r.nextInt(15);
	if(i==0||i==1||i==2){
		Random b = new Random();
		int bbb = b.nextInt(normal.size()+1);
		item = Main.normal.get(normal.get(bbb-1));
		inv.setItem(9, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.WHITE));
		inv.setItem(27, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.WHITE));
	}else if(i==3||i==4){
		Random b = new Random();
		int bbb = b.nextInt(gut.size()+1);
		item = Main.gut.get(gut.get(bbb-1));
		inv.setItem(9, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.BLUE));
		inv.setItem(27, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.BLUE));
	}else if(i==14){
			Random b = new Random();
			int bbb = b.nextInt(op.size()+1);
			item = Main.op.get(op.get(bbb-1));
			inv.setItem(9, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.PURPLE));
			inv.setItem(27, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.PURPLE));
		}else{
			Random b = new Random();
			int bbb = b.nextInt(normal.size()+1);
			item = Main.normal.get(normal.get(bbb-1));
			inv.setItem(9, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.WHITE));
			inv.setItem(27, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.WHITE));
		}
	if(has.contains(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
		ItemStack bra = new ItemStack(Material.BARRIER);
		ItemMeta meta = bra.getItemMeta();
		meta.setDisplayName(ChatColor.RED+"Nichts");
		bra.setItemMeta(meta);
		item = bra;
		inv.setItem(9, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.RED));
		inv.setItem(27, glasses(Material.STAINED_GLASS_PANE, " ", DyeColor.RED));
	}
	return item;
}

public void next(Inventory inv,List<String>normal,List<String>gut,List<String>op,Player p){
	inv.setItem(17, inv.getItem(16));
	inv.setItem(16, inv.getItem(15));
	inv.setItem(15, inv.getItem(14));
	inv.setItem(14, inv.getItem(13));
	inv.setItem(13, inv.getItem(12));
	inv.setItem(12, inv.getItem(11));
	inv.setItem(11, inv.getItem(10));
	inv.setItem(10, inv.getItem(9));
//	==================================
	inv.setItem(26, inv.getItem(25));
	inv.setItem(25, inv.getItem(24));
	inv.setItem(24, inv.getItem(23));
	inv.setItem(23, inv.getItem(22));
	inv.setItem(22, inv.getItem(21));
	inv.setItem(21, inv.getItem(20));
	inv.setItem(20, inv.getItem(19));
	inv.setItem(19, inv.getItem(18));
//	==================================
	inv.setItem(35, inv.getItem(34));
	inv.setItem(34, inv.getItem(33));
	inv.setItem(33, inv.getItem(32));
	inv.setItem(32, inv.getItem(31));
	inv.setItem(31, inv.getItem(30));
	inv.setItem(30, inv.getItem(29));
	inv.setItem(29, inv.getItem(28));
	inv.setItem(28, inv.getItem(27));
//	==================================
	List<String>has = SQLStats.getkit(p.getUniqueId().toString(), p.getName());
	has.add("Love-Boots");
	inv.setItem(18, randomitem(inv, normal, gut, op, has,p));
	
}

	
public static ItemStack item(Material m , String displayname){
	ItemStack item = new ItemStack(m);
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	item.setItemMeta(meta);
	return item;
}

public static ItemStack amountitem(Material m , String displayname, int ammount){
	ItemStack item = new ItemStack(m,ammount);
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	item.setItemMeta(meta);
	return item;
}

public static ItemStack glass(Material m , String displayname, Color dye){
	ItemStack item = new ItemStack(m);
	LeatherArmorMeta  meta = (LeatherArmorMeta) item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setColor(dye);
	item.setItemMeta(meta);
	return item;
}

@SuppressWarnings("deprecation")
public static ItemStack glasses(Material m , String displayname, DyeColor dye){
	ItemStack item = new ItemStack(m,1,dye.getData());
	ItemMeta  meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	item.setItemMeta(meta);
	return item;
}

public static ItemStack glasss(Material m , String displayname, short dye){
	ItemStack item = new ItemStack(m, 1, dye);
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	item.setItemMeta(meta);
	return item;
}
public static ItemStack head(String displayname, String skullowner){
	ItemStack item = new ItemStack(Material.SKULL_ITEM, 1,(short)3);
	SkullMeta meta = (SkullMeta) item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setOwner(skullowner);
	item.setItemMeta(meta);
	return item;
}

public void win(Inventory inv,Player p){
	Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
		@SuppressWarnings("null")
		@Override
		public void run() {
			ItemStack item = inv.getItem(22);
			ItemStack glass = inv.getItem(13);
			for(int b =9;b<36;b++){
				inv.setItem(b, glass);
			}
			inv.setItem(22, item);
			if(item!=null||item.getItemMeta() != null){
			if(normal.containsKey(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
				SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
			}else if(gut.containsKey(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 1F, 1F);
				SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
			}else if(op.containsKey(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				for(Player pp : Bukkit.getOnlinePlayers()){
				pp.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);
				}
				SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.YELLOW+p.getName()+ChatColor.GRAY+" hat "+item.getItemMeta().getDisplayName()+ChatColor.GRAY+" gewonnen!");
			}else{
				p.playSound(p.getLocation(), Sound.ZOMBIE_DEATH, 1F, 1F);
			}
			SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
			p.sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+"Du hast "+item.getItemMeta().getDisplayName()+ChatColor.GRAY+" gewonnen!");
			}else{
				p.playSound(p.getLocation(), Sound.ZOMBIE_DEATH, 1F, 1F);
				p.sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+"Du hast "+ChatColor.RED+"Nichts"+ChatColor.GRAY+" gewonnen!");
			}
		}
	}, 25);

}

@SuppressWarnings("null")
public void rwin(Inventory inv,Player p){
			List<String> has = SQLStats.getkit(p.getUniqueId().toString(), p.getName());
			List<String> n = new ArrayList<>();
			for(String s : normal.keySet()){
				n.add(s);
			}
			List<String> g = new ArrayList<>();
			for(String s : gut.keySet()){
				g.add(s);
			}
			List<String> o = new ArrayList<>();
			for(String s : op.keySet()){
				o.add(s);
			}
			ItemStack item = randomitem(inv, n, g, o, has,p);
			if(item!=null||item.getItemMeta() != null){
			if(normal.containsKey(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
				SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
			}else if(gut.containsKey(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 1F, 1F);
				SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
			}else if(op.containsKey(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				for(Player pp : Bukkit.getOnlinePlayers()){
				pp.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);
				}
				SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.YELLOW+p.getName()+ChatColor.GRAY+" hat "+item.getItemMeta().getDisplayName()+ChatColor.GRAY+" gewonnen!");
			}else{
				p.playSound(p.getLocation(), Sound.ZOMBIE_DEATH, 1F, 1F);
			}
			SQLStats.addKit(p.getUniqueId().toString(), ChatColor.stripColor(item.getItemMeta().getDisplayName()), p.getName());
			p.sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+"Du hast "+item.getItemMeta().getDisplayName()+ChatColor.GRAY+" gewonnen!");
			}else{
				p.playSound(p.getLocation(), Sound.ZOMBIE_DEATH, 1F, 1F);
				p.sendMessage(ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Chest"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+"Du hast "+ChatColor.RED+"Nichts"+ChatColor.GRAY+" gewonnen!");
			}

}

@EventHandler
public void onc(InventoryCloseEvent e){
	Player p = (Player) e.getPlayer();
	if(e.getInventory().getTitle().equals(ChatColor.YELLOW+"ChestOpening")){
		if(list.containsKey(p)){
				Bukkit.getScheduler().cancelTask(list.get(p));
				list.remove(p);
				List<String> n = new ArrayList<>();
				for(String s : normal.keySet()){
					n.add(s);
				}
				List<String> g = new ArrayList<>();
				for(String s : gut.keySet()){
					g.add(s);
				}
				List<String> o = new ArrayList<>();
				for(String s : op.keySet()){
					o.add(s);
				}
//				next(e.getInventory(), n, g, o, p);
				rwin(e.getInventory(),p);
		}
	}
}


@EventHandler
public void onkl(PlayerInteractEvent e){
	if(e.getAction() == Action.RIGHT_CLICK_AIR||e.getAction() == Action.RIGHT_CLICK_BLOCK){
		if(e.getPlayer().getItemInHand().getType() == Material.CHEST){
			e.setCancelled(true);
			Player p = e.getPlayer();
			List<String> list = SQLStats.getkit(p.getUniqueId().toString(), p.getName());
			Inventory inv = Bukkit.createInventory(null, 9*3, ChatColor.YELLOW+"Items");
			inv.setItem(10, item(Material.GOLD_BOOTS, ChatColor.YELLOW+"Boots"));
			inv.setItem(12, item(Material.MOB_SPAWNER, ChatColor.DARK_PURPLE+"Haustiere"));
			inv.setItem(14, item(Material.SKULL_ITEM, ChatColor.RED+"Kopf"));
			inv.setItem(16, item(Material.GOLD_INGOT, ChatColor.GOLD+"Extras"));
			inv.setItem(18, item(Material.BARRIER, ChatColor.RED+"Alle items Ausziehen"));
			p.openInventory(inv);
		}
	}
}

@EventHandler
public void click(InventoryClickEvent e){
	Player p = (Player) e.getWhoClicked();
	if(e.getClickedInventory().getTitle().equals(ChatColor.YELLOW+"Items")){
		e.setCancelled(true);
		List<String> list = SQLStats.getkit(p.getUniqueId().toString(), p.getName());
		if(e.getCurrentItem().getType()==Material.GOLD_BOOTS){
			Inventory inv = Bukkit.createInventory(null, 9, e.getCurrentItem().getItemMeta().getDisplayName());
			for(String s:list){
				if(s.contains("Boots")){
					inv.addItem(normal.get(s));
				}
			}
			p.openInventory(inv);
		}else if(e.getCurrentItem().getType()==Material.MOB_SPAWNER){
			Inventory inv = Bukkit.createInventory(null, 9, e.getCurrentItem().getItemMeta().getDisplayName());
			for(String s:list){
				if(gut.containsKey(s)){
				ItemStack item = gut.get(s);
				if(item.getType() == Material.MONSTER_EGG){
					inv.addItem(item);
				}
				}
			}
			p.openInventory(inv);
		} else if(e.getCurrentItem().getType()==Material.SKULL_ITEM){
			Inventory inv = Bukkit.createInventory(null, 9, e.getCurrentItem().getItemMeta().getDisplayName());
			for(String s:list){
				if(op.containsKey(s)){
				ItemStack item = op.get(s);
				if(item.getType() == Material.SKULL_ITEM){
					inv.addItem(item);
				}
				}
			}
			p.openInventory(inv);
		} else if(e.getCurrentItem().getType()==Material.GOLD_INGOT){
			Inventory inv = Bukkit.createInventory(null, 9, e.getCurrentItem().getItemMeta().getDisplayName());
			p.openInventory(inv);
		} else if(e.getCurrentItem().getType() == Material.BARRIER){
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			if(pets.containsKey(p)){
				Entity ee = pets.get(p);
				((org.bukkit.entity.Entity) ee).remove();
			}
			p.closeInventory();
		}
	}else if(e.getClickedInventory().getTitle().equals(ChatColor.RED+"Kopf")){
		e.setCancelled(true);
		if(e.getCurrentItem()!=null&&e.getCurrentItem().getType() != Material.AIR){
		p.sendMessage(ChatColor.GREEN+"Du hast nun den Kopf von "+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.GREEN+" an");
		p.getInventory().setHelmet(e.getCurrentItem());
		p.closeInventory();
		}
	}else if(e.getClickedInventory().getTitle().equals(ChatColor.YELLOW+"Boots")){
		e.setCancelled(true);
		if(e.getCurrentItem()!=null&&e.getCurrentItem().getType() != Material.AIR){
		p.sendMessage(ChatColor.GREEN+"Du hast nun "+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.GREEN+" an");
		p.getInventory().setBoots(e.getCurrentItem());
		p.closeInventory();
		}
	}else if(e.getClickedInventory().getTitle().equals(ChatColor.DARK_PURPLE+"Haustiere")){
		e.setCancelled(true);
		if(e.getCurrentItem()!=null&&e.getCurrentItem().getType() != Material.AIR){
		p.sendMessage(ChatColor.GREEN+"Du hast nun das "+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.GREEN+" als Haustier gewählt!");
		String s = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
		if(pets.containsKey(p)){
			Entity ee = pets.get(p);
			((org.bukkit.entity.Entity) ee).remove();
		}
		if(s.equals("Zombie")){
		Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE); 
		zombie.setTarget(p);
		zombie.setCustomNameVisible(true);
		zombie.setCustomName(ChatColor.RED+p.getName());
		zombie.setBaby(true);
		pets.put(p, (Entity)zombie);
		}else if(s.equals("Schwein")){
			Pig zombie = (Pig) p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG); 
			zombie.setTarget(p);
			zombie.setCustomNameVisible(true);
			zombie.setCustomName(ChatColor.RED+p.getName());
			pets.put(p, (Entity)zombie);	
		}else if(s.equals("Hexe")){
			Witch zombie = (Witch) p.getWorld().spawnEntity(p.getLocation(), EntityType.WITCH); 
			zombie.setTarget(p);
			zombie.setCustomNameVisible(true);
			zombie.setCustomName(ChatColor.RED+p.getName());
			pets.put(p, (Entity)zombie);	
		}else if(s.equals("Huhn")){
			Chicken zombie = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN); 
			zombie.setTarget(p);
			zombie.setCustomNameVisible(true);
			zombie.setCustomName(ChatColor.RED+p.getName());
			pets.put(p, (Entity)zombie);	
		}
		
		p.closeInventory();
		}
	}
}


public static HashMap<Player,Entity> pets = new HashMap<>();

@SuppressWarnings("deprecation")
@EventHandler
public void onmove(PlayerMoveEvent e){
	Player p = e.getPlayer();
	if(pets.containsKey(p)){
		if(p.getLocation().distance(pets.get(p).getLocation())>10){
			pets.get(p).teleport(p.getLocation());
		}
	}
	
	if(p.getInventory().getBoots() !=null &&p.getInventory().getBoots().getType() != Material.AIR){
		
		if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"Ice-Boots")){
			p.playEffect(p.getLocation(), Effect.SNOW_SHOVEL, 1);
		}else if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.RED+"Fire-Boots")){
			p.playEffect(p.getLocation(), Effect.FLAME, 1);
		}else if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.RED+"Love-Boots")){
			p.playEffect(p.getLocation(), Effect.HEART, 1);
		}else if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.RED+"Ghost-Boots")){
			p.playEffect(p.getLocation(), Effect.SMOKE, 1);
		}else if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.RED+"Lava-Boots")){
			p.playEffect(p.getLocation(), Effect.LAVA_POP, 1);
		}else if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.RED+"Smoke-Boots")){
			p.playEffect(p.getLocation(), Effect.LARGE_SMOKE, 1);
		}
		
	}
}

@EventHandler
public void ons(PlayerToggleSneakEvent e){
	Player p = e.getPlayer();
	if(p!=null){
		if(p.getInventory()!=null){
	if(p.getInventory().getBoots()!=null){
if(p.getInventory().getBoots().getType() != Material.AIR){
		
		if(p.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.RED+"Ghost-Boots")){
			if(!p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 10));
			}else{
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
}
}
	}
}
}

@EventHandler
public void onquit(PlayerQuitEvent e){
	Player p = e.getPlayer();
	if(pets.containsKey(p)){
		Entity ee = pets.get(p);
		((org.bukkit.entity.Entity) ee).remove();
	}
}

}
