CREATE DATABASE IF NOT EXISTS stock DEFAULT CHARSET = utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS stock_test DEFAULT CHARSET = utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'stock_admin'@'%' IDENTIFIED BY 'stock';
GRANT ALL PRIVILEGES ON *.* TO 'stock_admin'@'%';
ALTER USER 'stock_admin'@'%' IDENTIFIED BY 'stock';
FLUSH PRIVILEGES;