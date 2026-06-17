-- 1. Status Table (No dependencies)
CREATE TABLE IF NOT EXISTS status_tbl (
    status INT PRIMARY KEY,
    status_name VARCHAR(50)
);

-- 2. Plan Table (No dependencies)
CREATE TABLE IF NOT EXISTS plan_tbl (
    plan_id SERIAL PRIMARY KEY,
    price FLOAT NOT NULL,
    speed VARCHAR(50) NOT NULL
);

-- 3. Customers Table (Depends on status_tbl and plan_tbl)
CREATE TABLE IF NOT EXISTS customers_tbl (
    cust_id SERIAL PRIMARY KEY,
    f_name VARCHAR(100) NOT NULL,
    l_name VARCHAR(100) NOT NULL,
    
    -- Embedded Address Fields
    house_number VARCHAR(50),
    street VARCHAR(100),
    barangay VARCHAR(100),
    city VARCHAR(100),
    
    -- Foreign Keys
    plan_id INT,
    status_id INT,
    
    -- Business Logic Fields
    due_date DATE,
    
    -- Automated Timestamps
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT fk_plan FOREIGN KEY (plan_id) REFERENCES plan_tbl(plan_id),
    CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES status_tbl(status)
);

-- 4. Payment Log Table
CREATE TABLE IF NOT EXISTS payment_log_tbl (
    bill_id SERIAL PRIMARY KEY,
    cust_id INT NOT NULL,
    amount DECIMAL(19, 4) NOT NULL DEFAULT 0.00,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_customer
        FOREIGN KEY(cust_id) 
        REFERENCES customers_tbl(cust_id)
        ON DELETE CASCADE
);