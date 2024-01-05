use prj321_asm03;
show tables;

select * from role;
select * from user;
select * from doctor;
select * from authorities order by user_id;
select * from price_range;
select * from category;
select * from location;
select * from clinic;
select * from specialization;
select * from post;
select * from post_time_slot order by post_id;
select * from booking;
select * from forgot_password_token;