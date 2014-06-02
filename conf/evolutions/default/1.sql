# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                        bigint not null,
  name                      varchar(255),
  patient_id                bigint,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  age                       integer,
  last_update               timestamp not null,
  constraint pk_app_user primary key (id))
;

create table appointment (
  date                      timestamp,
  role                      integer,
  appointment_status        integer,
  last_update               timestamp not null,
  constraint ck_appointment_role check (role in (0,1,2,3)),
  constraint ck_appointment_appointment_status check (appointment_status in (0,1,2)))
;

create table diagnostic_rep (
  id                        bigint not null,
  name                      varchar(255),
  patient_id                bigint,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  age                       integer,
  diagnostic_type           varchar(255),
  last_update               timestamp not null,
  constraint pk_diagnostic_rep primary key (id))
;

create table doctor (
  id  
   name                      varchar(255),
  patient_id                bigint,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  age                       integer,
                        bigint not null,
  specialization            varchar(255),
  degree                    varchar(255),
  doctor_type               varchar(255),
  experience                varchar(255),
  home_facility             varchar(255),
  fees                      integer,
  clinic_address            varchar(255),
  hospital_address          varchar(255),
  timings                   varchar(255),
  category_of_doctor        varchar(255),
  reg_app_usr_id            bigint,
  last_update               timestamp not null,
  constraint pk_doctor primary key (id))
;

create table patient (
  id                        bigint not null,
  picture                   bytea,
  disease                   varchar(255),
  appointment_id            bigint,
  doctor_availability       varchar(255),
  is_urgent_patient         varchar(255),
  constraint pk_patient primary key (id))
;

create table pharmacist (
  id                        bigint not null,
  category                  varchar(255),
  constraint pk_pharmacist primary key (id))
;

create table pharmacy (
  name                      varchar(255),
  address                   varchar(255))
;

create table register_app_user (
  id                        bigint not null,
  name                      varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  gender                    varchar(255),
  age                       integer,
  last_update               timestamp not null,
  constraint pk_register_app_user primary key (id))
;

create table sales_rep (
  id                        bigint not null,
  name                      varchar(255),
  patient_id                bigint,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  age                       integer,
  region_alloted            varchar(255),
  company_name              varchar(255),
  types_of_medecine         varchar(255),
  no_of_doctors_visit       integer,
  last_update               timestamp not null,
  constraint pk_sales_rep primary key (id))
;

create sequence app_user_seq;

create sequence diagnostic_rep_seq;

create sequence doctor_seq;

create sequence patient_seq;

create sequence pharmacist_seq;

create sequence register_app_user_seq;

create sequence sales_rep_seq;

alter table doctor add constraint fk_doctor_regAppUsr_1 foreign key (reg_app_usr_id) references register_app_user (id);
create index ix_doctor_regAppUsr_1 on doctor (reg_app_usr_id);
alter table app_user add constraint fk_app_user_patient_1 foreign key (patient_id) references patient (id);
create index ix_app_user_patient_1 on app_user (patient_id);
alter table diagnostic_rep add constraint fk_diagnostic_rep_patient_2 foreign key (patient_id) references patient (id);
create index ix_diagnostic_rep_patient_2 on diagnostic_rep (patient_id);
alter table doctor add constraint fk_doctor_patient_3 foreign key (patient_id) references patient (id);
create index ix_doctor_patient_3 on doctor (patient_id);
alter table sales_rep add constraint fk_sales_rep_patient_4 foreign key (patient_id) references patient (id);
create index ix_sales_rep_patient_4 on sales_rep (patient_id);



# --- !Downs

drop table if exists app_user cascade;

drop table if exists appointment cascade;

drop table if exists diagnostic_rep cascade;

drop table if exists doctor cascade;

drop table if exists patient cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop table if exists register_app_user cascade;

drop table if exists sales_rep cascade;

drop sequence if exists app_user_seq;

drop sequence if exists diagnostic_rep_seq;

drop sequence if exists doctor_seq;

drop sequence if exists patient_seq;

drop sequence if exists pharmacist_seq;

drop sequence if exists register_app_user_seq;

drop sequence if exists sales_rep_seq;

