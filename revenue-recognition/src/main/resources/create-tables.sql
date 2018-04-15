CREATE TABLE products (ID int primary key, name varchar, type varchar) 
CREATE TABLE contracts (ID int primary key, product int, revenue decimal, dateSigned date)
CREATE TABLE revenueRecognitions (contract int, amount decimal, recognizedOn date,
                                  PRIMARY KEY (contract, recognizedOn))