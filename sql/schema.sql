-- Create database (optional if createDatabaseIfNotExist=true is enabled in application.yml)
CREATE DATABASE IF NOT EXISTS student_db_google
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

-- 2. Create application user
CREATE USER IF NOT EXISTS 'student_app_google'@'%'
IDENTIFIED BY '123456';

-- 3. Grant database permissions
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX
      ON student_db_google.* TO 'student_app_google'@'%';

-- 4. Verify the user
SELECT user, host
FROM mysql.user
WHERE user = 'student_app_google';

-- 5. Verify privileges
SHOW GRANTS FOR 'student_app_google'@'%';

USE student_db_google;
          
-- Hibernate with ddl-auto: update will automatically create this table,
-- or you can run this DDL manually:
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    date_of_birth DATE,
    major VARCHAR(100) NOT NULL,
    gpa DOUBLE,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

