# 🎯 ĐỀ XUẤT HỆ THỐNG PHÂN QUYỀN LINH HOẠT (Permission-Based)

## 📊 1. PHÂN TÍCH YÊU CẦU

### **Yêu cầu của bạn:**
1. ✅ Tách Role thành bảng riêng trong database
2. ✅ Admin có thể gán quyền chi tiết cho từng user
3. ✅ Quyền bao gồm: Module nào, CRUD bảng nào, xem được gì
4. ✅ UI-friendly: Tick/tick để gán quyền

### **Ưu điểm:**
- ✅ **Linh hoạt cao:** Không bị giới hạn bởi role cố định
- ✅ **Chi tiết:** Có thể phân quyền từng chức năng, từng bảng
- ✅ **Dễ mở rộng:** Thêm permission mới không cần sửa code
- ✅ **Phù hợp doanh nghiệp:** Mỗi user có thể có quyền khác nhau
- ✅ **Audit tốt:** Biết chính xác user có quyền gì

### **Nhược điểm:**
- ⚠️ **Phức tạp hơn:** Cần nhiều bảng, nhiều logic
- ⚠️ **Performance:** Nhiều query hơn khi check permission
- ⚠️ **Cần UI:** Phải có giao diện quản lý quyền
- ⚠️ **Dễ lỗi:** Admin có thể gán quyền sai

---

## 🗄️ 2. THIẾT KẾ DATABASE

### **Bảng 1: `roles` - Vai trò cơ bản**

```sql
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,  -- ADMIN, HR, WAREHOUSE, MANAGER, STAFF
    description VARCHAR(255),
    is_system_role BOOLEAN DEFAULT FALSE,  -- Role hệ thống không được xóa
    created_at DATETIME,
    updated_at DATETIME
);

INSERT INTO roles (name, description, is_system_role) VALUES
('ADMIN', 'Administrator - Full access', TRUE),
('HR', 'Human Resources', FALSE),
('WAREHOUSE', 'Warehouse Manager', FALSE),
('MANAGER', 'Department Manager', FALSE),
('STAFF', 'Employee', FALSE);
```

### **Bảng 2: `modules` - Các module trong hệ thống**

```sql
CREATE TABLE modules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,  -- HRM, WAREHOUSE
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME
);

INSERT INTO modules (code, name, description) VALUES
('HRM', 'Human Resource Management', 'Quản lý nhân sự'),
('WAREHOUSE', 'Warehouse Management', 'Quản lý kho');
```

### **Bảng 3: `resources` - Các bảng/tài nguyên trong hệ thống**

```sql
CREATE TABLE resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module_id BIGINT NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,  -- EMPLOYEE, DEPARTMENT, STOCK_IMPORT
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    table_name VARCHAR(100),  -- Tên bảng trong database
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);

INSERT INTO resources (module_id, code, name, table_name) VALUES
(1, 'EMPLOYEE', 'Employee', 'employees'),
(1, 'DEPARTMENT', 'Department', 'departments'),
(1, 'POSITION', 'Position', 'positions'),
(1, 'ATTENDANCE', 'Attendance', 'attendances'),
(1, 'PAYROLL', 'Payroll', 'payrolls'),
(2, 'STOCK_IMPORT', 'Stock Import', 'stock_imports'),
(2, 'STOCK_EXPORT', 'Stock Export', 'stock_exports'),
(2, 'PRODUCT', 'Product', 'products'),
(2, 'CATEGORY', 'Category', 'categories');
```

### **Bảng 4: `permissions` - Các quyền có thể có**

```sql
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,  -- CREATE, READ, UPDATE, DELETE, READ_ALL, READ_OWN
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    UNIQUE KEY unique_permission (resource_id, code),
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

INSERT INTO permissions (resource_id, code, name, description) VALUES
-- Employee permissions
(1, 'CREATE', 'Create Employee', 'Tạo mới nhân viên'),
(1, 'READ_ALL', 'View All Employees', 'Xem tất cả nhân viên'),
(1, 'READ_OWN', 'View Own Employee', 'Xem thông tin của chính mình'),
(1, 'UPDATE', 'Update Employee', 'Cập nhật nhân viên'),
(1, 'DELETE', 'Delete Employee', 'Xóa nhân viên'),
-- Department permissions
(2, 'CREATE', 'Create Department', 'Tạo mới phòng ban'),
(2, 'READ_ALL', 'View All Departments', 'Xem tất cả phòng ban'),
(2, 'UPDATE', 'Update Department', 'Cập nhật phòng ban'),
(2, 'DELETE', 'Delete Department', 'Xóa phòng ban');
-- ... tiếp tục cho các resource khác
```

### **Bảng 5: `user_roles` - User có những role nào**

```sql
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME,
    UNIQUE KEY unique_user_role (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
```

### **Bảng 6: `role_permissions` - Role có những permission nào (Template)**

```sql
CREATE TABLE role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at DATETIME,
    UNIQUE KEY unique_role_permission (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Ví dụ: Role HR có quyền gì
INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 1),  -- HR có quyền CREATE Employee
(2, 2),  -- HR có quyền READ_ALL Employee
(2, 4),  -- HR có quyền UPDATE Employee
(2, 5);  -- HR có quyền DELETE Employee
```

### **Bảng 7: `user_permissions` - User có những permission cụ thể (Override)**

```sql
CREATE TABLE user_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    granted BOOLEAN DEFAULT TRUE,  -- TRUE = cho phép, FALSE = từ chối
    created_by BIGINT,  -- Admin nào gán quyền
    created_at DATETIME,
    UNIQUE KEY unique_user_permission (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
```

### **Bảng 8: Cập nhật bảng `users`**

```sql
-- Xóa cột role cũ
ALTER TABLE users DROP COLUMN role;

-- Hoặc giữ lại làm default role (optional)
-- ALTER TABLE users MODIFY role VARCHAR(50) NULL;
```

---

## 🏗️ 3. KIẾN TRÚC CODE

### **3.1. Entity Classes**

```java
// Role.java
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;
    
    @Column(name = "is_system_role")
    private Boolean isSystemRole;
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    
    @ManyToMany
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}

// Permission.java
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;
    
    @Column(nullable = false)
    private String code;  // CREATE, READ_ALL, READ_OWN, UPDATE, DELETE
    
    private String name;
    private String description;
}

// Resource.java
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
    
    @Column(nullable = false, unique = true)
    private String code;  // EMPLOYEE, DEPARTMENT, STOCK_IMPORT
    
    private String name;
    
    @Column(name = "table_name")
    private String tableName;
}
```

### **3.2. Cập nhật User Entity**

```java
@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    // ... các field khác
    
    // Xóa: private Role role;
    
    // Thêm:
    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    @ManyToMany
    @JoinTable(
        name = "user_permissions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> directPermissions;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Lấy permissions từ roles
        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(
                    "PERM_" + permission.getResource().getCode() + "_" + permission.getCode()
                ));
            }
        }
        
        // Lấy permissions trực tiếp (override)
        for (Permission permission : directPermissions) {
            authorities.add(new SimpleGrantedAuthority(
                "PERM_" + permission.getResource().getCode() + "_" + permission.getCode()
            ));
        }
        
        // Admin có tất cả quyền
        if (hasRole("ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("PERM_ALL"));
        }
        
        return authorities;
    }
    
    public boolean hasRole(String roleName) {
        return roles.stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }
}
```

### **3.3. Permission Service**

```java
@Service
@RequiredArgsConstructor
public class PermissionService {
    
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    
    /**
     * Kiểm tra user có permission không
     */
    public boolean hasPermission(Long userId, String resourceCode, String permissionCode) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        // Admin có tất cả quyền
        if (user.hasRole("ADMIN")) {
            return true;
        }
        
        String permissionString = "PERM_" + resourceCode + "_" + permissionCode;
        
        return user.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals(permissionString) 
                           || auth.getAuthority().equals("PERM_ALL"));
    }
    
    /**
     * Kiểm tra user có quyền đọc tất cả hay chỉ đọc của mình
     */
    public boolean canReadAll(String resourceCode) {
        User currentUser = getCurrentUser();
        return hasPermission(currentUser.getId(), resourceCode, "READ_ALL");
    }
    
    /**
     * Lấy danh sách permissions của user
     */
    public List<PermissionDto> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        Set<Permission> allPermissions = new HashSet<>();
        
        // Lấy từ roles
        for (Role role : user.getRoles()) {
            allPermissions.addAll(role.getPermissions());
        }
        
        // Lấy trực tiếp (override)
        allPermissions.addAll(user.getDirectPermissions());
        
        return allPermissions.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
```

### **3.4. Custom Permission Evaluator**

```java
@Component("permissionEvaluator")
public class CustomPermissionEvaluator implements PermissionEvaluator {
    
    private final PermissionService permissionService;
    
    @Override
    public boolean hasPermission(Authentication authentication, 
                                 Object targetDomainObject, 
                                 Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        User user = (User) authentication.getPrincipal();
        
        // Admin có tất cả quyền
        if (user.hasRole("ADMIN")) {
            return true;
        }
        
        String resourceCode = (String) targetDomainObject;
        String permissionCode = (String) permission;
        
        return permissionService.hasPermission(user.getId(), resourceCode, permissionCode);
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, 
                                 Serializable targetId, 
                                 String targetType, 
                                 Object permission) {
        return hasPermission(authentication, targetType, permission);
    }
}
```

### **3.5. Cập nhật Controller**

```java
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    
    private final EmployeeService service;
    private final PermissionService permissionService;
    
    @PostMapping
    @PreAuthorize("hasPermission('EMPLOYEE', 'CREATE')")
    public ResponseEntity<?> create(@Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }
    
    @GetMapping
    @PreAuthorize("hasPermission('EMPLOYEE', 'READ_ALL') or hasPermission('EMPLOYEE', 'READ_OWN')")
    public ResponseEntity<?> list() {
        // Logic: Nếu có READ_ALL → trả về tất cả
        // Nếu chỉ có READ_OWN → trả về của chính mình
        if (permissionService.canReadAll("EMPLOYEE")) {
            return ResponseEntity.ok(service.getAll());
        } else {
            return ResponseEntity.ok(service.getMyOwn());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('EMPLOYEE', 'READ_ALL') or @permissionService.canViewEmployee(#id)")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('EMPLOYEE', 'UPDATE')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('EMPLOYEE', 'DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
```

### **3.6. Cấu hình Security**

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    
    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }
}
```

---

## 🎨 4. UI QUẢN LÝ QUYỀN (Frontend)

### **Giao diện gán quyền cho User:**

```
┌─────────────────────────────────────────────────────────┐
│  Quản lý quyền: User - manager1                         │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  Roles:                                                 │
│  ☑ ADMIN  ☐ HR  ☐ WAREHOUSE  ☑ MANAGER  ☐ STAFF        │
│                                                          │
│  Permissions (Override):                                │
│                                                          │
│  📁 HRM Module                                           │
│    ├─ Employee                                          │
│    │  ☑ Create  ☑ Read All  ☐ Read Own  ☑ Update  ☑ Delete │
│    ├─ Department                                        │
│    │  ☐ Create  ☐ Read All  ☐ Read Own  ☐ Update  ☐ Delete │
│    └─ Position                                          │
│       ☐ Create  ☐ Read All  ☐ Read Own  ☐ Update  ☐ Delete │
│                                                          │
│  📁 Warehouse Module                                    │
│    ├─ Stock Import                                       │
│    │  ☐ Create  ☐ Read All  ☐ Read Own  ☐ Update  ☐ Delete │
│    └─ Product                                           │
│       ☐ Create  ☐ Read All  ☐ Read Own  ☐ Update  ☐ Delete │
│                                                          │
│  [Save] [Cancel]                                        │
└─────────────────────────────────────────────────────────┘
```

---

## 📋 5. API QUẢN LÝ QUYỀN

### **5.1. Lấy danh sách permissions của user**

```http
GET /api/admin/users/{userId}/permissions
Authorization: Bearer <admin_token>

Response:
{
  "userId": 3,
  "username": "manager1",
  "roles": ["MANAGER"],
  "permissions": [
    {
      "resourceCode": "EMPLOYEE",
      "resourceName": "Employee",
      "permissionCode": "READ_ALL",
      "permissionName": "View All Employees",
      "source": "ROLE"  // hoặc "DIRECT"
    },
    {
      "resourceCode": "EMPLOYEE",
      "resourceName": "Employee",
      "permissionCode": "READ_OWN",
      "permissionName": "View Own Employee",
      "source": "DIRECT"
    }
  ]
}
```

### **5.2. Gán quyền cho user**

```http
POST /api/admin/users/{userId}/permissions
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

### **5.3. Gán role cho user**

```http
POST /api/admin/users/{userId}/roles
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "roleIds": [3, 4]  // MANAGER, STAFF
}
```

---

## ⚡ 6. TỐI ƯU PERFORMANCE

### **6.1. Cache Permissions**

```java
@Service
public class PermissionCacheService {
    
    @Cacheable(value = "userPermissions", key = "#userId")
    public Set<String> getUserPermissions(Long userId) {
        // Load từ database
    }
    
    @CacheEvict(value = "userPermissions", key = "#userId")
    public void evictUserPermissions(Long userId) {
        // Xóa cache khi update quyền
    }
}
```

### **6.2. Load permissions một lần khi login**

- Thay vì query mỗi request, có thể load tất cả permissions vào JWT hoặc cache
- Hoặc load vào SecurityContext khi authentication

---

## ✅ 7. KẾT LUẬN VÀ KHUYẾN NGHỊ

### **Nên làm nếu:**
- ✅ Hệ thống cần phân quyền chi tiết, linh hoạt
- ✅ Có nhiều user với quyền khác nhau
- ✅ Cần audit trail (ai gán quyền gì)
- ✅ Có thời gian phát triển UI quản lý quyền

### **Không nên làm nếu:**
- ❌ Hệ thống nhỏ, đơn giản
- ❌ Quyền cố định, không thay đổi
- ❌ Không có thời gian phát triển

### **Lộ trình triển khai:**
1. **Phase 1:** Thiết kế database, tạo entities
2. **Phase 2:** Implement PermissionService, PermissionEvaluator
3. **Phase 3:** Cập nhật Controllers sử dụng `@PreAuthorize` với permissions
4. **Phase 4:** Tạo API quản lý quyền (Admin)
5. **Phase 5:** Tạo UI quản lý quyền (Frontend)
6. **Phase 6:** Tối ưu performance (cache)

---

## 🎯 SO SÁNH VỚI CÁCH HIỆN TẠI

| Tiêu chí | Cách hiện tại (Role-based) | Cách mới (Permission-based) |
|---------|---------------------------|---------------------------|
| **Độ linh hoạt** | ⭐⭐ Thấp (role cố định) | ⭐⭐⭐⭐⭐ Cao (tick/tick) |
| **Độ phức tạp** | ⭐⭐ Đơn giản | ⭐⭐⭐⭐ Phức tạp |
| **Performance** | ⭐⭐⭐⭐ Nhanh | ⭐⭐⭐ Trung bình (cần cache) |
| **Dễ maintain** | ⭐⭐⭐⭐ Dễ | ⭐⭐⭐ Trung bình |
| **Phù hợp** | Hệ thống nhỏ | Hệ thống lớn, doanh nghiệp |

---

**Bạn có muốn tôi implement code cụ thể không?** 🚀

