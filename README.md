# Depo Alım Satım Uygulaması

Bu proje, Java Swing kullanılarak geliştirilmiş, kullanıcıların depo alım satımı yapabileceği bir masaüstü uygulamasıdır. Kullanıcılar, farklı boyutlardaki depoları satın alabilir, stok bilgilerini görüntüleyebilir ve bakiye işlemlerini yönetebilirler. Yönetici ise siparişleri onaylayabilir/reddedebilir ve kullanıcıları yönetebilir.

## Özellikler

* **Kullanıcı Yönetimi:**
    * Kayıt ve giriş işlemleri
    * Bakiye ekleme/görüntüleme
    * Depo satın alma
    * Sipariş takibi
    * Depo stok bilgilerini görüntüleme.
* **Yönetici Yönetimi:**
    * Sipariş onayı/reddi
    * Kullanıcı yönetimi
    * Sipariş geçmişini görüntüleme.
* **Veritabanı:**
    * MSSQL kullanılarak veritabanı işlemleri gerçekleştirilmiştir.

## Teknolojiler

* **Java Swing:** Kullanıcı arayüzü için
* **Maven:** Bağımlılık yönetimi ve proje yapılandırması
* **FlatLaf:** Modern ve özelleştirilebilir görünüm teması
* **MigLayout:** Esnek ve güçlü layout yönetimi
* **Swing Blur Background:** Pencere arka planı için bulanıklık efekti
* **Swing Glasspane Popup:** Özel açılır pencereler
* **TwelveMonkeys Common Image:** Gelişmiş görüntü işleme
* **MSSQL:** Veritabanı yönetimi için

## Kurulum

1.  Projeyi GitHub'dan klonlayın:

    ```bash
    git clone [repo_url]
    ```

2.  Maven kullanarak bağımlılıkları yükleyin:

    ```bash
    mvn clean install
    ```

3.  MSSQL veritabanını kurun ve yapılandırın.
4.  Uygulamayı çalıştırın:

    ```bash
    mvn exec:java -Dexec.mainClass="[main_class_name]"
    ```

    *Not: \[repo\_url] ve \[main\_class\_name] değerlerini kendi projenize göre değiştirin.*

    Uygulama giriş ekranı:

    ![Giriş Ekranı](images/giris_ekrani.png)

## Kullanım

1.  Uygulamayı çalıştırdıktan sonra, kullanıcı veya yönetici olarak giriş yapabilirsiniz.
2.  Kullanıcılar, bakiye ekleyebilir, depo satın alabilir ve siparişlerini takip edebilirler.
3.  Yöneticiler, siparişleri onaylayabilir/reddedebilir ve kullanıcıları yönetebilirler.

    Kullanıcı arayüzü aşağıdaki gibidir:

    ![Kullanıcı Arayüzü](images/kullanici_arayuzu.png)

    Kullanıcılar bu arayüz üzerinden depo satın alabilir ve bakiyelerini yönetebilirler.

## Yönetici Yönetimi

Yönetici paneli aşağıdaki özelliklere sahiptir:

### Üyeleri Görüntüleme

![Üyeleri Görüntüleme](images/admin_uyeler.png)

Yöneticiler bu ekran üzerinden kullanıcıları görüntüleyebilirler.

### Bekleyen Depo İstekleri

![Bekleyen Depo İstekleri](images/admin_istekler.png)

Yöneticiler bu ekran üzerinden bekleyen depo isteklerini onaylayabilir veya reddedebilirler.

### Depoları Görüntüleme

![Depoları Görüntüleme](images/admin_depolar.png)

Yöneticiler bu ekran üzerinden depoları görüntüleyebilirler.

## Katkıda Bulunma

Katkıda bulunmak isterseniz, lütfen bir "pull request" gönderin veya "issue" açın.

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## İletişim

Sorularınız veya önerileriniz için \[e-posta adresiniz] ile iletişime geçebilirsiniz.
