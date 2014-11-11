--- Author: Anand
--- Script to create 'SugarTracker' table for every appuser . 
--- sugartracker entity is added to model package.

create table sugar_tracker (
  id                        bigint not null,
  date			    		timestamp not null,
  sugar_level               double precision,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_sugar_tracker primary key (id))
;

create sequence sugar_tracker_seq;

alter table sugar_tracker add column app_user_id bigint references app_user(id);

alter table sugar_tracker add constraint fk_sugar_tracker_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_sugar_tracker_app_user_1 on sugar_tracker (app_user_id);
