-- select database

use finances;

-- RESET DATABASE  TABLES

drop table EXPENSES;
drop table INCOME;
drop table USERS;

-- ADD TABLES

CREATE TABLE USERS (
    USERID varchar(20),
    username varchar(30),
    password varchar(20),
    name varchar(30),
    email varchar(30),
    PRIMARY KEY (USERID)
);

CREATE TABLE EXPENSES (
    expenseid int,
    USERID varchar(20),
    date varchar(20),
    type varchar(20),
    amount varchar(20),
    notes varchar(100),
    PRIMARY KEY (expenseID)
);

ALTER TABLE
  finances.EXPENSES
ADD CONSTRAINT FK_UserExpense
  FOREIGN KEY (USERID) REFERENCES USERS (userID) ON DELETE CASCADE;

CREATE TABLE INCOME (
    INCOMEID int,
    USERID varchar(20),
    date varchar(20),
    type varchar(20),
    amount varchar(20),
    notes varchar(100),
    PRIMARY KEY (INCOMEID)
);
ALTER TABLE
  finances.INCOME
ADD CONSTRAINT FK_UserIncome
  FOREIGN KEY (USERID) REFERENCES USERS (userID) ON DELETE CASCADE;

---- ADD DATA TO TABLES

---- USERS

INSERT INTO USERS (userid, username, password, name, email)
VALUES ('1', 'Matt77','m', 'Mateo Paizano','matt@yahoo.com');
INSERT INTO USERS (userid, username, password, name, email)
VALUES ('2', 'Ali','p', 'Alibaba','Ali@yahoo.com');
INSERT INTO USERS (userid, username, password, name, email)
VALUES ('3', 'Saratoga22','Springs', 'Sandy Cave','Candy@gmail.com');
INSERT INTO USERS (userid, username, password, name, email)
VALUES ('4', 'SantaHasAgift','hohoho', 'Harry Santa','harry@aol.com');

---- EXPENSES

INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (1, '1','2020/06/22', 'Restaurant','22.50', 'Me and the girl');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (2, '1','2020/07/22', 'Groceries','35.50', 'I was hungry');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (3, '1','2020/05/22', 'Groceries','70.99', 'I bought lots of guava');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (4, '1','2020/04/22', 'Groceries','52.86', 'Papaya...');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (5, '2','2020/06/21', 'Groceries','27.19', 'None');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (6, '2','2020/06/25', 'Groceries','16.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (7, '1','2020/07/22', 'Groceries','70.99', 'I bought a lot of food. I was really hungry.  Attempt to fill note with a lot of crap.');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (8, '2','2020/06/25', 'Groceries','16.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (9, '4','2020/06/25', 'gifts','16.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (10, '4','2019/08/25', 'tools','176.32', 'elf tools');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (11, '2','2019/06/25', 'Groceries','167.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (12, '4','2020/07/25', 'Groceries','186.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (13, '2','2020/07/25', 'Groceries','16.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (14, '4','2020/06/25', 'Groceries','161.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (15, '2','2020/01/25', 'Groceries','126.32', 'lots of yumm');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (16, '4','2020/01/25', 'candy','16.32', 'yumm');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (17, '3','2020/02/15', 'Factory','416.32', 'factory supplies');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (18, '3','2020/06/15', 'Groceries','126.32', 'TV-dinners');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (19, '3','2020/02/15', 'Motorcycle','160000.32', 'I like this');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (20, '3','2020/06/15', 'Groceries','16.32', 'apples');
INSERT INTO EXPENSES (EXPENSEid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (21, '3','2020/02/15', 'Groceries','146.32', 'guava');
-- INCOME

INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (1, '1','2020/06/01', 'pay day','1700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (2, '2','2020/06/01', 'pay day','5700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (3, '1','2020/07/01', 'pay day','1700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (4, '2','2020/07/01', 'pay day','5700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (5, '2','2020/08/01', 'pay day','5700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (6, '1','2020/07/01', 'pay day','1700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (7, '1','2020/08/01', 'pay day','1700.23', 'typical payday');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (8, '1','2020/06/22', 'stocks','1700.23', 'Sold my tesla stock!');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (9, '3','2020/06/22', 'pay','3700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (10, '4','2020/06/22', 'pay','5700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (11, '4','2020/06/22', 'pay','5700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (12, '4','2020/06/22', 'pay','5700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (13, '3','2020/06/22', 'pay','2700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (14, '4','2020/06/22', 'pay','1700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (15, '3','2020/06/22', 'pay','2700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (16, '3','2020/06/22', 'pay','2700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (17, '3','2020/06/22', 'pay','2700.23', 'From my job');
INSERT INTO INCOME (incomeid, USERID, DATE, TYPE, AMOUNT, NOTES)
VALUES (18, '2','2020/06/22', 'pay','5700.23', 'From my job');




select * from USERS;
select * from EXPENSES;
select * from INCOME;

