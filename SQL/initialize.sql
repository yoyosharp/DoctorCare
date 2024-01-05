drop database if exists prj321_asm03;
create database prj321_asm03 charset 'utf8mb4';
use prj321_asm03;

create table role (
	id int not null auto_increment,
    name varchar(50),
    primary key(id)
);

insert into role(name) values ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_DOCTOR');

create table specialization(
	id int not null auto_increment,
    name varchar(50),
    image varchar(255),
    description text,
    visited int default 0,
    booked int default 0,
    primary key(id)
);

insert into specialization(name)
values ('specialization 1'), ('specialization 2');

update specialization set description = 'Demo specialization';
update specialization set image = '/image/demo-specialization';

create table location(
	id int auto_increment,
    name varchar(50),
    primary key(id)
);

insert into location(name) values ("Location 1"), ("Location 2");

create table clinic(
	id int not null auto_increment,
    name varchar(255),
	address varchar(255),
    location_id int,
    phone varchar(50),
    image varchar(255),
    description text,
    booked int default 0,
    primary key(id),
    foreign key(location_id) references location(id)
);

insert into clinic(name) 
values ('Clinic 1'), ('Clinic 2');

update clinic set address = concat('Demo location ', id);
update clinic set location_id = 1 + floor(2*rand());
update clinic set phone = id*100000000;
update clinic set image = '/image/demo-clinic';
update clinic set description = 'Demo clinic';

create table user (
	id int not null auto_increment,
    name varchar(255),
    email varchar(255),
    password varchar(60),
    address varchar(255),
    phone varchar(50),
    gender varchar(10),
    avatar varchar(255),
    enabled tinyint,
    non_locked tinyint default 1,
    create_at date,
    lock_detail varchar(255),
    primary key(id)
);

insert into user(name, email, password, enabled)
values ('demo admin', 'admin@admin.localhost', '$2a$10$ZIOxyG8.burmV2KLHu/5YuwBSIaomHrArLZtXQUOJQE/k4xh/iV6O', 1),
		('demo user', 'user@user.localhost', '$2a$10$AjyrfmYhA8fikdxILZMs4.aRf75PMmumACT7oyxV5xm2OdvIVdfLq', 1),
        ('demo doctor 1', 'doctor1@doctor.localhost', '$2a$10$zD8AjJXrLVluqqN0hcureebbXQ0.5Sf4EBs53ACFrF.1hY7bB5fQS', 1),
        ('demo doctor 2', 'doctor2@doctor.localhost', '$2a$10$zD8AjJXrLVluqqN0hcureebbXQ0.5Sf4EBs53ACFrF.1hY7bB5fQS', 1),
        ('demo doctor 3', 'doctor3@doctor.localhost', '$2a$10$zD8AjJXrLVluqqN0hcureebbXQ0.5Sf4EBs53ACFrF.1hY7bB5fQS', 1);

update user set address = concat('demo addresss ', id);
update user set phone = id*111111111;
update user set gender = 'Male';
update user set avatar = '/image/anonymous-user';
update user set create_at = current_date();

create table authorities(
    user_id int,
    role_id int,
    primary key(user_id, role_id),
    foreign key(user_id) references user(id),
    foreign key(role_id) references role(id)
);

insert into authorities(user_id, role_id) values (1, 1), (2, 2), (3, 2), (3, 3), (4, 2), (4, 3), (5, 2), (5, 3);

create table doctor(
	id int auto_increment,
    user_id int,
    clinic_id int,
    specialization_id int,
    description text,
    education text,
    achievement text,
    primary key(id),
    foreign key(user_id) references user(id),
    foreign key(clinic_id) references clinic(id),
    foreign key(specialization_id) references specialization(id)
);

insert into doctor(user_id, clinic_id, specialization_id) values (3, 1, 1), (4, 1, 2), (5, 2, 2);
update doctor set education = 'Master';
update doctor set achievement = 'Achievement';
update doctor set description = 'Demo doctor created from database';

create table time_slot(
	id int auto_increment,
    value varchar(50),
    primary key(id)
);

insert into time_slot(value) values ('8:00 - 9:00'), ('9:00 - 10:00'), ('10:00 - 11:00'), ('11:00 - 12:00'),
									('13:00 - 14:00'), ('14:00 - 15:00'), ('15:00 - 16:00'), ('16:00 - 17:00');

create table category(
	id int auto_increment,
    name varchar(50),
    primary key(id)
);
-- i.e Khám, xét nghiệm, Khám bệnh tại nhà... -- 

insert into category(name) values ("Category 1"), ("Category 2");

create table price_range(
	id int auto_increment,
    `range` varchar(50),
    primary key(id)
);

insert into price_range(`range`)
values ("Cheap"), ("Affordable"), ("Expensive"), ("Very expensive");

create table post(
	id int auto_increment,
    title varchar(255),
    description text,
    image varchar(255),
    price varchar(255),
    price_range_id int,
    specialization_id int,
    clinic_id int,
    doctor_id int,
    category_id int,
    booked int default 0,
    primary key(id),
    foreign key(price_range_id) references price_range(id),
    foreign key(doctor_id) references doctor(id),
    foreign key(clinic_id) references clinic(id),
    foreign key(category_id) references category(id),
    foreign key(specialization_id) references specialization(id)
);

insert into post(specialization_id, clinic_id, doctor_id, category_id)
values (1, 1, 1, 1), (2, 1, 2, 2), (2, 2, 3, 1);

update post set title = concat('Post ', id);
update post set description = 'Demo post';
update post set image = '/image/demo-post';
update post set price = floor(rand()*10) * 1000000;
update post set price_range_id = 
case
	when price < 1000000 then 1
    when price < 2000000 and price >= 1000000 then 2
    when price < 5000000 and price >= 2000000 then 3
    when price >= 5000000 then 4
 end
 ;

create table post_time_slot(
    post_id int,
    time_slot_id int,
    primary key(post_id, time_slot_id),
    foreign key(post_id) references post(id),
    foreign key(time_slot_id) references time_slot(id)
);

insert into post_time_slot(post_id, time_slot_id) 
values (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
		 (2, 1), (2, 2), (2, 3), (2, 4),
          (3, 5), (3, 6), (3, 7), (3, 8);

create table booking(
	id int auto_increment,
    user_id int,
    post_id int,
    time_slot_id int,
    `date` date,
    description text,
    status tinyint default 0,
    rejection_detail varchar(255),
    primary key(id),
    foreign key(user_id) references user(id),
    foreign key(post_id) references post(id),
    foreign key(time_slot_id) references time_slot(id)
);

insert into booking(user_id, post_id, time_slot_id)
values (2, 1, 2), (2, 2, 2),  (2, 3, 5);

update booking set description = 'No symptom';
update booking set date = current_date();

create table forgot_password_token(
	id int auto_increment,
    user_id int not null,
    token varchar(255),
    expired_time long,
    primary key(id),
    foreign key(user_id) references user(id)
)