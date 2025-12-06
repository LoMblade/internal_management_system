-- ============================================
-- DATABASE MIGRATION SCRIPT
-- Hệ thống phân quyền linh hoạt (Permission-Based)
-- ============================================

-- 1. Tạo bảng roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    is_system_role BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Tạo bảng modules
CREATE TABLE IF NOT EXISTS modules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Tạo bảng resources
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module_id BIGINT NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    table_name VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE
);

-- 4. Tạo bảng permissions
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_permission (resource_id, code),
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

-- 5. Tạo bảng user_roles
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_role (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 6. Tạo bảng role_permissions
CREATE TABLE IF NOT EXISTS role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_role_permission (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- 7. Tạo bảng user_permissions
CREATE TABLE IF NOT EXISTS user_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    granted BOOLEAN DEFAULT TRUE,
    created_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_permission (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- 8. Cập nhật bảng users (giữ lại cột role cũ để backward compatibility)
-- ALTER TABLE users MODIFY COLUMN role VARCHAR(50) NULL;

-- ============================================
-- INSERT INITIAL DATA
-- ============================================

-- Insert Roles
INSERT INTO roles (name, description, is_system_role) VALUES
('ADMIN', 'Administrator - Full access', TRUE),
('HR', 'Human Resources', FALSE),
('WAREHOUSE', 'Warehouse Manager', FALSE),
('MANAGER', 'Department Manager', FALSE),
('STAFF', 'Employee', FALSE)
ON DUPLICATE KEY UPDATE name=name;

-- Insert Modules
INSERT INTO modules (code, name, description, display_order) VALUES
('HRM', 'Human Resource Management', 'Quản lý nhân sự', 1),
('WAREHOUSE', 'Warehouse Management', 'Quản lý kho', 2)
ON DUPLICATE KEY UPDATE code=code;

-- Insert Resources
INSERT INTO resources (module_id, code, name, description, table_name) VALUES
((SELECT id FROM modules WHERE code = 'HRM'), 'EMPLOYEE', 'Employee', 'Nhân viên', 'employees'),
((SELECT id FROM modules WHERE code = 'HRM'), 'DEPARTMENT', 'Department', 'Phòng ban', 'departments'),
((SELECT id FROM modules WHERE code = 'HRM'), 'POSITION', 'Position', 'Chức vụ', 'positions'),
((SELECT id FROM modules WHERE code = 'HRM'), 'ATTENDANCE', 'Attendance', 'Chấm công', 'attendances'),
((SELECT id FROM modules WHERE code = 'HRM'), 'PAYROLL', 'Payroll', 'Lương', 'payrolls'),
((SELECT id FROM modules WHERE code = 'WAREHOUSE'), 'STOCK_IMPORT', 'Stock Import', 'Phiếu nhập kho', 'stock_imports'),
((SELECT id FROM modules WHERE code = 'WAREHOUSE'), 'STOCK_EXPORT', 'Stock Export', 'Phiếu xuất kho', 'stock_exports'),
((SELECT id FROM modules WHERE code = 'WAREHOUSE'), 'PRODUCT', 'Product', 'Sản phẩm', 'products'),
((SELECT id FROM modules WHERE code = 'WAREHOUSE'), 'CATEGORY', 'Category', 'Danh mục', 'categories'),
((SELECT id FROM modules WHERE code = 'WAREHOUSE'), 'WAREHOUSE', 'Warehouse', 'Kho hàng', 'warehouses'),
((SELECT id FROM modules WHERE code = 'WAREHOUSE'), 'INVENTORY', 'Inventory', 'Tồn kho', 'inventories')
ON DUPLICATE KEY UPDATE code=code;

-- Insert Permissions cho EMPLOYEE
INSERT INTO permissions (resource_id, code, name, description) VALUES
((SELECT id FROM resources WHERE code = 'EMPLOYEE'), 'CREATE', 'Create Employee', 'Tạo mới nhân viên'),
((SELECT id FROM resources WHERE code = 'EMPLOYEE'), 'READ_ALL', 'View All Employees', 'Xem tất cả nhân viên'),
((SELECT id FROM resources WHERE code = 'EMPLOYEE'), 'READ_OWN', 'View Own Employee', 'Xem thông tin của chính mình'),
((SELECT id FROM resources WHERE code = 'EMPLOYEE'), 'UPDATE', 'Update Employee', 'Cập nhật nhân viên'),
((SELECT id FROM resources WHERE code = 'EMPLOYEE'), 'DELETE', 'Delete Employee', 'Xóa nhân viên')
ON DUPLICATE KEY UPDATE code=code;

-- Insert Permissions cho DEPARTMENT
INSERT INTO permissions (resource_id, code, name, description) VALUES
((SELECT id FROM resources WHERE code = 'DEPARTMENT'), 'CREATE', 'Create Department', 'Tạo mới phòng ban'),
((SELECT id FROM resources WHERE code = 'DEPARTMENT'), 'READ_ALL', 'View All Departments', 'Xem tất cả phòng ban'),
((SELECT id FROM resources WHERE code = 'DEPARTMENT'), 'UPDATE', 'Update Department', 'Cập nhật phòng ban'),
((SELECT id FROM resources WHERE code = 'DEPARTMENT'), 'DELETE', 'Delete Department', 'Xóa phòng ban')
ON DUPLICATE KEY UPDATE code=code;

-- Insert Permissions cho POSITION
INSERT INTO permissions (resource_id, code, name, description) VALUES
((SELECT id FROM resources WHERE code = 'POSITION'), 'CREATE', 'Create Position', 'Tạo mới chức vụ'),
((SELECT id FROM resources WHERE code = 'POSITION'), 'READ_ALL', 'View All Positions', 'Xem tất cả chức vụ'),
((SELECT id FROM resources WHERE code = 'POSITION'), 'UPDATE', 'Update Position', 'Cập nhật chức vụ'),
((SELECT id FROM resources WHERE code = 'POSITION'), 'DELETE', 'Delete Position', 'Xóa chức vụ')
ON DUPLICATE KEY UPDATE code=code;

-- Insert Permissions cho STOCK_IMPORT
INSERT INTO permissions (resource_id, code, name, description) VALUES
((SELECT id FROM resources WHERE code = 'STOCK_IMPORT'), 'CREATE', 'Create Stock Import', 'Tạo mới phiếu nhập'),
((SELECT id FROM resources WHERE code = 'STOCK_IMPORT'), 'READ_ALL', 'View All Stock Imports', 'Xem tất cả phiếu nhập'),
((SELECT id FROM resources WHERE code = 'STOCK_IMPORT'), 'READ_OWN', 'View Own Stock Imports', 'Xem phiếu nhập của mình'),
((SELECT id FROM resources WHERE code = 'STOCK_IMPORT'), 'UPDATE', 'Update Stock Import', 'Cập nhật phiếu nhập'),
((SELECT id FROM resources WHERE code = 'STOCK_IMPORT'), 'DELETE', 'Delete Stock Import', 'Xóa phiếu nhập')
ON DUPLICATE KEY UPDATE code=code;

-- Insert Permissions cho STOCK_EXPORT
INSERT INTO permissions (resource_id, code, name, description) VALUES
((SELECT id FROM resources WHERE code = 'STOCK_EXPORT'), 'CREATE', 'Create Stock Export', 'Tạo mới phiếu xuất'),
((SELECT id FROM resources WHERE code = 'STOCK_EXPORT'), 'READ_ALL', 'View All Stock Exports', 'Xem tất cả phiếu xuất'),
((SELECT id FROM resources WHERE code = 'STOCK_EXPORT'), 'READ_OWN', 'View Own Stock Exports', 'Xem phiếu xuất của mình'),
((SELECT id FROM resources WHERE code = 'STOCK_EXPORT'), 'UPDATE', 'Update Stock Export', 'Cập nhật phiếu xuất'),
((SELECT id FROM resources WHERE code = 'STOCK_EXPORT'), 'DELETE', 'Delete Stock Export', 'Xóa phiếu xuất')
ON DUPLICATE KEY UPDATE code=code;

-- Gán permissions cho role HR
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'HR'),
    p.id
FROM permissions p
WHERE p.resource_id IN (
    SELECT id FROM resources WHERE code IN ('EMPLOYEE', 'DEPARTMENT', 'POSITION', 'ATTENDANCE', 'PAYROLL')
)
ON DUPLICATE KEY UPDATE role_id=role_id;

-- Gán permissions cho role WAREHOUSE
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'WAREHOUSE'),
    p.id
FROM permissions p
WHERE p.resource_id IN (
    SELECT id FROM resources WHERE code IN ('STOCK_IMPORT', 'STOCK_EXPORT', 'PRODUCT', 'CATEGORY', 'WAREHOUSE', 'INVENTORY')
)
ON DUPLICATE KEY UPDATE role_id=role_id;

-- Gán permissions cho role MANAGER (chỉ đọc EMPLOYEE)
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'MANAGER'),
    p.id
FROM permissions p
WHERE p.resource_id = (SELECT id FROM resources WHERE code = 'EMPLOYEE')
AND p.code = 'READ_ALL'
ON DUPLICATE KEY UPDATE role_id=role_id;

-- Gán permissions cho role STAFF (chỉ đọc của chính mình)
INSERT INTO role_permissions (role_id, permission_id)
SELECT 
    (SELECT id FROM roles WHERE name = 'STAFF'),
    p.id
FROM permissions p
WHERE p.resource_id = (SELECT id FROM resources WHERE code = 'EMPLOYEE')
AND p.code = 'READ_OWN'
ON DUPLICATE KEY UPDATE role_id=role_id;

-- ============================================
-- MIGRATION COMPLETE
-- ============================================
