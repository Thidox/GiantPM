package nl.giantit.minecraft.GiantPM.core.Database;

import nl.giantit.minecraft.GiantPM.core.config;
import nl.giantit.minecraft.GiantPM.core.Database.drivers.iDriver;
import nl.giantit.minecraft.GiantPM.core.Database.drivers.SQLite;
import nl.giantit.minecraft.GiantPM.core.Database.drivers.MySQL;
import nl.giantit.minecraft.GiantPM.GiantPM;

import java.util.logging.Level;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class db {
	
	private static db instance;
	private GiantPM plugin;
	private config conf = config.Obtain();
	private MySQL mysql;
	private SQLite sqlite;
	private iDriver dbDriver;
	private String driver;
	
	private void dbInitMySQL() {
		if(!this.dbDriver.tableExists("#__versions")){
			this.dbDriver.buildQuery("CREATE TABLE #__versions \n");
			this.dbDriver.buildQuery("(tableName VARCHAR(100) NOT NULL, version DOUBLE NOT NULL DEFAULT '1.0');", true, true, false);
			this.dbDriver.updateQuery();
			
			plugin.log.log(Level.INFO, "Revisions table successfully created!");
		}
		
		if(!this.dbDriver.tableExists("#__mail")){
			this.dbDriver.buildQuery("INSERT INTO #__versions \n");
			this.dbDriver.buildQuery("(tableName, version) \n", true);
			this.dbDriver.buildQuery("VALUES \n", true);
			this.dbDriver.buildQuery("('mail', '1.0');", true, true);
			this.dbDriver.updateQuery();
			
			this.dbDriver.buildQuery("CREATE TABLE #__mail \n");
			this.dbDriver.buildQuery("(id INT(3) NOT NULL AUTO_INCREMENT, sender VARCHAR(100) NOT NULL, \n", true);
			this.dbDriver.buildQuery("receiver VARCHAR(100) NOT NULL, message VARCHAR(100) NOT NULL, \n", true);
			this.dbDriver.buildQuery("PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=latin1;", true, true, false);
			this.dbDriver.updateQuery();
			
			plugin.log.log(Level.INFO, "Mail table successfully created!");
		}
		
		if(!this.dbDriver.tableExists("#__muted")){
			this.dbDriver.buildQuery("INSERT INTO #__versions \n");
			this.dbDriver.buildQuery("(tableName, version) \n", true);
			this.dbDriver.buildQuery("VALUES \n", true);
			this.dbDriver.buildQuery("('muted', '1.0');", true, true);
			this.dbDriver.updateQuery();
			
			this.dbDriver.buildQuery("CREATE TABLE #__muted \n");
			this.dbDriver.buildQuery("(id INT(3) NOT NULL AUTO_INCREMENT, owner VARCHAR(100) NOT NULL, \n", true);
			this.dbDriver.buildQuery("muted VARCHAR(100) NOT NULL, \n", true, false, false);
			this.dbDriver.buildQuery("PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=latin1;", true, true, false);
			this.dbDriver.updateQuery();
			
			plugin.log.log(Level.INFO, "Mute table successfully created!");
		}
	}
	
	private void dbInitSQLite() {
		if(!this.dbDriver.tableExists("#__versions")){
			this.dbDriver.buildQuery("CREATE TABLE #__versions \n");
			this.dbDriver.buildQuery("(tableName VARCHAR(100) NOT NULL, version DOUBLE NOT NULL DEFAULT '1.0');", true, true, false);
			this.dbDriver.updateQuery();
			
			plugin.log.log(Level.INFO, "Revisions table successfully created!");
		}
		
		if(!this.dbDriver.tableExists("#__mail")){
			this.dbDriver.buildQuery("INSERT INTO #__versions \n");
			this.dbDriver.buildQuery("(tableName, version) \n", true);
			this.dbDriver.buildQuery("VALUES \n", true);
			this.dbDriver.buildQuery("('mail', '1.0');", true, true);
			this.dbDriver.updateQuery();
			
			this.dbDriver.buildQuery("CREATE TABLE #__mail \n");
			this.dbDriver.buildQuery("(id INTEGER PRIMARY KEY, sender VARCHAR(100) NOT NULL, \n", true);
			this.dbDriver.buildQuery("receiver VARCHAR(100) NOT NULL, message VARCHAR(100) NOT NULL);", true, true, false);
			this.dbDriver.updateQuery();
			
			plugin.log.log(Level.INFO, "Mail table successfully created!");
		}
		
		if(!this.dbDriver.tableExists("#__muted")){
			this.dbDriver.buildQuery("INSERT INTO #__versions \n");
			this.dbDriver.buildQuery("(tableName, version) \n", true);
			this.dbDriver.buildQuery("VALUES \n", true);
			this.dbDriver.buildQuery("('muted', '1.0');", true, true);
			this.dbDriver.updateQuery();
			
			this.dbDriver.buildQuery("CREATE TABLE #__muted \n");
			this.dbDriver.buildQuery("(id INTEGER PRIMARY KEY, owner VARCHAR(100) NOT NULL, \n", true);
			this.dbDriver.buildQuery("muted VARCHAR(100) NOT NULL);", true, true, false);
			this.dbDriver.updateQuery();
			
			plugin.log.log(Level.INFO, "Mute table successfully created!");
		}
	}
	
	private void dbUpdateMySQL() {
		//do some update stuff
		this.dbDriver.buildQuery("SELECT tableName, version FROM #__versions");
		ArrayList<HashMap<String, String>> res = this.mysql.execQuery();
		for(int i = 0; i < res.size(); i++) {
			HashMap<String, String> row = res.get(i);
			String table = row.get("tableName");
			Double version = Double.parseDouble(row.get("version"));
			
			if(table.equalsIgnoreCase("mail") && version < 1.0)
				dbUpdater.updateShop();
			else if(table.equalsIgnoreCase("muted") && version < 1.0)
				dbUpdater.updateItems();
		}
	}
	
	private void dbUpdateSQLite() {
		//do some update stuff
		this.dbDriver.buildQuery("SELECT tableName, version FROM #__versions");
		ArrayList<HashMap<String, String>> res = this.sqlite.execQuery();
		for(int i = 0; i < res.size(); i++) {
			HashMap<String, String> row = res.get(i);
			String table = row.get("tableName");
			Double version = Double.parseDouble(row.get("version"));
			
			if(table.equalsIgnoreCase("mail") && version < 1.0)
				dbUpdater.updateShop();
			else if(table.equalsIgnoreCase("muted") && version < 1.0)
				dbUpdater.updateItems();
		}
	}
	
	private void init() {
		driver = conf.getString("GiantPM.db.driver");
		
		if(driver.equalsIgnoreCase("MySQL")) {
			this.mysql = MySQL.Obtain();
			this.dbDriver = MySQL.Obtain();
			this.dbInitMySQL();
			this.dbUpdateMySQL();
		}else{
			this.sqlite = SQLite.Obtain();
			this.dbDriver = SQLite.Obtain();
			this.dbInitSQLite();
			this.dbUpdateSQLite();
		}
		
		db.instance = this;
	}
	
	public db(GiantPM plugin) {
		this.plugin = plugin;
		this.init();
	};
	
	public void buildQuery(String string) {
		buildQuery(string, false, false, false);
		return;
	}
	
	public void buildQuery(String string, Boolean add) {
		buildQuery(string, add, false, false);
		return;
	}
	
	public void buildQuery(String string, Boolean add, Boolean finalize) {
		buildQuery(string, add, finalize, false);
		return;
	}
	
	public void buildQuery(String string, Boolean add, Boolean finalize, Boolean debug) {
		this.dbDriver.buildQuery(string, add, finalize, debug);
	}
	
	public void buildQuery(String string, Integer add) {
		buildQuery(string, add, false, false);
		return;
	}
	
	public void buildQuery(String string, Integer add, Boolean finalize) {
		buildQuery(string, add, finalize, false);
		return;
	}
	
	public void buildQuery(String string, Integer add, Boolean finalize, Boolean debug) {
		this.dbDriver.buildQuery(string, add, finalize, debug);
	}
	
	public ArrayList<HashMap<String, String>> execQuery() {
		return this.dbDriver.execQuery();
	}
	
	public ArrayList<HashMap<String, String>> execQuery(Integer qID) {
		return this.dbDriver.execQuery(qID);
	}
	
	public void updateQuery() {
		this.dbDriver.updateQuery();
	}
	
	public void updateQuery(Integer qID) {
		this.dbDriver.updateQuery(qID);
	}
	
	public int countResult() {
		return this.dbDriver.countResult();
	}
	
	public int countResult(Integer qID) {
		return this.dbDriver.countResult(qID);
	}
	
	public iDriver select(String field) {
		return this.dbDriver.select(field);
	}
	
	public iDriver select(ArrayList<String> fields) {
		return this.dbDriver.select(fields);
	}
	
	public iDriver select(HashMap<String, String> fields) {
		return this.dbDriver.select(fields);
	}
	
	public iDriver from(String table) {
		return this.dbDriver.from(table);
	}
	
	public iDriver where(HashMap<String, String> fields) {
		return this.dbDriver.where(fields);
	}
	
	public iDriver where(HashMap<String, HashMap<String, String>> fields, Boolean shite) {
		return this.dbDriver.where(fields, shite);
	}
	
	public iDriver orderBy(HashMap<String, String> fields) {
		return this.dbDriver.orderBy(fields);
	}
	
	public iDriver limit(int limit) {
		return this.dbDriver.limit(limit);
	}
	
	public iDriver limit(int limit, Integer start) {
		return this.dbDriver.limit(limit, start);
	}
	
	public iDriver insert(String table, ArrayList<String> fields, ArrayList<HashMap<Integer, HashMap<String, String>>> values) {
		return this.dbDriver.insert(table, fields, values);
	}
	
	public iDriver update(String table) {
		return this.dbDriver.update(table);
	}
	
	public iDriver set(HashMap<String, String> fields) {
		return this.dbDriver.set(fields);
	}
	
	public iDriver set(HashMap<String, HashMap<String, String>> fields, Boolean shite) {
		return this.dbDriver.set(fields, shite);
	}
	
	public iDriver delete(String table) {
		return this.dbDriver.delete(table);
	}
	
	public iDriver Truncate(String table) {
		return this.dbDriver.Truncate(table);
	}
	
	public static db Obtain() {
		if(db.instance != null)
			return db.instance;
		
		return null;
	}
}
