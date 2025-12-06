# 🔐 MỐI LIÊN HỆ GIỮA BẢNG CÓ ROLE VÀ PHÂN QUYỀN THEO ROLE

## 📊 1. BẢNG DATABASE - NƠI LƯU TRỮ ROLE

### Bảng `users` trong Database

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,  -- ⭐ ĐÂY LÀ CỘT QUAN TRỌNG NHẤT
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME
);
```

**Giá trị có thể có trong cột `role`:**
- `ADMIN`
- `HR`
- `WAREHOUSE`
- `MANAGER`
- `STAFF`

---

## 🔄 2. FLOW PHÂN QUYỀN TỪ DATABASE ĐẾN API

### **Bước 1: User đăng nhập (Login)**

**File:** `AuthController.java` → `AuthService.java`

```java
// User gửi username + password
POST /api/auth/login
{
    "username": "admin",
    "password": "admin123"
}
```

**Quá trình:**
1. Hệ thống tìm user trong bảng `users` theo `username`
2. So sánh password (đã được hash bằng BCrypt)
3. Nếu đúng → tạo JWT token

---

### **Bước 2: Lấy Role từ Database và tạo JWT Token**

**File:** `JwtUtils.java` - Dòng 30-41

```java
public String generateJwtToken(Authentication authentication) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
    
    return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .claim("roles", userPrincipal.getAuthorities().stream()  // ⭐ LẤY ROLE TỪ USER
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}
```

**Điều quan trọng:**
- JWT token **CHỨA ROLE** trong claim `"roles"`
- Role được lấy từ `userPrincipal.getAuthorities()` → từ `User.getAuthorities()`

---

### **Bước 3: User.getAuthorities() - Chuyển đổi Role từ Database**

**File:** `User.java` - Dòng 68-70

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
}
```

**Giải thích:**
- `this.role` → Lấy từ cột `role` trong bảng `users` (ADMIN, HR, WAREHOUSE, etc.)
- `"ROLE_" + this.role.name()` → Chuyển thành `ROLE_ADMIN`, `ROLE_HR`, `ROLE_WAREHOUSE`
- Spring Security **BẮT BUỘC** phải có prefix `ROLE_` để dùng `hasRole()`

**Ví dụ:**
- Database: `role = "ADMIN"` → Spring Security: `ROLE_ADMIN`
- Database: `role = "MANAGER"` → Spring Security: `ROLE_MANAGER`

---

### **Bước 4: User gọi API với JWT Token**

```http
GET /api/employees
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### **Bước 5: AuthTokenFilter - Xác thực JWT và lấy Role**

**File:** `AuthTokenFilter.java` - Dòng 28-48

```java
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {
    try {
        String jwt = parseJwt(request);  // Lấy JWT từ header
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);  // Lấy username từ JWT
            
            // ⭐ QUAN TRỌNG: Load user từ database để lấy role
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // Tạo Authentication object với Authorities (roles)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()  // ⭐ ROLE ĐƯỢC SET VÀO ĐÂY
                    );
            
            // Set vào SecurityContext để Spring Security sử dụng
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    } catch (Exception e) {
        log.error("Cannot set user authentication: {}", e.getMessage());
    }
    
    filterChain.doFilter(request, response);
}
```

**Quá trình:**
1. Parse JWT token từ header `Authorization: Bearer ...`
2. Validate JWT token (kiểm tra signature, expiration)
3. Lấy `username` từ JWT
4. **Load user từ database** → `UserDetailsService.loadUserByUsername(username)`
5. User entity được load → gọi `User.getAuthorities()` → lấy role từ database
6. Set Authentication vào `SecurityContextHolder`

---

### **Bước 6: UserDetailsServiceImpl - Load User từ Database**

**File:** `UserDetailsServiceImpl.java` - Dòng 18-23

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // ⭐ QUERY DATABASE: SELECT * FROM users WHERE username = ?
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    
    // Tạo UserDetailsImpl với authorities từ User.getAuthorities()
    return new UserDetailsImpl(user);
}
```

**Quá trình:**
1. Query database: `SELECT * FROM users WHERE username = ?`
2. Lấy được User entity với cột `role` (ví dụ: `role = "ADMIN"`)
3. Tạo `UserDetailsImpl` → gọi `user.getAuthorities()` → chuyển `"ADMIN"` thành `"ROLE_ADMIN"`

---

### **Bước 7: @PreAuthorize kiểm tra Role**

**File:** `EmployeeController.java` - Dòng 59

```java
@GetMapping
@PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER') or hasRole('STAFF')")
public ResponseEntity<?> list() {
    return ResponseEntity.ok(service.getAll());
}
```

**Quá trình:**
1. Spring Security intercept request trước khi vào method
2. Lấy Authentication từ `SecurityContextHolder.getContext().getAuthentication()`
3. Lấy Authorities từ Authentication: `authentication.getAuthorities()`
4. Kiểm tra: `hasRole('ADMIN')` → tìm `ROLE_ADMIN` trong authorities
5. Nếu có → cho phép truy cập
6. Nếu không → trả về **403 Forbidden**

---

## 🔗 3. SƠ ĐỒ MỐI LIÊN HỆ

```
┌─────────────────────────────────────────────────────────────────┐
│                    DATABASE (MySQL)                             │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Table: users                                            │  │
│  │  ┌────┬──────────┬──────────┬──────────┐                │  │
│  │  │ id │ username │ password │  role    │                │  │
│  │  ├────┼──────────┼──────────┼──────────┤                │  │
│  │  │ 1  │ admin    │ $2a$...  │ ADMIN    │ ⭐ LƯU TRỮ     │  │
│  │  │ 2  │ hr_user  │ $2a$...  │ HR       │    ROLE       │  │
│  │  │ 3  │ manager1 │ $2a$...  │ MANAGER  │    Ở ĐÂY      │  │
│  │  └────┴──────────┴──────────┴──────────┘                │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ SELECT * FROM users WHERE username = ?
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│              User Entity (User.java)                            │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  @Entity                                                  │  │
│  │  @Table(name = "users")                                  │  │
│  │  public class User {                                     │  │
│  │      private Role role;  // ⭐ LẤY TỪ DATABASE         │  │
│  │                                                           │  │
│  │      @Override                                           │  │
│  │      public Collection<GrantedAuthority> getAuthorities()│  │
│  │          return List.of(                                 │  │
│  │              new SimpleGrantedAuthority(                 │  │
│  │                  "ROLE_" + this.role.name()  ⭐ CHUYỂN   │  │
│  │              )                                           │  │
│  │          );                                              │  │
│  │      }                                                    │  │
│  │  }                                                       │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ user.getAuthorities()
                            │ → "ROLE_ADMIN"
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│         JWT Token (JwtUtils.java)                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  {                                                        │  │
│  │    "sub": "admin",                                        │  │
│  │    "roles": ["ROLE_ADMIN"],  ⭐ ĐƯỢC GHI VÀO JWT        │  │
│  │    "exp": 1234567890                                      │  │
│  │  }                                                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ Client gửi JWT trong header
                            │ Authorization: Bearer <token>
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│         AuthTokenFilter (Mỗi request)                           │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  1. Parse JWT từ header                                  │  │
│  │  2. Validate JWT                                         │  │
│  │  3. Lấy username từ JWT                                 │  │
│  │  4. Load User từ database (lại query DB)                │  │
│  │  5. User.getAuthorities() → ["ROLE_ADMIN"]               │  │
│  │  6. Set vào SecurityContext                             │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ SecurityContext.getAuthentication()
                            │ .getAuthorities() = ["ROLE_ADMIN"]
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│         @PreAuthorize Check (EmployeeController)                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")      │  │
│  │                                                           │  │
│  │  Spring Security kiểm tra:                               │  │
│  │  - Authorities có chứa "ROLE_ADMIN"?                     │  │
│  │  - Nếu có → ✅ Cho phép                                   │  │
│  │  - Nếu không → ❌ 403 Forbidden                           │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📝 4. TÓM TẮT MỐI LIÊN HỆ

### **Mối liên hệ chính:**

1. **Database (`users.role`)** 
   - ⬇️ Lưu trữ role dạng string: `"ADMIN"`, `"HR"`, `"MANAGER"`, `"STAFF"`

2. **User Entity (`User.java`)**
   - ⬇️ Map cột `role` từ database vào field `private Role role`
   - ⬇️ Method `getAuthorities()` chuyển `"ADMIN"` → `"ROLE_ADMIN"`

3. **JWT Token (`JwtUtils.java`)**
   - ⬇️ Ghi role vào JWT claim: `"roles": ["ROLE_ADMIN"]`
   - ⬇️ JWT được gửi về client

4. **AuthTokenFilter (Mỗi request)**
   - ⬇️ Parse JWT từ header
   - ⬇️ Load User từ database (lại query để lấy role mới nhất)
   - ⬇️ Set Authorities vào SecurityContext

5. **@PreAuthorize (Controller)**
   - ⬇️ Kiểm tra role từ SecurityContext
   - ⬇️ Quyết định cho phép hay từ chối request

---

## ⚠️ 5. LƯU Ý QUAN TRỌNG

### **1. Role được load từ Database MỖI REQUEST**

- Mỗi khi user gọi API, `AuthTokenFilter` sẽ **query database** để load User và lấy role
- **Lý do:** Đảm bảo role luôn được cập nhật (nếu admin thay đổi role của user)
- JWT chỉ chứa username, không chứa role trực tiếp (hoặc có nhưng không dùng)

### **2. Prefix "ROLE_" là bắt buộc**

- Database: `role = "ADMIN"`
- Spring Security: `"ROLE_ADMIN"`
- `@PreAuthorize("hasRole('ADMIN')")` → tìm `"ROLE_ADMIN"` trong authorities

### **3. Enum Role phải khớp với Database**

- Enum `Role` trong `User.java` phải khớp với giá trị trong database
- Nếu database có `"MANAGER"` nhưng enum không có → sẽ lỗi

### **4. Cách thêm Role mới:**

1. Thêm vào Enum `User.Role`: `MANAGER, STAFF`
2. Update database: `ALTER TABLE users MODIFY role ENUM('ADMIN','HR','WAREHOUSE','MANAGER','STAFF')`
3. Thêm vào `@PreAuthorize`: `hasRole('MANAGER')`

---

## 🎯 6. VÍ DỤ CỤ THỂ

### **Scenario: User có role MANAGER truy cập `/api/employees`**

```
1. Database:
   users table: id=3, username="manager1", role="MANAGER"

2. User đăng nhập:
   POST /api/auth/login → Nhận JWT token

3. User gọi API:
   GET /api/employees
   Header: Authorization: Bearer <jwt_token>

4. AuthTokenFilter:
   - Parse JWT → username = "manager1"
   - Query DB: SELECT * FROM users WHERE username = "manager1"
   - Lấy được: role = "MANAGER"
   - User.getAuthorities() → ["ROLE_MANAGER"]
   - Set vào SecurityContext

5. EmployeeController:
   @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER') or hasRole('STAFF')")
   - Kiểm tra: Authorities có "ROLE_MANAGER"? → ✅ CÓ
   - Cho phép truy cập → Trả về danh sách employees
```

### **Scenario: User có role STAFF truy cập `/api/departments`**

```
1. Database:
   users table: id=4, username="staff1", role="STAFF"

2. User gọi API:
   GET /api/departments
   Header: Authorization: Bearer <jwt_token>

3. AuthTokenFilter:
   - Load User → role = "STAFF"
   - Authorities = ["ROLE_STAFF"]

4. DepartmentController:
   @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
   - Kiểm tra: Authorities có "ROLE_ADMIN" hoặc "ROLE_HR"? → ❌ KHÔNG
   - Từ chối → Trả về 403 Forbidden
```

---

## ✅ KẾT LUẬN

**Mối liên hệ giữa bảng có role và phân quyền:**

1. **Database là nguồn gốc:** Role được lưu trong bảng `users`, cột `role`
2. **User Entity là cầu nối:** Map từ database → Java object, chuyển đổi format
3. **JWT Token là phương tiện:** Chứa username để xác định user
4. **AuthTokenFilter là người gác cổng:** Load role từ database mỗi request
5. **@PreAuthorize là người kiểm tra:** So sánh role với quyền được phép

**Quan trọng:** Role **LUÔN ĐƯỢC LOAD TỪ DATABASE** mỗi request, không phụ thuộc vào JWT token!

