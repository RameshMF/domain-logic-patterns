INSERT INTO products (name, type) VALUES ('Product 1', 'Software Package');
INSERT INTO products (name, type) VALUES ('Product 2', 'Software Package');
INSERT INTO products (name, type) VALUES ('Product 3', 'Software Package');

INSERT INTO contracts (product_id, revenue, dateSigned) VALUES (1, 1000.0, DATE '2015-08-21');

INSERT INTO revenueRecognitions (contract_id, amount, recognizedOn) VALUES (1, 333.34, DATE '2015-08-21');
INSERT INTO revenueRecognitions (contract_id, amount, recognizedOn) VALUES (1, 333.34, DATE '2015-09-21');
INSERT INTO revenueRecognitions (contract_id, amount, recognizedOn) VALUES (1, 333.32, DATE '2015-10-21');
