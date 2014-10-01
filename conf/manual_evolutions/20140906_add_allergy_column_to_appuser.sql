--- Author: Lakshmi
--- Add allergy and is_blood_donor column to appuser

alter table app_user add column allergy text;
alter table app_user add column is_blood_donor boolean DEFAULT 'FALSE';
