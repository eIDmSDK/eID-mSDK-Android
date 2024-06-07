# eID-SDK (v1.4.0)

eID mSDK je zverejnené v Maven repozitári: https://github.com/eIDmSDK/eID-mSDK-Android/packages<br>
V tomto repozitári je zverejnená Android sample aplikácia demonštrujúca použitie eID mSDK. 

## Inštalácia

setting.gradle

```groovy
repositories {
    maven {
        url = "https://maven.pkg.github.com/eIDmSDK/eID-mSDK-Android/"
        credentials {
            username = "eIDmSDK"
            password = "PERSONAL_ACCESS_TOKEN"
        }
    }
}
```
PERSONAL_ACCESS_TOKEN nájdete na https://eidas.minv.sk/download/ v časti "eID mSDK (pre vývojárov mobilných aplikácii)"<br><br>

build.gradle

```groovy
dependencies {
    implementation 'sk.eid:eid-sdk:x.x.x'
}
```

<br>

## Použitie SDK

**SDK** poskytuje nasledujúce okruhy **funkcionalít**:
1.	**Autentifikácia** osoby na **najvyššej úrovni** zabezpečenia (Vysoká) podľa eIDAS
2.	Vyhotovenie **kvalifikovaného elektronického podpisu**
3.	**Zobrazenie certifikátov** z občianskeho preukazu
4.	**Manažment znalostných faktorov** (BOK, KEP PIN, PUK) 

### Inicializácia

eID SDK je potrebné pred zavolaním akejkoľvek funkcie **inicializovať**. Inicializáciu je nutné vykonať len raz počas životného cyklu aplikácie, pomocou volania:

```kotlin
EIDHandler.initialize(this, EIDEnvironment.MINV_PROD)
```

**Poznámka**: inicializáciu odporúčame vykonať hneď po spustení aplikácie v Application class projektu

<br>

### Autentifikácia

Registrácia Activity result launcher-a:

```kotlin
 authenticationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        // Retrieve ID token from eID SDK
        val idToken = result.data?.getStringExtra("ID_TOKEN")
        // Process idToken
        
        // Retrieve Auth code from eID SDK
        val authCode = result.data?.getStringExtra("AUTH_CODE")
        // Process Auth code
    } else if (result.resultCode == Activity.RESULT_CANCELED) {
        // Retrieve exception
        val exception = result.data?.extras?.getSerializable("EXCEPTION") as Throwable?
    }
}
```

Spustenie procesu autentifikácie:

Auth code flow:

```kotlin
EIDHandler.startAuth(CLIENT_ID, CLIENT_SECRET, API_KEY_ID?, API_KEY_VALUE?, this, authenticationLauncher, language)
```

Implicit flow:

```kotlin
EIDHandler.startAuth(CLIENT_ID, API_KEY_ID?, API_KEY_VALUE?, this, authenticationLauncher, language)
```

Auth code / Implicit flow:

```kotlin
EIDHandler.startAuth(CLIENT_ID, CLIENT_SECRET?, API_KEY_ID?, API_KEY_VALUE?, this, authenticationLauncher, EIDAuthenticationFlow, language)
```

<br>

### Vyhotovenie kvalifikovaného elektronického podpisu

Registrácia Activity result launcher-a pre získanie certifikátov:

```kotlin
getCertificatesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val certificatesJson = result.data?.getStringExtra("CERTIFICATES")
        // Process certificates JSON
    }
}
```

Spustenie procesu získania certifikátov:

```kotlin
EIDHandler.startCertificates(this, generalActionLauncher, language)
```

Registrácia Activity result launcher-a pre získanie podpísaných dát:

```kotlin
signLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val signedDataEncoded = result.data?.getStringExtra("SIGNED_DATA")
        // Process signed data 
    }
}
```

Spustenie procesu podpisu:

```kotlin
EIDHandler.startSign(certIndex, signatureScheme, dataToSignEncoded, this, signLauncher, language)
```

<br>

### Zobrazenie certifikátov z občianskeho preukazu

Registrácia Activity result launcher-a:

```kotlin
noActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
```

Spustenie procesu zobrazenia údajov:

```kotlin
EIDHandler.startCertificates(this, noActionLauncher, language)
```

<br>

### Manažment znalostných faktorov (BOK, KEP PIN, PUK) 

Registrácia Activity result launcher-a:

```kotlin
noActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
```

Spustenie procesu manažmentu znalostných faktorov:

```kotlin
EIDHandler.startPINManagement(this, noActionLauncher, language)
```

<br>

### Knižnice tretích strán
eID mSDK v rámci svojej funkčnosti používa nasledujúce knižnice s otvoreným zdrojovým kódom:

Koin (Apache License 2.0) - https://github.com/InsertKoinIO/koin/blob/main/LICENSE<br>
Retrofit (Apache License 2.0) - https://github.com/square/retrofit/blob/master/LICENSE.txt<br>
OKHTTP3 (Apache License 2.0) - https://github.com/square/okhttp/blob/master/LICENSE.txt<br>
Timber (Apache License 2.0) - https://github.com/JakeWharton/timber/blob/trunk/LICENSE.txt<br>
Glide (BSD, MIT, Apache License 2.0) - https://github.com/bumptech/glide/blob/master/LICENSE<br>
JMRTD (LGPL License) - https://jmrtd.org/license.shtml<br>
Bouncycastle (MIT License) - https://www.bouncycastle.org/licence.html<br>
EJBCA (LGPL License) - https://github.com/Keyfactor/ejbca-ce/blob/main/LICENSE<br>
SCUBA (LGPL License) - https://mvnrepository.com/artifact/net.sf.scuba/scuba-sc-android/0.0.23<br>
JP2ForAndroid (BSD 2-Clause License) - https://github.com/ThalesGroup/JP2ForAndroid/blob/master/LICENSE<br>
JNBIS (Apache License 2.0) - https://github.com/mhshams/jnbis/blob/main/LICENSE<br>
Lottie - (Apache License 2.0) - https://github.com/airbnb/lottie-android/blob/master/LICENSE<br>
Rootbeer (Apache License 2.0) - https://github.com/scottyab/rootbeer/blob/master/LICENSE<br>
