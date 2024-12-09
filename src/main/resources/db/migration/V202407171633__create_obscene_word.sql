CREATE TABLE obscene_word (
	word varchar NOT NULL,
	CONSTRAINT obscene_word_pk PRIMARY KEY (word)
);

insert into obscene_word values ('сыр'),
('ночь');
