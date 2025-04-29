-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create user_roles join table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert roles into the roles table
INSERT INTO roles (name) VALUES ('ROLE_SUPER_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

-- Insert a super admin user into the users table
-- Hash the password first (bcrypt)
INSERT INTO users (first_name, last_name, username, password)
VALUES ('Super', 'Admin', 'superadmin', 'hashed_superadminpassword_here');

-- Assign the super admin role to the super admin user in the user_roles table
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'superadmin' AND r.name = 'ROLE_SUPER_ADMIN';
