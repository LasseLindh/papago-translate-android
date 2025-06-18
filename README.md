# PapagoTextView - Auto Translate TextView

안드로이드 앱에서 TextView를 대체하여 **자동 번역 기능**을 제공하는 CustomView 라이브러리입니다. 이 라이브러리는 **Cursor IDE**를 통해 개발되었으며, Git 버전 관리와 Maven 배포 과정도 Cursor의 AI 기능을 활용하여 진행되었습니다.

## 주요 기능

- 🎯 **TextView 대체**: 기존 TextView를 완전히 대체하는 CustomView
- 🌐 **자동 번역**: Papago 번역 API를 통한 실시간 자동 번역
- 💾 **번역 결과 캐싱**: 중복 번역 방지를 위한 자동 캐싱
- 🔄 **언어 자동 감지**: 입력 텍스트의 언어를 자동으로 감지
- ⚡ **코루틴 기반**: 비동기 처리로 UI 블로킹 방지
- 🚀 **Cursor IDE**: AI 기반 개발 및 배포

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
    implementation 'com.lasse.language:papago-translate:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.lasse.language</groupId>
    <artifactId>papago-translate</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 사용법

### 1. 초기화

```kotlin
// Application 클래스나 MainActivity에서 초기화
PapagoTextView.initialize(
    clientId = "YOUR_PAPAGO_CLIENT_ID",
    clientSecret = "YOUR_PAPAGO_CLIENT_SECRET",
    autoTranslateMode = true // 자동 번역 모드 (기본값: true)
)
```

### 2. XML에서 TextView 대체

```xml
<!-- 기존 TextView 대신 PapagoTextView 사용 -->
<com.lasse.language.util.translate.PapagoTextView
    android:id="@+id/translateTextView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="안녕하세요"
    android:textSize="16sp" />
```

### 3. 프로그래밍 방식으로 사용

```kotlin
val papagoTextView = findViewById<PapagoTextView>(R.id.translateTextView)

// 수동 번역 모드로 설정
papagoTextView.setAutoTranslateMode(false)
papagoTextView.setSourceLanguage("ko")
papagoTextView.setTargetLanguage("en")

// 텍스트 설정 (자동 번역됨)
papagoTextView.setText("안녕하세요")
```

### 4. 캐시 관리

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

### 1.0.0
- 초기 릴리즈
- TextView 대체 CustomView 구현
- Papago 번역 API 연동
- 자동 캐싱 기능
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