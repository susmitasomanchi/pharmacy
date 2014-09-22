--- Author: Lakshmi
--- Script to create ClinicUser
--- entities is added to models.bloodBank package.


---ClinicUser
ALTER TABLE clinic Drop clinic_administrator_id;
DROP TABLE clinic_administrator;
DROP SEQUENCE clinic_administrator_seq;

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



