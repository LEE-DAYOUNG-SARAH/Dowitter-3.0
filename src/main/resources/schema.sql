DROP TABLE IF EXISTS DOC;
DROP TABLE IF EXISTS MEMBER;

CREATE TABLE MEMBER (
                        uid bigint generated by default as identity,
                        user_id varchar(255) not null,
                        password varchar(255) not null,
                        primary key (uid)
);

CREATE TABLE DOC (
                     uid bigint generated by default as identity,
                     member_uid bigint not null,
                     content text not null,
                     reg_datetime TIMESTAMP not null default now(),
                     primary key (uid),
                     foreign key (member_uid) references member(uid)
);