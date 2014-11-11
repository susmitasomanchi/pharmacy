



alter table sample rename column dcr_line_item_id to dcrline_item_id;
ALTER TABLE  sample drop tpline_item_id;
ALTER TABLE  sample add column tpline_item_id bigint;
alter table sample add constraint fk_sample_tpline_item_id_80 foreign key (tpline_item_id) references tpline_item (id);
create index ix_sample_tpline_item_id_80 on sample (tpline_item_id);