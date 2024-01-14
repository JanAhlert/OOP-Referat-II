-- DO NOT REMOVE THIS LINE
UPDATE migrations SET version = 1 WHERE version = 0;

-- PUT YOUR SQL BELOW THIS POINT
INSERT INTO events (title, description, priority, full_day, start_date, end_date, created_at) VALUES ('Meeting', 'Project meeting with team', 1, 0, '1704541872000', '1704541872000', '1704541872000');
INSERT INTO events (title, description, priority, full_day, start_date, end_date, created_at) VALUES ('Conference', 'Annual tech conference', 1, 1, '1704541872000', '1704541872000', '1704541872000');
INSERT INTO events (title, description, priority, full_day, start_date, end_date, created_at) VALUES ('Workshop', 'Workshop on Java Programming', 1, 0, '1704541872000', '1704541872000', '1704541872000');