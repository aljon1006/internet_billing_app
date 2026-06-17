INSERT INTO status_tbl (status, status_name)
VALUES 
    (0, 'Active'),
    (1, 'Inactive')
ON CONFLICT (status) DO NOTHING;