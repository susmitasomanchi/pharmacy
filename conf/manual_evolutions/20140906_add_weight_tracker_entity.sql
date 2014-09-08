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

create table blood_pressure_tracker (
id                        bigint not null,
created_on                timestamp not null,
app_user_id               bigint,
low_bp                    integer,
high_bp			  integer,
date                      timestamp,
last_update             timestamp not null,
constraint pk_blood_pressure_tracker primary key (id))
;

create sequence blood_pressure_tracker_seq;
