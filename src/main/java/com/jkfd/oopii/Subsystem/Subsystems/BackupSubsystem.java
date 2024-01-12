package com.jkfd.oopii.Subsystem.Subsystems;

import com.jkfd.oopii.Subsystem.Subsystem;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

/**
 * The event reminder subsystem periodically checks if there is an upcoming event, and notifies the user accordingly.
 */
public class BackupSubsystem extends Subsystem {
    public BackupSubsystem() {
        this.name = "backup";
        this.fireTime = TimeUnit.MINUTES.toMillis(15);
    }

    @Override
    public void Fire() {
        try {
            File directory = new File("./data/backup");
            if (!directory.exists()) {
                boolean result = directory.mkdirs();
                if (!result) {
                    throw new Exception("data directory could not be created.");
                }
            }

            File f = new File("./data/app.db");
            if(!f.exists()) {
                LoggerFactory.getLogger(EventReminderSubsystem.class).info("Database does not exist yet, skipping backup.");
                return;
            }

            Path target = Paths.get("./data/backup/app.db.bak");
            Path source = Paths.get("./data/app.db");

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            File bak = new File("./data/backup/app.db.bak");
            if(!bak.exists()) {
                throw new FileNotFoundException("backup was not created properly");
            }

            LoggerFactory.getLogger(EventReminderSubsystem.class).info("Database backed up.");
        } catch(Exception e) {
            super.HandleException(e);
        }
    }
}
