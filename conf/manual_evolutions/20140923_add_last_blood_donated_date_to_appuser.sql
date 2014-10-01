--- Author: Lakshmi
--- Add allergy and is_blood_donor column to appuser

ALTER TABLE app_user ADD COLUMN is_mobile_number_shared boolean DEFAULT 'FALSE',ADD COLUMN last_blood_donated_date timestamp;
