--- Author: Buta
--- Script to create 'primary_city' table and its OneToOne relation with patient 
--- PrimaryCity added to Patient model

ALTER TABLE patient ADD primary_city_id bigint;

alter table patient add constraint fk_patient_primary_city_1 foreign key (primary_city_id) references primary_city (id);
create index ix_patient_primary_city_1 on patient (primary_city_id);