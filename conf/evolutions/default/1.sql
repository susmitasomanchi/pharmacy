# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table address (
  id                        bigint not null,
  addrress_line1            varchar(255),
  addrress_line2            varchar(255),
  addrress_line3            varchar(255),
  area                      varchar(255),
  latitude                  varchar(255),
  longitude                 varchar(255),
  city                      varchar(255),
  state                     varchar(35),
  pin_code                  varchar(255),
  fetched_pin_code          varchar(255),
  country                   varchar(27),
  last_update               timestamp not null,
  constraint ck_address_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH','CHNADIGARH')),
  constraint ck_address_country check (country in ('ARMENIA','ANGUILLA','AUSTRALIA','ARUBA','CHAD','BOSNIA_AND_HERZEGOVINA','ANTIGUA_AND_BARBUDA','CHINA','ASHMORE_AND_CARTIER_ISLANDS','AMERICAN_SAMOA','COMOROS','INDIA','BOLIVIA','CAMEROON','PAKISTAN','DENMARK','BURUNDI','CAPE_VERDE','BULGARIA','ARGENTINA','DJIBOUTI','BELGIUM','ALBANIA','BAHRAIN','ALGERIA','ECUADOR','BELARUS','BARBADOS','BURMA','CHILE','BRUNEI','BELIZE','AZERBAIJAN','BHUTAN','CANADA','AFGHANISTAN','ANDORRA','CAMBODIA','AKROTIRI','AUSTRIA','BOUVET_ISLAND','BERMUDA','DOMINICA','ANGOLA','EGYPT','BENIN','UNITED_STATES','DHEKELIA','BOTSWANA','CUBA','ANTARCTICA','BRAZIL','CYPRUS','BURKINA_FASO','BANGLADESH','COLOMBIA')),
  constraint pk_address primary key (id))
;

create table app_user (
  id                        bigint not null,
  image                     bytea,
  name                      varchar(255),
  username                  varchar(255),
  mobileno                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  sex                       varchar(6),
  dob                       timestamp,
  role                      varchar(16),
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
  remarks                   varchar(255),
  doctor_id                 bigint,
  clinic_id                 bigint,
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
  remarks                   varchar(255),
  last_update               timestamp not null,
  constraint pk_dcrline_item primary key (id))
;

create table daily_call_report (
  id                        bigint not null,
  medical_representative_id bigint not null,
  for_date                  timestamp,
  last_update               timestamp not null,
  constraint pk_daily_call_report primary key (id))
;

create table day_of_the_week (
  id                        bigint not null,
  day                       varchar(9),
  last_update               timestamp not null,
  constraint ck_day_of_the_week_day check (day in ('MONDAY','SUNDAY','WEDNESDAY','THURSDAY','SATURDAY','TUESDAY','FRIDAY')),
  constraint pk_day_of_the_week primary key (id))
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
  address                   varchar(255),
  mobile_no                 varchar(255),
  email_id                  varchar(255),
  website_name              varchar(255),
  diagnostic_rep_admin_id   bigint,
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
  prescription_id           bigint not null,
  remarks                   varchar(255),
  last_update               timestamp not null)
;

create table doctor (
  id                        bigint not null,
  app_user_id               bigint,
  specialization            varchar(255),
  position                  varchar(255),
  degree                    varchar(255),
  experience                varchar(255),
  search_index              TEXT,
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
  award_for                 varchar(255),
  year                      varchar(255),
  comment_for_awards        varchar(255),
  last_update               timestamp not null,
  constraint pk_doctor_award primary key (id))
;

create table doctor_clinic_info (
  id                        bigint not null,
  clinic_id                 bigint,
  doctor_id                 bigint,
  slot                      integer,
  slotmr                    integer,
  active                    boolean,
  last_update               timestamp not null,
  constraint pk_doctor_clinic_info primary key (id))
;

create table doctor_education (
  id                        bigint not null,
  doctor_id                 bigint not null,
  college_name              varchar(255),
  degree                    varchar(255),
  from_year                 integer,
  to_year                   integer,
  last_update               timestamp not null,
  constraint pk_doctor_education primary key (id))
;

create table doctor_experience (
  id                        bigint not null,
  doctor_id                 bigint not null,
  previous_hospital_name    varchar(255),
  worked_as                 varchar(255),
  location                  varchar(255),
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
  article_name              varchar(255),
  article_for               varchar(255),
  year                      varchar(255),
  comment_for_article       varchar(255),
  last_update               timestamp not null,
  constraint pk_doctor_publication primary key (id))
;

create table doctor_social_work (
  id                        bigint not null,
  doctor_id                 bigint not null,
  social_work_tittle        varchar(255),
  comment_social_work       varchar(255),
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
  pharmacy_id               bigint not null,
  file_name                 varchar(255),
  mime_type                 varchar(255),
  byte_content              bytea,
  last_update               timestamp not null,
  constraint pk_file_entity primary key (id))
;

create table head_quarter (
  state                     varchar(35),
  name                      varchar(255),
  last_update               timestamp not null,
  constraint ck_head_quarter_state check (state in ('DADRA_AND_NAGAR_HAVELI','KERALA','WEST_BENGAL','JAMMU_AND_KASHMIR','HIMACHAL_PRADESH','MANIPUR','MIZORAM','MAHARASHTRA','JHARKHAND','ASSAM','UTTARAKHAND','SIKKIM','KARNATAKA','CHHATTISGARH','ANDHRA_PRADESH','NATIONAL_CAPITAL_TERRITORY_OF_DELHI','UTTAR_PRADESH','PUDUCHERRY','ANDAMAN_AND_NICOBAR_ISLANDS','TRIPURA','GOA','DAMAN_AND_DIU','NAGALAND','ODISHA','TAMIL_NADU','BIHAR','RAJASTHAN','LAKSHADWEEP','HARYANA','MEGHALAYA','PUNJAB','ARUNACHAL_PRADESH','GUJARAT','TELANGANA','MADHYA_PRADESH','CHNADIGARH')))
;

create table inventory (
  id                        bigint not null,
  pharmacy_id               bigint not null,
  product_id                bigint,
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
  types_of_medecine         varchar(255),
  mr_admin_id               bigint,
  pharmaceutical_company_id bigint,
  last_update               timestamp not null,
  constraint pk_medical_representative primary key (id))
;

create table medicine_line_item (
  prescription_id           bigint not null,
  frequency                 varchar(255),
  remarks                   varchar(255),
  last_update               timestamp not null)
;

create table order_line_item (
  id                        bigint not null,
  pharmacy_order_id         bigint not null,
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
  app_user_id               bigint,
  mbno                      varchar(255),
  date                      varchar(255),
  disease                   varchar(255),
  appointment_id            varchar(255),
  doctor_availability       varchar(255),
  is_urgent_patient         varchar(255),
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
  last_update               timestamp not null,
  constraint pk_pharmaceutical_company primary key (id))
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
  test_field                varchar(255),
  background_image          bytea,
  address_id                bigint,
  contact_no                varchar(255),
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_pharmacy primary key (id))
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

create table prescription (
  id                        bigint not null,
  appointment_id            bigint,
  problem_statement         varchar(255),
  prognosis                 varchar(255),
  remarks                   varchar(255),
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


create table dcrline_item_product (
  dcrline_item_id                bigint not null,
  product_id                     bigint not null,
  constraint pk_dcrline_item_product primary key (dcrline_item_id, product_id))
;

create table doctor_doctor_language (
  doctor_id                      bigint not null,
  doctor_language_id             bigint not null,
  constraint pk_doctor_doctor_language primary key (doctor_id, doctor_language_id))
;

create table medical_representative_doctor (
  medical_representative_id      bigint not null,
  doctor_id                      bigint not null,
  constraint pk_medical_representative_doctor primary key (medical_representative_id, doctor_id))
;

create table patient_diagnostic_centre (
  patient_id                     bigint not null,
  diagnostic_centre_id           bigint not null,
  constraint pk_patient_diagnostic_centre primary key (patient_id, diagnostic_centre_id))
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

create sequence day_of_the_week_seq;

create sequence day_schedule_seq;

create sequence diagnostic_centre_seq;

create sequence diagnostic_order_seq;

create sequence diagnostic_report_seq;

create sequence diagnostic_representative_seq;

create sequence diagnostic_test_seq;

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

create sequence inventory_seq;

create sequence language_app_user_seq;

create sequence medical_representative_seq;

create sequence order_line_item_seq;

create sequence patient_seq;

create sequence patient_doctor_info_seq;

create sequence pharmaceutical_company_seq;

create sequence pharmacist_seq;

create sequence pharmacy_seq;

create sequence pharmacy_order_seq;

create sequence prescription_seq;

create sequence product_seq;

create sequence question_and_answer_seq;

create sequence sample_seq;

create sequence social_user_seq;

alter table appointment add constraint fk_appointment_requestedBy_1 foreign key (requested_by_id) references app_user (id);
create index ix_appointment_requestedBy_1 on appointment (requested_by_id);
alter table appointment add constraint fk_appointment_apporovedBy_2 foreign key (apporoved_by_id) references app_user (id);
create index ix_appointment_apporovedBy_2 on appointment (apporoved_by_id);
alter table appointment add constraint fk_appointment_doctor_3 foreign key (doctor_id) references doctor (id);
create index ix_appointment_doctor_3 on appointment (doctor_id);
alter table appointment add constraint fk_appointment_clinic_4 foreign key (clinic_id) references clinic (id);
create index ix_appointment_clinic_4 on appointment (clinic_id);
alter table article add constraint fk_article_category_5 foreign key (category_id) references article_category (id);
create index ix_article_category_5 on article (category_id);
alter table batch add constraint fk_batch_inventory_6 foreign key (inventory_id) references inventory (id);
create index ix_batch_inventory_6 on batch (inventory_id);
alter table batch add constraint fk_batch_product_7 foreign key (product_id) references product (id);
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
alter table daily_call_report add constraint fk_daily_call_report_medical__17 foreign key (medical_representative_id) references medical_representative (id);
create index ix_daily_call_report_medical__17 on daily_call_report (medical_representative_id);
alter table day_schedule add constraint fk_day_schedule_doctor_clinic_18 foreign key (doctor_clinic_info_id) references doctor_clinic_info (id);
create index ix_day_schedule_doctor_clinic_18 on day_schedule (doctor_clinic_info_id);
alter table diagnostic_centre add constraint fk_diagnostic_centre_diagnost_19 foreign key (diagnostic_rep_admin_id) references diagnostic_representative (id);
create index ix_diagnostic_centre_diagnost_19 on diagnostic_centre (diagnostic_rep_admin_id);
alter table diagnostic_order add constraint fk_diagnostic_order_diagnosti_20 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_order_diagnosti_20 on diagnostic_order (diagnostic_centre_id);
alter table diagnostic_order add constraint fk_diagnostic_order_patient_21 foreign key (patient_id) references patient (id);
create index ix_diagnostic_order_patient_21 on diagnostic_order (patient_id);
alter table diagnostic_report add constraint fk_diagnostic_report_diagnost_22 foreign key (diagnostic_order_id) references diagnostic_order (id);
create index ix_diagnostic_report_diagnost_22 on diagnostic_report (diagnostic_order_id);
alter table diagnostic_report add constraint fk_diagnostic_report_diagnost_23 foreign key (diagnostic_test_id) references diagnostic_test (id);
create index ix_diagnostic_report_diagnost_23 on diagnostic_report (diagnostic_test_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__24 foreign key (app_user_id) references app_user (id);
create index ix_diagnostic_representative__24 on diagnostic_representative (app_user_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__25 foreign key (patient_id) references patient (id);
create index ix_diagnostic_representative__25 on diagnostic_representative (patient_id);
alter table diagnostic_representative add constraint fk_diagnostic_representative__26 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_representative__26 on diagnostic_representative (diagnostic_centre_id);
alter table diagnostic_test add constraint fk_diagnostic_test_diagnostic_27 foreign key (diagnostic_centre_id) references diagnostic_centre (id);
create index ix_diagnostic_test_diagnostic_27 on diagnostic_test (diagnostic_centre_id);
alter table diagnostic_test_line_item add constraint fk_diagnostic_test_line_item__28 foreign key (prescription_id) references prescription (id);
create index ix_diagnostic_test_line_item__28 on diagnostic_test_line_item (prescription_id);
alter table doctor add constraint fk_doctor_appUser_29 foreign key (app_user_id) references app_user (id);
create index ix_doctor_appUser_29 on doctor (app_user_id);
alter table doctor_assistant add constraint fk_doctor_assistant_appUser_30 foreign key (app_user_id) references app_user (id);
create index ix_doctor_assistant_appUser_30 on doctor_assistant (app_user_id);
alter table doctor_award add constraint fk_doctor_award_doctor_31 foreign key (doctor_id) references doctor (id);
create index ix_doctor_award_doctor_31 on doctor_award (doctor_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_clinic_32 foreign key (clinic_id) references clinic (id);
create index ix_doctor_clinic_info_clinic_32 on doctor_clinic_info (clinic_id);
alter table doctor_clinic_info add constraint fk_doctor_clinic_info_doctor_33 foreign key (doctor_id) references doctor (id);
create index ix_doctor_clinic_info_doctor_33 on doctor_clinic_info (doctor_id);
alter table doctor_education add constraint fk_doctor_education_doctor_34 foreign key (doctor_id) references doctor (id);
create index ix_doctor_education_doctor_34 on doctor_education (doctor_id);
alter table doctor_experience add constraint fk_doctor_experience_doctor_35 foreign key (doctor_id) references doctor (id);
create index ix_doctor_experience_doctor_35 on doctor_experience (doctor_id);
alter table doctor_publication add constraint fk_doctor_publication_doctor_36 foreign key (doctor_id) references doctor (id);
create index ix_doctor_publication_doctor_36 on doctor_publication (doctor_id);
alter table doctor_social_work add constraint fk_doctor_social_work_doctor_37 foreign key (doctor_id) references doctor (id);
create index ix_doctor_social_work_doctor_37 on doctor_social_work (doctor_id);
alter table file_entity add constraint fk_file_entity_pharmacy_38 foreign key (pharmacy_id) references pharmacy (id);
create index ix_file_entity_pharmacy_38 on file_entity (pharmacy_id);
alter table inventory add constraint fk_inventory_pharmacy_39 foreign key (pharmacy_id) references pharmacy (id);
create index ix_inventory_pharmacy_39 on inventory (pharmacy_id);
alter table inventory add constraint fk_inventory_product_40 foreign key (product_id) references product (id);
create index ix_inventory_product_40 on inventory (product_id);
alter table language_app_user add constraint fk_language_app_user_doctor_l_41 foreign key (doctor_language_id) references doctor_language (id);
create index ix_language_app_user_doctor_l_41 on language_app_user (doctor_language_id);
alter table medical_representative add constraint fk_medical_representative_app_42 foreign key (app_user_id) references app_user (id);
create index ix_medical_representative_app_42 on medical_representative (app_user_id);
alter table medical_representative add constraint fk_medical_representative_pha_43 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_medical_representative_pha_43 on medical_representative (pharmaceutical_company_id);
alter table medicine_line_item add constraint fk_medicine_line_item_prescri_44 foreign key (prescription_id) references prescription (id);
create index ix_medicine_line_item_prescri_44 on medicine_line_item (prescription_id);
alter table order_line_item add constraint fk_order_line_item_pharmacy_o_45 foreign key (pharmacy_order_id) references pharmacy_order (id);
create index ix_order_line_item_pharmacy_o_45 on order_line_item (pharmacy_order_id);
alter table order_line_item add constraint fk_order_line_item_product_46 foreign key (product_id) references product (id);
create index ix_order_line_item_product_46 on order_line_item (product_id);
alter table patient add constraint fk_patient_appUser_47 foreign key (app_user_id) references app_user (id);
create index ix_patient_appUser_47 on patient (app_user_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_doctor_48 foreign key (doctor_id) references doctor (id);
create index ix_patient_doctor_info_doctor_48 on patient_doctor_info (doctor_id);
alter table patient_doctor_info add constraint fk_patient_doctor_info_patien_49 foreign key (patient_id) references patient (id);
create index ix_patient_doctor_info_patien_49 on patient_doctor_info (patient_id);
alter table pharmacist add constraint fk_pharmacist_appUser_50 foreign key (app_user_id) references app_user (id);
create index ix_pharmacist_appUser_50 on pharmacist (app_user_id);
alter table pharmacist add constraint fk_pharmacist_pharmacy_51 foreign key (pharmacy_id) references pharmacy (id);
create index ix_pharmacist_pharmacy_51 on pharmacist (pharmacy_id);
alter table pharmacy add constraint fk_pharmacy_address_52 foreign key (address_id) references address (id);
create index ix_pharmacy_address_52 on pharmacy (address_id);
alter table prescription add constraint fk_prescription_appointment_53 foreign key (appointment_id) references appointment (id);
create index ix_prescription_appointment_53 on prescription (appointment_id);
alter table product add constraint fk_product_pharmaceuticalComp_54 foreign key (pharmaceutical_company_id) references pharmaceutical_company (id);
create index ix_product_pharmaceuticalComp_54 on product (pharmaceutical_company_id);
alter table product add constraint fk_product_pharmacy_55 foreign key (pharmacy_id) references pharmacy (id);
create index ix_product_pharmacy_55 on product (pharmacy_id);
alter table question_and_answer add constraint fk_question_and_answer_questi_56 foreign key (question_by_id) references app_user (id);
create index ix_question_and_answer_questi_56 on question_and_answer (question_by_id);
alter table question_and_answer add constraint fk_question_and_answer_answer_57 foreign key (answer_by_id) references app_user (id);
create index ix_question_and_answer_answer_57 on question_and_answer (answer_by_id);
alter table sample add constraint fk_sample_dcrline_item_58 foreign key (dcrline_item_id) references dcrline_item (id);
create index ix_sample_dcrline_item_58 on sample (dcrline_item_id);
alter table sample add constraint fk_sample_product_59 foreign key (product_id) references product (id);
create index ix_sample_product_59 on sample (product_id);



alter table dcrline_item_product add constraint fk_dcrline_item_product_dcrli_01 foreign key (dcrline_item_id) references dcrline_item (id);

alter table dcrline_item_product add constraint fk_dcrline_item_product_produ_02 foreign key (product_id) references product (id);

alter table doctor_doctor_language add constraint fk_doctor_doctor_language_doc_01 foreign key (doctor_id) references doctor (id);

alter table doctor_doctor_language add constraint fk_doctor_doctor_language_doc_02 foreign key (doctor_language_id) references doctor_language (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_01 foreign key (medical_representative_id) references medical_representative (id);

alter table medical_representative_doctor add constraint fk_medical_representative_doc_02 foreign key (doctor_id) references doctor (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__01 foreign key (patient_id) references patient (id);

alter table patient_diagnostic_centre add constraint fk_patient_diagnostic_centre__02 foreign key (diagnostic_centre_id) references diagnostic_centre (id);

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

drop table if exists day_of_the_week cascade;

drop table if exists day_schedule cascade;

drop table if exists diagnostic_centre cascade;

drop table if exists diagnostic_order cascade;

drop table if exists diagnostic_report cascade;

drop table if exists diagnostic_representative cascade;

drop table if exists diagnostic_test cascade;

drop table if exists diagnostic_test_line_item cascade;

drop table if exists doctor cascade;

drop table if exists doctor_doctor_language cascade;

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

drop table if exists medicine_line_item cascade;

drop table if exists order_line_item cascade;

drop table if exists patient cascade;

drop table if exists patient_diagnostic_centre cascade;

drop table if exists patient_doctor_info cascade;

drop table if exists pharmaceutical_company cascade;

drop table if exists pharmacist cascade;

drop table if exists pharmacy cascade;

drop table if exists pharmacy_order cascade;

drop table if exists prescription cascade;

drop table if exists product cascade;

drop table if exists question_and_answer cascade;

drop table if exists sample cascade;

drop table if exists sig_code cascade;

drop table if exists social_user cascade;

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

drop sequence if exists day_of_the_week_seq;

drop sequence if exists day_schedule_seq;

drop sequence if exists diagnostic_centre_seq;

drop sequence if exists diagnostic_order_seq;

drop sequence if exists diagnostic_report_seq;

drop sequence if exists diagnostic_representative_seq;

drop sequence if exists diagnostic_test_seq;

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

drop sequence if exists inventory_seq;

drop sequence if exists language_app_user_seq;

drop sequence if exists medical_representative_seq;

drop sequence if exists order_line_item_seq;

drop sequence if exists patient_seq;

drop sequence if exists patient_doctor_info_seq;

drop sequence if exists pharmaceutical_company_seq;

drop sequence if exists pharmacist_seq;

drop sequence if exists pharmacy_seq;

drop sequence if exists pharmacy_order_seq;

drop sequence if exists prescription_seq;

drop sequence if exists product_seq;

drop sequence if exists question_and_answer_seq;

drop sequence if exists sample_seq;

drop sequence if exists social_user_seq;

