VitalCare Tech · MedControl APK

Este pacote já está preparado como projeto Capacitor para Android.
Ele inclui:
- www/index.html com o sistema MedControl
- modo dinâmico e modo fixo/ancorado
- plugin de notificações locais nativas
- agendamento de alerta quando inicia dose e quando gera próxima dose

COMANDOS NO PC:
1) Instale Node.js LTS e Android Studio.
2) Abra esta pasta no PowerShell/CMD.
3) Rode:
   npm install
   npx cap add android
   npx cap sync android
   npx cap open android

No Android Studio:
- espere sincronizar
- conecte o celular em modo desenvolvedor/depuração USB
- clique Run para instalar

Para gerar APK:
Android Studio > Build > Build Bundle(s) / APK(s) > Build APK(s)

Observação:
Notificações nativas dependem das permissões do Android. No primeiro uso, toque em "Alertas" e permita notificações.
