--- Author: Lakshmi
--- Script to create table weight_tracker  

create table weight_tracker (
id                        bigint not null,
created_on                timestamp not null,
app_user_id               bigint,
weight                    float,
date                      timestamp,
last_update             timestamp not null,
constraint pk_weight_tracker primary key (id))
;

create sequence weight_tracker_seq;

alter table weight_tracker add constraint fk_weight_tracker_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_weight_tracker_app_user_1 on weight_tracker (app_user_id);


create table blood_pressure_tracker (
id                        bigint not null,
created_on                timestamp not null,
app_user_id               bigint,
low_bp                    float,
high_bp			  float,
date                      timestamp,
last_update             timestamp not null,
constraint pk_blood_pressure_tracker primary key (id))
;

create sequence blood_pressure_tracker_seq;

alter table blood_pressure_tracker add constraint fk_blood_pressure_tracker_app_user_1 foreign key (app_user_id) references app_user (id);
create index ix_blood_pressure_tracker_app_user_1 on blood_pressure_tracker (app_user_id);
