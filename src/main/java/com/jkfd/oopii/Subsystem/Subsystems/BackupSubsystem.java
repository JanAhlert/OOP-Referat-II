package com.jkfd.oopii.Subsystem.Subsystems;

import com.jkfd.oopii.Subsystem.Subsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

/**
 * The backup subsystem runs every 15 minutes, copying the database to ./data/backup/app.db.bak.
 */
public class BackupSubsystem extends Subsystem {
    public BackupSubsystem() {
        this.name = "backup";
        this.fireTime = TimeUnit.MINUTES.toMillis(15);
    }

    @Override
    public void Fire() {
        try {
            // Make sure backup directory exists.
            File directory = new File("./data/backup");
            if (!directory.exists()) {
                boolean result = directory.mkdirs();
                if (!result) {
                    throw new Exception("data directory could not be created.");
                }
            }

            // Make sure that the application database was already created.
            // Ignore the current fire if it wasn't.
            File f = new File("./data/app.db");
            if(!f.exists()) {
                logger.atInfo().setMessage("Database does not exist yet, skipping backup.").log();
                return;
            }

            // Get the source and target path for the backup operation.
            Path target = Paths.get("./data/backup/app.db.bak");
            Path source = Paths.get("./data/app.db");

            // Copy and basically "backup" the database in the backup directory.
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            File bak = new File("./data/backup/app.db.bak");
            if(!bak.exists()) {
                throw new FileNotFoundException("backup was not created properly");
            }

            logger.atInfo().setMessage("Database backed up.").log();
        } catch(Exception e) {
            super.HandleException(e);
        }
    }
}
