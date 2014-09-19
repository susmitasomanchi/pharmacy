--- Author: Lakshmi
--- Script to add 'blood_group' column to the table app_user 

ALTER TABLE app_user ADD blood_Group varchar(16);
ALTER TABLE app_user 
  ADD CONSTRAINT ck_app_user_blood_group check (blood_group in ('A+','A-','B+','B-','AB+','AB-','O+','O-'));

