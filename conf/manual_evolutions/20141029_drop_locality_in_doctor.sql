--- Author: Lakshmi
--- drop locality in Doctor,Pharmacy,DiagnosticCentre table
--- Locality is in models package






---Drop Locality from Doctor Entity

Alter table doctor DROP if exists locality_id;

---Drop Locality from DiagnosticCentre Entity

Alter table diagnostic_centre DROP locality_id;


---drop Locality from Pharmacy Entity

Alter table pharmacy DROP if exists locality_id;


---drop Locality from Clinic Entity

Alter table clinic DROP if exists locality_id;
