-- Author: Buta
-- DDL Script to implement Designation, Designation List in PharmaCompany and Designation in MR


create table designation (
  id                        bigint not null,
  pharmaceutical_company_id bigint not null,
  name                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_designation primary key (id))
;

create sequence designation_seq;

alter table designation add constraint fk_designation_pharmaceutical_22 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_designation_pharmaceutical_22 on designation (pharmaceutical_company_id);


alter table medical_representative drop if exists designation;

ALTER TABLE medical_representative ADD designation_id bigint;

alter table medical_representative add constraint fk_mr_designation_1 foreign key (designation_id) references designation (id);
create index ix_mr_designation_1 on medical_representative (designation_id); 
