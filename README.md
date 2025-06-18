# Papago Translate Android Library

안드로이드 앱에서 Papago 번역 API를 쉽게 사용할 수 있는 라이브러리입니다.

## 주요 기능

- 🌐 Papago 번역 API 연동
- 💾 번역 결과 자동 캐싱
- 🎯 커스텀 TextView 컴포넌트
- 🔄 자동 언어 감지 및 번역
- ⚡ 코루틴 기반 비동기 처리

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

### 2. XML에서 사용

```xml
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

## 라이선스

MIT License - 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 문의사항

버그 리포트나 기능 요청은 [Issues](https://github.com/yourusername/papago-translate-android/issues)를 이용해 주세요.

## 변경 이력

### 1.0.0
- 초기 릴리즈
- Papago 번역 API 연동
- 자동 캐싱 기능
- 커스텀 TextView 컴포넌트 