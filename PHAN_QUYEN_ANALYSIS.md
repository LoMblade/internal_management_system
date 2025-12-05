# 📋 PHÂN TÍCH PHÂN QUYỀN THEO ROLE TRONG PROJECT

## 🔐 1. CẤU HÌNH SECURITY TỔNG THỂ

### File: `WebSecurityConfig.java`

**Vị trí:** `src/main/java/com/example/internal_management_system/security/config/WebSecurityConfig.java`

**Cấu hình:**
```java
@EnableMethodSecurity(prePostEnabled = true)  // Dòng 22
```

**Ý nghĩa:**
- Bật tính năng `@PreAuthorize` và `@PostAuthorize` để phân quyền ở method level
- Cho phép sử dụng annotation `@PreAuthorize` trên các controller methods

**HTTP Security Configuration:**
```java
auth.requestMatchers("/api/auth/**").permitAll()      // Không cần authentication
    .requestMatchers("/api/test/**").permitAll()      // Không cần authentication
    .anyRequest().authenticated()                      // Tất cả request khác cần đăng nhập
```

**Kết luận:**
- ✅ Endpoints `/api/auth/**` và `/api/test/**` không cần đăng nhập
- ✅ Tất cả endpoints khác **BẮT BUỘC** phải có JWT token (authenticated)
- ✅ Phân quyền theo role được thực hiện bằng `@PreAuthorize` trên từng endpoint

---

## 👥 2. ĐỊNH NGHĨA ROLES

### File: `User.java`

**Vị trí:** `src/main/java/com/example/internal_management_system/security/model/User.java`

**Enum Role:**
```java
public enum Role {
    ADMIN, HR, WAREHOUSE
}
```

**Method getAuthorities():**
```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
}
```

**Ý nghĩa:**
- User có 3 roles: `ADMIN`, `HR`, `WAREHOUSE`
- Spring Security yêu cầu prefix `ROLE_` → `ROLE_ADMIN`, `ROLE_HR`, `ROLE_WAREHOUSE`
- Method này được gọi khi user đăng nhập để set authorities vào SecurityContext

---

## 📊 3. PHÂN QUYỀN CHI TIẾT THEO TỪNG MODULE

### 🏢 MODULE HRM (Quản lý nhân sự)

#### ✅ 3.1. EmployeeController - CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/hrm/controller/EmployeeController.java`

| Endpoint | Method | Phân quyền | Vị trí code |
|----------|--------|------------|------------|
| `/api/employees` | POST | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 31 |
| `/api/employees/{id}` | PUT | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 40 |
| `/api/employees/{id}` | DELETE | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 49 |
| `/api/employees` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 59 |
| `/api/employees/filtered` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 70 |
| `/api/employees/{id}` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 79 |

**Kết luận:**
- ✅ **ADMIN**: Có đầy đủ quyền (Create, Read, Update, Delete)
- ✅ **HR**: Có đầy đủ quyền (Create, Read, Update, Delete)
- ❌ **WAREHOUSE**: Không có quyền truy cập (sẽ bị 403 Forbidden)

---

#### ✅ 3.2. DepartmentController - CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/hrm/controller/DepartmentController.java`

| Endpoint | Method | Phân quyền | Vị trí code |
|----------|--------|------------|------------|
| `/api/departments` | POST | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 23 |
| `/api/departments/{id}` | PUT | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 32 |
| `/api/departments/{id}` | DELETE | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 41 |
| `/api/departments` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 51 |
| `/api/departments/{id}` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 60 |

**Kết luận:**
- ✅ **ADMIN**: Có đầy đủ quyền
- ✅ **HR**: Có đầy đủ quyền
- ❌ **WAREHOUSE**: Không có quyền truy cập

---

#### ✅ 3.3. PositionController - CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/hrm/controller/PositionController.java`

| Endpoint | Method | Phân quyền | Vị trí code |
|----------|--------|------------|------------|
| `/api/positions` | POST | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 23 |
| `/api/positions/{id}` | PUT | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 32 |
| `/api/positions/{id}` | DELETE | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 41 |
| `/api/positions` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 51 |
| `/api/positions/{id}` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` | Dòng 60 |

**Kết luận:**
- ✅ **ADMIN**: Có đầy đủ quyền
- ✅ **HR**: Có đầy đủ quyền
- ❌ **WAREHOUSE**: Không có quyền truy cập

---

#### ⚠️ 3.4. AttendanceController - KHÔNG CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/hrm/controller/AttendanceController.java`

**Vấn đề:** ❌ **KHÔNG CÓ** `@PreAuthorize` annotation trên bất kỳ endpoint nào!

| Endpoint | Method | Phân quyền | Trạng thái |
|----------|--------|------------|------------|
| `/api/attendances` | POST | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/attendances/{id}` | PUT | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/attendances/{id}` | DELETE | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/attendances` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/attendances/{id}` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |

**Kết luận:**
- ⚠️ **BẤT KỲ USER NÀO** đã đăng nhập (ADMIN, HR, WAREHOUSE) đều có thể truy cập
- ⚠️ **LỖ HỔNG BẢO MẬT**: WAREHOUSE có thể xem/sửa/xóa attendance của HR module

---

#### ⚠️ 3.5. PayrollController - KHÔNG CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/hrm/controller/PayrollController.java`

**Vấn đề:** ❌ **KHÔNG CÓ** `@PreAuthorize` annotation trên bất kỳ endpoint nào!

| Endpoint | Method | Phân quyền | Trạng thái |
|----------|--------|------------|------------|
| `/api/payrolls` | POST | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/payrolls/{id}` | PUT | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/payrolls/{id}` | DELETE | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/payrolls` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/payrolls/{id}` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |

**Kết luận:**
- ⚠️ **BẤT KỲ USER NÀO** đã đăng nhập đều có thể truy cập
- ⚠️ **LỖ HỔNG BẢO MẬT**: WAREHOUSE có thể xem/sửa/xóa payroll (thông tin nhạy cảm!)

---

### 📦 MODULE WAREHOUSE (Quản lý kho)

#### ✅ 4.1. StockImportController - CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/warehouse/controller/StockImportController.java`

| Endpoint | Method | Phân quyền | Vị trí code |
|----------|--------|------------|------------|
| `/api/stock-imports` | POST | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 23 |
| `/api/stock-imports/{id}` | PUT | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE') or @securityService.canModifyStockImport(#id)")` | Dòng 33 |
| `/api/stock-imports/{id}` | DELETE | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 42 |
| `/api/stock-imports` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 52 |
| `/api/stock-imports/filtered` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 61 |
| `/api/stock-imports/my-records` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 70 |
| `/api/stock-imports/{id}` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 79 |

**Đặc biệt - Endpoint UPDATE:**
- Dòng 33: Có thêm điều kiện `@securityService.canModifyStockImport(#id)`
- **Ý nghĩa:** Cho phép owner (người tạo) có thể sửa record của mình (nếu chưa approve)
- ⚠️ **VẤN ĐỀ:** Method `canModifyStockImport()` **CHƯA ĐƯỢC IMPLEMENT** trong `SecurityService`!

**Kết luận:**
- ✅ **ADMIN**: Có đầy đủ quyền
- ✅ **WAREHOUSE**: Có đầy đủ quyền
- ❌ **HR**: Không có quyền truy cập

---

#### ✅ 4.2. StockExportController - CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/warehouse/controller/StockExportController.java`

| Endpoint | Method | Phân quyền | Vị trí code |
|----------|--------|------------|------------|
| `/api/stock-exports` | POST | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 23 |
| `/api/stock-exports/{id}` | PUT | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE') or @securityService.canModifyStockExport(#id)")` | Dòng 33 |
| `/api/stock-exports/{id}` | DELETE | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 42 |
| `/api/stock-exports` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 52 |
| `/api/stock-exports/filtered` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 61 |
| `/api/stock-exports/my-records` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 70 |
| `/api/stock-exports/{id}` | GET | `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` | Dòng 79 |

**Đặc biệt - Endpoint UPDATE:**
- Dòng 33: Có thêm điều kiện `@securityService.canModifyStockExport(#id)`
- ⚠️ **VẤN ĐỀ:** Method `canModifyStockExport()` **CHƯA ĐƯỢC IMPLEMENT** trong `SecurityService`!

**Kết luận:**
- ✅ **ADMIN**: Có đầy đủ quyền
- ✅ **WAREHOUSE**: Có đầy đủ quyền
- ❌ **HR**: Không có quyền truy cập

---

#### ⚠️ 4.3. CategoryController - KHÔNG CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/warehouse/controller/CategoryController.java`

**Vấn đề:** ❌ **KHÔNG CÓ** `@PreAuthorize` annotation!

| Endpoint | Method | Phân quyền | Trạng thái |
|----------|--------|------------|------------|
| `/api/categories` | POST | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/categories/{id}` | PUT | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/categories/{id}` | DELETE | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/categories` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/categories/{id}` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |

**Kết luận:**
- ⚠️ **BẤT KỲ USER NÀO** đã đăng nhập đều có thể truy cập
- ⚠️ **LỖ HỔNG BẢO MẬT**: HR có thể xem/sửa/xóa categories của Warehouse module

---

#### ⚠️ 4.4. ProductController - KHÔNG CÓ PHÂN QUYỀN

**File:** `src/main/java/com/example/internal_management_system/modules/warehouse/controller/ProductController.java`

**Vấn đề:** ❌ **KHÔNG CÓ** `@PreAuthorize` annotation!

| Endpoint | Method | Phân quyền | Trạng thái |
|----------|--------|------------|------------|
| `/api/products` | POST | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/products/{id}` | PUT | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/products/{id}` | DELETE | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/products` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |
| `/api/products/{id}` | GET | ❌ Không có | ⚠️ Chỉ cần authenticated |

**Kết luận:**
- ⚠️ **BẤT KỲ USER NÀO** đã đăng nhập đều có thể truy cập
- ⚠️ **LỖ HỔNG BẢO MẬT**: HR có thể xem/sửa/xóa products

---

## 📊 4. BẢNG TỔNG HỢP PHÂN QUYỀN

| Module | Controller | Endpoint | ADMIN | HR | WAREHOUSE | Trạng thái |
|--------|-----------|----------|-------|----|-----------|------------|
| **HRM** | EmployeeController | Tất cả | ✅ | ✅ | ❌ | ✅ Có phân quyền |
| **HRM** | DepartmentController | Tất cả | ✅ | ✅ | ❌ | ✅ Có phân quyền |
| **HRM** | PositionController | Tất cả | ✅ | ✅ | ❌ | ✅ Có phân quyền |
| **HRM** | AttendanceController | Tất cả | ✅ | ✅ | ✅ | ⚠️ **KHÔNG có phân quyền** |
| **HRM** | PayrollController | Tất cả | ✅ | ✅ | ✅ | ⚠️ **KHÔNG có phân quyền** |
| **Warehouse** | StockImportController | Tất cả | ✅ | ❌ | ✅ | ✅ Có phân quyền |
| **Warehouse** | StockExportController | Tất cả | ✅ | ❌ | ✅ | ✅ Có phân quyền |
| **Warehouse** | CategoryController | Tất cả | ✅ | ✅ | ✅ | ⚠️ **KHÔNG có phân quyền** |
| **Warehouse** | ProductController | Tất cả | ✅ | ✅ | ✅ | ⚠️ **KHÔNG có phân quyền** |

---

## ⚠️ 5. CÁC VẤN ĐỀ CẦN KHẮC PHỤC

### 🔴 Vấn đề 1: Thiếu phân quyền cho AttendanceController và PayrollController

**Mức độ:** 🔴 **Nghiêm trọng** (Lỗ hổng bảo mật)

**Vấn đề:**
- WAREHOUSE có thể xem/sửa/xóa attendance và payroll (thông tin nhạy cảm!)
- Không đúng với business logic (HR module chỉ dành cho ADMIN và HR)

**Giải pháp:**
Thêm `@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")` vào tất cả endpoints trong:
- `AttendanceController.java`
- `PayrollController.java`

---

### 🔴 Vấn đề 2: Thiếu phân quyền cho CategoryController và ProductController

**Mức độ:** 🔴 **Nghiêm trọng** (Lỗ hổng bảo mật)

**Vấn đề:**
- HR có thể xem/sửa/xóa categories và products (thuộc Warehouse module)
- Không đúng với business logic (Warehouse module chỉ dành cho ADMIN và WAREHOUSE)

**Giải pháp:**
Thêm `@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")` vào tất cả endpoints trong:
- `CategoryController.java`
- `ProductController.java`

---

### 🟡 Vấn đề 3: Method canModifyStockImport() và canModifyStockExport() chưa được implement

**Mức độ:** 🟡 **Trung bình** (Logic chưa hoàn chỉnh)

**Vấn đề:**
- Trong `StockImportController` và `StockExportController`, endpoint UPDATE có gọi:
  ```java
  @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE') or @securityService.canModifyStockImport(#id)")
  ```
- Nhưng method `canModifyStockImport()` và `canModifyStockExport()` **CHƯA TỒN TẠI** trong `SecurityService`

**Hậu quả:**
- Code sẽ **BỊ LỖI** khi compile hoặc runtime
- Logic cho phép owner sửa record của mình không hoạt động

**Giải pháp:**
Implement 2 methods này trong `SecurityService.java`:
```java
public boolean canModifyStockImport(Long id) {
    // Logic: Kiểm tra user hiện tại có phải là owner không
    // Và record có status chưa được approve không
}

public boolean canModifyStockExport(Long id) {
    // Tương tự
}
```

---

## ✅ 6. TÓM TẮT

### ✅ Những gì đã làm tốt:
1. ✅ Phân quyền rõ ràng cho Employee, Department, Position (HRM module)
2. ✅ Phân quyền rõ ràng cho StockImport, StockExport (Warehouse module)
3. ✅ Sử dụng `@PreAuthorize` đúng cách
4. ✅ Có logic cho phép owner sửa record của mình (mặc dù chưa implement)

### ⚠️ Những gì cần sửa:
1. 🔴 **Thiếu phân quyền** cho AttendanceController và PayrollController
2. 🔴 **Thiếu phân quyền** cho CategoryController và ProductController
3. 🟡 **Chưa implement** methods `canModifyStockImport()` và `canModifyStockExport()`

### 📝 Kết luận:
- **Tỷ lệ phân quyền đúng:** 5/9 controllers (55.6%)
- **Tỷ lệ phân quyền sai/thiếu:** 4/9 controllers (44.4%)
- **Cần khắc phục ngay:** 4 controllers thiếu phân quyền + 2 methods chưa implement

---

**Ngày phân tích:** $(date)
**Người phân tích:** AI Assistant

