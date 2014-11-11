--- Author: Lakshmi
--- Script to create clinic_file_entity,blood_bank_file_entity.
--- entities is added to models.clinic package.


---clinic_file_entity

create table clinic_file_entity (
  clinic_id           bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_clinic_file_entity primary key (clinic_id, file_entity_id))
;
alter table clinic_file_entity add constraint fk_clinic_file_entity_01 foreign key (clinic_id) references clinic (id);

alter table clinic_file_entity add constraint fk_clinic_file_entity_02 foreign key (file_entity_id) references file_entity (id);


-----blood_bank_file_entity
create table blood_bank_file_entity (
  blood_bank_id           bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_blood_bank_file_entity primary key (blood_bank_id, file_entity_id))
;
alter table blood_bank_file_entity add constraint fk_blood_bank_file_entity_01 foreign key (blood_bank_id ) references blood_bank (id);

alter table blood_bank_file_entity add constraint fk_blood_bank_file_entity_02 foreign key (file_entity_id) references file_entity (id);









