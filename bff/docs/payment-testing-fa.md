# تست جریان پرداخت BFF در محیط توسعه محلی

سرویس **BFF** تمام عملیات مربوط به پرداخت را به سرویس **رزرو (Reservation)** پروکسی می‌کند.  
در پروفایل توسعه (`dev`)، سرویس رزرو با مقدار `reservation.payment.sep.mock-enabled=true` پیکربندی شده است.  
به این معنی که هیچ درگاه پرداخت واقعی (SEP) فراخوانی نمی‌شود و کل فرآیند پرداخت را می‌توان به‌صورت کامل در محیط محلی شبیه‌سازی و تست کرد.

در ادامه مراحل اجرای تست انتها به انتها (End-to-End) در حالت Mock آورده شده است:

---

## ۱. راه‌اندازی محیط توسعه

در مسیر اصلی پروژه (ریشه ریپازیتوری)، دستور زیر را اجرا کنید تا محیط توسعه با Docker Compose بالا بیاید.  
این محیط شامل **PostgreSQL، Redis، سرویس احراز هویت (Authentication)، سرویس رزرو (Reservation)** و **BFF** روی پورت‌های پیش‌فرض خود است.

```bash
docker-compose -f devops/docker-compose.dev.yml up --build
```

اجازه دهید این استک در یک ترمینال جداگانه در حال اجرا بماند.

---

## ۲. دریافت توکن JWT

نقاط دسترسی احراز هویت در آدرس زیر قرار دارند:  
`http://localhost:8082/api/v1/auth`

پایگاه داده به‌صورت پیش‌فرض با چند کاربر نمایشی (Demo Users) مقداردهی اولیه شده است.  
رمز عبور تمام کاربران `123` است (به‌صورت bcrypt در فایل `data.sql` ذخیره شده).

برای دریافت توکن، با یکی از حساب‌های نمایشی (مثلاً `john_doe@example.com`) درخواست زیر را ارسال کنید:

```bash
curl -X POST   http://localhost:8082/api/v1/auth/signin   -H 'Content-Type: application/json'   -d '{
        "email": "john_doe@example.com",
        "password": "123",
        "deviceModel": "local-cli",
        "os": "linux",
        "browser": "curl"
      }'
```

در پاسخ، فیلدی به نام `token` دریافت می‌کنید.  
آن را برای درخواست‌های بعدی ذخیره کنید:

```bash
export JWT="<token-value>"
```

---

## ۳. انتخاب یک رزرو برای پرداخت

پایگاه داده‌ی رزرو از قبل با داده‌های نمونه پر شده است.  
می‌توانید لیست رزروها را از طریق BFF با آدرس زیر دریافت کنید:

`GET http://localhost:8083/api/v1/portal/user/reservations`

نمونه درخواست:

```bash
curl -H "Authorization: Bearer $JWT"   http://localhost:8083/api/v1/portal/user/reservations | jq '.[0].id'
```

هر `id` که در خروجی مشاهده کردید را برای مرحله بعد (شروع پرداخت) انتخاب کنید.

---

## ۴. آغاز فرآیند پرداخت از طریق BFF

درخواست زیر را به اندپوینت  
`POST /portal/user/payments/create`  
ارسال کنید. در حالت mock، سرویس رزرو یک توکن ساختگی و آدرس بازگشت (redirectUrl) جعلی برمی‌گرداند.

```bash
curl -X POST   http://localhost:8083/api/v1/portal/user/payments/create   -H 'Content-Type: application/json'   -H "Authorization: Bearer $JWT"   -d '{
        "reservationId": "<reservation-uuid>",
        "amount": 150000
      }'
```

در پاسخ JSON این فیلدها را مشاهده می‌کنید:
- `paymentId`: شناسه پرداخت ایجادشده  
- `token`: که در حالت mock با `MOCK-` شروع می‌شود  
- `referenceNumber`: شماره مرجع مورد نیاز برای شبیه‌سازی Callback  
- `redirectUrl`: آدرس ساختگی درگاه SEP

برای مراحل بعدی مقدار `paymentId` و `referenceNumber` را نگه دارید.

---

## ۵. شبیه‌سازی Callback درگاه SEP

زمانی که حالت mock فعال باشد، هر Callback که شامل فیلد `resNum` با همان مقدار `referenceNumber` باشد، پرداخت را فوراً تأیید می‌کند.

```bash
curl -X POST   http://localhost:8083/api/v1/portal/user/payments/callback   -H 'Content-Type: application/json'   -H "Authorization: Bearer $JWT"   -d '{
        "state": "OK",
        "status": "0",
        "refNum": "<reference-number>",
        "resNum": "<reference-number>",
        "rrn": "123456789012",
        "traceNo": "654321",
        "amount": 150000,
        "terminalId": "TENNIS-TIME"
      }'
```

در پاسخ، فیلدی مانند `success: true` و پیام تأیید Mock مشاهده می‌کنید.

---

## ۶. برگشت پرداخت (اختیاری)

در صورت نیاز، می‌توانید پرداخت را با استفاده از `paymentId` بازگردانید.  
در حالت mock، سرویس رزرو بلافاصله پرداخت را به‌عنوان برگشت‌شده علامت می‌زند و زمان تأیید را بازمی‌گرداند.

```bash
curl -X POST   http://localhost:8083/api/v1/portal/user/payments/<paymentId>/reverse   -H "Authorization: Bearer $JWT"
```

پاسخ شامل چنین پیامی خواهد بود:

```json
{
  "reversed": true,
  "message": "Reverse transaction completed in mock mode"
}
```

این پیام نشان می‌دهد که کل فرآیند پرداخت و برگشت، از ابتدا تا انتها، با موفقیت در حالت شبیه‌سازی شده (بدون درگاه واقعی SEP) انجام شده است.

---
