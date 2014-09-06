--- Author: Anand
--- Script to create 'SugarTracker' table for every appuser . 
--- sugartracker entity is added to model package.

create table sugar_tracker (
  id                        bigint not null,
  sugerLevel                double precision,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_sugar_tracker primary key (id))
;

create sequence sugar_tracker_seq;

alter table app_user add column allergy text;

alter table app_user add column sugar_tracker_id bigint ;
alter table app_user add constraint fk_app_user_sugar_tracker_1 foreign key (sugar_tracker_id) references sugar_tracker (id);
create index ix_app_user_sugar_tracker_1 on app_user (sugar_tracker_id);
