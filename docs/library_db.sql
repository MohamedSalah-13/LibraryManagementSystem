-- إنشاء قاعدة البيانات
CREATE DATABASE library_db;
USE library_db;


-- جدول المستخدمين (للتسجيل والدخول)
CREATE TABLE users
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    full_name   VARCHAR(100),
    email       VARCHAR(100),
    role        ENUM ('admin', 'librarian') DEFAULT 'librarian',
    profile_pic VARCHAR(255),
    created_at  TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

-- جدول الكتب
CREATE TABLE books
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(200) NOT NULL,
    author         VARCHAR(100) NOT NULL,
    isbn           VARCHAR(20) UNIQUE,
    category       VARCHAR(50),
    quantity       INT       DEFAULT 1,
    available      INT       DEFAULT 1,
    price          DECIMAL(10, 2),
    published_year INT,
    added_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- جدول الأعضاء
CREATE TABLE members
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email     VARCHAR(100) UNIQUE,
    phone     VARCHAR(20),
    address   VARCHAR(200),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- جدول الاستعارة
CREATE TABLE borrowings
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    book_id     INT  NOT NULL,
    member_id   INT  NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    status      ENUM ('borrowed','returned') DEFAULT 'borrowed',
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (member_id) REFERENCES members (id)
);

-- إدخال مستخدم تجريبي (كلمة المرور: admin123)
INSERT INTO users (username, password, full_name, email, role)
VALUES ('admin', 'admin123', 'System Admin', 'admin@library.com', 'admin');

-- بيانات تجريبية للكتب
INSERT INTO books (title, author, isbn, category, quantity, available, price, published_year)
VALUES ('Clean Code', 'Robert C. Martin', '978-0132350884', 'Programming', 3, 3, 45.99, 2008),
       ('Java Programming', 'James Gosling', '978-0134685991', 'Programming', 5, 4, 55.00, 2018),
       ('Design Patterns', 'Gang of Four', '978-0201633610', 'Software', 2, 2, 60.00, 1994);