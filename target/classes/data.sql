INSERT INTO account VALUES (1001, '001', 'pass123', 'ACC1001', 'Savings', '2025-01-01 10:00:00', '45000.50');
INSERT INTO account VALUES (1002, '002', 'pass234', 'ACC1002', 'Current', '2025-02-15 11:30:00', '120000.75');
INSERT INTO account VALUES (1003, '003', 'pass345', 'ACC1003', 'Savings', '2025-03-20 09:45:00', '8700.00');
INSERT INTO account VALUES (1004, '004', 'pass456', 'ACC1004', 'Fixed',   '2025-04-05 14:20:00', '250000.00');
INSERT INTO account VALUES (1005, '005', 'pass567', 'ACC1005', 'Current', '2025-05-12 16:00:00', '33000.85');
INSERT INTO account VALUES (1006, '006', 'pass678', 'ACC1006', 'Savings', '2025-06-22 08:00:00', '55600.10');
INSERT INTO account VALUES (1007, '007', 'pass789', 'ACC1007', 'Fixed',   '2025-07-18 17:40:00', '110000.00');
INSERT INTO account VALUES (1008, '008', 'pass890', 'ACC1008', 'Current', '2025-08-03 12:15:00', '9100.20');
INSERT INTO account VALUES (1009, '009', 'pass901', 'ACC1009', 'Savings', '2025-09-25 13:30:00', '76550.00');
INSERT INTO account VALUES (1010, '010', 'pass012', 'ACC1010', 'Fixed',   '2025-10-10 19:10:00', '200000.00');


INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1001', 'Credit', 7500.00, '2025-07-01 09:00:00', 'Monthly Salary', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1001', 'Debit', 1200.50, '2025-07-02 15:30:00', 'Mobile Recharge', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1001', 'Debit', 3200.00, '2025-07-04 11:20:00', 'Rent Payment', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1001', 'Credit', 500.00, '2025-07-06 17:10:00', 'Cashback Bonus', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1001', 'Debit', 860.75, '2025-07-08 20:00:00', 'Grocery Purchase', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1002', 'Debit', 1200.50, '2025-07-03 14:20:00', 'Online Shopping', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1003', 'Credit', 3000.00, '2025-07-05 09:00:00', 'Cash Deposit', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1004', 'Debit', 250.00, '2025-07-07 12:45:00', 'ATM Withdrawal', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1005', 'Debit', 650.75, '2025-07-08 16:10:00', 'Utility Bill Payment', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1006', 'Credit', 10000.00, '2025-07-10 11:30:00', 'Loan Disbursement', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1007', 'Debit', 350.00, '2025-07-11 18:20:00', 'Restaurant Payment', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1008', 'Credit', 2000.00, '2025-07-12 08:55:00', 'Refund Received', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1009', 'Debit', 980.00, '2025-07-13 13:40:00', 'Insurance Premium', 'Success');

INSERT INTO transactions (accountNumber, transaction_type, amount, transaction_date, description, status)
VALUES ('ACC1010', 'Debit', 1750.25, '2025-07-14 20:15:00', 'EMI Payment', 'Success');
