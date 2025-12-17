CREATE DATABASE user_service_db OWNER user_service_admin;
CREATE DATABASE task_service_db OWNER task_service_admin;
CREATE DATABASE auth_service_db OWNER auth_service_admin;

REVOKE ALL ON DATABASE user_service_db FROM PUBLIC;
REVOKE ALL ON DATABASE task_service_db FROM PUBLIC;
REVOKE ALL ON DATABASE auth_service_db FROM PUBLIC;

