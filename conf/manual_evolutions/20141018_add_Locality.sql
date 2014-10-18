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


Alter table doctor add locality_id bigint;
alter table doctor add constraint fk_doctor_locality_1 foreign key (locality_id) references locality(id);
create index ix_doctor_locality_1 on doctor (locality_id);
