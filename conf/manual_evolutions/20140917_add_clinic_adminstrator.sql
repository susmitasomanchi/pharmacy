--- Author: Lakshmi
--- Script to create 'ClinicAdministrator' table and to alter Clinic table with clinic_administrator_id and primary_city_id columns .
--- Added CLINIC_ADMIN to the app_user role 
--- clinic_administrator entity is added to models.clinic package.



create table clinic_administrator (
  id                        bigint not null,
  app_user_id               bigint,
  clinic_id		    		bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_clinic_administrator primary key (id)
);

create sequence clinic_administrator_seq;

alter table clinic_administrator add constraint fk_clinic_administrator_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_clinic_administrator_app_user_1 on clinic_administrator (app_user_id);
alter table clinic_administrator add constraint fk_clinic_administrator_clinic_2 foreign key (clinic_id) references clinic (id);
create index ix_clinic_administrator_clinic_2 on clinic_administrator (clinic_id);


alter table clinic ADD COLUMN primary_city_id bigint,ADD COLUMN clinic_administrator_id bigint;

alter table clinic add constraint fk_clinic_primary_city_1 foreign key (primary_city_id) references primary_city (id);
create index ix_clinic_primary_city_1 on clinic (primary_city_id);
alter table clinic add constraint fk_clinic_administrator_2 foreign key (clinic_administrator_id) references clinic_administrator (id);
create index ix_clinic_administrator_2 on clinic (clinic_administrator_id);


ALTER TABLE app_user 
  DROP CONSTRAINT ck_app_user_role RESTRICT;
ALTER TABLE app_user 
  ADD CONSTRAINT ck_app_user_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY','CLINIC_ADMIN'));
