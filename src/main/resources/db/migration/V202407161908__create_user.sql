CREATE TABLE my_user (
	id serial NOT NULL,
	login varchar NOT NULL,
	password varchar NOT NULL,
	roles varchar NOT NULL,
	status varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT user_unique UNIQUE (login)
);

insert into my_user (login, password, roles, status) values ('admin', '$2a$10$UCWk3AoxU6r1ZL0vMDV6eOUX7/wDTqmxwzkvxpsG8XoB.M2WqPJOq', 'ADMIN', 'ACTIVE');

