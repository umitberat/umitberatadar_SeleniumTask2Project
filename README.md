# umitberatadar_SeleniumTask2Project

Selenium-Java ile hazırlanmış adım-adım Insider Careers akışında ilerleyip Quality Assurance basvurusu otomasyon testi icerir.  

Test, Insider ana sayfasından başlayarak QA iş ilanına kadar bütün süreci doğrular:

1. Ana sayfa dan Careers sayfasina gecis
2. Careers sayfasi scroll yapilarak inceleme
3. See all teams a kadar yukari kaydirip, butona tiklama  
4. Acilan sayfada Quality Assurance ekibini seçme  
5. Açılan listede lokasyon “Istanbul, Turkey” ve departman “Quality Assurance” filtresinden secme
6. Quality Assurance rolunu secme ve “View Role” linkine tiklama
7. Lever başvuru formuna yönlendirme

---

## Kurulum

> **On koşullar**  
> * Java 17+  
> * Maven 3.8+  
> * Chrome 116+ (ChromeDriver sürümü otomatik yönetilir)

```bash
git clone https://github.com/umitberat/umitberatadar_SeleniumTask2Project.git
cd umitberatadar_SeleniumTask2Project
mvn clean test
