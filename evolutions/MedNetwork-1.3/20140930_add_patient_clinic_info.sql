--- Author: Lakshmi
--- add PatientClinicInfo table
--- PatientClinicInfo is in models.patient





create table patient_clinic_info (
  id                        bigint not null,
  clinic_id                 bigint,
  patient_id                bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_patient_clinic_info primary key (id))
;

create sequence patient_clinic_info_seq;


alter table patient_clinic_info add constraint fk_patient_clinic_info_clinic_1 foreign key (clinic_id) references clinic (id);
create index ix_patient_clinic_info_clinic_1 on patient_clinic_info (clinic_id);
alter table patient_clinic_info add constraint fk_patient_clinic_info_patient_2 foreign key (patient_id) references patient (id);
create index ix_patient_clinic_info_patient_2 on patient_clinic_info (patient_id);

