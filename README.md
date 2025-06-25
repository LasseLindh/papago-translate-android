# PapagoTextView - Auto Translate TextView

안드로이드 앱에서 TextView를 대체하여 **자동 번역 기능**을 제공하는 CustomView 라이브러리입니다. 이 라이브러리는 **Cursor IDE**를 통해 개발되었으며, Git 버전 관리와 Maven 배포 과정도 Cursor의 AI 기능을 활용하여 진행되었습니다.

## 주요 기능

- 🎯 **TextView 대체**: 기존 TextView를 완전히 대체하는 CustomView
- 🌐 **자동 번역**: Papago 번역 API를 통한 실시간 자동 번역
- 💾 **번역 결과 캐싱**: 중복 번역 방지를 위한 자동 캐싱
- 🔄 **언어 자동 감지**: 입력 텍스트의 언어를 자동으로 감지
- ⚡ **코루틴 기반**: 비동기 처리로 UI 블로킹 방지
- 🚀 **Cursor IDE**: AI 기반 개발 및 배포
- 📱 **XML 설정**: XML에서 직접 언어 및 번역 옵션 설정 가능
- 🔧 **다양한 초기화 방법**: 소스코드 또는 AndroidManifest에서 설정 가능

## 개발 환경

- **IDE**: Cursor (AI 기반 코드 에디터)
- **언어**: Kotlin
- **플랫폼**: Android
- **버전 관리**: Git
- **배포**: Maven Central / JitPack

## 설치 방법

### Gradle (권장)

```gradle
dependencies {
    implementation 'com.lasse.language:papago-translate:1.1.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.lasse.language</groupId>
    <artifactId>papago-translate</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 초기화 방법

PapagoTextView는 두 가지 방법으로 초기화할 수 있습니다:

### 방법 1: AndroidManifest를 통한 초기화 (권장)

소스코드를 전혀 건드리지 않고 AndroidManifest에서 설정할 수 있습니다.

#### 1. AndroidManifest.xml 설정

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:name=".TranslateApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.YourApp"
        tools:targetApi="31">
        
        <!-- Papago API 설정 -->
        <meta-data
            android:name="papago_client_id"
            android:value="YOUR_PAPAGO_CLIENT_ID" />
        <meta-data
            android:name="papago_client_secret"
            android:value="YOUR_PAPAGO_CLIENT_SECRET" />
        <meta-data
            android:name="papago_auto_translate_mode"
            android:value="true" />
            
    </application>

</manifest>
```

#### 2. XML에서 바로 사용

```xml
<!-- 별도의 초기화 코드 없이 바로 사용 가능 -->
<com.lasse.language.util.translate.PapagoTextView
    android:id="@+id/translateTextView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="안녕하세요"
    android:textSize="16sp" />
```

**장점:**
- ✅ 소스코드 수정 불필요
- ✅ 앱 시작 시 자동 초기화
- ✅ 설정 변경 시 AndroidManifest만 수정
- ✅ 기존 코드와 완전히 분리

### 방법 2: 소스코드를 통한 초기화

프로그래밍 방식으로 직접 초기화할 수 있습니다.

#### 1. Application 클래스에서 초기화

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // PapagoTextView 초기화
        PapagoTextView.initialize(
            clientId = "YOUR_PAPAGO_CLIENT_ID",
            clientSecret = "YOUR_PAPAGO_CLIENT_SECRET",
            autoTranslateMode = true // 자동 번역 모드 (기본값: true)
        )
    }
}
```

#### 2. MainActivity에서 초기화

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // PapagoTextView 초기화
        PapagoTextView.initialize(
            clientId = "YOUR_PAPAGO_CLIENT_ID",
            clientSecret = "YOUR_PAPAGO_CLIENT_SECRET",
            autoTranslateMode = true
        )
    }
}
```

**장점:**
- ✅ 런타임에 동적으로 설정 변경 가능
- ✅ 조건부 초기화 가능
- ✅ 더 세밀한 제어 가능

## 사용법

### XML에서 TextView 대체

#### 기본 사용법
```xml
<!-- 기존 TextView 대신 PapagoTextView 사용 -->
<com.lasse.language.util.translate.PapagoTextView
    android:id="@+id/translateTextView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="안녕하세요"
    android:textSize="16sp" />
```

#### 고급 설정 (언어 및 옵션 지정)
```xml
<com.lasse.language.util.translate.PapagoTextView
    android:id="@+id/advancedTranslateTextView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Hello, how are you?"
    android:textSize="16sp"
    
    <!-- 번역 모드 설정 -->
    app:autoTranslateMode="true"
    
    <!-- 언어 설정 -->
    app:sourceLanguage="en"
    app:targetLanguage="ko"
    
    <!-- 번역 옵션 -->
    app:translationDelay="1000"
    app:useCache="true"
    app:showOriginalOnError="true"
    app:showToastOnComplete="false" />
```

#### 지원하는 XML 속성

| 속성 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `autoTranslateMode` | boolean | true | 자동 번역 모드 활성화/비활성화 |
| `sourceLanguage` | string | "ko" | 원본 언어 코드 (ko, en, ja, zh-CN, zh-TW, vi, th, id, fr, es, ru, de, it) |
| `targetLanguage` | string | "en" | 번역할 언어 코드 |
| `translationDelay` | integer | 0 | 번역 시작 전 지연 시간 (밀리초) |
| `useCache` | boolean | true | 번역 결과 캐싱 사용 여부 |
| `showOriginalOnError` | boolean | true | 번역 실패 시 원본 텍스트 표시 여부 |
| `showToastOnComplete` | boolean | true | 번역 완료 시 토스트 메시지 표시 여부 |

### 프로그래밍 방식으로 사용

```kotlin
val papagoTextView = findViewById<PapagoTextView>(R.id.translateTextView)

// 수동 번역 모드로 설정
papagoTextView.setAutoTranslateMode(false)
papagoTextView.setSourceLanguage("ko")
papagoTextView.setTargetLanguage("en")

// 텍스트 설정 (자동 번역됨)
papagoTextView.setText("안녕하세요")

// 전역 자동번역 모드 설정 변경
PapagoTextView.setGlobalAutoTranslateMode(false)

// 초기화 상태 확인
if (PapagoTextView.isInitialized()) {
    // 초기화 완료된 상태
}
```

### 캐시 관리

```kotlin
// 캐시 정보 확인
val cacheInfo = papagoTextView.getCacheInfo()
Log.d("Cache", cacheInfo)

// 캐시 삭제
papagoTextView.clearCache()

// 현재 텍스트의 번역 개수 확인
val translationCount = papagoTextView.getTranslationCountForCurrentText()
```

## API 키 발급

1. [Naver Cloud Platform](https://www.ncloud.com/)에 가입
2. Papago API 서비스 신청
3. API Gateway에서 API 키 발급
4. Client ID와 Client Secret 확인

## 지원 언어

- 한국어 (ko)
- 영어 (en)
- 일본어 (ja)
- 중국어 (zh-CN, zh-TW)
- 베트남어 (vi)
- 태국어 (th)
- 인도네시아어 (id)
- 프랑스어 (fr)
- 스페인어 (es)
- 러시아어 (ru)
- 독일어 (de)
- 이탈리아어 (it)

기타 [Papago API에서 지원하는 언어](https://api.ncloud-docs.com/docs/ai-naver-papagonmt-translation)는 공식 문서를 참조하세요.

## 변경 이력

### 1.1.0
- AndroidManifest를 통한 초기화 기능 추가
- 소스코드 없이 AndroidManifest에서 API 키 설정 가능
- TranslateApplication 클래스 추가
- 두 가지 초기화 방법 지원 (AndroidManifest / 소스코드)
- 초기화 방법별 장점 명시

### 1.0.0
- 초기 릴리즈
- TextView 대체 CustomView 구현
- Papago 번역 API 연동
- 자동 캐싱 기능
- XML 속성을 통한 언어 및 옵션 설정 지원
- Cursor IDE를 통한 AI 기반 개발 및 배포

## 라이선스

MIT License

Copyright (c) 2024 Lasse

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. 