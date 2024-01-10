CREATE TABLE IF NOT EXISTS migrations (
    version integer NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    full_day BOOLEAN NOT NULL,
    start_date DATETIME,
    end_date DATETIME,
    created_at DATETIME
);

CREATE TABLE IF NOT EXISTS todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    list INTEGER,
    completed_date DATETIME,
    time_required INTEGER
);

INSERT INTO migrations (version) VALUES (0);

-- TEST DATA
INSERT INTO events (title, description, full_day, start_date, end_date, created_at) VALUES ('Meeting', 'Project meeting with team', 0, '2023-01-15 00:00:00', '2023-01-15 00:00:00', '2023-01-01 00:00:00');
INSERT INTO events (title, description, full_day, start_date, end_date, created_at) VALUES ('Conference', 'Annual tech conference', 1, '2023-03-20 00:00:00', '2023-03-22 00:00:00', '2023-02-15 00:00:00');
INSERT INTO events (title, description, full_day, start_date, end_date, created_at) VALUES ('Workshop', 'Workshop on Java Programming', 0, '2023-05-05 00:00:00', '2023-05-05 00:00:00', '2023-04-10 00:00:00');