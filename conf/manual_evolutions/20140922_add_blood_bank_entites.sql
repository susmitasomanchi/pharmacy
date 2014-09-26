--- Author: Lakshmi
--- Script to create BloodBankUser,BloodBank,BloodDonation entities
--- entities is added to models.bloodBank package.

---BloodBank
create table blood_bank (
  id                        bigint not null,
  name			               varchar(255),
  contact_person_name				varchar(255),
  contact_no						varchar(255),
  address_id					bigint,					
  primary_city_id				bigint,
  blood_bank_admin_id			bigint,	
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_blood_bank primary key (id)
);

create sequence blood_bank_seq;

alter table blood_bank add constraint fk_blood_bank_address_1 foreign key (address_id) references address (id);
create index ix_blood_bank_address_1 on blood_bank (address_id);
alter table blood_bank add constraint fk_blood_bank_primary_city_2 foreign key (primary_city_id) references primary_city(id);
create index ix__blood_bank_primary_city_2  on blood_bank (primary_city_id);

---BloodBankUser

create table blood_bank_user (
  id                        bigint not null,
  blood_bank_id				bigint,
  app_user_id               bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_blood_bank_user primary key (id)
);

create sequence blood_bank_user_seq;

alter table blood_bank_user add constraint fk_blood_bank_user_blood_bank_1 foreign key ( blood_bank_id) references  blood_bank (id);
create index ix_blood_bank_user_blood_bank_1 on blood_bank_user (blood_bank_id);
alter table blood_bank_user add constraint fk_blood_bank_user_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_blood_bank_user_app_user_1 on blood_bank_user (app_user_id);


---- Add BloodBank_BBUser FK constraint 
alter table blood_bank add constraint fk_blood_bank_admin_1 foreign key (blood_bank_admin_id) references blood_bank_user (id);
create index ix_blood_bank_admin_1 on blood_bank (blood_bank_admin_id);



----BloodBankDonation

create table blood_donation (
  id                        bigint not null,
  blood_bank_id					bigint,
  date_donated               	timestamp,
  quantity_donated	    		float,
  weight						float,
  hemoglobin_level				float,
  blood_group					varchar(16),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_blood_bank_blood_group check (blood_group in ('OPLUS','OMINUS','APLUS','AMINUS','BPLUS','BMINUS','ABPLUS','ABMINUS')),
  constraint pk_blood_donation primary key (id)
);

create sequence blood_donation_seq;

alter table blood_donation add constraint fk_blood_donation_bank_1 foreign key ( blood_bank_id) references  blood_bank (id);
create index ix_blood_donation_bank_1 on blood_donation (blood_bank_id);


---AppUser

ALTER TABLE app_user 
  DROP CONSTRAINT ck_app_user_role RESTRICT;
ALTER TABLE app_user 
  ADD CONSTRAINT ck_app_user_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY','CLINIC_ADMIN','CLINIC_USER','BLOOD_BANK_ADMIN','BLOOD_BANK_USER'));



