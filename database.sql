create database shopdb;
show databases;
use shopdb;
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(100),
    total_sell DECIMAL(10, 2),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE order_product_matrix (
    order_id INT,
    product_id INT,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
ALTER TABLE order_product_matrix
ADD COLUMN quantity INT NOT NULL DEFAULT 1;

select * from products;
select * from orders;
select * from order_product_matrix;

drop table products;
