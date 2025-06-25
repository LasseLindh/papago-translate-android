# 프로젝트 요구사항 (Requirements)

## 1. 프로젝트 개요
- Papago 기반 번역 Android 라이브러리
- 커스텀 TextView를 통한 자동 번역 기능 제공

## 2. 주요 기능
- 텍스트 자동 번역 기능
- 다양한 언어 지원
- 커스텀 TextView(PapagoTextView) 제공
- 번역 결과 캐싱 기능

## 3. 기술 스택
- Android (Kotlin/Java)
- Gradle

## 4. 오픈소스 및 라이선스 고지
- Apache License 2.0, MIT, BSD 등 다양한 라이선스 고지
- 각 라이브러리별 라이선스 전문 포함

## 5. 기타 요구사항
- GitHub 저장소와 소스 동기화 유지
- 불필요한 파일(테스트/임시/스크립트 등) 정리
- README, REQUIREMENTS 등 문서화 파일 포함

## 6. 주요 요구 프롬프트(명령어/지시문)
- Android TextView를 상속받아 Papago API를 활용한 자동 번역 기능을 구현하라.
- 번역 결과를 캐싱하여 동일 문장에 대해 API 호출을 최소화하라.
- 번역 언어, 원본 언어, 번역 옵션을 XML 속성으로 지정할 수 있도록 하라.
- 프로젝트는 Gradle 기반으로 구성하라.
- README.md, REQUIREMENTS.md 등 문서화 파일을 포함하라.
- GitHub 저장소와 동기화가 잘 되도록 .gitignore, 라이선스 파일 등을 포함하라.

## 7. 사용법
1. 이 프롬프트(요구사항)를 Cursor, ChatGPT, Copilot 등 AI 개발 도구에 입력한다.
2. AI가 요구사항에 맞는 Android 라이브러리를 자동 생성한다.
3. 생성된 소스코드를 GitHub 저장소에 업로드하여 관리한다.

## 8. 참고 예시
```
Android에서 사용할 수 있는 Papago 기반 자동 번역 TextView 라이브러리를 만들어줘.  
번역 결과를 캐싱하고, XML 속성으로 번역 옵션을 설정할 수 있도록 해줘.  
프로젝트는 Gradle 기반으로, 문서화 파일도 포함해줘. 