<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý hồ sơ</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 10px 20px;
            background-color: white;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .form-group input[type="file"] {
            padding: 3px;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }

        .btn-primary {
            background-color: #1877f2;
            color: white;
        }

        .btn-primary:hover {
            background-color: #166fe5;
        }

        .error-message {
            color: #ff0000;
            margin-top: 5px;
            font-size: 0.9em;
        }

        .avatar-preview {
            margin-top: 10px;
            max-width: 100px;
            max-height: 100px;
            border-radius: 50%;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>Quản lý hồ sơ</h1>
    <div class="user-info">
        <a href="${pageContext.request.contextPath}/home" class="btn">Quay lại</a>
        <span>Xin chào, ${user.username}</span>
        <a href="${pageContext.request.contextPath}/logout" class="btn">Đăng xuất</a>
    </div>
</div>

<div class="container">
    <h2>${user.email != null ? 'Sửa hồ sơ' : 'Thêm hồ sơ'}</h2>
    <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="email">Địa chỉ email:</label>
            <input type="email" id="email" name="email" value="${user.email}" required>
            <c:if test="${not empty emailError}">
                <p class="error-message">${emailError}</p>
            </c:if>
        </div>

        <div class="form-group">
            <label for="birthDate">Ngày sinh:</label>
            <input type="date" id="birthDate" name="birthDate" value="${user.birthDate}" required>
            <c:if test="${not empty birthDateError}">
                <p class="error-message">${birthDateError}</p>
            </c:if>
        </div>

        <div class="form-group">
            <label for="provinceId">Nơi ở:</label>

            <select id="provinceId" name="provinceId" required>
                <option value="">-- Chọn tỉnh/thành --</option>
                <c:forEach var="province" items="${provinces}">
                    <option value="${province.idProvince}"
                            <c:if test="${user.provinceId == province.idProvince}">selected</c:if>>
                            ${province.nameProvince}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="avatar">Hình ảnh avatar:</label>
            <input type="file" id="avatar" name="avatar" accept=".jpg,.png">
            <c:if test="${not empty avatarError}">
                <p class="error-message">${avatarError}</p>
            </c:if>
            <c:if test="${not empty user.avatar}">
                <p>Hiện tại: <img src="${pageContext.request.contextPath}/img/${user.avatar}" alt="Avatar" class="avatar-preview" /></p>
            </c:if>
        </div>

        <button type="submit" class="btn btn-primary">Lưu</button>
    </form>
</div>

<script>
    // Kiểm tra định dạng email trên client-side
    document.getElementById('email').addEventListener('input', function() {
        const email = this.value;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            this.setCustomValidity('Vui lòng nhập email hợp lệ.');
        } else {
            this.setCustomValidity('');
        }
    });

    // Kiểm tra tuổi trên client-side
    document.getElementById('birthDate').addEventListener('change', function() {
        const birthDate = new Date(this.value);
        const today = new Date('2025-04-16'); // Ngày hiện tại
        const age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        const dateDiff = today.getDate() - birthDate.getDate();
        let adjustedAge = age;
        if (monthDiff < 0 || (monthDiff === 0 && dateDiff < 0)) {
            adjustedAge--;
        }
        if (adjustedAge < 15) {
            this.setCustomValidity('Bạn phải trên 15 tuổi để đăng ký.');
        } else {
            this.setCustomValidity('');
        }
    });

    // Kiểm tra file avatar trên client-side
    document.getElementById('avatar').addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const validFormats = ['image/jpeg', 'image/png'];
            const maxSize = 200 * 1024; // 200KB
            if (!validFormats.includes(file.type)) {
                this.setCustomValidity('Chỉ chấp nhận file JPG hoặc PNG.');
            } else if (file.size > maxSize) {
                this.setCustomValidity('Dung lượng file không được vượt quá 200KB.');
            } else {
                this.setCustomValidity('');
            }
        }
    });
</script>
</body>
</html>