-- MEMBER 초기 데이터
INSERT INTO MEMBER(user_id,password) VALUES('test1','1234');
INSERT INTO MEMBER(user_id,password) VALUES('test2','1234');
INSERT INTO MEMBER(user_id,password) VALUES('test3','1234');

-- DOC 초기 데이터
INSERT INTO DOC(member_uid, content) VALUES(1, '테스트 입니당1');
INSERT INTO DOC(member_uid, content) VALUES(2, '테스트 입니당2');
INSERT INTO DOC(member_uid, content) VALUES(3, '테스트 입니당3');