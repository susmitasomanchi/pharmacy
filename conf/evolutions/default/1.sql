# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                        bigint not null,
  image                     bytea,
  name                      varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  sex                       varchar(6),
  dob                       timestamp,
  role                      varchar(16),
  last_update               timestamp not null,
  constraint ck_app_user_sex check (sex in ('OTHER','MALE','FEMALE')),
  constraint ck_app_user_role check (role in ('PHARMACIST','MR','PATIENT','ADMIN','DIAGREP','DOCTOR','ADMIN_PHARMACIST')),
  constraint pk_app_user primary key (id))
;

create table appointment (
  id                        bigint not null,
  appointment_time          timestamp,
  appointment_status        integer,
  starttime                 timestamp,
  requested_by_id           bigint,
  apporoved_by_id           bigint,
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint ck_appointment_appointment_status check (appointment_status in (0,1,2,3)),
  constraint pk_appointment primary key (id))
;

create table diagnostic_center (
  id                        bigint not null,
  diagno_center_name        varchar(255),
  services                  varchar(255),
  contact_person_name       varchar(255),
  address                   varchar(255),
  city                      varchar(255),
  mobile_no                 varchar(255),
  email_id                  varchar(255),
  website_name              varchar(255),
  constraint pk_diagnostic_center primary key (id))
;

create table diagnostic_representative (
  id                        bigint not null,
  app_user_id               bigint,
  diagnostic_type           varchar(255),
  last_update               timestamp not null,
  constraint pk_diagnostic_representative primary key (id))
;

create table doctor (
  id                        bigint not null,
  app_user_id               bigint,
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
  last_update               timestamp not null,
  constraint pk_doctor primary key (id))
;

create table doctor_assistant (
  id                        bigint not null,
  degree                    varchar(255),
  experience                varchar(255),
  app_user_id               bigint,
  last_update               timestamp not null,
  constraint pk_doctor_assistant primary key (id))
;

create table medical_representative (
  id                        bigint not null,
  app_user_id               bigint,
  region_alloted            varchar(255),
  company_name              varchar(255),
  types_of_medecine         varchar(255),
  no_of_doctors_visit       integer,
  last_update               timestamp not null,
  constraint pk_medical_representative primary key (id))
;

create table patient (
  id                        bigint not null,
  app_user_id               bigint,
  mbno                      varchar(255),
  date                      varchar(255),
  disease                   varchar(255),
  appointment_id            varchar(255),
  doctor_availability       varchar(255),
  is_urgent_patient         varchar(255),
  last_update               timestamp not null,
  constraint pk_patient primary key (id))
;

create table pharmacist (
  id                        bigint not null,
  app_user_id               bigint,
  category                  varchar(255),
  last_update               timestamp not null,
  constraint pk_pharmacist primary key (id))
;

create table pharmacy (
  id                        bigint not null,
  name                      varchar(255),
  address                   varchar(255),
  contact_no                varchar(255),
  admmin_pharmacist_id      bigint,
  last_update               timestamp not null,
  constraint pk_pharmacy primary key (id))
;

create table question_and_answer (
  id                        bigint not null,
  question                  TEXT,
  answer                    TEXT,
  question_date             timestamp,
  answer_date               timestamp,
  question_by_id            bigint,
  answer_by_id              bigint,
  last_update               timestamp not null,
  constraint pk_question_and_answer primary key (id))
;

create sequence app_user_seq;

create sequence appointment_seq;

create sequence diagnostic_center_seq;

create sequence diagnostic_representative_seq;

create sequence doctor_seq;

create sequence doctor_assistant_seq;

create sequence medical_representative_seq;

create sequence patient_seq;

create sequence pharmacist_seq;

create sequence pharmacy_seq;

create sequence question_and_answer_seq;

alter table appointment add constraint fk_appointment_requestedBy_1 foreign key (requested_by_id) references app_user (id);
create index ix_appointment_requestedBy_1 on appointment (requested_by_id);
alter table appointment add constraint fk_appointment_apporovedBy_2 foreign key (apporoved_by_id) references app_user (id);
create index ix_appointment_apporovedBy_2 on appointment (apporoved_by_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative_a_3 foreign key (app_user_id) references app_user (id);
create index ix_diagnostic_representative_a_3 on diagnostic_representative (app_user_id);
alter table doctor add constraint fk_doctor_appUser_4 foreign key (app_user_id) references app_user (id);
create index ix_doctor_appUser_4 on doctor (app_user_id);
alter table doctor_assistant add constraint fk_doctor_assistant_appUser_5 foreign key (app_user_id) references app_user (id);
create index ix_doctor_assistant_appUser_5 on doctor_assistant (app_user_id);
alter table medical_representative add constraint fk_medical_representative_appU_6 foreign key (app_user_id) references app_user (id);
create index ix_medical_representative_appU_6 on medical_representative (app_user_id);
alter table patient add constraint fk_patient_appUser_7 foreign key (app_user_id) references app_user (id);
create index ix_patient_appUser_7 on patient (app_user_id);
alter table pharmacist add constraint fk_pharmacist_appUser_8 foreign key (app_user_id) references app_user (id);
create index ix_pharmacist_appUser_8 on pharmacist (app_user_id);
alter table pharmacy add constraint fk_pharmacy_admminPharmacist_9 foreign key (admmin_pharmacist_id) references pharmacist (id);
create index ix_pharmacy_admminPharmacist_9 on pharmacy (admmin_pharmacist_id);
alter table question_and_answer add constraint fk_question_and_answer_questi_10 foreign key (question_by_id) references app_user (id);
create index ix_question_and_answer_questi_10 on question_and_answer (question_by_id);
alter table question_and_answer add constraint fk_question_and_answer_answer_11 foreign key (answer_by_id) references app_user (id);
create index ix_question_and_answer_answer_11 on question_and_answer (answer_by_id);



# --- !Downs

drop table if exists app_user cascade;

drop table if exists appointment cascade;

drop table if exists diagnostic_center cascade;

drop table if exists diagnostic_representative cascade;

drop table if exists doctor cascade;

drop table if exists doctor_assistant cascade;

drop table if exists medical_representative cascade;

drop table if exists patient cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop table if exists question_and_answer cascade;

drop sequence if exists app_user_seq;

drop sequence if exists appointment_seq;

drop sequence if exists diagnostic_center_seq;

drop sequence if exists diagnostic_representative_seq;

drop sequence if exists doctor_seq;

drop sequence if exists doctor_assistant_seq;

drop sequence if exists medical_representative_seq;

drop sequence if exists patient_seq;

drop sequence if exists pharmacist_seq;

drop sequence if exists pharmacy_seq;

drop sequence if exists question_and_answer_seq;

