CREATE DATABASE bnbase;
use bnbase;
create table listings (
	listno int,
    housetype varchar(32) default "house",
    longtitude int default 0,
    latitude int default 0,
    address varchar(32),
    postal int,
    city varchar(32),
    country varchar(32),
    primary key(listno)
);
create table listing_comments (
	listno int references listings(listno),
    comments varchar(128) default "",
    primary key (listno, comments)
);
CREATE table user (
	sin int,
    uname varchar(32),
    job varchar(32),
    address varchar(32),
    dob date,
    primary key(sin),
    constraint check_birth check (dob <= "2002-8-8")
);
CREATE table REVIEWS (
	reviewer int,
    reviewee int,
    rating int,
    comment varchar(64) DEFAULT "No Comment",
    type varchar(32) DEFAULT "--",
    primary key(reviewer, reviewee),
    CHECK (rating <= 5 and rating >= 1)
);
CREATE TABLE AMENITIES (
	listno int NOT NULL PRIMARY KEY REFERENCES LISTINGS(listno),
    AC int default 0,
    Heat int default 0,
	Lake int default 0,
	Pets int default 0,
	Internet int default 0   
);
CREATE TABLE CALENDAR (
	listno int NOT NULL REFERENCES LISTINGS(listno),
    Date_Start DATE,
    Date_End DATE,
    Price real,
    CHECK (Date_Start < Date_End),
    PRIMARY KEY (listno, date_start, date_end)
);
Create table renter (
	sin int not null primary key references user(sin),
    cc_no int
);
CREATE TABLE OWNERSHIP (
	sin int references user(sin),
	listno int references listings(listno),
    primary key (sin, listno)
);
Create TABLE House_History (
	listno int references listings(listno),
    sin int references user(sin),
    primary key (listno, sin)
);
Create table Renter_History (
	sin int references user(sin),
    rentedlistno int references listings(listno),
    ispast varchar(32) DEFAULT "Past",
    primary key (sin, rentedlistno)
);
CREATE TABLE BOOKING (
	listno int references listing(listno),
    renterSIN int references user(SIN),
    Date_Start DATE,
    Date_End DATE,
    cancel varchar(32) default "no",
    primary key (listno, renterSIN)
);
insert into listings values (1, "house", 2, 4, "99 Yonge St.",12345, "Toronto", "Canada");
insert into listings values (2, "house", 2, 4, "792 Yonge St.",13335, "Toronto", "Canada");
insert into listings values (3, "apartment", 5, 5, "501 Von Rd.",15000, "Toronto", "Canada");
insert into listings values (4, "bedroom", 2, 2, "1 Yonge St.",12000, "Toronto", "Canada");
insert into listings values (5, "cottage", 2, 3, "26 Sheppard Ave.",13000, "Toronto", "Canada");
insert into listings values (6, "apartment", 25, 25, "76 Houston Wk.",60000, "Texas", "USA");
insert into listings values (7, "apartment", 25, 30, "575 Houston Wk.",62000, "Texas", "USA");
insert into listings values (8, "house", 17, 31, "1054 Rant Rd.",57500, "Texas", "USA");
insert into listings values (9, "cottage", 26, 24, "202 Houston Wk.",61052, "Texas", "USA");
insert into listings values (10, "bedroom", 23, 22, "888 Wentworth Ln.",59332, "Texas", "USA");
insert into listings values (11, "duplex", 3, 22, "7 Yonge St.",12000, "Toronto", "Canada");
insert into listings values (12, "mansion", -14, 0, "278 Herman Ln.",12000, "Calgary", "Canada");
insert into listings values (13, "house", -15, 1, "12 Herman Ln.",12000, "Calgary", "Canada");
insert into listings values (14, "apartment", -16, 1, "845 Herman Ln.",12000, "Calgary", "Canada");

insert into listing_comments values (1, "An interesting shelf of books lay on the top floor. Lots and lots of books, books everywhere books could be.");
insert into listing_comments values (1, "Curious amount of books. All kinds of books, maybe this used to be a library?");
insert into listing_comments values (2, "very colorful. colorful walls, colorful floors. that's what this house is.");

insert into user values (1111,"John L", "politician", "22 Lane", "1990-10-30");
insert into user values (1001,"Jason", "firefighter", "13 Lane", "1995-7-22");
insert into user values (1002,"Erick", "police", "56 Lane", "2000-1-1");
insert into user values (1003,"Van", "mayor", "999 Avenue", "1977-2-2");
insert into user values (1004,"Donte", "salesman", "800 Avenue", "1996-3-3");
insert into user values (1005,"Herminia", "secretary", "250 Avenue", "1952-4-4");
insert into user values (1006,"Freda", "cashier", "777 Avenue", "1982-5-5");
insert into user values (1007,"Jeri", "soldier", "62 Road", "1979-6-6");
insert into user values (1008,"Caroline", "teacher", "1 Road", "1989-7-7");
insert into user values (1009,"Willa", "guard", "1052 Road", "2001-8-8");

insert into AMENITIES values (1, 1, 0, 0, 0, 1);
insert into AMENITIES values (2, 1, 1, 1, 1, 1);
insert into AMENITIES values (3, 1, 1, 0, 1, 1);
insert into AMENITIES values (4, 1, 0, 0, 0, 1);
insert into AMENITIES values (5, 1, 1, 0, 0, 1);
insert into AMENITIES values (6, 1, 1, 0, 1, 1);
insert into AMENITIES values (7, 0, 0, 1, 0, 0);
insert into AMENITIES values (8, 1, 1, 0, 0, 1);
insert into AMENITIES values (9, 1, 1, 1, 1, 1);
insert into AMENITIES values (10, 1, 1, 1, 1, 1);
insert into AMENITIES values (11, 0, 1, 1, 1, 1);
insert into AMENITIES values (12, 1, 1, 1, 0, 1);
insert into AMENITIES values (13, 1, 1, 0, 0, 1);
insert into AMENITIES values (14, 1, 1, 0, 1, 0);

insert into CALENDAR values (1,"2022-1-1","2022-6-6",1673);
insert into CALENDAR values (2,"2021-1-1","2022-9-9",999);
insert into CALENDAR values (3,"2022-5-3","2022-12-30",600);
insert into CALENDAR values (4,"2021-11-16","2024-7-22",750);
insert into CALENDAR values (5,"2022-2-15","2023-5-24",2000);
insert into CALENDAR values (6,"2022-12-31","2023-1-29",1100);
insert into CALENDAR values (9,"2022-9-1","2023-1-1",750);
insert into CALENDAR values (10,"2022-8-1","2023-1-20",980);
insert into CALENDAR values (11,"2022-8-3","2023-2-19",1400);
insert into CALENDAR values (12,"2022-8-4","2023-3-12",333);
insert into CALENDAR values (13,"2022-8-5","2023-4-22",500);
insert into CALENDAR values (14,"2022-8-6","2023-5-28",600);

insert into booking values (4,1003,'2022-8-9','2022-10-9',"no");
insert into booking values (2,1004,'2022-8-10','2022-9-10',"no");
insert into booking values (3,1004,'2022-9-11','2022-9-28',"no");
insert into booking values (6,1007,'2022-8-11','2022-12-30',"no");
insert into booking values (7,1009,'2023-1-6','2023-3-6',"no");
insert into booking values (9,1000,'2022-9-2','2022-11-11',"yes");
insert into booking values (10,1002,'2022-10-10','2022-10-12',"yes");

insert into ownership values (1111,1);
insert into ownership values (1111,2);
insert into ownership values (1111,3);
insert into ownership values (1005,4);
insert into ownership values (1111,5);
insert into ownership values (1005,6);
insert into ownership values (1005,7);
insert into ownership values (1005,8);
insert into ownership values (1111,9);
insert into ownership values (1111,10);
insert into ownership values (1005,11);
insert into ownership values (1002,12);
insert into ownership values (1001,13);
insert into ownership values (1007,14);

insert into House_History values (1,2222);
insert into House_History values (1,1002);
insert into House_History values (1,1003);
insert into House_History values (1,1004);
insert into House_History values (1,1008);
insert into House_History values (2,1002);
insert into House_History values (3,1003);
insert into House_History values (4,1004);
insert into House_History values (6,1006);
insert into House_History values (7,1007);
insert into House_History values (8,1008);
insert into House_History values (9,1009);

insert into renter_history values (2222,1,"Past");
insert into renter_history values (1002,1,"Past");
insert into renter_history values (1003,1,"Past");
insert into renter_history values (1004,1,"Past");
insert into renter_history values (1008,1,"Past");
insert into renter_history values (1002,2,"Past");
insert into renter_history values (1003,3,"Past");
insert into renter_history values (1004,4,"Past");
insert into renter_history values (1006,6,"Past");
insert into renter_history values (1007,7,"Past");
insert into renter_history values (1008,8,"Past");
insert into renter_history values (1009,9,"Past");

insert into RENTER values (2222, 9603);
insert into RENTER values (1002, 1212);
insert into RENTER values (1003, 1313);
insert into RENTER values (1004, 1414);
insert into RENTER values (1006, 2626);
insert into RENTER values (1007, 3737);
insert into RENTER values (1008, 4848);
insert into RENTER values (1009, 5959);

insert into REVIEWS values (1009,1111, 3, "This landlord was alright, I guess.", "Host Review");
insert into REVIEWS values (1001,1005, 5,"Very nice and was helpful in lowering the rent cost.", "Host Review");
insert into REVIEWS values (1111, 2222, 4,"They kept my room clean, for the most part.", "Renter Review");
insert into REVIEWS values (1003, 3, 5,"I enjoyed the garden. It had lots of flowers.","BNB Review");






