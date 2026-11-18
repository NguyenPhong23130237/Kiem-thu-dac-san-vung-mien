<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác minh email</title>
    <style>
        body main {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 150px;
        }

        button {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
        }
    </style>
    <link rel="stylesheet" href="css/global.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main>
    <h2>📩 Email của bạn chưa được xác minh</h2>
    <p>Chúng tôi đã gửi email xác nhận đến <strong>${sessionScope.get("email")}</strong></p>
    <p>Vui lòng kiểm tra email để xác nhận tài khoản.</p>

    <button id="resendBtn" disabled>Gửi lại email xác minh (60s)</button>

    <p id="msg" style="color: green;"></p>
</main>

<%@ include file="footer.jsp" %>

<script>
    let seconds = 60;
    const btn = document.getElementById("resendBtn");
    const msg = document.getElementById("msg");

    function startCountdown() {
        btn.disabled = true;
        btn.innerText = "Gửi lại email xác minh (" + seconds + "s)";

        const timer = setInterval(() => {
            seconds--;
            btn.innerText = "Gửi lại email xác minh (" + seconds + "s)";

            if (seconds <= 0) {
                clearInterval(timer);
                btn.disabled = false;
                btn.innerText = "Gửi lại email xác minh";
            }
        }, 1000);
    }

    startCountdown();

    btn.onclick = function () {
        btn.disabled = true;
        btn.innerText = "Đang gửi...";

        fetch("resend-verification", {method: "POST"})
            .then(res => res.json()) // 🔥 đọc JSON từ servlet
            .then(data => {
                msg.innerText = data.message;

                if (data.success) {
                    seconds = 60;
                    startCountdown();
                } else {
                    btn.disabled = false; // cho bấm lại nếu lỗi
                    btn.innerText = "Gửi lại email xác minh";
                }
            })
            .catch(err => {
                console.error(err);
                msg.innerText = "❌ Lỗi kết nối server";
                btn.disabled = false;
                btn.innerText = "Gửi lại email xác minh";
            });
    };


    // 🔁 Kiểm tra định kỳ xem user đã xác minh chưa
    setInterval(() => {
        fetch("check-verified")
            .then(res => res.json())
            .then(data => {
                if (data.verified) {
                    document.body.innerHTML = `
                    <h2>🎉 Xác minh thành công!</h2>
                    <p>Đang chuyển về trang chủ...</p>
                `;
                    setTimeout(() => {
                        window.location.href = "home";
                    }, 2000);
                }
            });
    }, 3000);
</script>

</body>
</html>
