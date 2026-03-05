-- Tabel Users
CREATE TABLE IF NOT EXISTS users(
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    fullname VARCHAR(100),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL
);

-- Tabel Categories
CREATE TABLE IF NOT EXISTS categories(
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL
);

-- Table Products
CREATE TABLE IF NOT EXISTS products(
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    product_description TEXT,
    product_image VARCHAR(255) NOT NULL,
    price DECIMAL(15,0) NOT NULL,
    stock INT NOT NULL,
    category_id BIGINT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Table Orders
CREATE TABLE IF NOT EXISTS orders(
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    invoice_number VARCHAR(100) UNIQUE NOT NULL,
    order_total DECIMAL(15,0) NOT NULL,
    order_date DATETIME(6) NOT NULL,
    order_status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Table Order Items
CREATE TABLE IF NOT EXISTS order_items(
    order_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    sku VARCHAR(50) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(15,0) NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(15,0) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Table Payments
CREATE TABLE IF NOT EXISTS payments(
    payment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    payment_number VARCHAR(100) UNIQUE NOT NULL,
    payment_method ENUM('CASH', 'TRANSFER') NOT NULL,
    payment_date DATETIME(6) NOT NULL,
    payment_proof VARCHAR(255),
    payment_status ENUM('UNPAID', 'NEED_VERIFY', 'PAID', 'REJECTED') DEFAULT 'UNPAID',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    finished_at DATETIME(6) DEFAULT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);