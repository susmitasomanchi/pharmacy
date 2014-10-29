--- Author: Lakshmi
--- Script to add isSearchable and isSuspended to the appUser,isRegVerified to the doctor



---app_user

alter table app_user ADD Column is_searchable Boolean default 'true',ADD Column is_suspended Boolean default 'false';


alter table doctor ADD is_reg_verified Boolean default 'false';