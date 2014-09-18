create table blood_pressure_tracker (
  id                        bigint not null,
  low_bp			    int,
  high_bp			int,
  date			    timestamp,
  created_on                timestamp not null,
  last_update               timestamp not null,
  app_user_id			bigint,
  constraint pk_blood_pressure_tracker primary key (id))
;

create sequence blood_pressure_tracker_seq;

alter table blood_pressure_tracker add constraint fk_blood_pressure_tracker_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_blood_pressure_tracker_app_user_1 on blood_pressure_tracker (app_user_id);
