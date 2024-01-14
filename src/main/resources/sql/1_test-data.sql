-- DO NOT REMOVE THIS LINE
UPDATE migrations SET version = 1 WHERE version = 0;

-- PUT YOUR SQL BELOW THIS POINT
INSERT INTO events (title, description, priority, full_day, start_date, end_date, created_at) VALUES ('Meeting', 'Project meeting with team', 1, 0, '2024-01-10T15:00:00', '2024-01-10T16:00:00', '2024-01-01T15:00:00');
INSERT INTO events (title, description, priority, full_day, start_date, end_date, created_at) VALUES ('Conference', 'Annual tech conference', 1, 1, '2024-01-10T17:00:00', '2024-01-11T01:00:00', '2024-01-01T15:00:00');
INSERT INTO events (title, description, priority, full_day, start_date, end_date, created_at) VALUES ('Workshop', 'Workshop on Java Programming', 1, 0, '2024-01-15T15:00:00', '2024-01-15T17:30:00', '2024-01-01T15:00:00');

INSERT INTO todos (title, description, priority) VALUES ('Test Todo', 'A test todo to test some features', 1);