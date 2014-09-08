--- Author: Lakshmi
--- Script to create table tracker column to all entity tables. 

create table weight_tracker (
id                        bigint not null,
created_on                timestamp not null,
app_user_id               bigint,
weight                    float,
date                      timestamp,
last_update             timestamp not null,
constraint pk_tracker primary key (id))
;
