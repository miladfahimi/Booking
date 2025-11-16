# اعلان‌های لحظه‌ای وضعیت اسلات (Realtime Slot Status Notifications)

این سند معماری جریان لحظه‌ای را که وضعیت اسلات‌ها را بین کلاینت‌ها همگام نگه می‌دارد توضیح می‌دهد و همچنین مراحل مورد نیاز برای مهاجرت این جریان به یک سرویس اعلان اختصاصی را زمانی که تعداد اعلان‌ها زیاد شود شرح می‌دهد.

## معماری فعلی

پیاده‌سازی لحظه‌ای (Realtime) به‌صورت عمدی در پکیج‌های جداگانه قرار گرفته است تا بتوان بدون دست‌زدن به ماژول‌های نامرتبط، مکانیزم انتقال را تغییر داد.

### سرویس رزرو (Reservation Service)

* `ReservationWebSocketConfig` بروکر پیام‌رسانی Spring را فعال می‌کند، مسیر `/ws/reservation` را در اختیار قرار می‌دهد و مقصدهای `/topic/**` را برای انتشار پیام تنظیم می‌کند تا سرویس‌های پایین‌دستی بتوانند با STOMP ساده مشترک شوند.

* تمام سرویس‌های دامنه از طریق رابط `SlotStatusNotifier` به‌روزرسانی‌ها را ارسال می‌کنند. پیاده‌سازی پیش‌فرض (`WebSocketSlotStatusNotifier`) با استفاده از  
  `SimpMessagingTemplate.convertAndSend("/topic/slot-status", notification)`  
  پیام را به همه مشترکان می‌فرستد.

* `ReservationBasketService` مسئول ارسال وضعیت‌های `IN_BASKET`، `PENDING` و `AVAILABLE` هنگام اضافه‌کردن، به‌روزرسانی یا حذف آیتم‌های سبد است. این سرویس `SlotStatusNotification` را با هر دو مقدار `slotId` و `compositeSlotId` تولید می‌کند.

* `ReservationService` وضعیت‌های نهایی مثل `PENDING`، `CONFIRMED` و سایر وضعیت‌ها را پس از عملیات پایگاه داده ارسال می‌کند تا سیستم وضعیت قطعی هر اسلات را دریافت کند.


### BFF

* `BffWebSocketConfig` همان تنظیمات WebSocket رزرو را منعکس می‌کند و مسیر `/ws/bff` را برای مرورگرها فراهم می‌کند. این مسیر پیام‌های خروجی را روی `/topic/slot-status` منتشر می‌کند تا فرانت‌اند نیاز به اتصال مستقیم به سرویس رزرو نداشته باشد.

* `RealtimeProperties` آدرس و تاپیک سرویس رزرو و BFF را نگه‌داری می‌کند تا بتوان بدون تغییر کد، محیط‌ها را تنظیم کرد.

* `RealtimeClientConfig` یک `WebSocketStompClient` مقاوم راه‌اندازی می‌کند که Bridge از آن برای اتصال و دریافت پیام‌ها استفاده می‌کند.

* `ReservationSlotStatusBridge` به `/ws/reservation` وصل می‌شود، تاپیک `/topic/slot-status` را گوش می‌دهد و پیام‌ها را دوباره به تاپیک مشابه در BFF منتشر می‌کند. همچنین در صورت خطای ارتباط، اتصال را دوباره برقرار می‌کند.


### فرانت‌اند (Frontend)

* `SlotStatusRealtimeService` یک WebSocket خام به مسیر `/api/v1/ws/bff` باز می‌کند، فریم‌های STOMP را ارسال و دریافت می‌کند، پیام‌ها را به `SlotStatusNotification` تبدیل کرده و به مشترکان RxJS انتشار می‌دهد. قابلیت reconnect خودکار نیز دارد.

* `ReservationEffects` هر پیام لحظه‌ای دریافت‌شده را به اکشن `slotStatusUpdated` تبدیل می‌کند و از لحظه‌ای که ماژول بارگذاری شود، این جریان را فعال نگه می‌دارد.

* `reservation.reducer` با یافتن اسلات مربوطه (بر اساس `serviceId + slotId`) وضعیت آن را به‌روزرسانی می‌کند و UI فوراً رندر می‌شود.

این لایه‌ها عمداً جزئیات انتقال را از دامنه جدا کرده‌اند تا بتوان به‌راحتی `SlotStatusNotifier` را به یک message broker یا پروتکل دیگر تغییر داد.


## چرخهٔ وضعیت اسلات و مثال جریان

دامنه از enum به نام `ReservationStatus` برای نمایش وضعیت هر اسلات استفاده می‌کند. مراحل اصلی شامل `AVAILABLE`، `IN_BASKET`، `PENDING` و `CONFIRMED` هستند. مراحل دیگری مانند `CANCELED`، `EXPIRED`، `MAINTENANCE` و `ADMIN_HOLD` برای فرایندهای مدیریتی استفاده می‌شوند.

### نمونه جریان

1. **انتخاب اسلات توسط کاربر (`IN_BASKET`)**  
   متد `ReservationBasketService.addOrUpdateItem` وضعیت را به `IN_BASKET` تغییر داده و پیام اطلاع‌رسانی ارسال می‌کند.

2. **شروع پرداخت (`PENDING`)**  
   فرانت‌اند وضعیت جدید سبد را درخواست می‌کند و سرویس سبد آن را به `PENDING` تغییر داده و پیام جدیدی ارسال می‌کند.

3. **پرداخت موفق (`CONFIRMED`)**  
   سرویس `ReservationService` رزروها را ثبت کرده، آن‌ها را در ابتدا `PENDING` می‌کند و سپس در مرحلهٔ تأیید وضعیت را به `CONFIRMED` تغییر می‌دهد و notifier پیام را منتشر می‌کند.

4. **رها شدن پرداخت یا حذف از سبد (`AVAILABLE`)**  
   حذف آیتم از سبد یا پاک‌کردن سبد پیام `AVAILABLE` ارسال می‌کند. در سرویس رزرو نیز وضعیت‌های لغو‌شده همین مسیر را طی می‌کنند.

به دلیل وجود یک `SlotStatusNotifier` مشترک، افزودن وضعیت جدید مانند `EXPIRED` نیازی به تغییر در لایه‌های دیگر ندارد.


## قرارداد Payload وضعیت اسلات

همهٔ لایه‌ها از DTO به نام `SlotStatusNotification` استفاده می‌کنند که شامل:

- `slotId`
- `compositeSlotId`
- `serviceId`
- `status`
- `reservationDate`

وجود شناسهٔ ترکیبی (`service + slot`) برای مواقعی که چند سرویس اسلات‌هایی با شناسه مشابه تولید می‌کنند ضروری است و فیلد `reservationDate` نیز مانع می‌شود تغییرات یک روز روی روز دیگری اعمال شود.
در عین حال، فرانت‌اند همچنان می‌تواند از `slotId` ساده استفاده کند.

## تنظیمات (Configuration)

## مقادیر پیش‌فرض اتصال:

```
realtime.reservation.url=ws://localhost:8085/ws/reservation
realtime.reservation.topic=/topic/slot-status
realtime.bff.topic=/topic/slot-status
```

در فرانت‌اند مقدار environment.slotStatusWebSocketUrl باید متناسب با محیط تنظیم شود.


### مراحل مهاجرت به سرویس اعلان مستقل

**1. اضافه‌کردن Message Broker**  
جایگزینی `WebSocketSlotStatusNotifier` با یک publisher که پیام‌ها را در Kafka، Redis Streams یا مشابه منتشر می‌کند، بدون تغییر در کلاس‌های دامنه.

**2. اضافه‌کردن Consumer در BFF (موقت)**  
BFF به‌جای اتصال WebSocket مستقیم به سرویس رزرو، پیام‌ها را از بروکر خوانده و سپس به `/topic/slot-status` در BFF منتشر می‌کند.

**3. ایجاد Notification Service مستقل**  
endpoint وب‌سوکت موجود در BFF به سرویس جدید منتقل می‌شود. این سرویس پیام‌ها را از بروکر مشترک شده و همان تاپیک `/topic/slot-status` را بدون تغییر Contract به کلاینت‌ها ارسال می‌کند.

**4. تغییر آدرس فرانت‌اند**  
فقط مقدار WebSocket URL عوض می‌شود. چون تاپیک ثابت می‌ماند، کد فعلی بدون هیچ تغییری کار می‌کند.

**5. حذف Bridge از BFF**  
پس از اطمینان از پایداری سرویس اعلان جدید، bridge و کانفیگ‌های مرتبط از BFF حذف می‌شود. سرویس رزرو همچنان پیام‌ها را مستقیماً به بروکر ارسال می‌کند.
