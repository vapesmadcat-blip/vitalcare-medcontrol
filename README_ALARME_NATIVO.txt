VitalCare MedControl - Alarme nativo Android

Arquivos principais:
- www/index.html
- package.json
- capacitor.config.json
- .github/workflows/build-apk.yml
- native/android/app/src/main/java/com/vitalcare/medcontrol/*.java

Como usar no GitHub pelo celular:
1. Substitua o seu www/index.html pelo arquivo desta pasta.
2. Substitua o package.json, capacitor.config.json e .github/workflows/build-apk.yml.
3. Crie no repo os arquivos Java nos caminhos:
   native/android/app/src/main/java/com/vitalcare/medcontrol/MainActivity.java
   native/android/app/src/main/java/com/vitalcare/medcontrol/MedicationAlarmPlugin.java
   native/android/app/src/main/java/com/vitalcare/medcontrol/AlarmReceiver.java
   native/android/app/src/main/java/com/vitalcare/medcontrol/AlarmActivity.java
4. Rode Actions > Build APK.

Observação:
A pasta android/ é criada automaticamente pelo GitHub Actions.
Não precisa subir android/ manualmente.
