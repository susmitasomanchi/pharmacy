--- Author: Lakhsmi
--- create a table blood_bank_app_user

create table blood_bank_app_user (
  blood_bank_id           bigint not null,
  app_user_id                 bigint not null,
  constraint pk_blood_bank_app_user primary key (blood_bank_id, app_user_id))
;
alter table blood_bank_app_user add constraint fk_blood_bank_app_user_01 foreign key (blood_bank_id ) references blood_bank (id);

alter table blood_bank_app_user add constraint fk_blood_bank_app_user_02 foreign key (app_user_id) references app_user (id);



