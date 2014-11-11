--- Author: Buta
--- Script to create 'primary_city' table and its OneToOne relation with doctor, pharmacy and diagnostic_centre 
--- PrimaryCity entity added in models package; PrimaryCity added to Doctor, Pharmacy & DiagnosticCentre entities

create table primary_city (
  id                        bigint not null,
  name                      varchar(255),
  state                     varchar(35),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_primary_city_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','CHANDIGARH','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH')),
  constraint pk_primary_city primary key (id))
;


ALTER TABLE doctor ADD primary_city_id bigint;

ALTER TABLE pharmacy ADD primary_city_id bigint;

ALTER TABLE diagnostic_centre ADD primary_city_id bigint;


create sequence primary_city_seq;

alter table doctor add constraint fk_doctor_primary_city_1 foreign key (primary_city_id) references primary_city (id);
create index ix_doctor_primary_city_1 on doctor (primary_city_id);

alter table pharmacy add constraint fk_pharmacy_primary_city_1 foreign key (primary_city_id) references primary_city (id);
create index ix_pharmacy_primary_city_1 on pharmacy (primary_city_id);

alter table diagnostic_centre add constraint fk_diagnostic_centre_primary_city_1 foreign key (primary_city_id) references primary_city (id);
create index ix_diagnostic_centre_primary_city_1 on diagnostic_centre (primary_city_id);