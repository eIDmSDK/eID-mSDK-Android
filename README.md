# eID-SDK (v1.3.4)

eID mSDK je zverejnené v Maven repozitáry: https://github.com/eIDmSDK/eID-mSDK-Android/packages
V tomto repozitáry je zverejnená Android sample aplikácia demonštrujúca použitie eID mSDK. 

## Inštalácia

setting.gradle

```groovy
repositories {
    maven {
        url = "https://maven.pkg.github.com/eIDmSDK/eID-mSDK-Android/"
        credentials {
            username = "eIDmSDK"
            password = "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"
        }
    }
}
```

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
3.	**Zobrazenie údajov** z občianskeho preukazu (biometrické a textové dáta)
4.	**Zobrazenie certifikátov** z občianskeho preukazu
5.	**Manažment znalostných faktorov** (BOK, KEP PIN, PUK) 

### Inicializácia

eID SDK je potrebné pred zavolaním akejkoľvek funkcie **inicializovať**. Inicializáciu je nutné vykonať len raz počas životného cyklu aplikácie, pomocou volania:

```kotlin
EIDHandler.initialize(this)
```

**Poznámka**: inicializáciu odporúčame vykonať hneď po spustení aplikácie v Application class projektu

<br>

### Autentifikácia

Registrácia Activity result launcher-a:

```kotlin
 authenticationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val idToken = result.data?.getStringExtra("ID_TOKEN")
        // Process idToken
    } else if (result.resultCode == Activity.RESULT_CANCELED) {
        // Retrieve exception
        val exception = result.data?.extras?.getSerializable("EXCEPTION") as Throwable?
    }
}
```

Spustenie procesu autentifikácie:

```kotlin
EIDHandler.startAuth(AUTH_CLIENT_ID, AUTH_CLIENT_SECRET, this, authenticationLauncher)
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
EIDHandler.getCertificates(this, getCertificatesLauncher)
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
EIDHandler.startSign(certIndex, signatureScheme, dataToSignEncoded, this, signLauncher)
```

<br>

### Zobrazenie certifikátov z občianskeho preukazu

Registrácia Activity result launcher-a:

```kotlin
noActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
```

Spustenie procesu zobrazenia údajov:

```kotlin
EIDHandler.startCertificates(this, noActionLauncher)
```

<br>

### Manažment znalostných faktorov (BOK, KEP PIN, PUK) 

Registrácia Activity result launcher-a:

```kotlin
noActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
```

Spustenie procesu manažmentu znalostných faktorov:

```kotlin
EIDHandler.startPINManagement(this, noActionLauncher)
```

