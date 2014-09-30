--- Author: Lakshmi
--- Add app_user_id for BloodDonation,backgroundImage and Description foe Clinic and BloodBank Entity 



---BloodDonation
ALTER TABLE blood_donation add app_user_id bigint;
alter table blood_donation add constraint fk_blood_donation_app_user_1 foreign key ( app_user_id) references  app_user (id);
create index ix_blood_donation_app_user_1 on blood_donation (app_user_id);

---Clinic
ALTER TABLE clinic ADD COLUMN backgroud_image bytea ,ADD COLUMN description TEXT;

---BloodBank
ALTER TABLE blood_bank ADD COLUMN backgroud_image bytea ,ADD COLUMN description TEXT;
