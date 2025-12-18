# Telegram Bot Backend

## Ümumi Məlumat

Bu project Java və Spring Boot istifadə edilərək hazırlanmış **Telegram Bot Backend**-dir.
Sistem istifadəçilər üçün **OTP əsaslı login**, **session idarəetməsi** və **state-based istifadəçi axınları** təmin edir.
Project real backend prinsiplərinə uyğun olaraq qurulub və CV üçün Junior Java Backend səviyyəsinə uyğundur.

---

## Funksionallıqlar

* OTP (One-Time Password) ilə login sistemi
* İstifadəçi session-larının idarə olunması
* State-based mesaj axını (Telegram update-lərə görə)
* DTO pattern istifadə edilərək API dizaynı
* Global Exception Handling
* Xarici data mənbələrindən məlumatların əldə olunması (scraping / API)

---

## Texnologiyalar

### Backend

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate

### Database

* MySQL

### Arxitektura və Konseptlər

* Layered Architecture (Controller, Service, Repository)
* DTO Pattern
* GlobalExceptionHandler
* Custom Runtime Exceptions
* Session Management

### Alətlər

* Git & GitHub
* IntelliJ IDEA
* Maven

---

## Project Strukturu

```
com.example.tgbot
 ├── controller
 ├── service
 │    └── impl
 ├── repository
 ├── dto
 │    ├── request
 │    └── response
 ├── exception
 │    ├── GlobalExceptionHandler
 │    └── Custom Exceptions
 └── util
```

---

## Exception Handling

Project-də bütün xətalar **GlobalExceptionHandler** vasitəsilə mərkəzləşdirilmiş formada idarə olunur.
Bu yanaşma controller-lərdə try-catch istifadəsini minimuma endirir və API üçün standart error response təmin edir.

İstifadə olunan exception-lərə nümunələr:

* UserNotFoundException
* InvalidOtpException
* SessionExpiredException
* BadRequestException

---

## Necə İşə Salmaq Olar

1. MySQL database yaradın
2. `application.yml` və ya `application.properties` faylında database məlumatlarını konfiqurasiya edin
3. Project-i IDE üzərindən və ya aşağıdakı komanda ilə işə salın:

```bash
mvn spring-boot:run
```

---

## Qeyd

Bu project **öyrənmə və praktika məqsədilə** hazırlanıb və Junior Java Backend Developer mövqeyi üçün CV-də təqdim olunmaq üçün nəzərdə tutulub.

---

## Müəllif

Ruslan Zeynalov
