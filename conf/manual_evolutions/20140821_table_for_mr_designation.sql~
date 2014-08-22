# --Author Anand
# --create designation table for mr

drop table if exists designation cascade;
drop table if exists medical_representative cascade;
drop table if exists pharmaceutical_company cascade;

drop sequence if exists designation_seq;
drop sequence if exists medical_representative_seq;
drop sequence if exists pharmaceutical_company_seq;

create table designation (
  id                        bigint not null,
  name               	    varchar(255),
  description	            TEXT,
  pharmaceutical_company_id bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_medical_representative_designation primary key (id))
;

create sequence designation_seq;
create sequence medical_representative_seq;
create sequence pharmaceutical_company_seq;

alter table medical_representative add constraint fk_medical_representative_designation_1 foreign key (designation_id) references designation (id);
alter table designation add constraint fk_pharmaceutical_company_designation_1 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);

create index ix_medical_representative_designation_1 on medical_representative (designation_id);
create index ix_designation_pharmaceutical_company_1 on designation (pharmaceutical_company_id);
