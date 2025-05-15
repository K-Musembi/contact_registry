CREATE TABLE users ( -- Use plural form to avoid potential keyword conflict with 'user'
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL, -- Store hashed passwords
                          category VARCHAR(50) NOT NULL, -- 'admin', 'regular'
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);