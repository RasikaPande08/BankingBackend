CREATE TABLE account (
    cin BIGINT,
    customerid VARCHAR(50),
    password VARCHAR(100),
    accountNumber VARCHAR(30),
    accountType VARCHAR(30),
    createdAt TIMESTAMP,
    balance VARCHAR(20),
    PRIMARY KEY (cin)
);

CREATE TABLE transactions (
    transaction_id BIGINT AUTO_INCREMENT,
    accountNumber VARCHAR(30),
    transaction_type VARCHAR(20),   -- e.g., 'Credit', 'Debit', 'Transfer'
    amount DECIMAL(15,2),
    transaction_date TIMESTAMP,
    description VARCHAR(255),
    status VARCHAR(20),             -- e.g., 'Success', 'Failed', 'Pending'
    PRIMARY KEY (transaction_id)
);

CREATE TABLE sms_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50),
    timestamp TIMESTAMP
);
