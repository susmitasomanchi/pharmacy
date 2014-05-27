# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                        bigint not null,
  name                      varchar(255),
  designation               varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  role                      integer,
  gender                    varchar(255),
  age                       integer,
  last_update               timestamp not null,
  constraint ck_app_user_role check (role in (0,1,2,3)),
  constraint pk_app_user primary key (id))
;

create table doctor (
  id                        bigint not null,
  name                      varchar(255),
  designation               varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  role                      integer,
  gender                    varchar(255),
  age                       integer,
  specialization            varchar(255),
  degree                    varchar(255),
  doctor_type               varchar(255),
  experience                varchar(255),
  home_facility             varchar(255),
  fees                      integer,
  clinic_address            varchar(255),
  hospital_address          varchar(255),
  timings                   varchar(255),
  last_update               timestamp not null,
  constraint ck_doctor_role check (role in (0,1,2,3)),
  constraint pk_doctor primary key (id))
;

create table patients (
  id                        bigint not null,
  appointment_id            bigint not null,
  name                      varchar(255),
  designation               varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  role                      integer,
  gender                    varchar(255),
  age                       integer,
  disease                   varchar(255),
  doctor_availability       varchar(255),
  is_urgent_patient         varchar(255),
  last_update               timestamp not null,
  constraint ck_patients_role check (role in (0,1,2,3)))
;

create sequence app_user_seq;

create sequence doctor_seq;

create sequence patients_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists app_user;

drop table if exists doctor;

drop table if exists patients;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists app_user_seq;

drop sequence if exists doctor_seq;

drop sequence if exists patients_seq;

