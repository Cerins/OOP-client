# OOP-client - AndroidStudio projekts

## Implementācijas

- Android Studio Hedgehog (vai jaunākas versijas)

- Kotlin programmēšanas valoda

- Gradlew v8.2.2

- OkHttp v4.12.0

- Jetpack navigācijai

- Hilt v2.48 atkarību injekcijas bibliotēka no Dagger

- Korutīnas v1.7.3 asinhronā koda vienkāršošanai

- Retrofit v2.9.0 tīkla pieprasījumiem

- Datastore preferences v1.1.1. tokenu un citu vērtību glabāšanai

## Darba uzsākšana

### Priekšnosacījumi

1. Android Studio: Lejupielādējiet un instalējiet Android Studio Hedgehog vai jaunāku versiju.

2. Git: Pārliecinieties, ka jums ir instalēts Git, lai klonētu repozitoriju.

3. OOP-serveris: Pabeidziet servera puses uzstādīšanas procesu pirms mēģināt sākt darbu ar klienta puses projektu.

Skatiet README.md no https://github.com/Cerins/OOP-server

### Instalēšana

#### 1) Klonēt repozitoriju

Klonējiet repozitoriju uz savu lokālo mašīnu, izmantojot SSH vai HTTPS.

SSH piemērs:

  ```shell
    git clone git@github.com:Cerins/OOP-client.git
  ```

HTTPS piemērs:

  ```shell
    git clone https://github.com/Cerins/OOP-client
  ```



#### 2) Atvērt projektu
  
  Atveriet projektu Android Studio:

  - Pārejiet uz direktoriju, kurā klonējāt repozitoriju.

  - Atlasiet OOP-Client mapi.

  - Noklikšķiniet uz "Open".



#### 3) Konfigurēt IP adresi

Lai iestatītu savu IP adresi NetworkModule failā, veiciet šādas darbības:

  1. Atveriet NetworkModule.kt failu, kas atrodas di paketē.

  2. Atrodiet līniju, kurā ir definēts bāzes URL Retrofit veidotājā:

    ```
     private const val BASE_URL = "http://insert-IP-address-here:8080"
    ```

  4. Nomainiet vietturi ar savu faktisko IP adresi, atbilstoši aizmugures risinājumam.



#### 4) Būvēt projektu

Pagaidiet, līdz Gradle būve ir pabeigta. Tas var aizņemt dažas minūtes, jo tiks lejupielādētas un konfigurētas visas atkarības.

### Lietotnes palaišana

1. Pievienojiet ierīci vai palaidiet emulatoru

Pārliecinieties, ka jums ir pievienota Android ierīce vai darbojas Android emulators.

2. Palaidiet lietotni

Noklikšķiniet uz "Run" pogas Android Studio vai nospiediet Shift + F10, lai būvētu un palaistu lietotni uz izvēlētās ierīces/emulatora.

### Papildus informācija:

    Daļa no “Back-End” funkcionalitātēm vēl netiek atbalstīta “Front-End” pusē;
    Darbs tika izstrādāts grupā.
    Projekts balstīts uz projektējumu, kas izstrādāts "Programminženierija" kursa ietvaros. Dokuments papildināts ar izvietojuma un programmas modeļu klašu diagrammu: https://docs.google.com/document/d/1Yi3KF1tdhZ42VxTFy1Twajkgzo7QjPu6B03mGTWt80s/edit?usp=sharing

