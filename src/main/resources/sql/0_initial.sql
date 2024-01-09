CREATE TABLE IF NOT EXISTS migrations (
    version integer NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id integer PRIMARY KEY AUTOINCREMENT NOT NULL,
    title text NOT NULL,
    description text,
    full_day boolean NOT NULL,
    Start_date text,
    End_date text,
    Created_at text
);

CREATE TABLE IF NOT EXISTS todos (
    id integer PRIMARY KEY AUTOINCREMENT NOT NULL,
    title text NOT NULL,
    description text,
    List integer,
    CompletedDate text,
    TimeRequired text
);

INSERT INTO migrations (version) VALUES (0);

-- TEST DATA
INSERT INTO events (title, description, full_day, Start_date, End_date, Created_at) VALUES ('Meeting', 'Project meeting with team', 0, '2023-01-15', '2023-01-15', '2023-01-01');
INSERT INTO events (title, description, full_day, Start_date, End_date, Created_at) VALUES ('Conference', 'Annual tech conference', 1, '2023-03-20', '2023-03-22', '2023-02-15');
INSERT INTO events (title, description, full_day, Start_date, End_date, Created_at) VALUES ('Workshop', 'Workshop on Java Programming', 0, '2023-05-05', '2023-05-05', '2023-04-10');