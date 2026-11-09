# MyPictureApp — API Design Document

> Bước 4.2: Thiết kế API (contract giữa FE và BE) — chốt trước khi code Controller. Base path: `/api`. Auth: Bearer JWT (trừ các endpoint đánh dấu "Không").

---

## 1. Auth

| Endpoint | Method | Công dụng | Auth | Path Params | Query Params | Request Body | Response Body | Status/Error chính |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `/api/auth/register` | POST | Tạo tài khoản mới | Không | — | — | `{ displayName, username, password, confirmPassword, email? }` | `{ id, username, displayName, email, avatarUrl, createdAt }` | 201 OK; 409 username đã tồn tại; 400 password ≠ confirmPassword |
| `/api/auth/login` | POST | Đăng nhập, cấp cặp access + refresh token | Không | — | — | `{ username, password }` | `{ accessToken, refreshToken, user: {...} }` | 200 OK; 401 sai username/password |
| `/api/auth/refresh` | POST | Cấp access token mới khi hết hạn (có rotation refresh token) | Không (dùng refreshToken) | — | — | `{ refreshToken }` | `{ accessToken, refreshToken }` (mới) | 200 OK; 401 refresh token hết hạn/revoked |
| `/api/auth/logout` | POST | Đăng xuất, revoke refresh token đang dùng | Có | — | — | `{ refreshToken }` | 204 No Content | 200/204 OK; 401 |

**Quyết định đã chốt:**

- `/register` chỉ trả user info, không auto-login — client gọi `/login` riêng.
- Refresh token có **rotation**: mỗi lần refresh, token cũ bị revoke, cấp token mới.
- Access token lưu ở `localStorage` phía FE, gửi qua header `Authorization: Bearer {token}`.
- Không có route `/api/auth/verify` riêng — dùng `GET /api/users/me` (401 nếu hết hạn) kết hợp `/api/auth/refresh` để xác nhận/khôi phục phiên đăng nhập khi mở lại app.

---

## 2. User Profile & Search

| Endpoint | Method | Công dụng | Auth | Path Params | Query Params | Request Body | Response Body | Status/Error chính |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `/api/users/me` | GET | Lấy thông tin cá nhân; đồng thời dùng để xác nhận access token còn hợp lệ (persist login) | Có | — | — | — | `{ id, username, displayName, email, avatarUrl, bio, createdAt }` | 200 OK; 401 |
| `/api/users/me` | PATCH | Cập nhật thông tin cá nhân (tên, avatar, bio) | Có | — | — | `{ displayName?, avatarUrl?, avatarId?, bio? }` | Profile đã cập nhật | 200 OK; 400; 401 |
| `/api/users/{id}` | GET | Xem profile người khác + biết trạng thái kết bạn hiện tại | Có | `id` | — | — | `{ id, username, displayName, avatarUrl, bio, friendStatus }` | 200 OK; 404; 401 |
| `/api/users/search` | GET | Tìm kiếm người dùng theo tên (phân trang) | Có | — | `q, page, size` | — | `{ content: [...], page, size, totalElements }` | 200 OK; 401 |

**Ghi chú:** `friendStatus` (NONE / PENDING_SENT / PENDING_RECEIVED / FRIEND) là field tính toán ở Service, không tồn tại trong DB.

---

## 3. Friend & Friend Request

| Endpoint | Method | Công dụng | Auth | Path Params | Query Params | Request Body | Response Body | Status/Error chính |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `/api/friends/requests` | POST | Gửi lời mời kết bạn | Có | — | — | `{ toUserId }` | `{ id, toUserId, status: "PENDING", createdAt }` | 201 OK; 409 đã bạn/đã có PENDING; 404 |
| `/api/friends/requests/{id}` | DELETE | Hủy lời mời **mình đã gửi** (khi còn PENDING) | Có | `id` | — | — | 204 No Content | 200/204; 403; 404 |
| `/api/friends/requests/{id}/accept` | PATCH | Chấp nhận lời mời **mình nhận được** → tạo `Friend` record | Có | `id` | — | — | `{ id, userA, userB, createdAt }` | 200 OK; 403; 409 |
| `/api/friends/requests/{id}/decline` | PATCH | Từ chối lời mời **mình nhận được** | Có | `id` | — | — | 204 No Content | 200/204; 403; 409 |
| `/api/friends/requests/incoming` | GET | Xem danh sách lời mời đã **nhận** | Có | — | `page, size` | — | `{ content: [...], page, size, totalElements }` | 200 OK; 401 |
| `/api/friends` | GET | Xem danh sách bạn bè | Có | — | `page, size` | — | `{ content: [...], page, size, totalElements }` | 200 OK; 401 |
| `/api/friends/{userId}` | DELETE | Hủy kết bạn | Có | `userId` | — | — | 204 No Content | 200/204; 404 |

**Quyết định đã chốt:**

- `PATCH .../accept` tạo luôn `Friend` record trong cùng transaction (không cần client gọi thêm API).
- Khi tạo `Friend`, chuẩn hóa thứ tự `userA`/`userB` (id nhỏ hơn luôn vào `userA`) để khớp `UNIQUE (userA, userB)`.
- **Không** làm `GET /api/friends/requests/outgoing` trong MVP — bỏ khỏi phạm vi trước mắt.

---

## 4. Post

| Endpoint | Method | Công dụng | Auth | Path Params | Query Params | Request Body | Response Body | Status/Error chính |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `/api/posts` | POST | Đăng bài viết mới (1 ảnh) | Có | — | — | `{ imageUrl, imageId }` | `{ id, poster: {...}, imageUrl, imageId, createdAt }` | 201 OK; 400 thiếu imageUrl/imageId; 401 |
| `/api/posts/{id}` | DELETE | Xóa bài viết (chỉ chủ post) | Có | `id` | — | — | 204 No Content | 200/204 OK; 403; 404 |
| `/api/users/{id}/posts` | GET | Xem danh sách bài viết trên profile của 1 người (phân trang) | Có | `id` | `page, size` | — | `{ content: [...], page, size, totalElements }` | 200 OK; 404; 401 |

**Quyết định đã chốt:**

- **Không** làm feed tổng hợp trong MVP — chỉ xem post theo profile từng người.
- FE tự upload ảnh lên Cloudinary, gửi `imageUrl/imageId` về BE dạng JSON — BE không nhận `multipart/form-data`, không tự gọi Cloudinary.
- Bất kỳ ai đã đăng nhập đều xem được post của bất kỳ user nào (chưa có khái niệm riêng tư/public trong MVP).
- Xóa post: chưa xử lý xóa ảnh thật trên Cloudinary (có thể để ảnh mồ côi, dọn sau).

---

## Tổng kết

| Module | Số endpoint |
| --- | --- |
| Auth | 4 |
| User Profile & Search | 4 |
| Friend & Friend Request | 7 |
| Post | 3 |
| **Tổng** | **18** |