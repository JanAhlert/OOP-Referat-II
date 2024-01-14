#set( $MigrateName = "change-my-title" )
#set( $MigrateVersion = -1 )
#set( $OldMigrateVersion = $MigrateVersion - 1)

-- DO NOT REMOVE THIS LINE
UPDATE migrations SET version = ${MigrateVersion} WHERE version = ${$OldMigrateVersion};

-- PUT YOUR SQL BELOW THIS POINT