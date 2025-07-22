create database shopdb;
show databases;
use shopdb;
CREATE TABLE products (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

select * from products;

drop table products;
