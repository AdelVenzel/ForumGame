CREATE TABLE comment (
	id serial4 NOT NULL,
	content varchar NOT NULL,
	user_id int4 NOT NULL,
	date_creation timestamp NOT NULL,
	number_likes int4 NULL,
	post_id int4 NOT NULL,
	CONSTRAINT comment_unique UNIQUE (id),
	CONSTRAINT comment_post_fk FOREIGN KEY (post_id) REFERENCES post(id),
	CONSTRAINT comment_user_fk FOREIGN KEY (user_id) REFERENCES my_user(id)
);