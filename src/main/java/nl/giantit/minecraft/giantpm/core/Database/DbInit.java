package nl.giantit.minecraft.giantpm.core.Database;

import java.util.ArrayList;
import nl.giantit.minecraft.giantcore.database.Driver;
import nl.giantit.minecraft.giantcore.database.query.Column;
import nl.giantit.minecraft.giantcore.database.query.CreateQuery;
import nl.giantit.minecraft.giantcore.database.query.InsertQuery;
import nl.giantit.minecraft.giantpm.GiantPM;

/**
 *
 * @author Jake
 */
public class DbInit {

    private final GiantPM plugin;
    private final Driver db;

    public DbInit(GiantPM plugin) {
        this.plugin = plugin;
        this.db = plugin.getDB().getEngine();
    }

    public void initialize() {
        if (!this.db.tableExists("#__versions")) {
            CreateQuery versionsTable = this.db.create("#__versions");
            Column tN = versionsTable.addColumn("tableName");
            tN.setDataType(Column.DataType.VARCHAR);
            tN.setLength(100);
            Column v = versionsTable.addColumn("version");
            v.setDataType(Column.DataType.DOUBLE);
            v.setDefault("1.0");

            versionsTable.exec();

            this.plugin.getLogger().info("Revisions table was successfully created!");
        }
        
        if (!this.db.tableExists("#__mail")) {
            InsertQuery mailIns = this.db.insert("#__versions");
            ArrayList<String> fields = new ArrayList<String>();
            fields.add("tableName");
            fields.add("version");
            mailIns.addFields(fields);
            mailIns.addRow();
            mailIns.assignValue("tableName", "mail");
            mailIns.assignValue("version", "1.0");

            mailIns.exec();

            CreateQuery mailCre = this.db.create("#__mail");
            Column id = mailCre.addColumn("id");
            id.setDataType(Column.DataType.INT);
            id.setLength(3);
            id.setPrimaryKey();
            id.setAutoIncr();
            
            Column sender = mailCre.addColumn("sender");
            sender.setDataType(Column.DataType.VARCHAR);
            sender.setLength(100);
            
            Column receiver = mailCre.addColumn("receiver");
            receiver.setDataType(Column.DataType.VARCHAR);
            receiver.setLength(100);
            
            Column message = mailCre.addColumn("message");
            message.setDataType(Column.DataType.VARCHAR);
            message.setLength(100);
            
            mailCre.exec();
            
            this.plugin.getLogger().info("Mail table was successfully created!");
        }
        
        if (!this.db.tableExists("#__muted")) {
            InsertQuery mutedIns = this.db.insert("#__versions");
            ArrayList<String> fields = new ArrayList<String>();
            fields.add("tableName");
            fields.add("version");
            mutedIns.addFields(fields);
            mutedIns.addRow();
            mutedIns.assignValue("tableName", "muted");
            mutedIns.assignValue("version", "1.0");
            
            mutedIns.exec();
            
            CreateQuery mutedCre = this.db.create("#__muted");
            Column id = mutedCre.addColumn("id");
            id.setDataType(Column.DataType.INT);
            id.setLength(3);
            id.setPrimaryKey();
            id.setAutoIncr();
            
            Column owner = mutedCre.addColumn("owner");
            owner.setDataType(Column.DataType.VARCHAR);
            owner.setLength(100);
            
            Column muted = mutedCre.addColumn("muted");
            muted.setDataType(Column.DataType.VARCHAR);
            muted.setLength(100);
            
            mutedCre.exec();
            
            this.plugin.getLogger().info("Muted table was successfully created!");
        }
    }
}
