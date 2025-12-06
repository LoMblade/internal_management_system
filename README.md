git clone https://github.com/LoMblade/internal_management_system.git
----
# Internal Management System

Hệ thống quản lý nội bộ (mini), bao gồm quản lý nhân sự (HRM) và quản lý kho hàng (Warehouse).

##Mục lục

- [Tổng quan](#tổng-quan)
- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [Hệ thống phân quyền](#hệ-thống-phân-quyền)
- [API Documentation](#api-documentation)
- [Testing với Postman](#testing-với-postman)
- [Testing với cURL](#testing-với-curl)
- [Git Workflow](#git-workflow)
- [Contributing](#contributing)

##Tổng quan

Internal Management System là một ứng dụng web REST API được xây dựng bằng Spring Boot, cung cấp giải pháp toàn diện cho việc quản lý nhân sự và kho hàng của doanh nghiệp. Hệ thống hỗ trợ xác thực JWT và **phân quyền linh hoạt theo Permission-Based Access Control (PBAC)**, cho phép Admin quản lý quyền chi tiết cho từng user.

### 🏢 Mô-đun HRM (Human Resource Management)
- Quản lý phòng ban và vị trí công việc
- Quản lý thông tin nhân viên
- Theo dõi điểm danh và chấm công
- Quản lý bảng lương và phúc lợi

### 📦 Mô-đun Warehouse (Quản lý kho hàng)
- Quản lý danh mục sản phẩm
- Quản lý thông tin sản phẩm
- Quản lý kho và vị trí lưu trữ
- Quản lý tồn kho theo thời gian thực
- Theo dõi phiếu nhập/xuất kho

## ✨ Tính năng

### 🎯 Phân Quyền Linh Hoạt (Permission-Based)
- **Admin** có thể gán quyền chi tiết cho từng user
- Phân quyền theo từng bảng (EMPLOYEE, DEPARTMENT, STOCK_IMPORT, etc.)
- Phân quyền theo từng chức năng (CREATE, READ_ALL, READ_OWN, UPDATE, DELETE)
- Hỗ trợ nhiều roles: ADMIN, HR, WAREHOUSE, MANAGER, STAFF
- User có thể có nhiều roles và permissions trực tiếp (override)
- API quản lý quyền: `/api/admin/permissions/**`

### 🔐 Authentication & Authorization
- Đăng ký và đăng nhập người dùng
- JWT Token-based authentication
- **Permission-Based Access Control (PBAC)** - Phân quyền linh hoạt
- Admin có thể gán quyền chi tiết cho từng user (tick/tick)
- Hỗ trợ nhiều roles: ADMIN, HR, WAREHOUSE, MANAGER, STAFF
- Phân quyền theo từng bảng, từng chức năng (CREATE, READ_ALL, READ_OWN, UPDATE, DELETE)
- Password encryption với BCrypt

### 👥 HRM Features
- ✅ CRUD Department (Phòng ban)
- ✅ CRUD Position (Vị trí công việc)
- ✅ CRUD Employee (Nhân viên)
- ✅ CRUD Attendance (Điểm danh)
- ✅ CRUD Payroll (Bảng lương)

### 📦 Warehouse Features
- ✅ CRUD Category (Danh mục sản phẩm)
- ✅ CRUD Product (Sản phẩm)
- ✅ CRUD Warehouse (Kho hàng)
- ✅ CRUD Inventory (Tồn kho)
- ✅ CRUD Stock Import (Phiếu nhập kho)
- ✅ CRUD Stock Export (Phiếu xuất kho)

### 🛠️ Technical Features
- RESTful API design
- Global exception handling
- Input validation
- Pagination support
- CORS configuration
- Swagger/OpenAPI documentation ready
- Docker support ready

## 🛠️ Công nghệ sử dụng

### Backend
- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Security 6**
- **Spring Data JPA**
- **JWT (JSON Web Token)**
- **MySQL 8**
- **MapStruct** (Object mapping)
- **Lombok** (Code generation)
- **Maven** (Build tool)

### Database
- **MySQL 8.0+**
- **Hibernate ORM**
- **HikariCP** (Connection pooling)

### Tools & Libraries
- **Postman** (API Testing)
- **Git** (Version Control)
- **Jackson** (JSON processing)

## 📁 Cấu trúc dự án

```
internal_management_system/
├── src/main/java/com/example/internal_management_system/
│   ├── config/                    # Configuration classes
│   │   ├── ApplicationConfig.java
│   │   ├── CorsConfig.java
│   │   ├── JacksonConfig.java
│   │   └── JpaConfig.java
│   ├── common/                    # Common utilities
│   │   ├── constants/
│   │   │   └── AppConstants.java
│   │   ├── dto/
│   │   │   ├── ApiResponse.java
│   │   │   ├── BaseDto.java
│   │   │   └── PageResponse.java
│   │   ├── exceptions/
│   │   │   ├── BusinessException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── ValidationException.java
│   ├── modules/
│   │   ├── hrm/                   # Human Resource Management
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── warehouse/             # Warehouse Management
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── mapper/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── security/                  # Security & Authentication
│       ├── config/
│       ├── controller/
│       │   └── AdminPermissionController.java  # Quản lý quyền
│       ├── dto/
│       │   ├── PermissionDto.java
│       │   ├── UserPermissionDto.java
│       │   ├── AssignPermissionRequest.java
│       │   └── AssignRoleRequest.java
│       ├── evaluator/
│       │   └── CustomPermissionEvaluator.java  # Permission evaluator
│       ├── jwt/
│       ├── model/
│       │   ├── User.java
│       │   ├── Role.java          # Role entity
│       │   ├── Permission.java    # Permission entity
│       │   ├── Resource.java      # Resource entity
│       │   └── Module.java        # Module entity
│       ├── repository/
│       │   ├── RoleRepository.java
│       │   ├── PermissionRepository.java
│       │   ├── ResourceRepository.java
│       │   └── ModuleRepository.java
│       └── service/
│           ├── PermissionService.java  # Service quản lý permissions
│           └── SecurityService.java
├── src/main/resources/
│   ├── application.properties     # Application configuration
│   └── static/                    # Static resources
├── src/test/                      # Test classes
├── target/                        # Build output
├── pom.xml                        # Maven configuration
├── postman_collection.json        # Postman collection
├── postman_test.json             # Postman test collection
├── database_migration.sql         # Database migration script
├── api_test_commands.sh          # cURL test commands
├── CURL_TEST_PHAN_QUYEN.txt     # cURL commands để test phân quyền
├── VI_TRI_PHAN_QUYEN.md         # Tài liệu vị trí phân quyền
└── README.md
```

## 🚀 Cài đặt và chạy

### Prerequisites
- **Java 21** hoặc cao hơn
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### 1. Clone repository
```bash
git clone https://github.com/LoMblade/internal_management_system.git
cd internal_management_system
```

### 2. Cấu hình Database
Tạo database MySQL:
```sql
CREATE DATABASE qlnb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Cập nhật cấu hình trong `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/qlnb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
```

### 2.1. Chạy Database Migration (QUAN TRỌNG)
Chạy script migration để tạo các bảng phân quyền:
```bash
mysql -u root -p qlnb < database_migration.sql
```

Hoặc import file `database_migration.sql` vào MySQL Workbench/phpMyAdmin.

**Lưu ý:** Script này sẽ tạo các bảng:
- `roles`, `modules`, `resources`, `permissions`
- `user_roles`, `role_permissions`, `user_permissions`
- Và insert data mẫu (roles, modules, resources, permissions)

### 3. Build và chạy ứng dụng
```bash
# Build project
mvn clean compile

# Chạy ứng dụng
mvn spring-boot:run
```

### 4. Kiểm tra
Ứng dụng sẽ chạy trên: `http://localhost:8080`

## 🔐 Hệ thống phân quyền

### Tổng quan
Hệ thống sử dụng **Permission-Based Access Control (PBAC)** cho phép Admin quản lý quyền chi tiết cho từng user. Thay vì chỉ có roles cố định, Admin có thể "tick/tick" permissions cho từng user.

### Cấu trúc phân quyền
- **Module**: Mô-đun chính (HRM, WAREHOUSE)
- **Resource**: Bảng/tài nguyên cụ thể (EMPLOYEE, DEPARTMENT, STOCK_IMPORT, etc.)
- **Permission**: Quyền cụ thể (CREATE, READ_ALL, READ_OWN, UPDATE, DELETE)
- **Role**: Nhóm quyền có thể gán cho nhiều user (ADMIN, HR, WAREHOUSE, MANAGER, STAFF)

### Cách sử dụng

#### 1. Admin gán quyền cho User
```bash
# Gán permissions trực tiếp cho user
POST /api/admin/permissions/users/{userId}/permissions
{
  "permissions": [
    {"resourceCode": "EMPLOYEE", "permissionCode": "CREATE"},
    {"resourceCode": "EMPLOYEE", "permissionCode": "READ_ALL"},
    {"resourceCode": "EMPLOYEE", "permissionCode": "UPDATE"},
    {"resourceCode": "EMPLOYEE", "permissionCode": "DELETE"}
  ]
}

# Hoặc gán role (role đã có sẵn permissions)
POST /api/admin/permissions/users/{userId}/roles
{
  "roleIds": [1, 2]
}
```

#### 2. Xem quyền của User
```bash
GET /api/admin/permissions/users/{userId}
```

#### 3. Ví dụ: Tạo Trưởng phòng (Manager)
- Có quyền CRUD đầy đủ trên bảng `EMPLOYEE`
- Không có quyền trên các bảng khác

#### 4. Ví dụ: Tạo Nhân viên (Staff)
- Chỉ có quyền `READ_OWN` trên bảng `EMPLOYEE` (xem thông tin của chính mình)
- Không có quyền trên các bảng khác

### Tài liệu tham khảo
- Xem file `VI_TRI_PHAN_QUYEN.md` để biết code phân quyền ở đâu
- Xem file `CURL_TEST_PHAN_QUYEN.txt` để có các lệnh curl test phân quyền
- Xem file `HUONG_DAN_SU_DUNG.md` (nếu có) để có hướng dẫn chi tiết

## 📚 API Documentation

### 🔐 Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "admin",
  "email": "admin@company.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 👥 HRM Endpoints

#### Departments
```http
GET    /api/departments          # Cần: DEPARTMENT_READ_ALL
POST   /api/departments          # Cần: DEPARTMENT_CREATE
GET    /api/departments/{id}     # Cần: DEPARTMENT_READ_ALL
PUT    /api/departments/{id}     # Cần: DEPARTMENT_UPDATE
DELETE /api/departments/{id}     # Cần: DEPARTMENT_DELETE
```

#### Employees
```http
GET    /api/employees            # Cần: EMPLOYEE_READ_ALL hoặc EMPLOYEE_READ_OWN
POST   /api/employees            # Cần: EMPLOYEE_CREATE
GET    /api/employees/{id}       # Cần: EMPLOYEE_READ_ALL hoặc canViewEmployee(id)
PUT    /api/employees/{id}       # Cần: EMPLOYEE_UPDATE
DELETE /api/employees/{id}      # Cần: EMPLOYEE_DELETE
GET    /api/employees/filtered   # Cần: EMPLOYEE_READ_ALL hoặc EMPLOYEE_READ_OWN
```

### 🔐 Admin Permission Management Endpoints

#### Quản lý quyền cho User
```http
GET    /api/admin/permissions/users/{userId}              # Xem quyền của user
POST   /api/admin/permissions/users/{userId}/roles         # Gán roles cho user
POST   /api/admin/permissions/users/{userId}/permissions   # Gán permissions cho user
GET    /api/admin/permissions/roles                       # Lấy tất cả roles
GET    /api/admin/permissions/permissions                  # Lấy tất cả permissions
GET    /api/admin/permissions/resources/{resourceCode}/permissions  # Lấy permissions theo resource
```

**Lưu ý:** Tất cả endpoints `/api/admin/permissions/**` chỉ ADMIN mới được truy cập.

### 📦 Warehouse Endpoints

#### Categories
```http
GET    /api/categories
POST   /api/categories
GET    /api/categories/{id}
PUT    /api/categories/{id}
DELETE /api/categories/{id}
```

#### Products
```http
GET    /api/products
POST   /api/products
GET    /api/products/{id}
PUT    /api/products/{id}
DELETE /api/products/{id}
```

## 🧪 Testing với Postman

### Import Collection
1. Mở **Postman**
2. Click **Import** (top left)
3. Chọn **File**
4. Import file `postman_test.json` hoặc `postman_collection.json`
5. Collection sẽ xuất hiện

### Sử dụng Collection
1. **Bước 1**: Chạy database migration (`database_migration.sql`)
2. **Bước 2**: Chạy request **"Register Admin"** để tạo admin
3. **Bước 3**: Chạy request **"Login Admin"** để lấy JWT token (tự động lưu vào environment)
4. **Bước 4**: Register và gán quyền cho user mới (dùng AdminPermissionController)
5. **Bước 5**: Test các API với permissions đã gán

### Test Phân Quyền
Xem file `CURL_TEST_PHAN_QUYEN.txt` để có các lệnh curl test phân quyền chi tiết.

## 🌐 Testing với cURL

### Bước 1: Register Admin User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@company.com",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

### Bước 2: Login (Lấy JWT Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Bước 3: Admin gán quyền cho User

**Ví dụ: Gán quyền CRUD Employee cho Trưởng phòng**
```bash
# Thay {userId} bằng ID của user, {adminToken} bằng token của admin
curl -X POST http://localhost:8080/api/admin/permissions/users/{userId}/permissions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {adminToken}" \
  -d '{
    "permissions": [
      {"resourceCode": "EMPLOYEE", "permissionCode": "CREATE"},
      {"resourceCode": "EMPLOYEE", "permissionCode": "READ_ALL"},
      {"resourceCode": "EMPLOYEE", "permissionCode": "UPDATE"},
      {"resourceCode": "EMPLOYEE", "permissionCode": "DELETE"}
    ]
  }'
```

**Ví dụ: Gán quyền READ_OWN cho Nhân viên**
```bash
curl -X POST http://localhost:8080/api/admin/permissions/users/{userId}/permissions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {adminToken}" \
  -d '{
    "permissions": [
      {"resourceCode": "EMPLOYEE", "permissionCode": "READ_OWN"}
    ]
  }'
```

### Bước 4: Test API với JWT Token
```bash
# Thay YOUR_JWT_TOKEN_HERE bằng token từ response login
TOKEN="YOUR_JWT_TOKEN_HERE"

# Tạo Department (cần DEPARTMENT_CREATE permission)
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Information Technology",
    "description": "IT Department responsible for technology infrastructure"
  }'

# Tạo Employee (cần EMPLOYEE_CREATE permission)
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "employeeCode": "EMP001",
    "code": "EMP001",
    "firstName": "Nguyen",
    "lastName": "Van A",
    "email": "nguyenvana@company.com",
    "phone": "0123456789",
    "hireDate": "2023-01-01",
    "departmentId": 1,
    "positionId": 1,
    "salary": 12000000
  }'
```

### Bước 5: Test Warehouse APIs
```bash
# Tạo Category
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and components"
  }'

# Tạo Product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "productCode": "LAP001",
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop",
    "categoryId": 1,
    "unitPrice": 25000000,
    "unitOfMeasure": "piece",
    "minStockLevel": 5,
    "maxStockLevel": 100
  }'
```

### Bước 6: Test Các API Khác
```bash
# Get all departments
curl -X GET http://localhost:8080/api/departments \
  -H "Authorization: Bearer $TOKEN"

# Get employee by ID
curl -X GET http://localhost:8080/api/employees/1 \
  -H "Authorization: Bearer $TOKEN"

# Update employee
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "Nguyen",
    "lastName": "Van A",
    "email": "nguyenvana@company.com",
    "salary": 15000000
  }'

# Delete employee
curl -X DELETE http://localhost:8080/api/employees/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 🔄 Git Workflow

### 1. Clone Repository
```bash
git clone <repository-url>
cd internal_management_system
```

### 2. Create Feature Branch
```bash
# Tạo branch mới cho feature
git checkout -b feature/your-feature-name

# Hoặc cho bug fix
git checkout -b bugfix/issue-description
```

### 3. Development Workflow
```bash
# Kiểm tra status
git status

# Add files
git add .

# Commit changes
git commit -m "feat: add new employee management feature"

# Push to remote
git push origin feature/your-feature-name
```

### 4. Pull Request Process
1. Tạo Pull Request trên GitHub/GitLab
2. Mô tả chi tiết thay đổi
3. Request review từ team members
4. Merge sau khi approved

### 5. Commit Message Convention
```
feat: add new feature
fix: bug fix
docs: documentation update
style: code formatting
refactor: code refactoring
test: add tests
chore: maintenance tasks
```

### 6. Branch Naming
```
feature/add-user-authentication
bugfix/fix-login-validation
hotfix/critical-security-patch
release/v1.2.0
```

## 🤝 Contributing

### Development Setup
1. Fork repository
2. Clone your fork: `git clone <your-fork-url>`
3. Create feature branch: `git checkout -b feature/amazing-feature`
4. Make changes and test thoroughly
5. Commit changes: `git commit -m 'feat: add amazing feature'`
6. Push to branch: `git push origin feature/amazing-feature`
7. Create Pull Request

### Code Standards
- Sử dụng **Java 21** features
- Follow **Spring Boot** best practices
- Implement proper **error handling**
- Add **unit tests** cho business logic
- Use **MapStruct** cho object mapping
- Follow **REST API** conventions
- Document APIs with **OpenAPI/Swagger**

### Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run with coverage
mvn test jacoco:report
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Blade** - *Intern* - [LoMblade]

## 🙏 Acknowledgments

- Spring Boot Team
- MySQL Community
- Open source contributors
