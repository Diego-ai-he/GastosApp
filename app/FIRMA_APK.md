# Generación de APK Firmado

## Proceso realizado:

### 1. Creación del Keystore
- **Ubicación:** `C:\Users\diego\gastosapp-keystore.jks`
- **Alias:** gastosapp-key
- **Algoritmo:** RSA 2048 bits
- **Validez:** 25 años

### 2. Firma del APK
- **Herramienta:** Android Studio Build Tools
- **Signature Versions:** V1 (Jar) + V2 (Full APK)
- **Build Variant:** Release

### 3. APK Generado
- **Ubicación:** `app/release/app-release.apk`
- **Firmado:** ✅ Sí
- **Listo para distribución:** ✅ Sí

## Comandos de verificación:
```bash
# Verificar firma del APK
jarsigner -verify -verbose -certs app-release.apk

# Ver información del keystore
keytool -list -v -keystore gastosapp-keystore.jks -alias gastosapp-key
```

## Instalación en dispositivo:
```bash
adb install app-release.apk
```