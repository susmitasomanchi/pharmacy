--- Author: Lakshmi
--- add Locality table
--- Locality is in models package





create table locality (
  id                        bigint not null,
  primary_city_id                 bigint,
  name			                varchar(255),
  pin_code                      varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_locality primary key (id))
;

create sequence locality_seq;


alter table locality add constraint fk_locality_primary_city_1 foreign key (primary_city_id) references primary_city(id);
create index ix_locality_primary_city_1 on locality (primary_city_id);

---Adding Locality to Doctor Entity

Alter table doctor add locality_id bigint;
alter table doctor add constraint fk_doctor_locality_1 foreign key (locality_id) references locality(id);
create index ix_doctor_locality_1 on doctor (locality_id);


---Adding Locality to DiagnosticCentre Entity

Alter table diagnostic_centre add locality_id bigint;
alter table diagnostic_centre add constraint fk_diagnostic_centre_locality_1 foreign key (locality_id) references locality(id);
create index ix_diagnostic_centre_locality_1 on diagnostic_centre (locality_id);


---Adding Locality to Pharmacy Entity

Alter table pharmacy add locality_id bigint;
alter table pharmacy add constraint fk_pharmacy_locality_1 foreign key (locality_id) references locality(id);
create index ix_pharmacy_locality_1 on pharmacy (locality_id);

addressaddress
---Adding Locality to Address Entity

Alter table address add locality_id bigint;
alter table address add constraint fk_address_locality_1 foreign key (locality_id) references locality(id);
create index ix_address_locality_1 on address (locality_id);


---Adding Locality to Clinic Entity

Alter table clinic add locality_id bigint;

alter table clinic add constraint fk_clinic_locality_1 foreign key (locality_id) references locality(id);
create index ix_clinic_locality_1 on clinic (locality_id);
