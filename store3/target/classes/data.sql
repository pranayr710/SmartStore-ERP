-- Insert admin only if not exists
INSERT INTO users (username, password, role, status)
SELECT 'admin', 'admin123', 'ADMIN', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='admin');

-- Insert staff only if not exists
INSERT INTO users (username, password, role, status)
SELECT 'staff1', 'staff123', 'STAFF', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='staff1');

-- Product 1
INSERT INTO products (name, category, brand, price, stock, battery, camera, storage, rating)
SELECT 'Galaxy S23', 'Smartphone', 'Samsung', 65000, 10, 5000, '50MP', 256, 4.5
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Galaxy S23');

-- Product 2
INSERT INTO products (name, category, brand, price, stock, battery, camera, storage, rating)
SELECT 'iPhone 15', 'Smartphone', 'Apple', 79000, 8, 4200, '48MP', 256, 4.7
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='iPhone 15');

-- Product 3
INSERT INTO products (name, category, brand, price, stock, battery, camera, storage, rating)
SELECT 'Redmi Note 13', 'Smartphone', 'Xiaomi', 22000, 20, 5000, '108MP', 128, 4.3
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Redmi Note 13');

-- Product 4
INSERT INTO products (name, category, brand, price, stock, battery, camera, storage, rating)
SELECT 'Realme GT Neo', 'Smartphone', 'Realme', 30000, 15, 4500, '64MP', 256, 4.2
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Realme GT Neo');
