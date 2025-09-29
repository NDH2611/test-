DROP DATABASE my_classicmodels;
CREATE DATABASE my_classicmodels;
USE my_classicmodels;

CREATE TABLE productlines (
	productLine VARCHAR(50) PRIMARY KEY NOT NULL,
    textDescription VARCHAR(4000),
    htmlDescription MEDIUMTEXT,
    image MEDIUMBLOB
);

CREATE TABLE orders (
	orderNumber INT(11) PRIMARY KEY AUTO_INCREMENT,
    orderDate DATETIME NOT NULL,
    requiredDate DATETIME NOT NULL,
    shippedDate DATETIME,
    status VARCHAR(15) NOT NULL,
    comment TEXT,
    customerNumber INT(11) NOT NULL
);

CREATE TABLE products (
	productCode VARCHAR(15) PRIMARY KEY,
    productName VARCHAR(70) NOT NULL,
    productLine VARCHAR(50) NOT NULL,
    productScale VARCHAR(10) NOT NULL,
    productVendor VARCHAR(50) NOT NULL,
    productDesciption TEXT NOT NULL,
    quantityInStock SMALLINT(6) NOT NULL,
    buyPrice DOUBLE NOT NULL,
    CONSTRAINT fk_product_line_to_product FOREIGN KEY (productLine)
		REFERENCES productlines(productLine)
		ON DELETE RESTRICT
		ON UPDATE CASCADE
);

CREATE TABLE orderdetails (
	orderNumber INT(11) UNIQUE,
    productCode VARCHAR(15) UNIQUE,
    quantityOrdered INT(11) NOT NULL,
    priceEach DOUBLE,
    orderLineNumber SMALLINT(6) NOT NULL,
    PRIMARY KEY (orderNumber, productCode),
    CONSTRAINT fk_orders_to_orderdetails
	FOREIGN KEY (orderNumber) REFERENCES
	orders (orderNumber)
	ON DELETE RESTRICT
	ON UPDATE CASCADE,

	CONSTRAINT fk_product_to_order_detail
	FOREIGN KEY (productCode)
	REFERENCES products (productCode)
	ON DELETE RESTRICT
	ON UPDATE CASCADE
);
