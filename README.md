# DBPhase3

Sql Code:

create database if not exists phase3;
use phase3;
CREATE TABLE resturant (
    resturant_id INT PRIMARY KEY,
    owner_name VARCHAR(35),
    resturant_name VARCHAR(35),
    location VARCHAR(40)
);
CREATE TABLE manu (
    menu_id INT PRIMARY KEY
);
CREATE TABLE branch (
    branch_id INT,
    phoneNumber VARCHAR(10),
    location VARCHAR(32),
    openinghours VARCHAR(10),
    menu_id INT,
    resturant_id INT,
    FOREIGN KEY (menu_id)
        REFERENCES manu (menu_id),
    FOREIGN KEY (resturant_id)
        REFERENCES resturant (resturant_id)
        ON DELETE CASCADE,
    PRIMARY KEY (branch_id , resturant_id)
);
CREATE TABLE employee (
    emp_id INT PRIMARY KEY,
    name VARCHAR(32),
    branch_id INT,
    salary INT,
    contactInfo VARCHAR(10),
    position VARCHAR(20)

);
CREATE TABLE DiningTable (
    capacity INT,
    table_id INT PRIMARY KEY,
    branch_id INT,
    FOREIGN KEY (branch_id)
        REFERENCES branch(branch_id)
);

