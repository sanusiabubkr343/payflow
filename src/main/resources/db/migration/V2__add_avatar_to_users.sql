create sequence IF NOT EXISTS revinfo_seq START with 1 INCREMENT BY 50;

create table revchanges
(
    rev        BIGINT not null,
    entityname varchar(255)
);

create table revinfo
(
    rev      BIGINT not null,
    revtstmp BIGINT,
    constraint pk_revinfo primary key (rev)
);

alter table users
    add avatar_public_id varchar(255);

alter table users
    add avatar_url varchar(255);

alter table revchanges
    add constraint fk_revchanges_on_default_tracking_modified_entities_changelog foreign key (rev) references revinfo (rev);