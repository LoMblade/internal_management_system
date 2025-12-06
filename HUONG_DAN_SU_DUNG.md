# 📖 HƯỚNG DẪN SỬ DỤNG HỆ THỐNG PHÂN QUYỀN LINH HOẠT

## 🚀 CÁC BƯỚC TRIỂN KHAI

### **Bước 1: Chạy SQL Migration**

Chạy file `database_migration.sql` trong database của bạn để tạo các bảng mới:

```bash
mysql -u root -p your_database < database_migration.sql
```

Hoặc copy và chạy từng câu lệnh trong MySQL Workbench.

### **Bước 2: Cập nhật User hiện có**

Nếu bạn đã có users trong database, cần migrate role cũ sang role mới:

```sql
-- Ví dụ: User có role = 'HR' trong cột cũ
-- Cần gán role HR từ bảng roles mới
INSERT INTO user_roles (user_id, role_id, created_at)
SELECT u.id, r.id, NOW()
FROM users u
JOIN roles r ON r.name = u.role
WHERE u.role IS NOT NULL
ON DUPLICATE KEY UPDATE created_at = NOW();
```

### **Bước 3: Tạo Admin User đầu tiên**

Nếu chưa có admin, tạo user và gán role ADMIN:

```sql
-- 1. Tạo user (password đã được hash)
INSERT INTO users (username, email, password, is_active, created_at, updated_at)
VALUES ('admin', 'admin@company.com', '$2a$10$...', TRUE, NOW(), NOW());

-- 2. Gán role ADMIN
INSERT INTO user_roles (user_id, role_id, created_at)
SELECT u.id, r.id, NOW()
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN';
```

---

## 📋 API QUẢN LÝ QUYỀN

### **1. Lấy danh sách permissions của user**

```http
GET /api/admin/permissions/users/{userId}
Authorization: Bearer <admin_token>
```

**Response:**
```json
{
  "userId": 3,
  "username": "manager1",
  "email": "manager1@company.com",
  "roles": ["MANAGER"],
  "permissions": [
    {
      "id": 1,
      "resourceCode": "EMPLOYEE",
      "resourceName": "Employee",
      "permissionCode": "READ_ALL",
      "permissionName": "View All Employees",
      "description": "Xem tất cả nhân viên",
      "source": "ROLE"
    }
  ]
}
```

### **2. Gán roles cho user**

```http
POST /api/admin/permissions/users/{userId}/roles
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "roleIds": [3, 4]  // MANAGER, STAFF
}
```

### **3. Gán permissions trực tiếp cho user**

```http
POST /api/admin/permissions/users/{userId}/permissions
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "permissions": [
    {
      "resourceCode": "EMPLOYEE",
      "permissionCode": "READ_ALL"
    },
    {
      "resourceCode": "EMPLOYEE",
      "permissionCode": "UPDATE"
    }
  ]
}
```

### **4. Lấy tất cả roles**

```http
GET /api/admin/permissions/roles
Authorization: Bearer <admin_token>
```

### **5. Lấy tất cả permissions**

```http
GET /api/admin/permissions/permissions
Authorization: Bearer <admin_token>
```

### **6. Lấy permissions theo resource**

```http
GET /api/admin/permissions/resources/{resourceCode}/permissions
Authorization: Bearer <admin_token>
```

---

## 🔐 CÁCH PHÂN QUYỀN HOẠT ĐỘNG

### **1. Permission Format**

Permissions được format như sau:
- `PERM_{RESOURCE_CODE}_{PERMISSION_CODE}`
- Ví dụ: `PERM_EMPLOYEE_READ_ALL`, `PERM_EMPLOYEE_CREATE`

### **2. Admin có tất cả quyền**

User có role `ADMIN` tự động có:
- `PERM_ALL` - Tất cả quyền
- `ROLE_ADMIN` - Role authority

### **3. Cách kiểm tra quyền trong Controller**

**Cách cũ (Role-based):**
```java
@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
```

**Cách mới (Permission-based):**
```java
@PreAuthorize("hasPermission('EMPLOYEE', 'CREATE')")
@PreAuthorize("hasPermission('EMPLOYEE', 'READ_ALL') or hasPermission('EMPLOYEE', 'READ_OWN')")
```

### **4. Logic phân quyền**

- **READ_ALL**: Xem tất cả records
- **READ_OWN**: Chỉ xem records của chính mình
- **CREATE**: Tạo mới
- **UPDATE**: Cập nhật
- **DELETE**: Xóa

---

## 🎯 VÍ DỤ SỬ DỤNG

### **Ví dụ 1: Tạo Manager chỉ xem employees**

```bash
# 1. Tạo user
POST /api/auth/register
{
  "username": "manager1",
  "email": "manager1@company.com",
  "password": "manager123",
  "role": "MANAGER"
}

# 2. Gán role MANAGER (đã có sẵn permission READ_ALL cho EMPLOYEE)
POST /api/admin/permissions/users/{userId}/roles
{
  "roleIds": [4]  // MANAGER role id
}
```

### **Ví dụ 2: Tạo Staff chỉ xem của chính mình**

```bash
# 1. Tạo user
POST /api/auth/register
{
  "username": "staff1",
  "email": "staff1@company.com",
  "password": "staff123",
  "role": "STAFF"
}

# 2. Gán role STAFF (đã có sẵn permission READ_OWN cho EMPLOYEE)
POST /api/admin/permissions/users/{userId}/roles
{
  "roleIds": [5]  // STAFF role id
}
```

### **Ví dụ 3: Gán quyền tùy chỉnh cho user**

```bash
# Gán thêm quyền UPDATE cho user (ngoài quyền từ role)
POST /api/admin/permissions/users/{userId}/permissions
{
  "permissions": [
    {
      "resourceCode": "EMPLOYEE",
      "permissionCode": "UPDATE"
    }
  ]
}
```

---

## ⚠️ LƯU Ý

1. **Backward Compatibility**: Code vẫn hỗ trợ role cũ trong cột `users.role` để không break existing code
2. **Performance**: Với nhiều users, nên implement cache cho permissions
3. **Admin Role**: User có role ADMIN tự động có tất cả quyền, không cần gán permissions
4. **Permission Override**: Permissions trực tiếp (user_permissions) sẽ override permissions từ roles

---

## 🔄 MIGRATION TỪ HỆ THỐNG CŨ

Nếu bạn đang dùng hệ thống cũ với role trong cột `users.role`:

1. Chạy SQL migration script
2. Migrate users từ role cũ sang bảng `user_roles`
3. Test lại các API
4. Sau khi ổn định, có thể xóa cột `role` cũ trong bảng `users`

---

## 📝 TODO

- [ ] Implement cache cho permissions
- [ ] Tạo UI quản lý quyền (Frontend)
- [ ] Thêm audit log (ai gán quyền gì, khi nào)
- [ ] Thêm validation (không cho gán quyền không hợp lệ)

