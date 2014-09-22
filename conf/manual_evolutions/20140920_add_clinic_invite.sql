--- Author: Buta
--- Script to create 'clinic_invite' table
--- Added new entity: ClinicInvite in models.clinic


create table clinic_invite (
  id                        bigint not null,
  doctor_id                 bigint,
  clinic_id		    		bigint,
  invited_by_id	    		bigint,
  accepted_by_id	   		bigint,
  accepted					boolean DEFAULT 'FALSE',  
  date_invited				timestamp,
  date_accepted				timestamp,
  verification_code			TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_clinic_invite primary key (id)
);

create sequence clinic_invite_seq;

--alter table clinic_invite add constraint fk_clinic_invite_doctor_1 foreign key (doctor_id) references doctor (id);
--create index ix_clinic_invite_doctor_1 on clinic_invite (doctor_id);


--alter table clinic_invite add constraint fk_clinic_invite_clinic_1 foreign key (clinic_id) references clinic (id);
--create index ix_clinic_invite_clinic_1 on clinic_invite (clinic_id);


--alter table clinic_invite add constraint fk_clinic_invite_app_user_1 foreign key (invited_by_id) references app_user (id);
--create index ix_clinic_invite_app_user_1 on clinic_invite (invited_by_id);


--alter table clinic_invite add constraint fk_clinic_invite_app_user_2 foreign key (accepted_by_id) references app_user (id);
--create index ix_clinic_invite_app_user_2 on clinic_invite (accepted_by_id);