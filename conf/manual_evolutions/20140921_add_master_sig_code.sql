--- Author: Lakshmi
--- Script to create 'MasterSigCode'  and sig_code table 
--- master_sig_code entity is added to models.doctor package.


DROP table if exists sig_code;
DROP sequence if exists sig_code_seq;

create table sig_code (
  id						bigint not null,
  doctor_id                 bigint not null,
  code                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_sig_code primary key (id))
;

create sequence sig_code_seq;

alter table sig_code add constraint fk_sig_code_doctor_1 foreign key (doctor_id) references doctor (id);
create index ix_sig_code_doctor_1 on sig_code (doctor_id);

DROP table if exists master_sig_code;
DROP sequence if exists master_sig_code_seq;

create table master_sig_code (
  id                        bigint not null,
  code               		varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_master_sig_code primary key (id))
;

create sequence master_sig_code_seq;


