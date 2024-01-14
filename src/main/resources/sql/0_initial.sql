PRAGMA journal_mode=WAL;

CREATE TABLE IF NOT EXISTS migrations (
    version integer NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    priority INTEGER NOT NULL,
    full_day BOOLEAN NOT NULL,
    start_date DATETIME,
    end_date DATETIME,
    created_at DATETIME
);

CREATE TABLE IF NOT EXISTS todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    priority INTEGER NOT NULL,
    list INTEGER,
    completed_date DATETIME,
    time_required INTEGER
);

INSERT INTO migrations (version) VALUES (0);