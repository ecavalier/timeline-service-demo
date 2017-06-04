# --- !Ups

insert into account(email, name, password) values ('asuka@evangelion.co.jp', '惣流', '$2a$10$6TpXvtzFGBoZrR1JJhwUI.ul/Cze35dKZmWpxwqqkARdRstqMRpVG'); -- password:bob
insert into account(email, name, password) values ('esthe@hanmail.net', 'lezhin', '$2a$10$8IDeXA6BnFNiNP9eVJcui.ygplDXSjlnSj3LDBQBXmnScFPm.E2n2'); -- password:alice

insert into post(user_id, text) values (3, 'あんた、バカぁ？');
insert into post(user_id, text) values (3, 'I''m Asuka! Asuka Langley Soryu! Charmed, huh?');
insert into post(user_id, text) values (3, 'I don''t want to die, I don''t want to die, I don''t want to die...');
insert into post(user_id, text) values (3, '気持ち悪い');
insert into post(user_id, text) values (3, 'そっか、私、笑えるんだ');

insert into post(user_id, text) values (4, '우물쭈물 하다 내 이럴줄 알았지');
insert into post(user_id, text) values (4, '정우성은 패스하지 않아. 져본 적이 없으니까.');
insert into post(user_id, text) values (4, '그래도 내 차례가 언제 오지 않겠어');
insert into post(user_id, text) values (4, '맥주를 위해 짖어라');
insert into post(user_id, text) values (4, '........');

insert into follow(user_id, follower_id, status) values (3, 4, 'follow');
insert into follow(user_id, follower_id, status) values (4, 1, 'follow');
insert into follow(user_id, follower_id, status) values (4, 2, 'follow');

