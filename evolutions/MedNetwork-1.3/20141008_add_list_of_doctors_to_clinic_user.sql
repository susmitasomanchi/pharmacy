--- Author: Lakshmi
--- Script to create clinic_user_doctor,blood_bank_file_entity.
--- entities is added to models.clinic package.


---clinic_file_entity

create table clinic_user_doctor (

  clinic_user_id           bigint not null,
  doctor_id                 bigint not null,
  constraint pk_clinic_user_doctor primary key (clinic_user_id, doctor_id))
;
alter table clinic_user_doctor add constraint fk_clinic_user_doctor_01 foreign key (clinic_user_id) references clinic_user (id);

alter table clinic_user_doctor add constraint fk_clinic_user_doctor_02 foreign key (doctor_id) references doctor (id);

