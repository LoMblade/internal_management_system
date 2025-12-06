# Hướng dẫn sử dụng Postman Collection

## File: `postman_collection_permissions.json`

## Cách import vào Postman

1. Mở **Postman**
2. Click **Import** (góc trên bên trái)
3. Chọn **File** tab
4. Chọn file `postman_collection_permissions.json`
5. Click **Import**

## Cấu trúc Collection

Collection được chia thành 4 folders:

### 1. Authentication
- **Register Admin**: Đăng ký tài khoản admin
- **Login Admin**: Đăng nhập admin (tự động lưu token vào biến `adminToken` và `adminId`)
- **Register Manager**: Đăng ký tài khoản trưởng phòng
- **Login Manager**: Đăng nhập manager (tự động lưu token vào biến `managerToken` và `managerId`)
- **Register Staff**: Đăng ký tài khoản nhân viên
- **Login Staff**: Đăng nhập staff (tự động lưu token vào biến `staffToken` và `staffId`)

### 2. Admin - Permission Management
- **Get All Roles**: Lấy danh sách tất cả roles
- **Get All Permissions**: Lấy danh sách tất cả permissions
- **Get Permissions by Resource (EMPLOYEE)**: Lấy permissions của resource EMPLOYEE
- **Get User Permissions (Manager)**: Xem quyền của manager
- **Get User Permissions (Staff)**: Xem quyền của staff
- **Assign Permissions to Manager**: Gán quyền CRUD Employee cho manager
- **Assign Permissions to Staff**: Gán quyền READ_OWN Employee cho staff

### 3. Test Manager (CRUD Employee)
- ✅ **Get All Employees**: Xem danh sách employees (nên thành công)
- ✅ **Create Employee**: Tạo employee mới (nên thành công)
- ✅ **Get Employee by ID**: Xem chi tiết employee (nên thành công)
- ✅ **Update Employee**: Cập nhật employee (nên thành công)
- ✅ **Delete Employee**: Xóa employee (nên thành công)
- ❌ **Get Departments**: Xem departments (nên bị chặn 403)
- ❌ **Get Stock Imports**: Xem stock imports (nên bị chặn 403)

### 4. Test Staff (READ_OWN Employee)
- ✅ **Get Filtered Employees**: Xem danh sách employees của chính mình (nên thành công)
- ❌ **Get All Employees**: Xem tất cả employees (nên bị chặn 403)
- ✅ **Get Employee by ID (Own)**: Xem thông tin của chính mình (nên thành công)
- ❌ **Create Employee**: Tạo employee (nên bị chặn 403)
- ❌ **Update Employee**: Cập nhật employee (nên bị chặn 403)
- ❌ **Delete Employee**: Xóa employee (nên bị chặn 403)
- ❌ **Get Departments**: Xem departments (nên bị chặn 403)

## Các bước test

### Bước 1: Chạy Database Migration
```bash
mysql -u root -p your_database < database_migration.sql
```

### Bước 2: Thiết lập biến môi trường trong Postman

1. Click vào **Environments** (bên trái)
2. Tạo environment mới hoặc sử dụng **Globals**
3. Thêm các biến sau (sẽ được tự động set khi login):
   - `baseUrl`: `http://localhost:8080`
   - `adminToken`: (sẽ được set tự động khi login admin)
   - `managerToken`: (sẽ được set tự động khi login manager)
   - `staffToken`: (sẽ được set tự động khi login staff)
   - `adminId`: (sẽ được set tự động khi login admin)
   - `managerId`: (sẽ được set tự động khi login manager)
   - `staffId`: (sẽ được set tự động khi login staff)

**Lưu ý**: Collection variables đã được định nghĩa sẵn, nhưng nếu muốn dùng environment variables, bạn có thể tạo environment riêng.

### Bước 3: Đăng ký và đăng nhập

1. Chạy **Register Admin** → **Login Admin**
2. Chạy **Register Manager** → **Login Manager**
3. Chạy **Register Staff** → **Login Staff**

**Lưu ý**: Sau mỗi lần login, token và userId sẽ được tự động lưu vào biến.

### Bước 4: Admin gán quyền

1. Chạy **Assign Permissions to Manager (CRUD Employee)**
2. Chạy **Assign Permissions to Staff (READ_OWN Employee)**

### Bước 5: Test quyền Manager

Chạy các request trong folder **"3. Test Manager"**:
- Các request có ✅ nên trả về 200 OK
- Các request có ❌ nên trả về 403 Forbidden

### Bước 6: Test quyền Staff

Chạy các request trong folder **"4. Test Staff"**:
- Các request có ✅ nên trả về 200 OK
- Các request có ❌ nên trả về 403 Forbidden

## Lưu ý quan trọng

1. **Thứ tự thực hiện**: Phải đăng nhập trước khi test các API khác
2. **Token tự động**: Token sẽ được tự động lưu sau mỗi lần login thành công
3. **User ID**: User ID sẽ được tự động lưu sau mỗi lần login thành công
4. **Kiểm tra response**: Xem console trong Postman để debug (View → Show Postman Console)

## Troubleshooting

### Lỗi 401 Unauthorized
- Kiểm tra token có được set chưa
- Thử login lại để lấy token mới

### Lỗi 403 Forbidden
- Đây là lỗi mong đợi cho các request bị chặn
- Kiểm tra xem đã gán quyền chưa (dùng Admin gán quyền)

### Lỗi 404 Not Found
- Kiểm tra `baseUrl` có đúng không
- Kiểm tra server có đang chạy không

### Token không được lưu
- Kiểm tra test script trong request Login
- Xem console để debug

## Tùy chỉnh

Bạn có thể chỉnh sửa:
- `baseUrl` trong collection variables
- Request body trong các request
- Test scripts để tự động hóa thêm

