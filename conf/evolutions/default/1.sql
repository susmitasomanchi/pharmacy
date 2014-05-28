# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                        bigint not null,
  name                      varchar(255),
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

create table patient (
  id                        bigint not null,
  appointment_id            bigint not null,
  name                      varchar(255),
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
  constraint ck_patient_role check (role in (0,1,2,3)))
;

create table pharmacist (
  id                        bigint not null,
  name                      varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  role                      integer,
  gender                    varchar(255),
  age                       integer,
  category                  varchar(255),
  last_update               timestamp not null,
  constraint ck_pharmacist_role check (role in (0,1,2,3)),
  constraint pk_pharmacist primary key (id))
;

create table pharmacy (
  name                      varchar(255),
  address                   varchar(255))
;

create sequence app_user_seq;

create sequence doctor_seq;

create sequence patient_seq;

create sequence pharmacist_seq;




# --- !Downs

drop table if exists app_user cascade;

drop table if exists doctor cascade;

drop table if exists patient cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop sequence if exists app_user_seq;

drop sequence if exists doctor_seq;

drop sequence if exists patient_seq;

drop sequence if exists pharmacist_seq;

