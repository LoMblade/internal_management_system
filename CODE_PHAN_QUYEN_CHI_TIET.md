# 📍 VỊ TRÍ CODE THỰC HIỆN PHÂN QUYỀN

## 🔍 CÁC ĐOẠN CODE CỤ THỂ

### **Bước 1: User gọi API với JWT token**

**Không có code server-side** - Đây là hành động từ client (Postman, Browser, etc.)

Client gửi request:
```http
GET /api/employees
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### **Bước 2: AuthTokenFilter parse JWT → lấy username**

**File:** `src/main/java/com/example/internal_management_system/security/jwt/AuthTokenFilter.java`

#### **2.1. Parse JWT từ Header**

**Dòng 50-58:**
```java
private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");  // ⭐ Lấy header "Authorization"
    
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
        return headerAuth.substring(7);  // ⭐ Cắt bỏ "Bearer " để lấy token
    }
    
    return null;
}
```

**Giải thích:**
- Lấy header `Authorization: Bearer <token>`
- Cắt bỏ `"Bearer "` để lấy JWT token thuần

--- 

#### **2.2. Validate JWT và lấy username**

**Dòng 28-48:**
```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {
    try {
        // ⭐ BƯỚC 1: Parse JWT từ header
        String jwt = parseJwt(request);  // Dòng 32
        
        // ⭐ BƯỚC 2: Validate JWT token (kiểm tra signature, expiration)
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {  // Dòng 33
            
            // ⭐ BƯỚC 3: Lấy username từ JWT token
            String username = jwtUtils.getUserNameFromJwtToken(jwt);  // Dòng 34
            
            // ⭐ BƯỚC 4: Load User từ database (sẽ giải thích ở dưới)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // Dòng 36
            
            // ⭐ BƯỚC 5: Tạo Authentication object với Authorities (roles)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()  // ⭐ Lấy roles từ đây
                    );
            
            // ⭐ BƯỚC 6: Set vào SecurityContext để Spring Security sử dụng
            SecurityContextHolder.getContext().setAuthentication(authentication);  // Dòng 41
        }
    } catch (Exception e) {
        log.error("Cannot set user authentication: {}", e.getMessage());
    }
    
    filterChain.doFilter(request, response);
}
```

**Dòng 34:** `jwtUtils.getUserNameFromJwtToken(jwt)` - Lấy username từ JWT

---

#### **2.3. JwtUtils.getUserNameFromJwtToken() - Lấy username từ JWT**

**File:** `src/main/java/com/example/internal_management_system/security/jwt/JwtUtils.java`

**Dòng 53-60:**
```java
public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())  // ⭐ Dùng secret key để verify
            .build()
            .parseClaimsJws(token)  // ⭐ Parse JWT token
            .getBody()
            .getSubject();  // ⭐ Lấy "sub" claim (username) từ JWT
}
```

**Giải thích:**
- Parse JWT token
- Lấy claim `"sub"` (subject) - đây là username được lưu khi tạo JWT

---

### **Bước 3: Query Database: SELECT * FROM users WHERE username = ?**

**File:** `src/main/java/com/example/internal_management_system/security/service/UserDetailsServiceImpl.java`

**Dòng 18-23:**
```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // ⭐ QUERY DATABASE: SELECT * FROM users WHERE username = ?
    User user = userRepository.findByUsername(username)  // Dòng 19
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    
    // ⭐ Tạo UserDetailsImpl với User entity (có chứa role)
    return new UserDetailsImpl(user);  // Dòng 22
}
```

**Dòng 19:** `userRepository.findByUsername(username)` - Query database

---

#### **3.1. UserRepository - Interface định nghĩa query**

**File:** `src/main/java/com/example/internal_management_system/security/repository/UserRepository.java`

**Dòng 10-12:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);  // ⭐ Spring Data JPA tự động tạo query
}
```

**Giải thích:**
- Spring Data JPA tự động tạo query: `SELECT * FROM users WHERE username = ?`
- Trả về `Optional<User>` - có thể có hoặc không có user

---

### **Bước 4: Load User entity → lấy role từ database**

#### **4.1. User Entity - Chứa role từ database**

**File:** `src/main/java/com/example/internal_management_system/security/model/User.java`

**Dòng 35-37:**
```java
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Role role;  // ⭐ Role được map từ cột "role" trong database
```

**Dòng 48-51:**
```java
public enum Role {
    ADMIN, HR, WAREHOUSE,
    MANAGER, STAFF  // ⭐ Các giá trị role có thể có
}
```

---

#### **4.2. User.getAuthorities() - Chuyển role thành Spring Security Authority**

**File:** `src/main/java/com/example/internal_management_system/security/model/User.java`

**Dòng 68-70:**
```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));  // ⭐ CHUYỂN ĐỔI
}
```

**Giải thích:**
- `this.role` → Lấy từ database (ví dụ: `Role.ADMIN`)
- `this.role.name()` → Chuyển thành string: `"ADMIN"`
- `"ROLE_" + this.role.name()` → Thêm prefix: `"ROLE_ADMIN"`
- `SimpleGrantedAuthority` → Tạo authority object cho Spring Security

**Ví dụ:**
- Database: `role = "ADMIN"` → `getAuthorities()` → `["ROLE_ADMIN"]`
- Database: `role = "MANAGER"` → `getAuthorities()` → `["ROLE_MANAGER"]`

---

#### **4.3. UserDetailsImpl - Wrap User entity với Authorities**

**File:** `src/main/java/com/example/internal_management_system/security/service/UserDetailsServiceImpl.java`

**Dòng 26-38:**
```java
public static class UserDetailsImpl extends org.springframework.security.core.userdetails.User {
    
    private final Long id;
    private final String email;
    
    public UserDetailsImpl(User user) {
        super(user.getUsername(),
                user.getPassword(),
                user.isEnabled(), true, true, true,
                user.getAuthorities());  // ⭐ GỌI user.getAuthorities() → LẤY ROLE
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
```

**Dòng 35:** `user.getAuthorities()` → Gọi method ở User.java để lấy role

---

## 📊 TÓM TẮT VỊ TRÍ CODE

| Bước | File | Dòng | Code |
|------|------|------|------|
| **1. Parse JWT từ header** | `AuthTokenFilter.java` | 32 | `String jwt = parseJwt(request);` |
| **2. Parse JWT method** | `AuthTokenFilter.java` | 50-58 | `parseJwt()` method |
| **3. Validate JWT** | `AuthTokenFilter.java` | 33 | `jwtUtils.validateJwtToken(jwt)` |
| **4. Lấy username từ JWT** | `AuthTokenFilter.java` | 34 | `jwtUtils.getUserNameFromJwtToken(jwt)` |
| **5. Lấy username từ JWT (chi tiết)** | `JwtUtils.java` | 53-60 | `getUserNameFromJwtToken()` |
| **6. Query database** | `UserDetailsServiceImpl.java` | 19 | `userRepository.findByUsername(username)` |
| **7. Repository interface** | `UserRepository.java` | 12 | `findByUsername(String username)` |
| **8. Load User entity** | `UserDetailsServiceImpl.java` | 19-22 | `loadUserByUsername()` |
| **9. Lấy role từ User** | `User.java` | 35-37 | `private Role role;` |
| **10. Chuyển role thành Authority** | `User.java` | 68-70 | `getAuthorities()` |
| **11. Set vào SecurityContext** | `AuthTokenFilter.java` | 41 | `SecurityContextHolder.getContext().setAuthentication()` |

---

## 🔄 FLOW CODE CHI TIẾT

```
1. Client gửi request:
   GET /api/employees
   Authorization: Bearer <token>
   
   ↓
   
2. AuthTokenFilter.doFilterInternal() - Dòng 28
   ├─ parseJwt(request) - Dòng 32, 50-58
   │  └─ request.getHeader("Authorization") - Dòng 51
   │  └─ headerAuth.substring(7) - Dòng 54
   │
   ├─ jwtUtils.validateJwtToken(jwt) - Dòng 33
   │  └─ JwtUtils.validateJwtToken() - Dòng 62-79
   │
   ├─ jwtUtils.getUserNameFromJwtToken(jwt) - Dòng 34
   │  └─ JwtUtils.getUserNameFromJwtToken() - Dòng 53-60
   │     └─ parseClaimsJws(token).getBody().getSubject() - Dòng 57-59
   │
   ├─ userDetailsService.loadUserByUsername(username) - Dòng 36
   │  └─ UserDetailsServiceImpl.loadUserByUsername() - Dòng 18-23
   │     ├─ userRepository.findByUsername(username) - Dòng 19
   │     │  └─ UserRepository.findByUsername() - Dòng 12
   │     │     └─ SELECT * FROM users WHERE username = ? (Spring Data JPA tự động)
   │     │
   │     └─ new UserDetailsImpl(user) - Dòng 22
   │        └─ UserDetailsImpl constructor - Dòng 31-38
   │           └─ user.getAuthorities() - Dòng 35
   │              └─ User.getAuthorities() - Dòng 68-70
   │                 └─ "ROLE_" + this.role.name() - Dòng 70
   │                    └─ this.role - Dòng 37 (từ database)
   │
   └─ SecurityContextHolder.getContext().setAuthentication() - Dòng 41
```

---

## ✅ KẾT LUẬN

**Các file quan trọng:**

1. **`AuthTokenFilter.java`** - Filter chính, xử lý mọi request
2. **`JwtUtils.java`** - Parse và validate JWT token
3. **`UserDetailsServiceImpl.java`** - Load user từ database
4. **`UserRepository.java`** - Interface query database
5. **`User.java`** - Entity chứa role và method `getAuthorities()`

**Flow chính:**
- **Dòng 32** (`AuthTokenFilter`): Parse JWT
- **Dòng 34** (`AuthTokenFilter`): Lấy username từ JWT
- **Dòng 36** (`AuthTokenFilter`): Load user từ database
- **Dòng 19** (`UserDetailsServiceImpl`): Query database
- **Dòng 70** (`User.java`): Chuyển role thành Authority
- **Dòng 41** (`AuthTokenFilter`): Set vào SecurityContext

