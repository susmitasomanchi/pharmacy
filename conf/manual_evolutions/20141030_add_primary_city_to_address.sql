--- Author: Lakshmi
--- add primarycity in address table


ALTER TABLE address ADD primary_city_id bigint;

alter table address add constraint fk_address_primary_city_1 foreign key (primary_city_id) references primary_city (id);
create index ix_address_primary_city_1 on address (primary_city_id);




