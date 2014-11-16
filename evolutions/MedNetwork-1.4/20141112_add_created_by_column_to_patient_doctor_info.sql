--- Author: lakshmi
--- Script to add created_by column to all patient_doctor_info tables. 

ALTER TABLE patient_doctor_info ADD created_by_id  bigint;

alter table patient_doctor_info add constraint fk_patient_doctor_info_created_by_1 foreign key (created_by_id) references app_user (id);
create index ix_patient_doctor_info_clinic_1 on patient_doctor_info (created_by_id);


