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
    start_date TEXT,
    end_date TEXT,
    created_at TEXT
);

CREATE TABLE IF NOT EXISTS todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    priority INTEGER NOT NULL,
    list INTEGER,
    completed_date TEXT,
    time_required INTEGER
);

INSERT INTO migrations (version) VALUES (0);