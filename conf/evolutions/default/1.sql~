# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

<<<<<<< HEAD
create table address (
  id                        bigint not null,
  address_line1             varchar(255),
  address_line2             varchar(255),
  address_line3             varchar(255),
  area                      varchar(255),
  latitude                  varchar(255),
  longitude                 varchar(255),
  city                      varchar(255),
  state                     varchar(35),
  pin_code                  varchar(255),
  fetched_pin_code          varchar(255),
  country                   varchar(27),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_address_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','CHANDIGARH','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH')),
  constraint ck_address_country check (country in ('ARMENIA','ANGUILLA','AUSTRALIA','ARUBA','CHAD','BOSNIA_AND_HERZEGOVINA','ANTIGUA_AND_BARBUDA','CHINA','ASHMORE_AND_CARTIER_ISLANDS','AMERICAN_SAMOA','COMOROS','INDIA','BOLIVIA','CAMEROON','PAKISTAN','DENMARK','BURUNDI','CAPE_VERDE','BULGARIA','ARGENTINA','DJIBOUTI','BELGIUM','ALBANIA','BAHRAIN','ALGERIA','ECUADOR','BELARUS','BARBADOS','BURMA','CHILE','BRUNEI','BELIZE','AZERBAIJAN','BHUTAN','CANADA','AFGHANISTAN','ANDORRA','CAMBODIA','AKROTIRI','AUSTRIA','BOUVET_ISLAND','BERMUDA','DOMINICA','ANGOLA','EGYPT','BENIN','UNITED_STATES','DHEKELIA','BOTSWANA','CUBA','ANTARCTICA','BRAZIL','CYPRUS','BURKINA_FASO','BANGLADESH','COLOMBIA')),
  constraint pk_address primary key (id))
;

create table app_user (
  id                        bigint not null,
  image                     bytea,
  name                      varchar(255),
  username                  varchar(255),
  mobile_number             bigint,
  email                     varchar(255),
  password                  TEXT,
  salt                      TEXT,
  sex                       varchar(6),
  dob                       timestamp,
  role                      varchar(16),
  address_id                bigint,
  email_confirmed           boolean,
  mobile_number_confirmed   boolean,
  email_confirmation_key    TEXT,
  mobile_number_confirmation_key TEXT,
  forgot_password_confirmation_key TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_app_user_sex check (sex in ('FEMALE','OTHER','MALE')),
  constraint ck_app_user_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_app_user primary key (id))
;

create table appointment (
  id                        bigint not null,
  appointment_time          timestamp,
  appointment_status        varchar(9),
  requested_by_id           bigint,
  apporoved_by_id           bigint,
  problem_statement         TEXT,
  doctor_clinic_info_id     bigint,
  booked_on                 timestamp,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_appointment_appointment_status check (appointment_status in ('CANCELLED','REQUESTED','APPROVED','AVAILABLE','SERVED')),
  constraint pk_appointment primary key (id))
;

create table article (
  id                        bigint not null,
  name                      TEXT,
  position                  integer,
  short_description         TEXT,
  on_hover_content          TEXT,
  content                   TEXT,
  slug_url                  TEXT,
  thumbnail                 bytea,
  image                     bytea,
  html_title                TEXT,
  html_meta_description     TEXT,
  html_keywords             TEXT,
  category_id               bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_article primary key (id))
;

create table article_category (
  id                        bigint not null,
  name                      TEXT,
  position                  integer,
  short_description         TEXT,
  on_hover_content          TEXT,
  content                   TEXT,
  slug_url                  TEXT,
  thumbnail                 bytea,
  image                     bytea,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_article_category primary key (id))
;

create table batch (
  id                        bigint not null,
  inventory_id              bigint not null,
  product_id                bigint,
  batch_no                  varchar(255),
  mrp                       float,
  quantity                  integer,
  tax                       float,
  discount                  float,
  created_on                timestamp not null,
  last_update               timestamp not null,
  expiry_date               timestamp not null,
  constraint pk_batch primary key (id))
;

create table blog_comment (
  id                        bigint not null,
  article_id                bigint not null,
  message                   TEXT,
  date                      timestamp,
  by_id                     bigint,
  social_by_id              bigint,
  blog_commentator_type     varchar(8),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_blog_comment_blog_commentator_type check (blog_commentator_type in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_blog_comment primary key (id))
;

create table blog_comment_reply (
  id                        bigint not null,
  blog_comment_id           bigint not null,
  message                   TEXT,
  date                      timestamp,
  by_id                     bigint,
  social_by_id              bigint,
  blog_commentator_type     varchar(8),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_blog_comment_reply_blog_commentator_type check (blog_commentator_type in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_blog_comment_reply primary key (id))
;

create table clinic (
  id                        bigint not null,
  name                      varchar(255),
  contact_person_name       varchar(255),
  contact_no                varchar(255),
  address_id                bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_clinic primary key (id))
;

create table dcrline_item (
  id                        bigint not null,
  daily_call_report_id      bigint not null,
  doctor_id                 bigint,
  in_time                   timestamp,
  out_time                  timestamp,
  pob                       integer,
  head_quater_id            bigint,
  remarks                   varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_dcrline_item primary key (id))
;

create table daily_call_report (
  id                        bigint not null,
  for_date                  timestamp,
  submitter_id              bigint,
  approver_id               bigint,
  dcr_status                varchar(9),
  submitted_date            timestamp,
  response_on               timestamp,
  re_opened_date            timestamp,
  head_quarter_id           bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_daily_call_report_dcr_status check (dcr_status in ('REJECTED','DRAFT','APPROVED','REOPENED','SUBMITTED')),
  constraint pk_daily_call_report primary key (id))
;

create table day_schedule (
  id                        bigint not null,
  doctor_clinic_info_id     bigint not null,
  day                       varchar(9),
  from_time                 varchar(255),
  to_time                   varchar(255),
  requester                 varchar(16),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_day_schedule_day check (day in ('MONDAY','SUNDAY','WEDNESDAY','THURSDAY','SATURDAY','TUESDAY','FRIDAY')),
  constraint ck_day_schedule_requester check (requester in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_day_schedule primary key (id))
;

create table designation (
  id                        bigint not null,
  pharmaceutical_company_id bigint not null,
  name                      varchar(255),
  description               TEXT,
  mr_id                     bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_designation primary key (id))
;

create table diagnostic_centre (
  id                        bigint not null,
  name                      varchar(255),
  contact_person            varchar(255),
  address_id                bigint,
  mobile_no                 varchar(255),
  description               TEXT,
  backgroud_image           bytea,
  email_id                  varchar(255),
  website_name              varchar(255),
  diagnostic_rep_admin_id   bigint,
  search_index              TEXT,
  slug_url                  TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_diagnostic_centre primary key (id))
;

create table diagnostic_centre_prescription_info (
  id                        bigint not null,
  s                         varchar(255),
  diagnostic_centre_id      bigint,
  prescription_id           bigint,
  diagnostic_centre_prescrition_status varchar(9),
  shared_by_id              bigint,
  shared_date               timestamp,
  served_date               timestamp,
  patients_consent          boolean,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_diagnostic_centre_prescription_info_diagnostic_centre_prescrition_status check (diagnostic_centre_prescrition_status in ('CANCELLED','RECEIVED','CONFIRMED','SERVED')),
  constraint pk_diagnostic_centre_prescriptio primary key (id))
;

create table diagnostic_order (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  prescription_id           bigint,
  diagnostic_order_status   varchar(15),
  received_date             timestamp,
  confirmed_date            timestamp,
  cancelled_date            timestamp,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_diagnostic_order_diagnostic_order_status check (diagnostic_order_status in ('ORDER_CANCELLED','ORDER_RECEIVED','ORDER_CONFIRMED','ORDER_SERVED')),
  constraint pk_diagnostic_order primary key (id))
;

create table diagnostic_report (
  id                        bigint not null,
  diagnostic_order_id       bigint not null,
  file_name                 varchar(255),
  file_content              bytea,
  master_diagnostic_test_id bigint,
  report_status             varchar(20),
  sample_collected_date     timestamp,
  report_generated_date     timestamp,
  remarks                   TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_diagnostic_report_report_status check (report_status in ('SAMPLE_NOT_COLLECTED','REPORT_READY','SAMPLE_COLLECTED')),
  constraint pk_diagnostic_report primary key (id))
;

create table diagnostic_representative (
  id                        bigint not null,
  app_user_id               bigint,
  patient_id                bigint,
  diagnostic_type           varchar(255),
  file                      bytea,
  diagnostic_centre_id      bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_diagnostic_representative primary key (id))
;

create table diagnostic_test (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  name                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_diagnostic_test primary key (id))
;

create table diagnostic_test_line_item (
  id                        bigint not null,
  prescription_id           bigint not null,
  master_diagnostic_test_id bigint,
  full_name_of_diagnostic_test varchar(255),
  remarks                   TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_diagnostic_test_line_item primary key (id))
;

create table doctor (
  id                        bigint not null,
  app_user_id               bigint,
  registration_number       varchar(255),
  position                  varchar(255),
  degree                    varchar(255),
  description               TEXT,
  background_image          bytea,
  profile_image             bytea,
  experience                integer,
  search_index              TEXT,
  slug_url                  TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor primary key (id))
;

create table doctor_assistant (
  id                        bigint not null,
  app_user_id               bigint,
  degree                    varchar(255),
  experience                varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_assistant primary key (id))
;

create table doctor_award (
  id                        bigint not null,
  doctor_id                 bigint not null,
  award_name                varchar(255),
  awarded_by                varchar(255),
  year                      varchar(255),
  description               TEXT,
  position                  integer,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_award primary key (id))
;

create table doctor_clinic_info (
  id                        bigint not null,
  clinic_id                 bigint,
  doctor_id                 bigint,
  slot                      integer,
  slot_mr                   integer,
  active                    boolean,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_clinic_info primary key (id))
;

create table doctor_diagnostic_test (
  id                        bigint not null,
  doctor_id                 bigint not null,
  name                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_diagnostic_test primary key (id))
;

create table doctor_education (
  id                        bigint not null,
  doctor_id                 bigint not null,
  institution_name          varchar(255),
  degree                    varchar(255),
  from_year                 integer,
  to_year                   integer,
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_education primary key (id))
;

create table doctor_experience (
  id                        bigint not null,
  doctor_id                 bigint not null,
  institution_name          varchar(255),
  position                  varchar(255),
  description               TEXT,
  worked_from               integer,
  worked_to                 integer,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_experience primary key (id))
;

create table doctor_product (
  id                        bigint not null,
  doctor_id                 bigint not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  description               varchar(255),
  units_per_pack            bigint,
  full_name                 varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_product primary key (id))
;

create table doctor_publication (
  id                        bigint not null,
  doctor_id                 bigint not null,
  name                      varchar(255),
  article_for               varchar(255),
  year                      varchar(255),
  position                  integer,
  brief_description         TEXT,
  content                   TEXT,
  image                     bytea,
  file                      bytea,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_publication primary key (id))
;

create table doctor_social_work (
  id                        bigint not null,
  doctor_id                 bigint not null,
  title                     varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_doctor_social_work primary key (id))
;

create table feedback (
  id                        bigint not null,
  app_user_id               bigint,
  name                      varchar(255),
  role                      varchar(16),
  email                     varchar(255),
  date                      timestamp,
  ip_address                varchar(255),
  remarks                   TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_feedback_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_feedback primary key (id))
;

create table file_entity (
  id                        bigint not null,
  file_name                 varchar(255),
  mime_type                 varchar(255),
  byte_content              bytea,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_file_entity primary key (id))
;

create table head_quarter (
  id                        bigint not null,
  state                     varchar(35),
  name                      varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_head_quarter_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','CHANDIGARH','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH')),
  constraint pk_head_quarter primary key (id))
;

create table inventory (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  product_id                bigint,
  shelf_no                  varchar(255),
  product_quantity          integer,
  remarks                   varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_inventory primary key (id))
;

create table master_diagnostic_test (
  id                        bigint not null,
  name                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_master_diagnostic_test primary key (id))
;

create table master_product (
  id                        bigint not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  description               TEXT,
  units_per_pack            bigint,
  full_name                 varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_master_product primary key (id))
;

create table master_sig_code (
  code                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null)
;

create table master_specialization (
  id                        bigint not null,
  name                      TEXT,
  remarks                   TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_master_specialization primary key (id))
;

create table medical_representative (
  id                        bigint not null,
  app_user_id               bigint,
  region_alloted            varchar(255),
  company_name              varchar(255),
  is_active                 boolean,
  status                    varchar(255),
  manager_id                bigint,
  pharmaceutical_company_id bigint,
  is_doctor_visible         boolean,
  is_sample_visible         boolean,
  is_promotion_visible      boolean,
  is_pob_visible            boolean,
  is_remarks_visible        boolean,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_medical_representative primary key (id))
;

create table medicine_line_item (
  id                        bigint not null,
  prescription_id           bigint not null,
  medicine_id               bigint,
  full_name_of_medicine     varchar(255),
  medicine_full_name        varchar(255),
  dosage                    TEXT,
  frequency                 TEXT,
  remarks                   TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_medicine_line_item primary key (id))
;

create table order_line_item (
  id                        bigint not null,
  product_id                bigint,
  quantity                  float,
  batch_no                  varchar(255),
  expiry_date               timestamp,
  price                     float,
  sub_total                 float,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_order_line_item primary key (id))
;

create table patient (
  id                        bigint not null,
  app_user_id               bigint,
  age                       varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_patient primary key (id))
;

create table patient_doctor_info (
  id                        bigint not null,
  doctor_id                 bigint,
  patient_id                bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_patient_doctor_info primary key (id))
;

create table pharmaceutical_company (
  id                        bigint not null,
  name                      varchar(255),
  admin_mr_id               bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_pharmaceutical_company primary key (id))
;

create table pharmaceutical_product (
  id                        bigint not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  type_of_medicine          varchar(255),
  description               varchar(255),
  units_per_pack            bigint,
  full_name                 varchar(255),
  pharmaceutical_company_id bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_pharmaceutical_product primary key (id))
;

create table pharmacist (
  id                        bigint not null,
  app_user_id               bigint,
  pharmacy_id               bigint,
  category                  varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_pharmacist primary key (id))
;

create table pharmacy (
  id                        bigint not null,
  name                      varchar(255),
  address_id                bigint,
  contact_person            varchar(255),
  contact_number            varchar(255),
  description               TEXT,
  admin_pharmacist_id       bigint,
  background_image          bytea,
  search_index              TEXT,
  slug_url                  TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_pharmacy primary key (id))
;

create table pharmacy_info (
  id                        bigint not null,
  pharmacy_id               bigint,
  constraint pk_pharmacy_info primary key (id))
;

create table pharmacy_order (
  id                        bigint not null,
  prescription_id           bigint,
  pharmacy_prescription_status varchar(9),
  ordered_date              timestamp,
  served_date               timestamp,
  total_amount              float,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_pharmacy_order_pharmacy_prescription_status check (pharmacy_prescription_status in ('RECEIVED','CONFIRMED','SERVED')),
  constraint pk_pharmacy_order primary key (id))
;

create table pharmacy_prescription_info (
  id                        bigint not null,
  pharmacy_id               bigint,
  prescription_id           bigint,
  pharmacy_prescription_status varchar(9),
  shared_by_id              bigint,
  shared_date               timestamp,
  served_date               timestamp,
  patients_consent          boolean,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_pharmacy_prescription_info_pharmacy_prescription_status check (pharmacy_prescription_status in ('RECEIVED','CONFIRMED','SERVED')),
  constraint pk_pharmacy_prescription_info primary key (id))
;

create table pharmacy_product (
  id                        bigint not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  type_of_medicine          varchar(255),
  description               TEXT,
  units_per_pack            bigint,
  full_name                 varchar(255),
  pharmacy_id               bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_pharmacy_product primary key (id))
;

create table prescription (
  id                        bigint not null,
  doctor_id                 bigint,
  clinic_id                 bigint,
  patient_id                bigint,
  appointment_id            bigint,
  prescription_date         timestamp,
  problem_statement         TEXT,
  prognosis                 TEXT,
  remarks                   TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_prescription primary key (id))
;

create table question_and_answer (
  id                        bigint not null,
  question                  TEXT,
  answer                    TEXT,
  question_date             timestamp,
  answer_date               timestamp,
  question_by_id            bigint,
  answer_by_id              bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_question_and_answer primary key (id))
;

create table sample (
  id                        bigint not null,
  tpline_item_id            bigint not null,
  dcr_line_item_id          bigint,
  pharmaceutical_product_id bigint,
  quantity                  integer,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_sample primary key (id))
;

create table show_cased_product (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  name                      varchar(255),
  description               TEXT,
  mrp                       float,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_show_cased_product primary key (id))
;

create table show_cased_service (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  name                      varchar(255),
  description               TEXT,
  cost                      float,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_show_cased_service primary key (id))
;

create table sig_code (
  doctor_id                 bigint not null,
  code                      varchar(255),
  description               TEXT,
  created_on                timestamp not null,
  last_update               timestamp not null)
;

create table social_user (
  id                        bigint not null,
  image                     bytea,
  name                      varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  sex                       varchar(6),
  dob                       timestamp,
  first_log_in_via          varchar(8),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_social_user_sex check (sex in ('FEMALE','OTHER','MALE')),
  constraint ck_social_user_first_log_in_via check (first_log_in_via in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_social_user primary key (id))
;

create table tpline_item (
  id                        bigint not null,
  tour_plan_id              bigint not null,
  date                      timestamp,
  day_status                varchar(11),
  pob                       integer,
  remarks                   varchar(255),
  is_addedto_tourplan       boolean,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint ck_tpline_item_day_status check (day_status in ('HOLIDAY','WORKING_DAY')),
  constraint pk_tpline_item primary key (id))
;

create table tour_plan (
  id                        bigint not null,
  medical_representative_id bigint not null,
  for_month                 timestamp,
  submit_date               timestamp,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_tour_plan primary key (id))
;


create table dcrline_item_pharmaceutical_prod (
  dcrline_item_id                bigint not null,
  pharmaceutical_product_id      bigint not null,
  constraint pk_dcrline_item_pharmaceutical_prod primary key (dcrline_item_id, pharmaceutical_product_id))
;

create table diagnostic_centre_file_entity (
  diagnostic_centre_id           bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_diagnostic_centre_file_entity primary key (diagnostic_centre_id, file_entity_id))
;

create table diagnostic_centre_doctor (
  diagnostic_centre_id           bigint not null,
  doctor_id                      bigint not null,
  constraint pk_diagnostic_centre_doctor primary key (diagnostic_centre_id, doctor_id))
;

create table diagnostic_centre_master_diagnos (
  diagnostic_centre_id           bigint not null,
  master_diagnostic_test_id      bigint not null,
  constraint pk_diagnostic_centre_master_diagnos primary key (diagnostic_centre_id, master_diagnostic_test_id))
;

create table diagnostic_centre_prescription (
  diagnostic_centre_id           bigint not null,
  prescription_id                bigint not null,
  constraint pk_diagnostic_centre_prescription primary key (diagnostic_centre_id, prescription_id))
;

create table diagnostic_centre_prescription_i (
  diagnostic_centre_prescription_info_id bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_diagnostic_centre_prescription_i primary key (diagnostic_centre_prescription_info_id, file_entity_id))
;

create table doctor_master_specialization (
  doctor_id                      bigint not null,
  master_specialization_id       bigint not null,
  constraint pk_doctor_master_specialization primary key (doctor_id, master_specialization_id))
;

create table doctor_pharmacy (
  doctor_id                      bigint not null,
  pharmacy_id                    bigint not null,
  constraint pk_doctor_pharmacy primary key (doctor_id, pharmacy_id))
;

create table doctor_diagnostic_centre (
  doctor_id                      bigint not null,
  diagnostic_centre_id           bigint not null,
  constraint pk_doctor_diagnostic_centre primary key (doctor_id, diagnostic_centre_id))
;

create table medical_representative_doctor (
  medical_representative_id      bigint not null,
  doctor_id                      bigint not null,
  constraint pk_medical_representative_doctor primary key (medical_representative_id, doctor_id))
;

create table medical_representative_head_quar (
  medical_representative_id      bigint not null,
  head_quarter_id                bigint not null,
  constraint pk_medical_representative_head_quar primary key (medical_representative_id, head_quarter_id))
;

create table patient_pharmacy (
  patient_id                     bigint not null,
  pharmacy_id                    bigint not null,
  constraint pk_patient_pharmacy primary key (patient_id, pharmacy_id))
;

create table patient_diagnostic_centre (
  patient_id                     bigint not null,
  diagnostic_centre_id           bigint not null,
  constraint pk_patient_diagnostic_centre primary key (patient_id, diagnostic_centre_id))
;

create table patient_file_entity (
  patient_id                     bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_patient_file_entity primary key (patient_id, file_entity_id))
;

create table pharmacy_file_entity (
  pharmacy_id                    bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_pharmacy_file_entity primary key (pharmacy_id, file_entity_id))
;

create table show_cased_service_file_entity (
  show_cased_service_id          bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_show_cased_service_file_entity primary key (show_cased_service_id, file_entity_id))
;

create table tpline_item_doctor (
  tpline_item_id                 bigint not null,
  doctor_id                      bigint not null,
  constraint pk_tpline_item_doctor primary key (tpline_item_id, doctor_id))
;

create table tpline_item_pharmaceutical_produ (
  tpline_item_id                 bigint not null,
  pharmaceutical_product_id      bigint not null,
  constraint pk_tpline_item_pharmaceutical_produ primary key (tpline_item_id, pharmaceutical_product_id))
;
create sequence address_seq;

create sequence app_user_seq;

create sequence appointment_seq;

create sequence article_seq;

create sequence article_category_seq;

create sequence batch_seq;

create sequence blog_comment_seq;

create sequence blog_comment_reply_seq;

create sequence clinic_seq;

create sequence dcrline_item_seq;

create sequence daily_call_report_seq;

create sequence day_schedule_seq;

create sequence designation_seq;

create sequence diagnostic_centre_seq;

create sequence diagnostic_centre_prescription_info_seq;

create sequence diagnostic_order_seq;

create sequence diagnostic_report_seq;

create sequence diagnostic_representative_seq;

create sequence diagnostic_test_seq;

create sequence diagnostic_test_line_item_seq;

create sequence doctor_seq;

create sequence doctor_assistant_seq;

create sequence doctor_award_seq;

create sequence doctor_clinic_info_seq;

create sequence doctor_diagnostic_test_seq;

create sequence doctor_education_seq;

create sequence doctor_experience_seq;

create sequence doctor_product_seq;

create sequence doctor_publication_seq;

create sequence doctor_social_work_seq;

create sequence feedback_seq;

create sequence file_entity_seq;

create sequence head_quarter_seq;

create sequence inventory_seq;

create sequence master_diagnostic_test_seq;

create sequence master_product_seq;

create sequence master_specialization_seq;

create sequence medical_representative_seq;

create sequence medicine_line_item_seq;

create sequence order_line_item_seq;

create sequence patient_seq;

create sequence patient_doctor_info_seq;

create sequence pharmaceutical_company_seq;

create sequence pharmaceutical_product_seq;

create sequence pharmacist_seq;

create sequence pharmacy_seq;

create sequence pharmacy_info_seq;

create sequence pharmacy_order_seq;

create sequence pharmacy_prescription_info_seq;

create sequence pharmacy_product_seq;

create sequence prescription_seq;

create sequence question_and_answer_seq;

create sequence sample_seq;

create sequence show_cased_product_seq;

create sequence show_cased_service_seq;

create sequence social_user_seq;

create sequence tpline_item_seq;

create sequence tour_plan_seq;

alter table app_user add constraint fk_app_user_address_1 foreign key (address_id) references address (id);
create index ix_app_user_address_1 on app_user (address_id);
alter table appointment add constraint fk_appointment_requestedBy_2 foreign key (requested_by_id) references app_user (id);
create index ix_appointment_requestedBy_2 on appointment (requested_by_id);
alter table appointment add constraint fk_appointment_apporovedBy_3 foreign key (apporoved_by_id) references app_user (id);
create index ix_appointment_apporovedBy_3 on appointment (apporoved_by_id);
alter table appointment add constraint fk_appointment_doctorClinicInf_4 foreign key (doctor_clinic_info_id) references doctor_clinic_info (id);
create index ix_appointment_doctorClinicInf_4 on appointment (doctor_clinic_info_id);
alter table article add constraint fk_article_category_5 foreign key (category_id) references article_category (id);
create index ix_article_category_5 on article (category_id);
alter table batch add constraint fk_batch_inventory_6 foreign key (inventory_id) references inventory (id);
create index ix_batch_inventory_6 on batch (inventory_id);
alter table batch add constraint fk_batch_product_7 foreign key (product_id) references master_product (id);
create index ix_batch_product_7 on batch (product_id);
alter table blog_comment add constraint fk_blog_comment_article_8 foreign key (article_id) references article (id);
create index ix_blog_comment_article_8 on blog_comment (article_id);
alter table blog_comment add constraint fk_blog_comment_by_9 foreign key (by_id) references app_user (id);
create index ix_blog_comment_by_9 on blog_comment (by_id);
alter table blog_comment add constraint fk_blog_comment_socialBy_10 foreign key (social_by_id) references social_user (id);
create index ix_blog_comment_socialBy_10 on blog_comment (social_by_id);
alter table blog_comment_reply add constraint fk_blog_comment_reply_blog_co_11 foreign key (blog_comment_id) references blog_comment (id);
create index ix_blog_comment_reply_blog_co_11 on blog_comment_reply (blog_comment_id);
alter table blog_comment_reply add constraint fk_blog_comment_reply_by_12 foreign key (by_id) references app_user (id);
create index ix_blog_comment_reply_by_12 on blog_comment_reply (by_id);
alter table blog_comment_reply add constraint fk_blog_comment_reply_socialB_13 foreign key (social_by_id) references social_user (id);
create index ix_blog_comment_reply_socialB_13 on blog_comment_reply (social_by_id);
alter table clinic add constraint fk_clinic_address_14 foreign key (address_id) references address (id);
create index ix_clinic_address_14 on clinic (address_id);
alter table dcrline_item add constraint fk_dcrline_item_daily_call_re_15 foreign key (daily_call_report_id) references daily_call_report (id);
create index ix_dcrline_item_daily_call_re_15 on dcrline_item (daily_call_report_id);
alter table dcrline_item add constraint fk_dcrline_item_doctor_16 foreign key (doctor_id) references doctor (id);
create index ix_dcrline_item_doctor_16 on dcrline_item (doctor_id);
alter table dcrline_item add constraint fk_dcrline_item_headQuater_17 foreign key (head_quater_id) references head_quarter (id);
create index ix_dcrline_item_headQuater_17 on dcrline_item (head_quater_id);
alter table daily_call_report add constraint fk_daily_call_report_submitte_18 foreign key (submitter_id) references medical_representative (id);
create index ix_daily_call_report_submitte_18 on daily_call_report (submitter_id);
alter table daily_call_report add constraint fk_daily_call_report_approver_19 foreign key (approver_id) references medical_representative (id);
create index ix_daily_call_report_approver_19 on daily_call_report (approver_id);
alter table daily_call_report add constraint fk_daily_call_report_headQuar_20 foreign key (head_quarter_id) references head_quarter (id);
create index ix_daily_call_report_headQuar_20 on daily_call_report (head_quarter_id);
alter table day_schedule add constraint fk_day_schedule_doctor_clinic_21 foreign key (doctor_clinic_info_id) references doctor_clinic_info (id);
create index ix_day_schedule_doctor_clinic_21 on day_schedule (doctor_clinic_info_id);
alter table designation add constraint fk_designation_pharmaceutical_22 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_designation_pharmaceutical_22 on designation (pharmaceutical_company_id);
alter table designation add constraint fk_designation_mr_23 foreign key (mr_id) references medical_representative (id);
create index ix_designation_mr_23 on designation (mr_id);
alter table diagnostic_centre add constraint fk_diagnostic_centre_address_24 foreign key (address_id) references address (id);
create index ix_diagnostic_centre_address_24 on diagnostic_centre (address_id);
alter table diagnostic_centre add constraint fk_diagnostic_centre_diagnost_25 foreign key (diagnostic_rep_admin_id) references diagnostic_representative (id);
create index ix_diagnostic_centre_diagnost_25 on diagnostic_centre (diagnostic_rep_admin_id);
alter table diagnostic_centre_prescription_info add constraint fk_diagnostic_centre_prescrip_26 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_centre_prescrip_26 on diagnostic_centre_prescription_info (diagnostic_centre_id);
alter table diagnostic_centre_prescription_info add constraint fk_diagnostic_centre_prescrip_27 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_centre_prescrip_27 on diagnostic_centre_prescription_info (prescription_id);
alter table diagnostic_centre_prescription_info add constraint fk_diagnostic_centre_prescrip_28 foreign key (shared_by_id) references app_user (id);
create index ix_diagnostic_centre_prescrip_28 on diagnostic_centre_prescription_info (shared_by_id);
alter table diagnostic_order add constraint fk_diagnostic_order_diagnosti_29 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_order_diagnosti_29 on diagnostic_order (diagnostic_centre_id);
alter table diagnostic_order add constraint fk_diagnostic_order_prescript_30 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_order_prescript_30 on diagnostic_order (prescription_id);
alter table diagnostic_report add constraint fk_diagnostic_report_diagnost_31 foreign key (diagnostic_order_id) references diagnostic_order (id);
create index ix_diagnostic_report_diagnost_31 on diagnostic_report (diagnostic_order_id);
alter table diagnostic_report add constraint fk_diagnostic_report_masterDi_32 foreign key (master_diagnostic_test_id) references master_diagnostic_test (id);
create index ix_diagnostic_report_masterDi_32 on diagnostic_report (master_diagnostic_test_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__33 foreign key (app_user_id) references app_user (id);
create index ix_diagnostic_representative__33 on diagnostic_representative (app_user_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__34 foreign key (patient_id) references patient (id);
create index ix_diagnostic_representative__34 on diagnostic_representative (patient_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__35 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_representative__35 on diagnostic_representative (diagnostic_centre_id);
alter table diagnostic_test add constraint fk_diagnostic_test_diagnostic_36 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_test_diagnostic_36 on diagnostic_test (diagnostic_centre_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__37 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_test_line_item__37 on diagnostic_test_line_item (prescription_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__38 foreign key (master_diagnostic_test_id) references master_diagnostic_test (id);
create index ix_diagnostic_test_line_item__38 on diagnostic_test_line_item (master_diagnostic_test_id);
alter table doctor add constraint fk_doctor_appUser_39 foreign key (app_user_id) references app_user (id);
create index ix_doctor_appUser_39 on doctor (app_user_id);
alter table doctor_assistant add constraint fk_doctor_assistant_appUser_40 foreign key (app_user_id) references app_user (id);
create index ix_doctor_assistant_appUser_40 on doctor_assistant (app_user_id);
alter table doctor_award add constraint fk_doctor_award_doctor_41 foreign key (doctor_id) references doctor (id);
create index ix_doctor_award_doctor_41 on doctor_award (doctor_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_clinic_42 foreign key (clinic_id) references clinic (id);
create index ix_doctor_clinic_info_clinic_42 on doctor_clinic_info (clinic_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_doctor_43 foreign key (doctor_id) references doctor (id);
create index ix_doctor_clinic_info_doctor_43 on doctor_clinic_info (doctor_id);
alter table doctor_diagnostic_test add constraint fk_doctor_diagnostic_test_doc_44 foreign key (doctor_id) references doctor (id);
create index ix_doctor_diagnostic_test_doc_44 on doctor_diagnostic_test (doctor_id);
alter table doctor_education add constraint fk_doctor_education_doctor_45 foreign key (doctor_id) references doctor (id);
create index ix_doctor_education_doctor_45 on doctor_education (doctor_id);
alter table doctor_experience add constraint fk_doctor_experience_doctor_46 foreign key (doctor_id) references doctor (id);
create index ix_doctor_experience_doctor_46 on doctor_experience (doctor_id);
alter table doctor_product add constraint fk_doctor_product_doctor_47 foreign key (doctor_id) references doctor (id);
create index ix_doctor_product_doctor_47 on doctor_product (doctor_id);
alter table doctor_publication add constraint fk_doctor_publication_doctor_48 foreign key (doctor_id) references doctor (id);
create index ix_doctor_publication_doctor_48 on doctor_publication (doctor_id);
alter table doctor_social_work add constraint fk_doctor_social_work_doctor_49 foreign key (doctor_id) references doctor (id);
create index ix_doctor_social_work_doctor_49 on doctor_social_work (doctor_id);
alter table feedback add constraint fk_feedback_appUser_50 foreign key (app_user_id) references app_user (id);
create index ix_feedback_appUser_50 on feedback (app_user_id);
alter table inventory add constraint fk_inventory_pharmacy_51 foreign key (pharmacy_id) references pharmacy (id);
create index ix_inventory_pharmacy_51 on inventory (pharmacy_id);
alter table inventory add constraint fk_inventory_product_52 foreign key (product_id) references master_product (id);
create index ix_inventory_product_52 on inventory (product_id);
alter table medical_representative add constraint fk_medical_representative_app_53 foreign key (app_user_id) references app_user (id);
create index ix_medical_representative_app_53 on medical_representative (app_user_id);
alter table medical_representative add constraint fk_medical_representative_man_54 foreign key (manager_id) references medical_representative (id);
create index ix_medical_representative_man_54 on medical_representative (manager_id);
alter table medical_representative add constraint fk_medical_representative_pha_55 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_medical_representative_pha_55 on medical_representative (pharmaceutical_company_id);
alter table medicine_line_item add constraint fk_medicine_line_item_prescri_56 foreign key (prescription_id) references prescription (id);
create index ix_medicine_line_item_prescri_56 on medicine_line_item (prescription_id);
alter table medicine_line_item add constraint fk_medicine_line_item_medicin_57 foreign key (medicine_id) references master_product (id);
create index ix_medicine_line_item_medicin_57 on medicine_line_item (medicine_id);
alter table order_line_item add constraint fk_order_line_item_product_58 foreign key (product_id) references master_product (id);
create index ix_order_line_item_product_58 on order_line_item (product_id);
alter table patient add constraint fk_patient_appUser_59 foreign key (app_user_id) references app_user (id);
create index ix_patient_appUser_59 on patient (app_user_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_doctor_60 foreign key (doctor_id) references doctor (id);
create index ix_patient_doctor_info_doctor_60 on patient_doctor_info (doctor_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_patien_61 foreign key (patient_id) references patient (id);
create index ix_patient_doctor_info_patien_61 on patient_doctor_info (patient_id);
alter table pharmaceutical_company add constraint fk_pharmaceutical_company_adm_62 foreign key (admin_mr_id) references medical_representative (id);
create index ix_pharmaceutical_company_adm_62 on pharmaceutical_company (admin_mr_id);
alter table pharmaceutical_product add constraint fk_pharmaceutical_product_pha_63 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_pharmaceutical_product_pha_63 on pharmaceutical_product (pharmaceutical_company_id);
alter table pharmacist add constraint fk_pharmacist_appUser_64 foreign key (app_user_id) references app_user (id);
create index ix_pharmacist_appUser_64 on pharmacist (app_user_id);
alter table pharmacist add constraint fk_pharmacist_pharmacy_65 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacist_pharmacy_65 on pharmacist (pharmacy_id);
alter table pharmacy add constraint fk_pharmacy_address_66 foreign key (address_id) references address (id);
create index ix_pharmacy_address_66 on pharmacy (address_id);
alter table pharmacy add constraint fk_pharmacy_adminPharmacist_67 foreign key (admin_pharmacist_id) references pharmacist (id);
create index ix_pharmacy_adminPharmacist_67 on pharmacy (admin_pharmacist_id);
alter table pharmacy_info add constraint fk_pharmacy_info_pharmacy_68 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_info_pharmacy_68 on pharmacy_info (pharmacy_id);
alter table pharmacy_order add constraint fk_pharmacy_order_prescriptio_69 foreign key (prescription_id) references prescription (id);
create index ix_pharmacy_order_prescriptio_69 on pharmacy_order (prescription_id);
alter table pharmacy_prescription_info add constraint fk_pharmacy_prescription_info_70 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_prescription_info_70 on pharmacy_prescription_info (pharmacy_id);
alter table pharmacy_prescription_info add constraint fk_pharmacy_prescription_info_71 foreign key (prescription_id) references prescription (id);
create index ix_pharmacy_prescription_info_71 on pharmacy_prescription_info (prescription_id);
alter table pharmacy_prescription_info add constraint fk_pharmacy_prescription_info_72 foreign key (shared_by_id) references app_user (id);
create index ix_pharmacy_prescription_info_72 on pharmacy_prescription_info (shared_by_id);
alter table pharmacy_product add constraint fk_pharmacy_product_pharmacy_73 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_product_pharmacy_73 on pharmacy_product (pharmacy_id);
alter table prescription add constraint fk_prescription_doctor_74 foreign key (doctor_id) references doctor (id);
create index ix_prescription_doctor_74 on prescription (doctor_id);
alter table prescription add constraint fk_prescription_clinic_75 foreign key (clinic_id) references clinic (id);
create index ix_prescription_clinic_75 on prescription (clinic_id);
alter table prescription add constraint fk_prescription_patient_76 foreign key (patient_id) references patient (id);
create index ix_prescription_patient_76 on prescription (patient_id);
alter table prescription add constraint fk_prescription_appointment_77 foreign key (appointment_id) references appointment (id);
create index ix_prescription_appointment_77 on prescription (appointment_id);
alter table question_and_answer add constraint fk_question_and_answer_questi_78 foreign key (question_by_id) references app_user (id);
create index ix_question_and_answer_questi_78 on question_and_answer (question_by_id);
alter table question_and_answer add constraint fk_question_and_answer_answer_79 foreign key (answer_by_id) references app_user (id);
create index ix_question_and_answer_answer_79 on question_and_answer (answer_by_id);
alter table sample add constraint fk_sample_tpline_item_80 foreign key (tpline_item_id) references tpline_item (id);
create index ix_sample_tpline_item_80 on sample (tpline_item_id);
alter table sample add constraint fk_sample_dcrLineItem_81 foreign key (dcr_line_item_id) references dcrline_item (id);
create index ix_sample_dcrLineItem_81 on sample (dcr_line_item_id);
alter table sample add constraint fk_sample_pharmaceuticalProdu_82 foreign key (pharmaceutical_product_id) references pharmaceutical_product (id);
create index ix_sample_pharmaceuticalProdu_82 on sample (pharmaceutical_product_id);
alter table show_cased_product add constraint fk_show_cased_product_pharmac_83 foreign key (pharmacy_id) references pharmacy (id);
create index ix_show_cased_product_pharmac_83 on show_cased_product (pharmacy_id);
alter table show_cased_service add constraint fk_show_cased_service_diagnos_84 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_show_cased_service_diagnos_84 on show_cased_service (diagnostic_centre_id);
alter table sig_code add constraint fk_sig_code_doctor_85 foreign key (doctor_id) references doctor (id);
create index ix_sig_code_doctor_85 on sig_code (doctor_id);
alter table tpline_item add constraint fk_tpline_item_tour_plan_86 foreign key (tour_plan_id) references tour_plan (id);
create index ix_tpline_item_tour_plan_86 on tpline_item (tour_plan_id);
alter table tour_plan add constraint fk_tour_plan_medical_represen_87 foreign key (medical_representative_id) references medical_representative (id);
create index ix_tour_plan_medical_represen_87 on tour_plan (medical_representative_id);



alter table dcrline_item_pharmaceutical_prod add constraint fk_dcrline_item_pharmaceutica_01 foreign key (dcrline_item_id) references dcrline_item (id);

alter table dcrline_item_pharmaceutical_prod add constraint fk_dcrline_item_pharmaceutica_02 foreign key (pharmaceutical_product_id) references pharmaceutical_product (id);

alter table diagnostic_centre_file_entity add constraint fk_diagnostic_centre_file_ent_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_file_entity add constraint fk_diagnostic_centre_file_ent_02 foreign key (file_entity_id) references file_entity (id);

alter table diagnostic_centre_doctor add constraint fk_diagnostic_centre_doctor_d_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_doctor add constraint fk_diagnostic_centre_doctor_d_02 foreign key (doctor_id) references doctor (id);

alter table diagnostic_centre_master_diagnos add constraint fk_diagnostic_centre_master_d_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_master_diagnos add constraint fk_diagnostic_centre_master_d_02 foreign key (master_diagnostic_test_id) references master_diagnostic_test (id);

alter table diagnostic_centre_prescription add constraint fk_diagnostic_centre_prescrip_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_prescription add constraint fk_diagnostic_centre_prescrip_02 foreign key (prescription_id) references prescription (id);

alter table diagnostic_centre_prescription_i add constraint fk_diagnostic_centre_prescrip_01 foreign key (diagnostic_centre_prescription_info_id) references diagnostic_centre_prescription_info (id);

alter table diagnostic_centre_prescription_i add constraint fk_diagnostic_centre_prescrip_02 foreign key (file_entity_id) references file_entity (id);

alter table doctor_master_specialization add constraint fk_doctor_master_specializati_01 foreign key (doctor_id) references doctor (id);

alter table doctor_master_specialization add constraint fk_doctor_master_specializati_02 foreign key (master_specialization_id) references master_specialization (id);

alter table doctor_pharmacy add constraint fk_doctor_pharmacy_doctor_01 foreign key (doctor_id) references doctor (id);

alter table doctor_pharmacy add constraint fk_doctor_pharmacy_pharmacy_02 foreign key (pharmacy_id) references pharmacy (id);

alter table doctor_diagnostic_centre add constraint fk_doctor_diagnostic_centre_d_01 foreign key (doctor_id) references doctor (id);

alter table doctor_diagnostic_centre add constraint fk_doctor_diagnostic_centre_d_02 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_02 foreign key (doctor_id) references doctor (id);

alter table medical_representative_head_quar add constraint fk_medical_representative_hea_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_head_quar add constraint fk_medical_representative_hea_02 foreign key (head_quarter_id) references head_quarter (id);

alter table patient_pharmacy add constraint fk_patient_pharmacy_patient_01 foreign key (patient_id) references patient (id);

alter table patient_pharmacy add constraint fk_patient_pharmacy_pharmacy_02 foreign key (pharmacy_id) references pharmacy (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__01 foreign key (patient_id) references patient (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__02 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table patient_file_entity add constraint fk_patient_file_entity_patien_01 foreign key (patient_id) references patient (id);

alter table patient_file_entity add constraint fk_patient_file_entity_file_e_02 foreign key (file_entity_id) references file_entity (id);

alter table pharmacy_file_entity add constraint fk_pharmacy_file_entity_pharm_01 foreign key (pharmacy_id) references pharmacy (id);

alter table pharmacy_file_entity add constraint fk_pharmacy_file_entity_file__02 foreign key (file_entity_id) references file_entity (id);

alter table show_cased_service_file_entity add constraint fk_show_cased_service_file_en_01 foreign key (show_cased_service_id) references show_cased_service (id);

alter table show_cased_service_file_entity add constraint fk_show_cased_service_file_en_02 foreign key (file_entity_id) references file_entity (id);

alter table tpline_item_doctor add constraint fk_tpline_item_doctor_tpline__01 foreign key (tpline_item_id) references tpline_item (id);

alter table tpline_item_doctor add constraint fk_tpline_item_doctor_doctor_02 foreign key (doctor_id) references doctor (id);

alter table tpline_item_pharmaceutical_produ add constraint fk_tpline_item_pharmaceutical_01 foreign key (tpline_item_id) references tpline_item (id);

alter table tpline_item_pharmaceutical_produ add constraint fk_tpline_item_pharmaceutical_02 foreign key (pharmaceutical_product_id) references pharmaceutical_product (id);
=======
create table address (
  id                        bigint not null,
  created_on                timestamp not null,
  address_line1             varchar(255),
  address_line2             varchar(255),
  address_line3             varchar(255),
  area                      varchar(255),
  latitude                  varchar(255),
  longitude                 varchar(255),
  city                      varchar(255),
  state                     varchar(35),
  pin_code                  varchar(255),
  fetched_pin_code          varchar(255),
  country                   varchar(27),
  last_update               timestamp not null,
  constraint ck_address_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','CHANDIGARH','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH')),
  constraint ck_address_country check (country in ('ARMENIA','ANGUILLA','AUSTRALIA','ARUBA','CHAD','BOSNIA_AND_HERZEGOVINA','ANTIGUA_AND_BARBUDA','CHINA','ASHMORE_AND_CARTIER_ISLANDS','AMERICAN_SAMOA','COMOROS','INDIA','BOLIVIA','CAMEROON','PAKISTAN','DENMARK','BURUNDI','CAPE_VERDE','BULGARIA','ARGENTINA','DJIBOUTI','BELGIUM','ALBANIA','BAHRAIN','ALGERIA','ECUADOR','BELARUS','BARBADOS','BURMA','CHILE','BRUNEI','BELIZE','AZERBAIJAN','BHUTAN','CANADA','AFGHANISTAN','ANDORRA','CAMBODIA','AKROTIRI','AUSTRIA','BOUVET_ISLAND','BERMUDA','DOMINICA','ANGOLA','EGYPT','BENIN','UNITED_STATES','DHEKELIA','BOTSWANA','CUBA','ANTARCTICA','BRAZIL','CYPRUS','BURKINA_FASO','BANGLADESH','COLOMBIA')),
  constraint pk_address primary key (id))
;

create table app_user (
  id                        bigint not null,
  created_on                timestamp not null,
  image                     bytea,
  name                      varchar(255),
  username                  varchar(255),
  mobile_number             bigint,
  email                     varchar(255),
  password                  TEXT,
  salt                      TEXT,
  sex                       varchar(6),
  dob                       timestamp,
  role                      varchar(16),
  address_id                bigint,
  email_confirmed           boolean,
  mobile_number_confirmed   boolean,
  email_confirmation_key    TEXT,
  mobile_number_confirmation_key TEXT,
  forgot_password_confirmation_key TEXT,
  last_update               timestamp not null,
  constraint ck_app_user_sex check (sex in ('FEMALE','OTHER','MALE')),
  constraint ck_app_user_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_app_user primary key (id))
;

create table appointment (
  id                        bigint not null,
  created_on                timestamp not null,
  appointment_time          timestamp,
  appointment_status        varchar(9),
  requested_by_id           bigint,
  apporoved_by_id           bigint,
  problem_statement         TEXT,
  doctor_clinic_info_id     bigint,
  booked_on                 timestamp,
  last_update               timestamp not null,
  constraint ck_appointment_appointment_status check (appointment_status in ('CANCELLED','REQUESTED','APPROVED','AVAILABLE','SERVED')),
  constraint pk_appointment primary key (id))
;

create table article (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      TEXT,
  position                  integer,
  short_description         TEXT,
  on_hover_content          TEXT,
  content                   TEXT,
  slug_url                  TEXT,
  thumbnail                 bytea,
  image                     bytea,
  html_title                TEXT,
  html_meta_description     TEXT,
  html_keywords             TEXT,
  category_id               bigint,
  last_update               timestamp not null,
  constraint pk_article primary key (id))
;

create table article_category (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      TEXT,
  position                  integer,
  short_description         TEXT,
  on_hover_content          TEXT,
  content                   TEXT,
  slug_url                  TEXT,
  thumbnail                 bytea,
  image                     bytea,
  last_update               timestamp not null,
  constraint pk_article_category primary key (id))
;

create table batch (
  id                        bigint not null,
  inventory_id              bigint not null,
  created_on                timestamp not null,
  product_id                bigint,
  batch_no                  varchar(255),
  mrp                       float,
  quantity                  integer,
  tax                       float,
  discount                  float,
  last_update               timestamp not null,
  expiry_date               timestamp not null,
  constraint pk_batch primary key (id))
;

create table blog_comment (
  id                        bigint not null,
  article_id                bigint not null,
  created_on                timestamp not null,
  message                   TEXT,
  date                      timestamp,
  by_id                     bigint,
  social_by_id              bigint,
  blog_commentator_type     varchar(8),
  last_update               timestamp not null,
  constraint ck_blog_comment_blog_commentator_type check (blog_commentator_type in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_blog_comment primary key (id))
;

create table blog_comment_reply (
  id                        bigint not null,
  blog_comment_id           bigint not null,
  created_on                timestamp not null,
  message                   TEXT,
  date                      timestamp,
  by_id                     bigint,
  social_by_id              bigint,
  blog_commentator_type     varchar(8),
  last_update               timestamp not null,
  constraint ck_blog_comment_reply_blog_commentator_type check (blog_commentator_type in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_blog_comment_reply primary key (id))
;

create table clinic (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  contact_person_name       varchar(255),
  contact_no                varchar(255),
  address_id                bigint,
  last_update               timestamp not null,
  constraint pk_clinic primary key (id))
;

create table dcrline_item (
  id                        bigint not null,
  daily_call_report_id      bigint not null,
  created_on                timestamp not null,
  doctor_id                 bigint,
  in_time                   timestamp,
  out_time                  timestamp,
  pob                       integer,
  head_quater_id            bigint,
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint pk_dcrline_item primary key (id))
;

create table daily_call_report (
  id                        bigint not null,
  created_on                timestamp not null,
  for_date                  timestamp,
  submitter_id              bigint,
  approver_id               bigint,
  dcr_status                varchar(9),
  submitted_date            timestamp,
  response_on               timestamp,
  re_opened_date            timestamp,
  head_quarter_id           bigint,
  last_update               timestamp not null,
  constraint ck_daily_call_report_dcr_status check (dcr_status in ('REJECTED','DRAFT','APPROVED','REOPENED','SUBMITTED')),
  constraint pk_daily_call_report primary key (id))
;

create table day_schedule (
  id                        bigint not null,
  doctor_clinic_info_id     bigint not null,
  created_on                timestamp not null,
  day                       varchar(9),
  from_time                 varchar(255),
  to_time                   varchar(255),
  requester                 varchar(16),
  last_update               timestamp not null,
  constraint ck_day_schedule_day check (day in ('MONDAY','SUNDAY','WEDNESDAY','THURSDAY','SATURDAY','TUESDAY','FRIDAY')),
  constraint ck_day_schedule_requester check (requester in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_day_schedule primary key (id))
;

create table diagnostic_centre (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  contact_person            varchar(255),
  address_id                bigint,
  mobile_no                 varchar(255),
  description               TEXT,
  backgroud_image           bytea,
  email_id                  varchar(255),
  website_name              varchar(255),
  diagnostic_rep_admin_id   bigint,
  search_index              TEXT,
  slug_url                  TEXT,
  last_update               timestamp not null,
  constraint pk_diagnostic_centre primary key (id))
;

create table diagnostic_centre_prescription_info (
  id                        bigint not null,
  created_on                timestamp not null,
  s                         varchar(255),
  diagnostic_centre_id      bigint,
  prescription_id           bigint,
  diagnostic_centre_prescrition_status varchar(9),
  shared_by_id              bigint,
  shared_date               timestamp,
  served_date               timestamp,
  patients_consent          boolean,
  last_update               timestamp not null,
  constraint ck_diagnostic_centre_prescription_info_diagnostic_centre_prescrition_status check (diagnostic_centre_prescrition_status in ('CANCELLED','RECEIVED','CONFIRMED','SERVED')),
  constraint pk_diagnostic_centre_prescriptio primary key (id))
;

create table diagnostic_order (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  created_on                timestamp not null,
  prescription_id           bigint,
  diagnostic_order_status   varchar(15),
  received_date             timestamp,
  confirmed_date            timestamp,
  cancelled_date            timestamp,
  last_update               timestamp not null,
  constraint ck_diagnostic_order_diagnostic_order_status check (diagnostic_order_status in ('ORDER_CANCELLED','ORDER_RECEIVED','ORDER_CONFIRMED','ORDER_SERVED')),
  constraint pk_diagnostic_order primary key (id))
;

create table diagnostic_report (
  id                        bigint not null,
  diagnostic_order_id       bigint not null,
  created_on                timestamp not null,
  file_name                 varchar(255),
  file_content              bytea,
  master_diagnostic_test_id bigint,
  report_status             varchar(20),
  sample_collected_date     timestamp,
  report_generated_date     timestamp,
  remarks                   TEXT,
  last_update               timestamp not null,
  constraint ck_diagnostic_report_report_status check (report_status in ('SAMPLE_NOT_COLLECTED','REPORT_READY','SAMPLE_COLLECTED')),
  constraint pk_diagnostic_report primary key (id))
;

create table diagnostic_representative (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  patient_id                bigint,
  diagnostic_type           varchar(255),
  file                      bytea,
  diagnostic_centre_id      bigint,
  last_update               timestamp not null,
  constraint pk_diagnostic_representative primary key (id))
;

create table diagnostic_test (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_diagnostic_test primary key (id))
;

create table diagnostic_test_line_item (
  id                        bigint not null,
  prescription_id           bigint not null,
  created_on                timestamp not null,
  master_diagnostic_test_id bigint,
  full_name_of_diagnostic_test varchar(255),
  remarks                   TEXT,
  last_update               timestamp not null,
  constraint pk_diagnostic_test_line_item primary key (id))
;

create table doctor (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  registration_number       varchar(255),
  position                  varchar(255),
  degree                    varchar(255),
  description               TEXT,
  background_image          bytea,
  profile_image             bytea,
  experience                integer,
  search_index              TEXT,
  slug_url                  TEXT,
  last_update               timestamp not null,
  constraint pk_doctor primary key (id))
;

create table doctor_assistant (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  degree                    varchar(255),
  experience                varchar(255),
  last_update               timestamp not null,
  constraint pk_doctor_assistant primary key (id))
;

create table doctor_award (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  award_name                varchar(255),
  awarded_by                varchar(255),
  year                      varchar(255),
  description               TEXT,
  position                  integer,
  last_update               timestamp not null,
  constraint pk_doctor_award primary key (id))
;

create table doctor_clinic_info (
  id                        bigint not null,
  created_on                timestamp not null,
  clinic_id                 bigint,
  doctor_id                 bigint,
  slot                      integer,
  slot_mr                   integer,
  active                    boolean,
  last_update               timestamp not null,
  constraint pk_doctor_clinic_info primary key (id))
;

create table doctor_diagnostic_test (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_doctor_diagnostic_test primary key (id))
;

create table doctor_education (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  institution_name          varchar(255),
  degree                    varchar(255),
  from_year                 integer,
  to_year                   integer,
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_doctor_education primary key (id))
;

create table doctor_experience (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  institution_name          varchar(255),
  position                  varchar(255),
  description               TEXT,
  worked_from               integer,
  worked_to                 integer,
  last_update               timestamp not null,
  constraint pk_doctor_experience primary key (id))
;

create table doctor_product (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  description               varchar(255),
  units_per_pack            bigint,
  full_name                 varchar(255),
  last_update               timestamp not null,
  constraint pk_doctor_product primary key (id))
;

create table doctor_publication (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  article_for               varchar(255),
  year                      varchar(255),
  position                  integer,
  brief_description         TEXT,
  content                   TEXT,
  image                     bytea,
  file                      bytea,
  last_update               timestamp not null,
  constraint pk_doctor_publication primary key (id))
;

create table doctor_social_work (
  id                        bigint not null,
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  title                     varchar(255),
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_doctor_social_work primary key (id))
;

create table feedback (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  name                      varchar(255),
  role                      varchar(16),
  email                     varchar(255),
  date                      timestamp,
  ip_address                varchar(255),
  remarks                   TEXT,
  last_update               timestamp not null,
  constraint ck_feedback_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','PHARMACIST','MEDNETWORK_ADMIN','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_feedback primary key (id))
;

create table file_entity (
  id                        bigint not null,
  created_on                timestamp not null,
  file_name                 varchar(255),
  mime_type                 varchar(255),
  byte_content              bytea,
  last_update               timestamp not null,
  constraint pk_file_entity primary key (id))
;

create table head_quarter (
  id                        bigint not null,
  created_on                timestamp not null,
  state                     varchar(35),
  name                      varchar(255),
  last_update               timestamp not null,
  constraint ck_head_quarter_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','CHANDIGARH','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH')),
  constraint pk_head_quarter primary key (id))
;

create table inventory (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  created_on                timestamp not null,
  product_id                bigint,
  shelf_no                  varchar(255),
  product_quantity          integer,
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint pk_inventory primary key (id))
;

create table master_diagnostic_test (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_master_diagnostic_test primary key (id))
;

create table master_product (
  id                        bigint not null,
  created_on                timestamp not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  description               TEXT,
  units_per_pack            bigint,
  full_name                 varchar(255),
  last_update               timestamp not null,
  constraint pk_master_product primary key (id))
;

create table master_sig_code (
  created_on                timestamp not null,
  code                      varchar(255),
  description               TEXT,
  last_update               timestamp not null)
;

create table master_specialization (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      TEXT,
  remarks                   TEXT,
  last_update               timestamp not null,
  constraint pk_master_specialization primary key (id))
;

create table medical_representative (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  region_alloted            varchar(255),
  company_name              varchar(255),
  designation               varchar(255),
  is_active                 boolean,
  status                    varchar(255),
  manager_id                bigint,
  pharmaceutical_company_id bigint,
  is_doctor_visible         boolean,
  is_sample_visible         boolean,
  is_promotion_visible      boolean,
  is_pob_visible            boolean,
  is_remarks_visible        boolean,
  last_update               timestamp not null,
  constraint pk_medical_representative primary key (id))
;

create table medicine_line_item (
  id                        bigint not null,
  prescription_id           bigint not null,
  created_on                timestamp not null,
  medicine_id               bigint,
  full_name_of_medicine     varchar(255),
  medicine_full_name        varchar(255),
  dosage                    TEXT,
  frequency                 TEXT,
  remarks                   TEXT,
  last_update               timestamp not null,
  constraint pk_medicine_line_item primary key (id))
;

create table order_line_item (
  id                        bigint not null,
  created_on                timestamp not null,
  product_id                bigint,
  quantity                  float,
  batch_no                  varchar(255),
  expiry_date               timestamp,
  price                     float,
  sub_total                 float,
  last_update               timestamp not null,
  constraint pk_order_line_item primary key (id))
;

create table patient (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  age                       varchar(255),
  last_update               timestamp not null,
  constraint pk_patient primary key (id))
;

create table patient_doctor_info (
  id                        bigint not null,
  created_on                timestamp not null,
  doctor_id                 bigint,
  patient_id                bigint,
  last_update               timestamp not null,
  constraint pk_patient_doctor_info primary key (id))
;

create table pharmaceutical_company (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  admin_mr_id               bigint,
  last_update               timestamp not null,
  constraint pk_pharmaceutical_company primary key (id))
;

create table pharmaceutical_product (
  id                        bigint not null,
  created_on                timestamp not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  type_of_medicine          varchar(255),
  description               varchar(255),
  units_per_pack            bigint,
  full_name                 varchar(255),
  pharmaceutical_company_id bigint,
  last_update               timestamp not null,
  constraint pk_pharmaceutical_product primary key (id))
;

create table pharmacist (
  id                        bigint not null,
  created_on                timestamp not null,
  app_user_id               bigint,
  pharmacy_id               bigint,
  category                  varchar(255),
  last_update               timestamp not null,
  constraint pk_pharmacist primary key (id))
;

create table pharmacy (
  id                        bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  address_id                bigint,
  contact_person            varchar(255),
  contact_number            varchar(255),
  description               TEXT,
  admin_pharmacist_id       bigint,
  background_image          bytea,
  search_index              TEXT,
  slug_url                  TEXT,
  last_update               timestamp not null,
  constraint pk_pharmacy primary key (id))
;

create table pharmacy_info (
  id                        bigint not null,
  pharmacy_id               bigint,
  constraint pk_pharmacy_info primary key (id))
;

create table pharmacy_order (
  id                        bigint not null,
  created_on                timestamp not null,
  prescription_id           bigint,
  pharmacy_prescription_status varchar(9),
  ordered_date              timestamp,
  served_date               timestamp,
  total_amount              float,
  last_update               timestamp not null,
  constraint ck_pharmacy_order_pharmacy_prescription_status check (pharmacy_prescription_status in ('RECEIVED','CONFIRMED','SERVED')),
  constraint pk_pharmacy_order primary key (id))
;

create table pharmacy_prescription_info (
  id                        bigint not null,
  created_on                timestamp not null,
  pharmacy_id               bigint,
  prescription_id           bigint,
  pharmacy_prescription_status varchar(9),
  shared_by_id              bigint,
  shared_date               timestamp,
  served_date               timestamp,
  patients_consent          boolean,
  last_update               timestamp not null,
  constraint ck_pharmacy_prescription_info_pharmacy_prescription_status check (pharmacy_prescription_status in ('RECEIVED','CONFIRMED','SERVED')),
  constraint pk_pharmacy_prescription_info primary key (id))
;

create table pharmacy_product (
  id                        bigint not null,
  created_on                timestamp not null,
  medicine_name             varchar(255),
  brand_name                varchar(255),
  salt                      varchar(255),
  strength                  varchar(255),
  type_of_medicine          varchar(255),
  description               TEXT,
  units_per_pack            bigint,
  full_name                 varchar(255),
  pharmacy_id               bigint,
  last_update               timestamp not null,
  constraint pk_pharmacy_product primary key (id))
;

create table prescription (
  id                        bigint not null,
  created_on                timestamp not null,
  doctor_id                 bigint,
  clinic_id                 bigint,
  patient_id                bigint,
  appointment_id            bigint,
  prescription_date         timestamp,
  problem_statement         TEXT,
  prognosis                 TEXT,
  remarks                   TEXT,
  last_update               timestamp not null,
  constraint pk_prescription primary key (id))
;

create table question_and_answer (
  id                        bigint not null,
  created_on                timestamp not null,
  question                  TEXT,
  answer                    TEXT,
  question_date             timestamp,
  answer_date               timestamp,
  question_by_id            bigint,
  answer_by_id              bigint,
  last_update               timestamp not null,
  constraint pk_question_and_answer primary key (id))
;

create table sample (
  id                        bigint not null,
  tpline_item_id            bigint not null,
  created_on                timestamp not null,
  dcr_line_item_id          bigint,
  pharmaceutical_product_id bigint,
  quantity                  integer,
  last_update               timestamp not null,
  constraint pk_sample primary key (id))
;

create table show_cased_product (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  description               TEXT,
  mrp                       float,
  last_update               timestamp not null,
  constraint pk_show_cased_product primary key (id))
;

create table show_cased_service (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  created_on                timestamp not null,
  name                      varchar(255),
  description               TEXT,
  cost                      float,
  last_update               timestamp not null,
  constraint pk_show_cased_service primary key (id))
;

create table sig_code (
  doctor_id                 bigint not null,
  created_on                timestamp not null,
  code                      varchar(255),
  description               TEXT,
  last_update               timestamp not null)
;

create table social_user (
  id                        bigint not null,
  created_on                timestamp not null,
  image                     bytea,
  name                      varchar(255),
  username                  varchar(255),
  email                     varchar(255),
  sex                       varchar(6),
  dob                       timestamp,
  first_log_in_via          varchar(8),
  last_update               timestamp not null,
  constraint ck_social_user_sex check (sex in ('FEMALE','OTHER','MALE')),
  constraint ck_social_user_first_log_in_via check (first_log_in_via in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_social_user primary key (id))
;

create table tpline_item (
  id                        bigint not null,
  tour_plan_id              bigint not null,
  created_on                timestamp not null,
  date                      timestamp,
  day_status                varchar(11),
  pob                       integer,
  remarks                   varchar(255),
  is_addedto_tourplan       boolean,
  last_update               timestamp not null,
  constraint ck_tpline_item_day_status check (day_status in ('HOLIDAY','WORKING_DAY')),
  constraint pk_tpline_item primary key (id))
;

create table tour_plan (
  id                        bigint not null,
  medical_representative_id bigint not null,
  created_on                timestamp not null,
  for_month                 timestamp,
  submit_date               timestamp,
  last_update               timestamp not null,
  constraint pk_tour_plan primary key (id))
;


create table dcrline_item_pharmaceutical_prod (
  dcrline_item_id                bigint not null,
  pharmaceutical_product_id      bigint not null,
  constraint pk_dcrline_item_pharmaceutical_prod primary key (dcrline_item_id, pharmaceutical_product_id))
;

create table diagnostic_centre_file_entity (
  diagnostic_centre_id           bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_diagnostic_centre_file_entity primary key (diagnostic_centre_id, file_entity_id))
;

create table diagnostic_centre_doctor (
  diagnostic_centre_id           bigint not null,
  doctor_id                      bigint not null,
  constraint pk_diagnostic_centre_doctor primary key (diagnostic_centre_id, doctor_id))
;

create table diagnostic_centre_master_diagnos (
  diagnostic_centre_id           bigint not null,
  master_diagnostic_test_id      bigint not null,
  constraint pk_diagnostic_centre_master_diagnos primary key (diagnostic_centre_id, master_diagnostic_test_id))
;

create table diagnostic_centre_prescription (
  diagnostic_centre_id           bigint not null,
  prescription_id                bigint not null,
  constraint pk_diagnostic_centre_prescription primary key (diagnostic_centre_id, prescription_id))
;

create table diagnostic_centre_prescription_i (
  diagnostic_centre_prescription_info_id bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_diagnostic_centre_prescription_i primary key (diagnostic_centre_prescription_info_id, file_entity_id))
;

create table doctor_master_specialization (
  doctor_id                      bigint not null,
  master_specialization_id       bigint not null,
  constraint pk_doctor_master_specialization primary key (doctor_id, master_specialization_id))
;

create table doctor_pharmacy (
  doctor_id                      bigint not null,
  pharmacy_id                    bigint not null,
  constraint pk_doctor_pharmacy primary key (doctor_id, pharmacy_id))
;

create table doctor_diagnostic_centre (
  doctor_id                      bigint not null,
  diagnostic_centre_id           bigint not null,
  constraint pk_doctor_diagnostic_centre primary key (doctor_id, diagnostic_centre_id))
;

create table medical_representative_doctor (
  medical_representative_id      bigint not null,
  doctor_id                      bigint not null,
  constraint pk_medical_representative_doctor primary key (medical_representative_id, doctor_id))
;

create table medical_representative_head_quar (
  medical_representative_id      bigint not null,
  head_quarter_id                bigint not null,
  constraint pk_medical_representative_head_quar primary key (medical_representative_id, head_quarter_id))
;

create table patient_pharmacy (
  patient_id                     bigint not null,
  pharmacy_id                    bigint not null,
  constraint pk_patient_pharmacy primary key (patient_id, pharmacy_id))
;

create table patient_diagnostic_centre (
  patient_id                     bigint not null,
  diagnostic_centre_id           bigint not null,
  constraint pk_patient_diagnostic_centre primary key (patient_id, diagnostic_centre_id))
;

create table patient_file_entity (
  patient_id                     bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_patient_file_entity primary key (patient_id, file_entity_id))
;

create table pharmacy_file_entity (
  pharmacy_id                    bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_pharmacy_file_entity primary key (pharmacy_id, file_entity_id))
;

create table show_cased_service_file_entity (
  show_cased_service_id          bigint not null,
  file_entity_id                 bigint not null,
  constraint pk_show_cased_service_file_entity primary key (show_cased_service_id, file_entity_id))
;

create table tpline_item_doctor (
  tpline_item_id                 bigint not null,
  doctor_id                      bigint not null,
  constraint pk_tpline_item_doctor primary key (tpline_item_id, doctor_id))
;

create table tpline_item_pharmaceutical_produ (
  tpline_item_id                 bigint not null,
  pharmaceutical_product_id      bigint not null,
  constraint pk_tpline_item_pharmaceutical_produ primary key (tpline_item_id, pharmaceutical_product_id))
;
create sequence address_seq;

create sequence app_user_seq;

create sequence appointment_seq;

create sequence article_seq;

create sequence article_category_seq;

create sequence batch_seq;

create sequence blog_comment_seq;

create sequence blog_comment_reply_seq;

create sequence clinic_seq;

create sequence dcrline_item_seq;

create sequence daily_call_report_seq;

create sequence day_schedule_seq;

create sequence diagnostic_centre_seq;

create sequence diagnostic_centre_prescription_info_seq;

create sequence diagnostic_order_seq;

create sequence diagnostic_report_seq;

create sequence diagnostic_representative_seq;

create sequence diagnostic_test_seq;

create sequence diagnostic_test_line_item_seq;

create sequence doctor_seq;

create sequence doctor_assistant_seq;

create sequence doctor_award_seq;

create sequence doctor_clinic_info_seq;

create sequence doctor_diagnostic_test_seq;

create sequence doctor_education_seq;

create sequence doctor_experience_seq;

create sequence doctor_product_seq;

create sequence doctor_publication_seq;

create sequence doctor_social_work_seq;

create sequence feedback_seq;

create sequence file_entity_seq;

create sequence head_quarter_seq;

create sequence inventory_seq;

create sequence master_diagnostic_test_seq;

create sequence master_product_seq;

create sequence master_specialization_seq;

create sequence medical_representative_seq;

create sequence medicine_line_item_seq;

create sequence order_line_item_seq;

create sequence patient_seq;

create sequence patient_doctor_info_seq;

create sequence pharmaceutical_company_seq;

create sequence pharmaceutical_product_seq;

create sequence pharmacist_seq;

create sequence pharmacy_seq;

create sequence pharmacy_info_seq;

create sequence pharmacy_order_seq;

create sequence pharmacy_prescription_info_seq;

create sequence pharmacy_product_seq;

create sequence prescription_seq;

create sequence question_and_answer_seq;

create sequence sample_seq;

create sequence show_cased_product_seq;

create sequence show_cased_service_seq;

create sequence social_user_seq;

create sequence tpline_item_seq;

create sequence tour_plan_seq;

alter table app_user add constraint fk_app_user_address_1 foreign key (address_id) references address (id);
create index ix_app_user_address_1 on app_user (address_id);
alter table appointment add constraint fk_appointment_requestedBy_2 foreign key (requested_by_id) references app_user (id);
create index ix_appointment_requestedBy_2 on appointment (requested_by_id);
alter table appointment add constraint fk_appointment_apporovedBy_3 foreign key (apporoved_by_id) references app_user (id);
create index ix_appointment_apporovedBy_3 on appointment (apporoved_by_id);
alter table appointment add constraint fk_appointment_doctorClinicInf_4 foreign key (doctor_clinic_info_id) references doctor_clinic_info (id);
create index ix_appointment_doctorClinicInf_4 on appointment (doctor_clinic_info_id);
alter table article add constraint fk_article_category_5 foreign key (category_id) references article_category (id);
create index ix_article_category_5 on article (category_id);
alter table batch add constraint fk_batch_inventory_6 foreign key (inventory_id) references inventory (id);
create index ix_batch_inventory_6 on batch (inventory_id);
alter table batch add constraint fk_batch_product_7 foreign key (product_id) references master_product (id);
create index ix_batch_product_7 on batch (product_id);
alter table blog_comment add constraint fk_blog_comment_article_8 foreign key (article_id) references article (id);
create index ix_blog_comment_article_8 on blog_comment (article_id);
alter table blog_comment add constraint fk_blog_comment_by_9 foreign key (by_id) references app_user (id);
create index ix_blog_comment_by_9 on blog_comment (by_id);
alter table blog_comment add constraint fk_blog_comment_socialBy_10 foreign key (social_by_id) references social_user (id);
create index ix_blog_comment_socialBy_10 on blog_comment (social_by_id);
alter table blog_comment_reply add constraint fk_blog_comment_reply_blog_co_11 foreign key (blog_comment_id) references blog_comment (id);
create index ix_blog_comment_reply_blog_co_11 on blog_comment_reply (blog_comment_id);
alter table blog_comment_reply add constraint fk_blog_comment_reply_by_12 foreign key (by_id) references app_user (id);
create index ix_blog_comment_reply_by_12 on blog_comment_reply (by_id);
alter table blog_comment_reply add constraint fk_blog_comment_reply_socialB_13 foreign key (social_by_id) references social_user (id);
create index ix_blog_comment_reply_socialB_13 on blog_comment_reply (social_by_id);
alter table clinic add constraint fk_clinic_address_14 foreign key (address_id) references address (id);
create index ix_clinic_address_14 on clinic (address_id);
alter table dcrline_item add constraint fk_dcrline_item_daily_call_re_15 foreign key (daily_call_report_id) references daily_call_report (id);
create index ix_dcrline_item_daily_call_re_15 on dcrline_item (daily_call_report_id);
alter table dcrline_item add constraint fk_dcrline_item_doctor_16 foreign key (doctor_id) references doctor (id);
create index ix_dcrline_item_doctor_16 on dcrline_item (doctor_id);
alter table dcrline_item add constraint fk_dcrline_item_headQuater_17 foreign key (head_quater_id) references head_quarter (id);
create index ix_dcrline_item_headQuater_17 on dcrline_item (head_quater_id);
alter table daily_call_report add constraint fk_daily_call_report_submitte_18 foreign key (submitter_id) references medical_representative (id);
create index ix_daily_call_report_submitte_18 on daily_call_report (submitter_id);
alter table daily_call_report add constraint fk_daily_call_report_approver_19 foreign key (approver_id) references medical_representative (id);
create index ix_daily_call_report_approver_19 on daily_call_report (approver_id);
alter table daily_call_report add constraint fk_daily_call_report_headQuar_20 foreign key (head_quarter_id) references head_quarter (id);
create index ix_daily_call_report_headQuar_20 on daily_call_report (head_quarter_id);
alter table day_schedule add constraint fk_day_schedule_doctor_clinic_21 foreign key (doctor_clinic_info_id) references doctor_clinic_info (id);
create index ix_day_schedule_doctor_clinic_21 on day_schedule (doctor_clinic_info_id);
alter table diagnostic_centre add constraint fk_diagnostic_centre_address_22 foreign key (address_id) references address (id);
create index ix_diagnostic_centre_address_22 on diagnostic_centre (address_id);
alter table diagnostic_centre add constraint fk_diagnostic_centre_diagnost_23 foreign key (diagnostic_rep_admin_id) references diagnostic_representative (id);
create index ix_diagnostic_centre_diagnost_23 on diagnostic_centre (diagnostic_rep_admin_id);
alter table diagnostic_centre_prescription_info add constraint fk_diagnostic_centre_prescrip_24 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_centre_prescrip_24 on diagnostic_centre_prescription_info (diagnostic_centre_id);
alter table diagnostic_centre_prescription_info add constraint fk_diagnostic_centre_prescrip_25 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_centre_prescrip_25 on diagnostic_centre_prescription_info (prescription_id);
alter table diagnostic_centre_prescription_info add constraint fk_diagnostic_centre_prescrip_26 foreign key (shared_by_id) references app_user (id);
create index ix_diagnostic_centre_prescrip_26 on diagnostic_centre_prescription_info (shared_by_id);
alter table diagnostic_order add constraint fk_diagnostic_order_diagnosti_27 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_order_diagnosti_27 on diagnostic_order (diagnostic_centre_id);
alter table diagnostic_order add constraint fk_diagnostic_order_prescript_28 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_order_prescript_28 on diagnostic_order (prescription_id);
alter table diagnostic_report add constraint fk_diagnostic_report_diagnost_29 foreign key (diagnostic_order_id) references diagnostic_order (id);
create index ix_diagnostic_report_diagnost_29 on diagnostic_report (diagnostic_order_id);
alter table diagnostic_report add constraint fk_diagnostic_report_masterDi_30 foreign key (master_diagnostic_test_id) references master_diagnostic_test (id);
create index ix_diagnostic_report_masterDi_30 on diagnostic_report (master_diagnostic_test_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__31 foreign key (app_user_id) references app_user (id);
create index ix_diagnostic_representative__31 on diagnostic_representative (app_user_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__32 foreign key (patient_id) references patient (id);
create index ix_diagnostic_representative__32 on diagnostic_representative (patient_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__33 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_representative__33 on diagnostic_representative (diagnostic_centre_id);
alter table diagnostic_test add constraint fk_diagnostic_test_diagnostic_34 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_test_diagnostic_34 on diagnostic_test (diagnostic_centre_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__35 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_test_line_item__35 on diagnostic_test_line_item (prescription_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__36 foreign key (master_diagnostic_test_id) references master_diagnostic_test (id);
create index ix_diagnostic_test_line_item__36 on diagnostic_test_line_item (master_diagnostic_test_id);
alter table doctor add constraint fk_doctor_appUser_37 foreign key (app_user_id) references app_user (id);
create index ix_doctor_appUser_37 on doctor (app_user_id);
alter table doctor_assistant add constraint fk_doctor_assistant_appUser_38 foreign key (app_user_id) references app_user (id);
create index ix_doctor_assistant_appUser_38 on doctor_assistant (app_user_id);
alter table doctor_award add constraint fk_doctor_award_doctor_39 foreign key (doctor_id) references doctor (id);
create index ix_doctor_award_doctor_39 on doctor_award (doctor_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_clinic_40 foreign key (clinic_id) references clinic (id);
create index ix_doctor_clinic_info_clinic_40 on doctor_clinic_info (clinic_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_doctor_41 foreign key (doctor_id) references doctor (id);
create index ix_doctor_clinic_info_doctor_41 on doctor_clinic_info (doctor_id);
alter table doctor_diagnostic_test add constraint fk_doctor_diagnostic_test_doc_42 foreign key (doctor_id) references doctor (id);
create index ix_doctor_diagnostic_test_doc_42 on doctor_diagnostic_test (doctor_id);
alter table doctor_education add constraint fk_doctor_education_doctor_43 foreign key (doctor_id) references doctor (id);
create index ix_doctor_education_doctor_43 on doctor_education (doctor_id);
alter table doctor_experience add constraint fk_doctor_experience_doctor_44 foreign key (doctor_id) references doctor (id);
create index ix_doctor_experience_doctor_44 on doctor_experience (doctor_id);
alter table doctor_product add constraint fk_doctor_product_doctor_45 foreign key (doctor_id) references doctor (id);
create index ix_doctor_product_doctor_45 on doctor_product (doctor_id);
alter table doctor_publication add constraint fk_doctor_publication_doctor_46 foreign key (doctor_id) references doctor (id);
create index ix_doctor_publication_doctor_46 on doctor_publication (doctor_id);
alter table doctor_social_work add constraint fk_doctor_social_work_doctor_47 foreign key (doctor_id) references doctor (id);
create index ix_doctor_social_work_doctor_47 on doctor_social_work (doctor_id);
alter table feedback add constraint fk_feedback_appUser_48 foreign key (app_user_id) references app_user (id);
create index ix_feedback_appUser_48 on feedback (app_user_id);
alter table inventory add constraint fk_inventory_pharmacy_49 foreign key (pharmacy_id) references pharmacy (id);
create index ix_inventory_pharmacy_49 on inventory (pharmacy_id);
alter table inventory add constraint fk_inventory_product_50 foreign key (product_id) references master_product (id);
create index ix_inventory_product_50 on inventory (product_id);
alter table medical_representative add constraint fk_medical_representative_app_51 foreign key (app_user_id) references app_user (id);
create index ix_medical_representative_app_51 on medical_representative (app_user_id);
alter table medical_representative add constraint fk_medical_representative_man_52 foreign key (manager_id) references medical_representative (id);
create index ix_medical_representative_man_52 on medical_representative (manager_id);
alter table medical_representative add constraint fk_medical_representative_pha_53 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_medical_representative_pha_53 on medical_representative (pharmaceutical_company_id);
alter table medicine_line_item add constraint fk_medicine_line_item_prescri_54 foreign key (prescription_id) references prescription (id);
create index ix_medicine_line_item_prescri_54 on medicine_line_item (prescription_id);
alter table medicine_line_item add constraint fk_medicine_line_item_medicin_55 foreign key (medicine_id) references master_product (id);
create index ix_medicine_line_item_medicin_55 on medicine_line_item (medicine_id);
alter table order_line_item add constraint fk_order_line_item_product_56 foreign key (product_id) references master_product (id);
create index ix_order_line_item_product_56 on order_line_item (product_id);
alter table patient add constraint fk_patient_appUser_57 foreign key (app_user_id) references app_user (id);
create index ix_patient_appUser_57 on patient (app_user_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_doctor_58 foreign key (doctor_id) references doctor (id);
create index ix_patient_doctor_info_doctor_58 on patient_doctor_info (doctor_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_patien_59 foreign key (patient_id) references patient (id);
create index ix_patient_doctor_info_patien_59 on patient_doctor_info (patient_id);
alter table pharmaceutical_company add constraint fk_pharmaceutical_company_adm_60 foreign key (admin_mr_id) references medical_representative (id);
create index ix_pharmaceutical_company_adm_60 on pharmaceutical_company (admin_mr_id);
alter table pharmaceutical_product add constraint fk_pharmaceutical_product_pha_61 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_pharmaceutical_product_pha_61 on pharmaceutical_product (pharmaceutical_company_id);
alter table pharmacist add constraint fk_pharmacist_appUser_62 foreign key (app_user_id) references app_user (id);
create index ix_pharmacist_appUser_62 on pharmacist (app_user_id);
alter table pharmacist add constraint fk_pharmacist_pharmacy_63 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacist_pharmacy_63 on pharmacist (pharmacy_id);
alter table pharmacy add constraint fk_pharmacy_address_64 foreign key (address_id) references address (id);
create index ix_pharmacy_address_64 on pharmacy (address_id);
alter table pharmacy add constraint fk_pharmacy_adminPharmacist_65 foreign key (admin_pharmacist_id) references pharmacist (id);
create index ix_pharmacy_adminPharmacist_65 on pharmacy (admin_pharmacist_id);
alter table pharmacy_info add constraint fk_pharmacy_info_pharmacy_66 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_info_pharmacy_66 on pharmacy_info (pharmacy_id);
alter table pharmacy_order add constraint fk_pharmacy_order_prescriptio_67 foreign key (prescription_id) references prescription (id);
create index ix_pharmacy_order_prescriptio_67 on pharmacy_order (prescription_id);
alter table pharmacy_prescription_info add constraint fk_pharmacy_prescription_info_68 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_prescription_info_68 on pharmacy_prescription_info (pharmacy_id);
alter table pharmacy_prescription_info add constraint fk_pharmacy_prescription_info_69 foreign key (prescription_id) references prescription (id);
create index ix_pharmacy_prescription_info_69 on pharmacy_prescription_info (prescription_id);
alter table pharmacy_prescription_info add constraint fk_pharmacy_prescription_info_70 foreign key (shared_by_id) references app_user (id);
create index ix_pharmacy_prescription_info_70 on pharmacy_prescription_info (shared_by_id);
alter table pharmacy_product add constraint fk_pharmacy_product_pharmacy_71 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_product_pharmacy_71 on pharmacy_product (pharmacy_id);
alter table prescription add constraint fk_prescription_doctor_72 foreign key (doctor_id) references doctor (id);
create index ix_prescription_doctor_72 on prescription (doctor_id);
alter table prescription add constraint fk_prescription_clinic_73 foreign key (clinic_id) references clinic (id);
create index ix_prescription_clinic_73 on prescription (clinic_id);
alter table prescription add constraint fk_prescription_patient_74 foreign key (patient_id) references patient (id);
create index ix_prescription_patient_74 on prescription (patient_id);
alter table prescription add constraint fk_prescription_appointment_75 foreign key (appointment_id) references appointment (id);
create index ix_prescription_appointment_75 on prescription (appointment_id);
alter table question_and_answer add constraint fk_question_and_answer_questi_76 foreign key (question_by_id) references app_user (id);
create index ix_question_and_answer_questi_76 on question_and_answer (question_by_id);
alter table question_and_answer add constraint fk_question_and_answer_answer_77 foreign key (answer_by_id) references app_user (id);
create index ix_question_and_answer_answer_77 on question_and_answer (answer_by_id);
alter table sample add constraint fk_sample_tpline_item_78 foreign key (tpline_item_id) references tpline_item (id);
create index ix_sample_tpline_item_78 on sample (tpline_item_id);
alter table sample add constraint fk_sample_dcrLineItem_79 foreign key (dcr_line_item_id) references dcrline_item (id);
create index ix_sample_dcrLineItem_79 on sample (dcr_line_item_id);
alter table sample add constraint fk_sample_pharmaceuticalProdu_80 foreign key (pharmaceutical_product_id) references pharmaceutical_product (id);
create index ix_sample_pharmaceuticalProdu_80 on sample (pharmaceutical_product_id);
alter table show_cased_product add constraint fk_show_cased_product_pharmac_81 foreign key (pharmacy_id) references pharmacy (id);
create index ix_show_cased_product_pharmac_81 on show_cased_product (pharmacy_id);
alter table show_cased_service add constraint fk_show_cased_service_diagnos_82 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_show_cased_service_diagnos_82 on show_cased_service (diagnostic_centre_id);
alter table sig_code add constraint fk_sig_code_doctor_83 foreign key (doctor_id) references doctor (id);
create index ix_sig_code_doctor_83 on sig_code (doctor_id);
alter table tpline_item add constraint fk_tpline_item_tour_plan_84 foreign key (tour_plan_id) references tour_plan (id);
create index ix_tpline_item_tour_plan_84 on tpline_item (tour_plan_id);
alter table tour_plan add constraint fk_tour_plan_medical_represen_85 foreign key (medical_representative_id) references medical_representative (id);
create index ix_tour_plan_medical_represen_85 on tour_plan (medical_representative_id);



alter table dcrline_item_pharmaceutical_prod add constraint fk_dcrline_item_pharmaceutica_01 foreign key (dcrline_item_id) references dcrline_item (id);

alter table dcrline_item_pharmaceutical_prod add constraint fk_dcrline_item_pharmaceutica_02 foreign key (pharmaceutical_product_id) references pharmaceutical_product (id);

alter table diagnostic_centre_file_entity add constraint fk_diagnostic_centre_file_ent_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_file_entity add constraint fk_diagnostic_centre_file_ent_02 foreign key (file_entity_id) references file_entity (id);

alter table diagnostic_centre_doctor add constraint fk_diagnostic_centre_doctor_d_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_doctor add constraint fk_diagnostic_centre_doctor_d_02 foreign key (doctor_id) references doctor (id);

alter table diagnostic_centre_master_diagnos add constraint fk_diagnostic_centre_master_d_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_master_diagnos add constraint fk_diagnostic_centre_master_d_02 foreign key (master_diagnostic_test_id) references master_diagnostic_test (id);

alter table diagnostic_centre_prescription add constraint fk_diagnostic_centre_prescrip_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_prescription add constraint fk_diagnostic_centre_prescrip_02 foreign key (prescription_id) references prescription (id);

alter table diagnostic_centre_prescription_i add constraint fk_diagnostic_centre_prescrip_01 foreign key (diagnostic_centre_prescription_info_id) references diagnostic_centre_prescription_info (id);

alter table diagnostic_centre_prescription_i add constraint fk_diagnostic_centre_prescrip_02 foreign key (file_entity_id) references file_entity (id);

alter table doctor_master_specialization add constraint fk_doctor_master_specializati_01 foreign key (doctor_id) references doctor (id);

alter table doctor_master_specialization add constraint fk_doctor_master_specializati_02 foreign key (master_specialization_id) references master_specialization (id);

alter table doctor_pharmacy add constraint fk_doctor_pharmacy_doctor_01 foreign key (doctor_id) references doctor (id);

alter table doctor_pharmacy add constraint fk_doctor_pharmacy_pharmacy_02 foreign key (pharmacy_id) references pharmacy (id);

alter table doctor_diagnostic_centre add constraint fk_doctor_diagnostic_centre_d_01 foreign key (doctor_id) references doctor (id);

alter table doctor_diagnostic_centre add constraint fk_doctor_diagnostic_centre_d_02 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_02 foreign key (doctor_id) references doctor (id);

alter table medical_representative_head_quar add constraint fk_medical_representative_hea_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_head_quar add constraint fk_medical_representative_hea_02 foreign key (head_quarter_id) references head_quarter (id);

alter table patient_pharmacy add constraint fk_patient_pharmacy_patient_01 foreign key (patient_id) references patient (id);

alter table patient_pharmacy add constraint fk_patient_pharmacy_pharmacy_02 foreign key (pharmacy_id) references pharmacy (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__01 foreign key (patient_id) references patient (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__02 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table patient_file_entity add constraint fk_patient_file_entity_patien_01 foreign key (patient_id) references patient (id);

alter table patient_file_entity add constraint fk_patient_file_entity_file_e_02 foreign key (file_entity_id) references file_entity (id);

alter table pharmacy_file_entity add constraint fk_pharmacy_file_entity_pharm_01 foreign key (pharmacy_id) references pharmacy (id);

alter table pharmacy_file_entity add constraint fk_pharmacy_file_entity_file__02 foreign key (file_entity_id) references file_entity (id);

alter table show_cased_service_file_entity add constraint fk_show_cased_service_file_en_01 foreign key (show_cased_service_id) references show_cased_service (id);

alter table show_cased_service_file_entity add constraint fk_show_cased_service_file_en_02 foreign key (file_entity_id) references file_entity (id);

alter table tpline_item_doctor add constraint fk_tpline_item_doctor_tpline__01 foreign key (tpline_item_id) references tpline_item (id);

alter table tpline_item_doctor add constraint fk_tpline_item_doctor_doctor_02 foreign key (doctor_id) references doctor (id);

alter table tpline_item_pharmaceutical_produ add constraint fk_tpline_item_pharmaceutical_01 foreign key (tpline_item_id) references tpline_item (id);

alter table tpline_item_pharmaceutical_produ add constraint fk_tpline_item_pharmaceutical_02 foreign key (pharmaceutical_product_id) references pharmaceutical_product (id);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

# --- !Downs

<<<<<<< HEAD
drop table if exists address cascade;

drop table if exists app_user cascade;

drop table if exists appointment cascade;

drop table if exists article cascade;

drop table if exists article_category cascade;

drop table if exists batch cascade;

drop table if exists blog_comment cascade;

drop table if exists blog_comment_reply cascade;

drop table if exists clinic cascade;

drop table if exists dcrline_item cascade;

drop table if exists dcrline_item_pharmaceutical_prod cascade;

drop table if exists daily_call_report cascade;

drop table if exists day_schedule cascade;

drop table if exists designation cascade;

drop table if exists diagnostic_centre cascade;

drop table if exists diagnostic_centre_file_entity cascade;

drop table if exists diagnostic_centre_doctor cascade;

drop table if exists diagnostic_centre_master_diagnos cascade;

drop table if exists diagnostic_centre_prescription cascade;

drop table if exists diagnostic_centre_prescription_info cascade;

drop table if exists diagnostic_centre_prescription_i cascade;

drop table if exists diagnostic_order cascade;

drop table if exists diagnostic_report cascade;

drop table if exists diagnostic_representative cascade;

drop table if exists diagnostic_test cascade;

drop table if exists diagnostic_test_line_item cascade;

drop table if exists doctor cascade;

drop table if exists doctor_master_specialization cascade;

drop table if exists doctor_pharmacy cascade;

drop table if exists doctor_diagnostic_centre cascade;

drop table if exists doctor_assistant cascade;

drop table if exists doctor_award cascade;

drop table if exists doctor_clinic_info cascade;

drop table if exists doctor_diagnostic_test cascade;

drop table if exists doctor_education cascade;

drop table if exists doctor_experience cascade;

drop table if exists doctor_product cascade;

drop table if exists doctor_publication cascade;

drop table if exists doctor_social_work cascade;

drop table if exists feedback cascade;

drop table if exists file_entity cascade;

drop table if exists head_quarter cascade;

drop table if exists inventory cascade;

drop table if exists master_diagnostic_test cascade;

drop table if exists master_product cascade;

drop table if exists master_sig_code cascade;

drop table if exists master_specialization cascade;

drop table if exists medical_representative cascade;

drop table if exists medical_representative_doctor cascade;

drop table if exists medical_representative_head_quar cascade;

drop table if exists medicine_line_item cascade;

drop table if exists order_line_item cascade;

drop table if exists patient cascade;

drop table if exists patient_pharmacy cascade;

drop table if exists patient_diagnostic_centre cascade;

drop table if exists patient_file_entity cascade;

drop table if exists patient_doctor_info cascade;

drop table if exists pharmaceutical_company cascade;

drop table if exists pharmaceutical_product cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop table if exists pharmacy_file_entity cascade;

drop table if exists pharmacy_info cascade;

drop table if exists pharmacy_order cascade;

drop table if exists pharmacy_prescription_info cascade;

drop table if exists pharmacy_product cascade;

drop table if exists prescription cascade;

drop table if exists question_and_answer cascade;

drop table if exists sample cascade;

drop table if exists show_cased_product cascade;

drop table if exists show_cased_service cascade;

drop table if exists show_cased_service_file_entity cascade;

drop table if exists sig_code cascade;

drop table if exists social_user cascade;

drop table if exists tpline_item cascade;

drop table if exists tpline_item_doctor cascade;

drop table if exists tpline_item_pharmaceutical_produ cascade;

drop table if exists tour_plan cascade;

drop sequence if exists address_seq;

drop sequence if exists app_user_seq;

drop sequence if exists appointment_seq;

drop sequence if exists article_seq;

drop sequence if exists article_category_seq;

drop sequence if exists batch_seq;

drop sequence if exists blog_comment_seq;

drop sequence if exists blog_comment_reply_seq;

drop sequence if exists clinic_seq;

drop sequence if exists dcrline_item_seq;

drop sequence if exists daily_call_report_seq;

drop sequence if exists day_schedule_seq;

drop sequence if exists designation_seq;

drop sequence if exists diagnostic_centre_seq;

drop sequence if exists diagnostic_centre_prescription_info_seq;

drop sequence if exists diagnostic_order_seq;

drop sequence if exists diagnostic_report_seq;

drop sequence if exists diagnostic_representative_seq;

drop sequence if exists diagnostic_test_seq;

drop sequence if exists diagnostic_test_line_item_seq;

drop sequence if exists doctor_seq;

drop sequence if exists doctor_assistant_seq;

drop sequence if exists doctor_award_seq;

drop sequence if exists doctor_clinic_info_seq;

drop sequence if exists doctor_diagnostic_test_seq;

drop sequence if exists doctor_education_seq;

drop sequence if exists doctor_experience_seq;

drop sequence if exists doctor_product_seq;

drop sequence if exists doctor_publication_seq;

drop sequence if exists doctor_social_work_seq;

drop sequence if exists feedback_seq;

drop sequence if exists file_entity_seq;

drop sequence if exists head_quarter_seq;

drop sequence if exists inventory_seq;

drop sequence if exists master_diagnostic_test_seq;

drop sequence if exists master_product_seq;

drop sequence if exists master_specialization_seq;

drop sequence if exists medical_representative_seq;

drop sequence if exists medicine_line_item_seq;

drop sequence if exists order_line_item_seq;

drop sequence if exists patient_seq;

drop sequence if exists patient_doctor_info_seq;

drop sequence if exists pharmaceutical_company_seq;

drop sequence if exists pharmaceutical_product_seq;

drop sequence if exists pharmacist_seq;

drop sequence if exists pharmacy_seq;

drop sequence if exists pharmacy_info_seq;

drop sequence if exists pharmacy_order_seq;

drop sequence if exists pharmacy_prescription_info_seq;

drop sequence if exists pharmacy_product_seq;

drop sequence if exists prescription_seq;

drop sequence if exists question_and_answer_seq;

drop sequence if exists sample_seq;

drop sequence if exists show_cased_product_seq;

drop sequence if exists show_cased_service_seq;

drop sequence if exists social_user_seq;

drop sequence if exists tpline_item_seq;

drop sequence if exists tour_plan_seq;

=======
drop table if exists address cascade;

drop table if exists app_user cascade;

drop table if exists appointment cascade;

drop table if exists article cascade;

drop table if exists article_category cascade;

drop table if exists batch cascade;

drop table if exists blog_comment cascade;

drop table if exists blog_comment_reply cascade;

drop table if exists clinic cascade;

drop table if exists dcrline_item cascade;

drop table if exists dcrline_item_pharmaceutical_prod cascade;

drop table if exists daily_call_report cascade;

drop table if exists day_schedule cascade;

drop table if exists diagnostic_centre cascade;

drop table if exists diagnostic_centre_file_entity cascade;

drop table if exists diagnostic_centre_doctor cascade;

drop table if exists diagnostic_centre_master_diagnos cascade;

drop table if exists diagnostic_centre_prescription cascade;

drop table if exists diagnostic_centre_prescription_info cascade;

drop table if exists diagnostic_centre_prescription_i cascade;

drop table if exists diagnostic_order cascade;

drop table if exists diagnostic_report cascade;

drop table if exists diagnostic_representative cascade;

drop table if exists diagnostic_test cascade;

drop table if exists diagnostic_test_line_item cascade;

drop table if exists doctor cascade;

drop table if exists doctor_master_specialization cascade;

drop table if exists doctor_pharmacy cascade;

drop table if exists doctor_diagnostic_centre cascade;

drop table if exists doctor_assistant cascade;

drop table if exists doctor_award cascade;

drop table if exists doctor_clinic_info cascade;

drop table if exists doctor_diagnostic_test cascade;

drop table if exists doctor_education cascade;

drop table if exists doctor_experience cascade;

drop table if exists doctor_product cascade;

drop table if exists doctor_publication cascade;

drop table if exists doctor_social_work cascade;

drop table if exists feedback cascade;

drop table if exists file_entity cascade;

drop table if exists head_quarter cascade;

drop table if exists inventory cascade;

drop table if exists master_diagnostic_test cascade;

drop table if exists master_product cascade;

drop table if exists master_sig_code cascade;

drop table if exists master_specialization cascade;

drop table if exists medical_representative cascade;

drop table if exists medical_representative_doctor cascade;

drop table if exists medical_representative_head_quar cascade;

drop table if exists medicine_line_item cascade;

drop table if exists order_line_item cascade;

drop table if exists patient cascade;

drop table if exists patient_pharmacy cascade;

drop table if exists patient_diagnostic_centre cascade;

drop table if exists patient_file_entity cascade;

drop table if exists patient_doctor_info cascade;

drop table if exists pharmaceutical_company cascade;

drop table if exists pharmaceutical_product cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop table if exists pharmacy_file_entity cascade;

drop table if exists pharmacy_info cascade;

drop table if exists pharmacy_order cascade;

drop table if exists pharmacy_prescription_info cascade;

drop table if exists pharmacy_product cascade;

drop table if exists prescription cascade;

drop table if exists question_and_answer cascade;

drop table if exists sample cascade;

drop table if exists show_cased_product cascade;

drop table if exists show_cased_service cascade;

drop table if exists show_cased_service_file_entity cascade;

drop table if exists sig_code cascade;

drop table if exists social_user cascade;

drop table if exists tpline_item cascade;

drop table if exists tpline_item_doctor cascade;

drop table if exists tpline_item_pharmaceutical_produ cascade;

drop table if exists tour_plan cascade;

drop sequence if exists address_seq;

drop sequence if exists app_user_seq;

drop sequence if exists appointment_seq;

drop sequence if exists article_seq;

drop sequence if exists article_category_seq;

drop sequence if exists batch_seq;

drop sequence if exists blog_comment_seq;

drop sequence if exists blog_comment_reply_seq;

drop sequence if exists clinic_seq;

drop sequence if exists dcrline_item_seq;

drop sequence if exists daily_call_report_seq;

drop sequence if exists day_schedule_seq;

drop sequence if exists diagnostic_centre_seq;

drop sequence if exists diagnostic_centre_prescription_info_seq;

drop sequence if exists diagnostic_order_seq;

drop sequence if exists diagnostic_report_seq;

drop sequence if exists diagnostic_representative_seq;

drop sequence if exists diagnostic_test_seq;

drop sequence if exists diagnostic_test_line_item_seq;

drop sequence if exists doctor_seq;

drop sequence if exists doctor_assistant_seq;

drop sequence if exists doctor_award_seq;

drop sequence if exists doctor_clinic_info_seq;

drop sequence if exists doctor_diagnostic_test_seq;

drop sequence if exists doctor_education_seq;

drop sequence if exists doctor_experience_seq;

drop sequence if exists doctor_product_seq;

drop sequence if exists doctor_publication_seq;

drop sequence if exists doctor_social_work_seq;

drop sequence if exists feedback_seq;

drop sequence if exists file_entity_seq;

drop sequence if exists head_quarter_seq;

drop sequence if exists inventory_seq;

drop sequence if exists master_diagnostic_test_seq;

drop sequence if exists master_product_seq;

drop sequence if exists master_specialization_seq;

drop sequence if exists medical_representative_seq;

drop sequence if exists medicine_line_item_seq;

drop sequence if exists order_line_item_seq;

drop sequence if exists patient_seq;

drop sequence if exists patient_doctor_info_seq;

drop sequence if exists pharmaceutical_company_seq;

drop sequence if exists pharmaceutical_product_seq;

drop sequence if exists pharmacist_seq;

drop sequence if exists pharmacy_seq;

drop sequence if exists pharmacy_info_seq;

drop sequence if exists pharmacy_order_seq;

drop sequence if exists pharmacy_prescription_info_seq;

drop sequence if exists pharmacy_product_seq;

drop sequence if exists prescription_seq;

drop sequence if exists question_and_answer_seq;

drop sequence if exists sample_seq;

drop sequence if exists show_cased_product_seq;

drop sequence if exists show_cased_service_seq;

drop sequence if exists social_user_seq;

drop sequence if exists tpline_item_seq;

drop sequence if exists tour_plan_seq;

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
