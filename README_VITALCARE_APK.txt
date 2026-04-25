VitalCare MedControl - Pacote completo para GitHub Actions

Como usar pelo celular:
1. Extraia este ZIP.
2. No GitHub, suba/substitua estes arquivos no repositório mantendo a estrutura das pastas.
3. Confirme que existem:
   - www/index.html
   - package.json
   - capacitor.config.json
   - native/android/app/src/main/java/com/vitalcare/medcontrol/MainActivity.java
   - native/android/app/src/main/java/com/vitalcare/medcontrol/AlarmScheduler.java
   - native/android/app/src/main/java/com/vitalcare/medcontrol/AlarmReceiver.java
   - native/android/app/src/main/java/com/vitalcare/medcontrol/AlarmActivity.java
   - native/android/app/src/main/AndroidManifest.xml
   - .github/workflows/build-apk.yml
4. Rode Actions > Build APK > Run workflow.
5. Baixe o artifact VitalCare-MedControl-APK.

Teste:
- Instale o APK.
- Abra o app e toque em Alertas para aceitar permissão.
- Crie/agende uma dose para +2 minutos.
- Feche o app e bloqueie a tela.

Observação:
Este pacote usa Java nativo, não Kotlin, para evitar erro de Gradle/Kotlin no Capacitor gerado automaticamente.
