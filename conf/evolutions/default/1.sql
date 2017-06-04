# --- !Ups

create sequence account_id_seq;
create table "ACCOUNT"
(
  "id" BIGINT NOT NULL DEFAULT nextval('account_id_seq') CONSTRAINT account_pkey PRIMARY KEY,
  "EMAIL" VARCHAR(500) NOT NULL,
  "NAME" VARCHAR(100) NOT NULL,
  "PASSWORD" VARCHAR(100) NOT NULL,
  "CREATED_AT" TIMESTAMP NOT NULL DEFAULT current_timestamp,
  CONSTRAINT account_mail_address_key UNIQUE (email)
);

insert into account(email, name, password) values ('bob@example.com', 'bob', '$2a$10$6TpXvtzFGBoZrR1JJhwUI.ul/Cze35dKZmWpxwqqkARdRstqMRpVG'); -- password:bob
insert into account(email, name, password) values ('alice@example.com', 'alice', '$2a$10$8IDeXA6BnFNiNP9eVJcui.ygplDXSjlnSj3LDBQBXmnScFPm.E2n2'); -- password:alice

create sequence post_id_seq;
create table "POST"
(
  "id" BIGINT NOT NULL DEFAULT nextval('post_id_seq') PRIMARY KEY,
  "USER_ID" BIGINT NOT NULL,
  "TEXT" VARCHAR(500) NOT NULL,
  "CREATED_AT" TIMESTAMP NOT NULL DEFAULT current_timestamp,
  FOREIGN KEY ("USER_ID") REFERENCES PUBLIC.ACCOUNT("id")
);

insert into post(user_id, text) values (1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');

insert into post(user_id, text) values (1, 'Nulla finibus mi non lorem sollicitudin vestibulum. Integer id nisl velit.
Maecenas eu nunc a nisi laoreet venenatis. Class aptent taciti sociosqu ad litora torquent per conubia nostra,
per inceptos himenaeos. Donec pellentesque eros ipsum, ac egestas lorem convallis sit amet.
Praesent non pellentesque turpis. Vestibulum sapien orci, vestibulum quis lacus in, volutpat hendrerit orci.
Vestibulum nisl metus, viverra sit amet quam condimentum, semper auctor metus.');

insert into post(user_id, text) values (2, 'Nunc rhoncus, est at euismod eleifend, lectus ex interdum ipsum,
eu tincidunt neque nulla nec nunc. Morbi eu lobortis quam.
Aliquam erat volutpat. Donec tortor nunc, mattis a placerat in, tempus et nisi.
Fusce posuere maximus turpis sit amet iaculis.
Etiam dolor est, congue quis odio non, tincidunt vulputate nisl. Nulla cursus augue quis scelerisque suscipit.');

insert into post(user_id, text) values (2, 'Vivamus id semper diam, sit amet eleifend nisi.
Suspendisse malesuada quis risus eget feugiat. Nunc id porttitor neque, et dapibus ante. Integer sed efficitur dui.
Morbi vitae odio molestie est consectetur dictum. Quisque rhoncus dui dolor, vitae convallis felis vulputate quis.
Donec non tortor non ex dapibus condimentum a ac lacus. Proin dignissim commodo eros eget condimentum.
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc metus.');

insert into post(user_id, text) values (2, 'Class aptent taciti sociosqu ad litora torquent per conubia nostra,
per inceptos himenaeos. Praesent vehicula urna erat. Vivamus vestibulum aliquam massa eget convallis.
Morbi consequat sapien sit amet justo commodo, nec porta purus mollis. Quisque placerat ornare arcu, eget cursus mi.
Maecenas eget elementum ante. Morbi vestibulum magna quam, vitae congue mauris lacinia eu. Curabitur elementum hendrerit rhoncus.
Vestibulum leo risus, dapibus id.');

create sequence follow_id_seq;
create tabLe "FOLLOW"
(
  "id" BIGINT NOT NULL DEFAULT nextval('follow_id_seq') PRIMARY KEY,
  "USER_ID" BIGINT NOT NULL,
  "FOLLOWER_ID" BIGINT NOT NULL,
  "STATUS" VARCHAR(20),
  "CREATED_AT" TIMESTAMP NOT NULL DEFAULT current_timestamp
--   FOREIGN KEY ("USER_ID") REFERENCES PUBLIC.ACCOUNT("id"),
--   FOREIGN KEY ("FOLLOWER_ID") REFERENCES PUBLIC.ACCOUNT("id")
);

insert into follow(user_id, follower_id, status) values (2, 1, 'follow');

# --- !Downs

drop table follow;
drop sequence follow_id_seq;

drop table post;
drop sequence post_id_seq;

drop table account;
drop sequence account_id_seq;

