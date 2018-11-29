package de.gamelos.chest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class SQLStats2 {

	public static boolean playerExists(String uuid){
		try {
			@SuppressWarnings("static-access")
			ResultSet rs = Main.mysql.querry("SELECT * FROM Chestinfo WHERE UUID = '"+ uuid + "'");
			
			if(rs.next()){
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void createPlayer(String uuid){
		if(!(playerExists(uuid))){
				Main.mysql.update("INSERT INTO Chestinfo(UUID, Truhen, Kit) VALUES ('" +uuid+ "', '0', ' ');");
		}
	}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static Integer getCoins(String uuid, String name){
		Integer i = 0;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Chestinfo WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (Integer.valueOf(rs.getInt("Truhen")) == null));
				
				i = rs.getInt("Truhen");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			createPlayer(uuid);
			getCoins(uuid, name);
		}
		return i;
	}
	
	public static String getlasttime(String uuid, String name){
		String i = "";
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Chestinfo WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Kit")) == null));
				
				i = rs.getString("Kit");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			createPlayer(uuid);
			getlasttime(uuid, name);
		}
		return i;
	}
	
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
	public static void setCoins(String uuid, Integer kills, String name){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Chestinfo SET Truhen= '" + kills+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setCoins(uuid, kills, name);
		}
		
	}
	
	public static void setKits(String uuid, String kills, String name){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Chestinfo SET Kit= '" + kills+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setKits(uuid, kills, name);
		}
		
	}
	
	//add------------------------------------------------------------------------------------------------------------------------------------
	
	public static void addCoins(String uuid, Integer kills, String name){
		
		if(playerExists(uuid)){
			setCoins(uuid, Integer.valueOf(getCoins(uuid, name).intValue() + kills.intValue()), name);
		}else{
			createPlayer(uuid);
			addCoins(uuid, kills, name);
		}
		
	}
	
	public static void addKit(String uuid, String kit, String name){
		
		if(playerExists(uuid)){
			String[] s = getlasttime(uuid, name).split(",");
			List<String> list = new ArrayList<>();
			for(String ss:s){
				list.add(ss);
			}
			if(!list.contains(kit)&&!kit.equals("Nichts")){
			list.add(kit);
			String sss = "";
			for(String b:list){
				sss = sss+b+",";
			}
			setKits(uuid, sss, name);
			}
		}else{
			createPlayer(uuid);
			addKit(uuid, kit, name);
		}
		
	}
	
	
	//remove------------------------------------------------------------------------------------------------------------------------------------
	
	public static void removeCoins(String uuid, Integer kills, String name){
		
		if(playerExists(uuid)){
			setCoins(uuid, Integer.valueOf(getCoins(uuid, name).intValue() - kills.intValue()), name);
		}else{
			createPlayer(uuid);
			removeCoins(uuid, kills, name);
		}
		
	}
	
public static void removeKit(String uuid, String kit, String name){
		
		if(playerExists(uuid)){
			String[] s = getlasttime(uuid, name).split(",");
			List<String> list = new ArrayList<>();
			for(String ss:s){
				list.add(ss);
			}
			list.remove(kit);
			String sss = "";
			for(String b:list){
				sss = sss+b+",";
			}
		}else{
			createPlayer(uuid);
			removeKit(uuid, kit, name);
		}
		
	}
public static List<String> getkit(String uuid,String name){
	List<String> list = new ArrayList<>();
	if(playerExists(uuid)){
		String[] s = getlasttime(uuid, name).split(",");
		for(String ss:s){
			if(!ss.equals(" ")&&!ss.equals("")){
			list.add(ss);
			}
		}
	}
	return list;
}
	
//	======================================================================================
public static Integer getkitanzahl(String uuid,String name){
	List<String> list = new ArrayList<>();
	if(playerExists(uuid)){
		String[] s = getlasttime(uuid, name).split(",");
		for(String ss:s){
			if(!ss.equals(" ")&&!ss.equals("")){
			list.add(ss);
			}
		}
	}
	return list.size();
}
}
