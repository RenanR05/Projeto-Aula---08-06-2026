# 📍 GeoLocation App — KMP

Projeto acadêmico desenvolvido para a disciplina de **Programação para Dispositivos Móveis II** da **Universidade do Vale do Itajaí (UNIVALI)**.

O aplicativo é multiplataforma (Android e iOS), desenvolvido com **Kotlin Multiplatform (KMP)** e **Compose Multiplatform**, e tem como objetivo recuperar e exibir a geolocalização do usuário em tempo real.

---

## 👥 Integrantes

| Nome | |
|---|---|
| Renan Regis | |
| Luan Regis | |

**Professor:** Welington Gadelha
**Disciplina:** Programação para Dispositivos Móveis II

---

## 📱 O que o app faz

- Solicita permissão de localização ao usuário de forma amigável
- Trata os casos de permissão negada e permanentemente negada
- Exibe em tempo real as coordenadas **Latitude**, **Longitude** e **Precisão** do dispositivo
- Permite atualização manual das coordenadas via botão na tela

---

## 🛠️ Tecnologias Utilizadas

- **Kotlin Multiplatform (KMP)** — lógica de negócio compartilhada entre Android e iOS
- **Compose Multiplatform** — UI 100% compartilhada em `commonMain`
- **calf-permissions** — gerenciamento de permissões cross-platform
- **FusedLocationProviderClient** (Android) — localização via Google Play Services
- **CLLocationManager / CoreLocation** (iOS) — localização nativa iOS
- **Kotlin Coroutines + Flow** — atualização reativa das coordenadas

---

## 🏗️ Arquitetura

O projeto segue **Clean Architecture** com três camadas bem definidas:

```
┌─────────────────────────────────────────────┐
│                  UI Layer                    │
│  App · PermissionScreen · DashboardScreen   │
│  DashboardViewModel                         │
├─────────────────────────────────────────────┤
│               Domain Layer                   │
│  LocationData · LocationRepository          │
├─────────────────────────────────────────────┤
│                Data Layer                    │
│  LocationRepositoryImpl                     │
│  AndroidLocationService · IOSLocationService│
└─────────────────────────────────────────────┘
```

---

## 📁 Estrutura do Projeto

```
GeoLocationApp/
├── shared/
│   └── src/
│       ├── commonMain/        # Código compartilhado (UI, Domain, Data)
│       ├── androidMain/       # Implementação nativa Android
│       └── iosMain/           # Implementação nativa iOS
├── androidApp/                # Entry point Android
└── iosApp/                    # Entry point iOS (Xcode)
```

---

## ▶️ Como Executar

### Android
1. Abra a pasta do projeto no **Android Studio**
2. Sincronize o Gradle
3. Selecione o módulo `androidApp` e clique em **Run**

### iOS
1. Abra a pasta `iosApp` no **Xcode** (necessário Mac)
2. Selecione um simulador e pressione **Run (⌘R)**

> O simulador iOS não possui GPS real. Use um dispositivo físico ou simule coordenadas via `Debug > Simulate Location` no Xcode.

---

## ⚙️ Dificuldades Encontradas

**Compatibilidade de bibliotecas com Kotlin 2.1**
A biblioteca `moko-permissions`, muito utilizada no ecossistema KMP, apresentou conflitos com Kotlin 2.1.20 e Compose Multiplatform 1.7+. A solução foi adotar a `calf-permissions`, mais moderna e com suporte nativo ao Compose Multiplatform.

**Injeção de dependência entre plataformas**
O `CLLocationManager` (iOS) e o `FusedLocationProviderClient` (Android) possuem contextos e ciclos de vida bem diferentes. A solução foi definir uma interface `LocationService` em `commonMain` e criar implementações específicas em `androidMain` e `iosMain`, com instâncias injetadas nos entry points de cada plataforma.

**Distinção entre "negado" e "negado permanentemente" no Android**
O Android não expõe diretamente essa informação. A distinção é feita verificando `shouldShowRationale` após a negativa — se retornar `false` e a permissão não foi concedida, o usuário selecionou "Não perguntar novamente".
