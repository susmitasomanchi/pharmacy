# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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
  last_update               timestamp not null,
  constraint ck_app_user_sex check (sex in ('FEMALE','OTHER','MALE')),
  constraint ck_app_user_role check (role in ('PATIENT','ADMIN_DIAGREP','DOCTOR','ADMIN','PHARMACIST','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_app_user primary key (id))
;

create table appointment (
  id                        bigint not null,
  appointment_time          timestamp,
  appointment_status        varchar(9),
  requested_by_id           bigint,
  apporoved_by_id           bigint,
  remarks                   TEXT,
  doctor_clinic_info_id     bigint,
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
  last_update               timestamp not null,
  constraint pk_article_category primary key (id))
;

create table batch (
  id                        bigint not null,
  inventory_id              bigint not null,
  pharmacy_product_id       bigint,
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
  name                      varchar(255),
  contact_person_name       varchar(255),
  contact_no                varchar(255),
  address_id                bigint,
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
  last_update               timestamp not null,
  constraint ck_day_schedule_day check (day in ('MONDAY','SUNDAY','WEDNESDAY','THURSDAY','SATURDAY','TUESDAY','FRIDAY')),
  constraint ck_day_schedule_requester check (requester in ('PATIENT','ADMIN_DIAGREP','DOCTOR','ADMIN','PHARMACIST','ADMIN_PHARMACIST','BLOG_ADMIN','ADMIN_MR','MR','DIAGREP','DOCTOR_SECRETARY')),
  constraint pk_day_schedule primary key (id))
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
  last_update               timestamp not null,
  constraint pk_diagnostic_centre primary key (id))
;

create table diagnostic_order (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  patient_id                bigint,
  diagnostic_order_status   varchar(9),
  received_date             timestamp,
  confirmed_date            timestamp,
  last_update               timestamp not null,
  constraint ck_diagnostic_order_diagnostic_order_status check (diagnostic_order_status in ('RECEIVED','CONFIRMED')),
  constraint pk_diagnostic_order primary key (id))
;

create table diagnostic_report (
  id                        bigint not null,
  diagnostic_order_id       bigint not null,
  file_name                 varchar(255),
  file_content              bytea,
  diagnostic_test_id        bigint,
  report_status             varchar(20),
  sample_collection_date    timestamp,
  report_generation_date    timestamp,
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
  last_update               timestamp not null,
  constraint pk_diagnostic_representative primary key (id))
;

create table diagnostic_test (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  name                      varchar(255),
  description               varchar(255),
  price                     float,
  last_update               timestamp not null,
  constraint pk_diagnostic_test primary key (id))
;

create table diagnostic_test_line_item (
  id                        bigint not null,
  prescription_id           bigint not null,
  diagnostic_test_id        bigint,
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint pk_diagnostic_test_line_item primary key (id))
;

create table doctor (
  id                        bigint not null,
  app_user_id               bigint,
  registration_number       varchar(255),
  specialization            varchar(255),
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
  app_user_id               bigint,
  degree                    varchar(255),
  experience                varchar(255),
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
  last_update               timestamp not null,
  constraint pk_doctor_clinic_info primary key (id))
;

create table doctor_education (
  id                        bigint not null,
  doctor_id                 bigint not null,
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
  institution_name          varchar(255),
  position                  varchar(255),
  description               TEXT,
  worked_from               integer,
  worked_to                 integer,
  last_update               timestamp not null,
  constraint pk_doctor_experience primary key (id))
;

create table doctor_language (
  id                        bigint not null,
  last_update               timestamp not null,
  constraint pk_doctor_language primary key (id))
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
  last_update               timestamp not null,
  constraint pk_doctor_publication primary key (id))
;

create table doctor_social_work (
  id                        bigint not null,
  doctor_id                 bigint not null,
  title                     varchar(255),
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_doctor_social_work primary key (id))
;

create table doctors_prescription (
  doctors_prescription_id   bigint not null,
  last_update               timestamp not null,
  constraint pk_doctors_prescription primary key (doctors_prescription_id))
;

create table file_entity (
  id                        bigint not null,
  file_name                 varchar(255),
  mime_type                 varchar(255),
  byte_content              bytea,
  last_update               timestamp not null,
  constraint pk_file_entity primary key (id))
;

create table head_quarter (
  id                        bigint not null,
  state                     varchar(35),
  name                      varchar(255),
  last_update               timestamp not null,
  constraint ck_head_quarter_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','CHANDIGARH','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH')),
  constraint pk_head_quarter primary key (id))
;

create table inventory (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  pharmacy_product_id       bigint,
  shelf_no                  varchar(255),
  product_quantity          integer,
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint pk_inventory primary key (id))
;

create table language_app_user (
  id                        bigint not null,
  doctor_language_id        bigint not null,
  language                  varchar(9),
  last_update               timestamp not null,
  constraint ck_language_app_user_language check (language in ('MALAYALAM','KONKANI','SINDHI','NEPALI','MANIPURI','ENGLISH','ASSAMESE','ORIYA','TELGU','BODO','GUJARATI','TAMIL','MARATHI','BENGALI','URDU','SANTALI','HINDI','PUNJABI','KASHMIRI','MAITHILI','DOGRI','KANNADA')),
  constraint pk_language_app_user primary key (id))
;

create table medical_representative (
  id                        bigint not null,
  app_user_id               bigint,
  region_alloted            varchar(255),
  company_name              varchar(255),
  designation               varchar(255),
  is_active                 boolean,
  status                    varchar(255),
  manager_id                bigint,
  pharmaceutical_company_id bigint,
  last_update               timestamp not null,
  constraint pk_medical_representative primary key (id))
;

create table medicine_line_item (
  id                        bigint not null,
  prescription_id           bigint not null,
  frequency                 varchar(255),
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint pk_medicine_line_item primary key (id))
;

create table order_line_item (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  pharmacy_product_id       bigint,
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
  app_user_id               bigint,
  last_update               timestamp not null,
  constraint pk_patient primary key (id))
;

create table patient_doctor_info (
  id                        bigint not null,
  doctor_id                 bigint,
  patient_id                bigint,
  last_update               timestamp not null,
  constraint pk_patient_doctor_info primary key (id))
;

create table pharmaceutical_company (
  id                        bigint not null,
  name                      varchar(255),
  admin_mr_id               bigint,
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
  last_update               timestamp not null,
  constraint pk_pharmaceutical_product primary key (id))
;

create table pharmacist (
  id                        bigint not null,
  app_user_id               bigint,
  pharmacy_id               bigint,
  category                  varchar(255),
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
  order_status              varchar(9),
  date                      timestamp,
  total_amount              float,
  last_update               timestamp not null,
  constraint ck_pharmacy_order_order_status check (order_status in ('DRAFT','DELIVERED','READY')),
  constraint pk_pharmacy_order primary key (id))
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
  last_update               timestamp not null,
  constraint pk_pharmacy_product primary key (id))
;

create table prescription (
  id                        bigint not null,
  pharmacy_id               bigint not null,
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

create table product (
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
  pharmacy_id               bigint,
  last_update               timestamp not null,
  constraint pk_product primary key (id))
;

create table question_and_answer (
  id                        bigint not null,
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
  dcrline_item_id           bigint not null,
  product_id                bigint,
  quantity                  integer,
  last_update               timestamp not null,
  constraint pk_sample primary key (id))
;

create table show_cased_product (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  name                      varchar(255),
  description               TEXT,
  mrp                       float,
  last_update               timestamp not null,
  constraint pk_show_cased_product primary key (id))
;

create table show_cased_service (
  id                        bigint not null,
  diagnostic_centre_id      bigint not null,
  name                      varchar(255),
  description               TEXT,
  cost                      float,
  last_update               timestamp not null,
  constraint pk_show_cased_service primary key (id))
;

create table sig_code (
  code                      varchar(255),
  description               varchar(255),
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
  last_update               timestamp not null,
  constraint ck_social_user_sex check (sex in ('FEMALE','OTHER','MALE')),
  constraint ck_social_user_first_log_in_via check (first_log_in_via in ('GOOGLE','FACEBOOK','APP_USER')),
  constraint pk_social_user primary key (id))
;

create table tpline_item (
  id                        bigint not null,
  tour_plan_id              bigint not null,
  month                     timestamp,
  last_update               timestamp not null,
  constraint pk_tpline_item primary key (id))
;

create table tour_plan (
  id                        bigint not null,
  medical_representative_id bigint not null,
  for_month                 timestamp,
  status                    varchar(9),
  submit_date               timestamp,
  last_update               timestamp not null,
  constraint ck_tour_plan_status check (status in ('REJECTED','DRAFT','APPROVED','REOPENED','SUBMITTED')),
  constraint pk_tour_plan primary key (id))
;


create table dcrline_item_product (
  dcrline_item_id                bigint not null,
  product_id                     bigint not null,
  constraint pk_dcrline_item_product primary key (dcrline_item_id, product_id))
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

create table diagnostic_centre_prescription (
  diagnostic_centre_id           bigint not null,
  prescription_id                bigint not null,
  constraint pk_diagnostic_centre_prescription primary key (diagnostic_centre_id, prescription_id))
;

create table doctor_doctor_language (
  doctor_id                      bigint not null,
  doctor_language_id             bigint not null,
  constraint pk_doctor_doctor_language primary key (doctor_id, doctor_language_id))
;

create table doctor_pharmacy (
  doctor_id                      bigint not null,
  pharmacy_id                    bigint not null,
  constraint pk_doctor_pharmacy primary key (doctor_id, pharmacy_id))
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

create table pharmaceutical_company_product (
  pharmaceutical_company_id      bigint not null,
  product_id                     bigint not null,
  constraint pk_pharmaceutical_company_product primary key (pharmaceutical_company_id, product_id))
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

create sequence diagnostic_order_seq;

create sequence diagnostic_report_seq;

create sequence diagnostic_representative_seq;

create sequence diagnostic_test_seq;

create sequence diagnostic_test_line_item_seq;

create sequence doctor_seq;

create sequence doctor_assistant_seq;

create sequence doctor_award_seq;

create sequence doctor_clinic_info_seq;

create sequence doctor_education_seq;

create sequence doctor_experience_seq;

create sequence doctor_language_seq;

create sequence doctor_publication_seq;

create sequence doctor_social_work_seq;

create sequence doctors_prescription_seq;

create sequence file_entity_seq;

create sequence head_quarter_seq;

create sequence inventory_seq;

create sequence language_app_user_seq;

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

create sequence pharmacy_product_seq;

create sequence prescription_seq;

create sequence product_seq;

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
alter table batch add constraint fk_batch_pharmacyProduct_7 foreign key (pharmacy_product_id) references pharmacy_product (id);
create index ix_batch_pharmacyProduct_7 on batch (pharmacy_product_id);
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
alter table diagnostic_order add constraint fk_diagnostic_order_diagnosti_24 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_order_diagnosti_24 on diagnostic_order (diagnostic_centre_id);
alter table diagnostic_order add constraint fk_diagnostic_order_patient_25 foreign key (patient_id) references patient (id);
create index ix_diagnostic_order_patient_25 on diagnostic_order (patient_id);
alter table diagnostic_report add constraint fk_diagnostic_report_diagnost_26 foreign key (diagnostic_order_id) references diagnostic_order (id);
create index ix_diagnostic_report_diagnost_26 on diagnostic_report (diagnostic_order_id);
alter table diagnostic_report add constraint fk_diagnostic_report_diagnost_27 foreign key (diagnostic_test_id) references diagnostic_test (id);
create index ix_diagnostic_report_diagnost_27 on diagnostic_report (diagnostic_test_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__28 foreign key (app_user_id) references app_user (id);
create index ix_diagnostic_representative__28 on diagnostic_representative (app_user_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__29 foreign key (patient_id) references patient (id);
create index ix_diagnostic_representative__29 on diagnostic_representative (patient_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__30 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_representative__30 on diagnostic_representative (diagnostic_centre_id);
alter table diagnostic_test add constraint fk_diagnostic_test_diagnostic_31 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_test_diagnostic_31 on diagnostic_test (diagnostic_centre_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__32 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_test_line_item__32 on diagnostic_test_line_item (prescription_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__33 foreign key (diagnostic_test_id) references diagnostic_test (id);
create index ix_diagnostic_test_line_item__33 on diagnostic_test_line_item (diagnostic_test_id);
alter table doctor add constraint fk_doctor_appUser_34 foreign key (app_user_id) references app_user (id);
create index ix_doctor_appUser_34 on doctor (app_user_id);
alter table doctor_assistant add constraint fk_doctor_assistant_appUser_35 foreign key (app_user_id) references app_user (id);
create index ix_doctor_assistant_appUser_35 on doctor_assistant (app_user_id);
alter table doctor_award add constraint fk_doctor_award_doctor_36 foreign key (doctor_id) references doctor (id);
create index ix_doctor_award_doctor_36 on doctor_award (doctor_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_clinic_37 foreign key (clinic_id) references clinic (id);
create index ix_doctor_clinic_info_clinic_37 on doctor_clinic_info (clinic_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_doctor_38 foreign key (doctor_id) references doctor (id);
create index ix_doctor_clinic_info_doctor_38 on doctor_clinic_info (doctor_id);
alter table doctor_education add constraint fk_doctor_education_doctor_39 foreign key (doctor_id) references doctor (id);
create index ix_doctor_education_doctor_39 on doctor_education (doctor_id);
alter table doctor_experience add constraint fk_doctor_experience_doctor_40 foreign key (doctor_id) references doctor (id);
create index ix_doctor_experience_doctor_40 on doctor_experience (doctor_id);
alter table doctor_publication add constraint fk_doctor_publication_doctor_41 foreign key (doctor_id) references doctor (id);
create index ix_doctor_publication_doctor_41 on doctor_publication (doctor_id);
alter table doctor_social_work add constraint fk_doctor_social_work_doctor_42 foreign key (doctor_id) references doctor (id);
create index ix_doctor_social_work_doctor_42 on doctor_social_work (doctor_id);
alter table inventory add constraint fk_inventory_pharmacy_43 foreign key (pharmacy_id) references pharmacy (id);
create index ix_inventory_pharmacy_43 on inventory (pharmacy_id);
alter table inventory add constraint fk_inventory_pharmacyProduct_44 foreign key (pharmacy_product_id) references pharmacy_product (id);
create index ix_inventory_pharmacyProduct_44 on inventory (pharmacy_product_id);
alter table language_app_user add constraint fk_language_app_user_doctor_l_45 foreign key (doctor_language_id) references doctor_language (id);
create index ix_language_app_user_doctor_l_45 on language_app_user (doctor_language_id);
alter table medical_representative add constraint fk_medical_representative_app_46 foreign key (app_user_id) references app_user (id);
create index ix_medical_representative_app_46 on medical_representative (app_user_id);
alter table medical_representative add constraint fk_medical_representative_man_47 foreign key (manager_id) references medical_representative (id);
create index ix_medical_representative_man_47 on medical_representative (manager_id);
alter table medical_representative add constraint fk_medical_representative_pha_48 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_medical_representative_pha_48 on medical_representative (pharmaceutical_company_id);
alter table medicine_line_item add constraint fk_medicine_line_item_prescri_49 foreign key (prescription_id) references prescription (id);
create index ix_medicine_line_item_prescri_49 on medicine_line_item (prescription_id);
alter table order_line_item add constraint fk_order_line_item_pharmacy_50 foreign key (pharmacy_id) references pharmacy (id);
create index ix_order_line_item_pharmacy_50 on order_line_item (pharmacy_id);
alter table order_line_item add constraint fk_order_line_item_pharmacyPr_51 foreign key (pharmacy_product_id) references pharmacy_product (id);
create index ix_order_line_item_pharmacyPr_51 on order_line_item (pharmacy_product_id);
alter table patient add constraint fk_patient_appUser_52 foreign key (app_user_id) references app_user (id);
create index ix_patient_appUser_52 on patient (app_user_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_doctor_53 foreign key (doctor_id) references doctor (id);
create index ix_patient_doctor_info_doctor_53 on patient_doctor_info (doctor_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_patien_54 foreign key (patient_id) references patient (id);
create index ix_patient_doctor_info_patien_54 on patient_doctor_info (patient_id);
alter table pharmaceutical_company add constraint fk_pharmaceutical_company_adm_55 foreign key (admin_mr_id) references medical_representative (id);
create index ix_pharmaceutical_company_adm_55 on pharmaceutical_company (admin_mr_id);
alter table pharmaceutical_product add constraint fk_pharmaceutical_product_pha_56 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_pharmaceutical_product_pha_56 on pharmaceutical_product (pharmaceutical_company_id);
alter table pharmacist add constraint fk_pharmacist_appUser_57 foreign key (app_user_id) references app_user (id);
create index ix_pharmacist_appUser_57 on pharmacist (app_user_id);
alter table pharmacist add constraint fk_pharmacist_pharmacy_58 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacist_pharmacy_58 on pharmacist (pharmacy_id);
alter table pharmacy add constraint fk_pharmacy_address_59 foreign key (address_id) references address (id);
create index ix_pharmacy_address_59 on pharmacy (address_id);
alter table pharmacy add constraint fk_pharmacy_adminPharmacist_60 foreign key (admin_pharmacist_id) references pharmacist (id);
create index ix_pharmacy_adminPharmacist_60 on pharmacy (admin_pharmacist_id);
alter table pharmacy_info add constraint fk_pharmacy_info_pharmacy_61 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_info_pharmacy_61 on pharmacy_info (pharmacy_id);
alter table pharmacy_product add constraint fk_pharmacy_product_pharmacy_62 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacy_product_pharmacy_62 on pharmacy_product (pharmacy_id);
alter table prescription add constraint fk_prescription_pharmacy_63 foreign key (pharmacy_id) references pharmacy (id);
create index ix_prescription_pharmacy_63 on prescription (pharmacy_id);
alter table prescription add constraint fk_prescription_doctor_64 foreign key (doctor_id) references doctor (id);
create index ix_prescription_doctor_64 on prescription (doctor_id);
alter table prescription add constraint fk_prescription_clinic_65 foreign key (clinic_id) references clinic (id);
create index ix_prescription_clinic_65 on prescription (clinic_id);
alter table prescription add constraint fk_prescription_patient_66 foreign key (patient_id) references patient (id);
create index ix_prescription_patient_66 on prescription (patient_id);
alter table prescription add constraint fk_prescription_appointment_67 foreign key (appointment_id) references appointment (id);
create index ix_prescription_appointment_67 on prescription (appointment_id);
alter table product add constraint fk_product_pharmaceuticalComp_68 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_product_pharmaceuticalComp_68 on product (pharmaceutical_company_id);
alter table product add constraint fk_product_pharmacy_69 foreign key (pharmacy_id) references pharmacy (id);
create index ix_product_pharmacy_69 on product (pharmacy_id);
alter table question_and_answer add constraint fk_question_and_answer_questi_70 foreign key (question_by_id) references app_user (id);
create index ix_question_and_answer_questi_70 on question_and_answer (question_by_id);
alter table question_and_answer add constraint fk_question_and_answer_answer_71 foreign key (answer_by_id) references app_user (id);
create index ix_question_and_answer_answer_71 on question_and_answer (answer_by_id);
alter table sample add constraint fk_sample_dcrline_item_72 foreign key (dcrline_item_id) references dcrline_item (id);
create index ix_sample_dcrline_item_72 on sample (dcrline_item_id);
alter table sample add constraint fk_sample_product_73 foreign key (product_id) references product (id);
create index ix_sample_product_73 on sample (product_id);
alter table show_cased_product add constraint fk_show_cased_product_pharmac_74 foreign key (pharmacy_id) references pharmacy (id);
create index ix_show_cased_product_pharmac_74 on show_cased_product (pharmacy_id);
alter table show_cased_service add constraint fk_show_cased_service_diagnos_75 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_show_cased_service_diagnos_75 on show_cased_service (diagnostic_centre_id);
alter table tpline_item add constraint fk_tpline_item_tour_plan_76 foreign key (tour_plan_id) references tour_plan (id);
create index ix_tpline_item_tour_plan_76 on tpline_item (tour_plan_id);
alter table tour_plan add constraint fk_tour_plan_medical_represen_77 foreign key (medical_representative_id) references medical_representative (id);
create index ix_tour_plan_medical_represen_77 on tour_plan (medical_representative_id);



alter table dcrline_item_product add constraint fk_dcrline_item_product_dcrli_01 foreign key (dcrline_item_id) references dcrline_item (id);

alter table dcrline_item_product add constraint fk_dcrline_item_product_produ_02 foreign key (product_id) references product (id);

alter table diagnostic_centre_file_entity add constraint fk_diagnostic_centre_file_ent_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_file_entity add constraint fk_diagnostic_centre_file_ent_02 foreign key (file_entity_id) references file_entity (id);

alter table diagnostic_centre_doctor add constraint fk_diagnostic_centre_doctor_d_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_doctor add constraint fk_diagnostic_centre_doctor_d_02 foreign key (doctor_id) references doctor (id);

alter table diagnostic_centre_prescription add constraint fk_diagnostic_centre_prescrip_01 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table diagnostic_centre_prescription add constraint fk_diagnostic_centre_prescrip_02 foreign key (prescription_id) references prescription (id);

alter table doctor_doctor_language add constraint fk_doctor_doctor_language_doc_01 foreign key (doctor_id) references doctor (id);

alter table doctor_doctor_language add constraint fk_doctor_doctor_language_doc_02 foreign key (doctor_language_id) references doctor_language (id);

alter table doctor_pharmacy add constraint fk_doctor_pharmacy_doctor_01 foreign key (doctor_id) references doctor (id);

alter table doctor_pharmacy add constraint fk_doctor_pharmacy_pharmacy_02 foreign key (pharmacy_id) references pharmacy (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_02 foreign key (doctor_id) references doctor (id);

alter table medical_representative_head_quar add constraint fk_medical_representative_hea_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_head_quar add constraint fk_medical_representative_hea_02 foreign key (head_quarter_id) references head_quarter (id);

alter table patient_pharmacy add constraint fk_patient_pharmacy_patient_01 foreign key (patient_id) references patient (id);

alter table patient_pharmacy add constraint fk_patient_pharmacy_pharmacy_02 foreign key (pharmacy_id) references pharmacy (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__01 foreign key (patient_id) references patient (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__02 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

alter table pharmaceutical_company_product add constraint fk_pharmaceutical_company_pro_01 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);

alter table pharmaceutical_company_product add constraint fk_pharmaceutical_company_pro_02 foreign key (product_id) references product (id);

alter table pharmacy_file_entity add constraint fk_pharmacy_file_entity_pharm_01 foreign key (pharmacy_id) references pharmacy (id);

alter table pharmacy_file_entity add constraint fk_pharmacy_file_entity_file__02 foreign key (file_entity_id) references file_entity (id);

alter table show_cased_service_file_entity add constraint fk_show_cased_service_file_en_01 foreign key (show_cased_service_id) references show_cased_service (id);

alter table show_cased_service_file_entity add constraint fk_show_cased_service_file_en_02 foreign key (file_entity_id) references file_entity (id);

# --- !Downs

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

drop table if exists dcrline_item_product cascade;

drop table if exists daily_call_report cascade;

drop table if exists day_schedule cascade;

drop table if exists diagnostic_centre cascade;

drop table if exists diagnostic_centre_file_entity cascade;

drop table if exists diagnostic_centre_doctor cascade;

drop table if exists diagnostic_centre_prescription cascade;

drop table if exists diagnostic_order cascade;

drop table if exists diagnostic_report cascade;

drop table if exists diagnostic_representative cascade;

drop table if exists diagnostic_test cascade;

drop table if exists diagnostic_test_line_item cascade;

drop table if exists doctor cascade;

drop table if exists doctor_doctor_language cascade;

drop table if exists doctor_pharmacy cascade;

drop table if exists doctor_assistant cascade;

drop table if exists doctor_award cascade;

drop table if exists doctor_clinic_info cascade;

drop table if exists doctor_education cascade;

drop table if exists doctor_experience cascade;

drop table if exists doctor_language cascade;

drop table if exists doctor_publication cascade;

drop table if exists doctor_social_work cascade;

drop table if exists doctors_prescription cascade;

drop table if exists file_entity cascade;

drop table if exists head_quarter cascade;

drop table if exists inventory cascade;

drop table if exists language_app_user cascade;

drop table if exists medical_representative cascade;

drop table if exists medical_representative_doctor cascade;

drop table if exists medical_representative_head_quar cascade;

drop table if exists medicine_line_item cascade;

drop table if exists order_line_item cascade;

drop table if exists patient cascade;

drop table if exists patient_pharmacy cascade;

drop table if exists patient_diagnostic_centre cascade;

drop table if exists patient_doctor_info cascade;

drop table if exists pharmaceutical_company cascade;

drop table if exists pharmaceutical_company_product cascade;

drop table if exists pharmaceutical_product cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop table if exists pharmacy_file_entity cascade;

drop table if exists pharmacy_info cascade;

drop table if exists pharmacy_order cascade;

drop table if exists pharmacy_product cascade;

drop table if exists prescription cascade;

drop table if exists product cascade;

drop table if exists question_and_answer cascade;

drop table if exists sample cascade;

drop table if exists show_cased_product cascade;

drop table if exists show_cased_service cascade;

drop table if exists show_cased_service_file_entity cascade;

drop table if exists sig_code cascade;

drop table if exists social_user cascade;

drop table if exists tpline_item cascade;

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

drop sequence if exists diagnostic_order_seq;

drop sequence if exists diagnostic_report_seq;

drop sequence if exists diagnostic_representative_seq;

drop sequence if exists diagnostic_test_seq;

drop sequence if exists diagnostic_test_line_item_seq;

drop sequence if exists doctor_seq;

drop sequence if exists doctor_assistant_seq;

drop sequence if exists doctor_award_seq;

drop sequence if exists doctor_clinic_info_seq;

drop sequence if exists doctor_education_seq;

drop sequence if exists doctor_experience_seq;

drop sequence if exists doctor_language_seq;

drop sequence if exists doctor_publication_seq;

drop sequence if exists doctor_social_work_seq;

drop sequence if exists doctors_prescription_seq;

drop sequence if exists file_entity_seq;

drop sequence if exists head_quarter_seq;

drop sequence if exists inventory_seq;

drop sequence if exists language_app_user_seq;

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

drop sequence if exists pharmacy_product_seq;

drop sequence if exists prescription_seq;

drop sequence if exists product_seq;

drop sequence if exists question_and_answer_seq;

drop sequence if exists sample_seq;

drop sequence if exists show_cased_product_seq;

drop sequence if exists show_cased_service_seq;

drop sequence if exists social_user_seq;

drop sequence if exists tpline_item_seq;

drop sequence if exists tour_plan_seq;

