INSERT INTO plan_tbl (plan_id, speed, price)
VALUES 
    (0, '12GB', 500),
    (1, '24GB', 800),
    (3, '48GB', 1100),
    (4, '80GB', 1500)
ON CONFLICT (plan_id) DO NOTHING;

-- This syncs the counter to the highest ID currently in the table
SELECT setval(pg_get_serial_sequence('plan_tbl', 'plan_id'), coalesce(max(plan_id), 0) + 1, false) FROM plan_tbl;