-- Seed basic roles and an admin user for tests (password is bcrypt('admin'))
INSERT INTO roles (id, name, description) VALUES
  (RANDOM_UUID(), 'ADMIN', 'Administrator role'),
  (RANDOM_UUID(), 'MORADOR', 'Resident role'),
  (RANDOM_UUID(), 'COLABORADOR', 'Worker role');

-- create admin user
INSERT INTO users (id, user_name, email, password, enabled) VALUES
  (RANDOM_UUID(), 'admin', 'admin@example.com', '$2a$10$DowJdJ/6y1q8QJQeK8rY9u1G8pY6xqKc1Yq1J8ZQfGQ9YpK7t1bG', TRUE);

-- link admin to ADMIN role
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.user_name = 'admin' AND r.name = 'ADMIN';
