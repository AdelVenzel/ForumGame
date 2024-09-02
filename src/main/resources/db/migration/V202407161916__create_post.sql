CREATE TABLE post (
	id serial4 NOT NULL,
	title varchar NOT NULL,
	user_id int4 NOT NULL,
	date_creation timestamp NOT NULL,
	content varchar(255) NULL,
	CONSTRAINT post_pk PRIMARY KEY (id),
	CONSTRAINT post_user_fk FOREIGN KEY (user_id) REFERENCES my_user(id)
);