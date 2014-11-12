--- Author: Lakshmi
--- Script to create ClinicUser
--- entities is added to models.clinic package.


---ClinicUser
ALTER TABLE clinic drop if exists clinic_administrator_id;
DROP TABLE if exists clinic_administrator;
DROP SEQUENCE if exists clinic_administrator_seq;

create table clinic_user (
  id                        bigint not null,
  app_user_id               bigint,
  clinic_id		    		bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_clinic_user primary key (id)
);

create sequence clinic_user_seq;


alter table clinic_user add constraint fk_clinic_user_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_clinic_user_app_user_1 on clinic_user (app_user_id);
alter table clinic_user add constraint fk_clinic_user_clinic_2 foreign key (clinic_id) references clinic (id);
create index ix_clinic_user_clinic_2 on clinic_user (clinic_id);


ALTER TABLE clinic ADD clinic_adminstrator_id bigint;
  
alter table clinic add constraint fk_clinic_clinic_user_1 foreign key (clinic_adminstrator_id) references clinic_user (id);
create index ix_clinic_clinic_user_1 on clinic (clinic_adminstrator_id);

--create table clinic_file_entity (
--  clinic_id           bigint not null,
--  file_entity_id                 bigint not null,
--  constraint pk_diagnostic_centre_file_entity primary key (clinic_id, file_entity_id))
--;
--alter table clinic_file_entity add constraint fk_clinic_file_entity_01 foreign key (clinic_id) references clinic (id);
--
--alter table clinic_file_entity add constraint fk_clinic_file_entity_02 foreign key (file_entity_id) references file_entity (id);









