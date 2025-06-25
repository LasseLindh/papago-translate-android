# 코딩 규칙 및 표준 (Coding Standards)

## 1. 일반적인 코딩 원칙

### 1.1 가독성 (Readability)
- 코드는 읽기 쉽고 이해하기 쉬워야 한다
- 명확한 변수명과 함수명 사용
- 적절한 들여쓰기와 공백 사용
- 주석을 통해 복잡한 로직 설명

### 1.2 유지보수성 (Maintainability)
- 코드 중복 최소화
- 모듈화된 구조 사용
- 일관된 코딩 스타일 유지
- 테스트 가능한 코드 작성

### 1.3 확장성 (Scalability)
- 확장 가능한 아키텍처 설계
- 인터페이스와 추상화 활용
- 의존성 주입 패턴 사용

## 2. 네이밍 규칙

### 2.1 변수명 (Variables)
```kotlin
// 좋은 예
val userName = "John"
val isUserLoggedIn = true
val maxRetryCount = 3

// 나쁜 예
val u = "John"
val flag = true
val num = 3
```

### 2.2 함수명 (Functions)
```kotlin
// 좋은 예
fun getUserById(id: String): User
fun isValidEmail(email: String): Boolean
fun calculateTotalPrice(): Double

// 나쁜 예
fun get(id: String): User
fun check(email: String): Boolean
fun calc(): Double
```

### 2.3 클래스명 (Classes)
```kotlin
// 좋은 예
class UserRepository
class NetworkManager
class TranslationService

// 나쁜 예
class userRepo
class netMgr
class transSvc
```

### 2.4 상수명 (Constants)
```kotlin
// 좋은 예
const val MAX_RETRY_COUNT = 3
const val API_BASE_URL = "https://api.example.com"
const val DEFAULT_TIMEOUT = 30000L

// 나쁜 예
const val max = 3
const val url = "https://api.example.com"
const val timeout = 30000L
```

## 3. 코드 구조

### 3.1 파일 구조
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── package/
│   │           ├── model/
│   │           ├── view/
│   │           ├── controller/
│   │           ├── service/
│   │           └── util/
│   └── res/
└── test/
```

### 3.2 클래스 구조
```kotlin
class ExampleClass {
    // 1. 상수
    companion object {
        private const val TAG = "ExampleClass"
    }
    
    // 2. 프로퍼티
    private var property1: String = ""
    private var property2: Int = 0
    
    // 3. 초기화 블록
    init {
        // 초기화 코드
    }
    
    // 4. 생성자
    constructor()
    constructor(param: String)
    
    // 5. public 메서드
    fun publicMethod() {
        // 구현
    }
    
    // 6. private 메서드
    private fun privateMethod() {
        // 구현
    }
}
```

## 4. 주석 규칙

### 4.1 클래스 주석
```kotlin
/**
 * 사용자 정보를 관리하는 클래스
 * 
 * @author 개발자명
 * @since 1.0.0
 */
class UserManager {
    // 구현
}
```

### 4.2 함수 주석
```kotlin
/**
 * 사용자 ID로 사용자 정보를 조회합니다.
 * 
 * @param userId 조회할 사용자 ID
 * @return 사용자 정보 객체, 없으면 null
 * @throws IllegalArgumentException userId가 null이거나 빈 문자열인 경우
 */
fun getUserById(userId: String): User? {
    // 구현
}
```

### 4.3 인라인 주석
```kotlin
// 복잡한 로직에 대한 설명
val result = complexCalculation()

/* 
 * 여러 줄에 걸친 
 * 복잡한 로직 설명
 */
val anotherResult = anotherComplexCalculation()
```

## 5. 예외 처리

### 5.1 예외 처리 원칙
```kotlin
try {
    // 위험한 작업
    val result = riskyOperation()
} catch (e: IllegalArgumentException) {
    // 구체적인 예외 처리
    logger.error("Invalid argument: ${e.message}")
    throw e
} catch (e: Exception) {
    // 일반적인 예외 처리
    logger.error("Unexpected error: ${e.message}")
    throw RuntimeException("Operation failed", e)
} finally {
    // 정리 작업
    cleanup()
}
```

### 5.2 커스텀 예외
```kotlin
class CustomException(message: String, cause: Throwable? = null) : 
    Exception(message, cause)
```

## 6. 테스트 규칙

### 6.1 테스트 네이밍
```kotlin
@Test
fun `should return user when valid id is provided`() {
    // 테스트 구현
}

@Test
fun `should throw exception when invalid id is provided`() {
    // 테스트 구현
}
```

### 6.2 테스트 구조 (AAA Pattern)
```kotlin
@Test
fun testMethod() {
    // Arrange (준비)
    val input = "test"
    val expected = "result"
    
    // Act (실행)
    val actual = methodUnderTest(input)
    
    // Assert (검증)
    assertEquals(expected, actual)
}
```

## 7. 성능 고려사항

### 7.1 메모리 관리
- 불필요한 객체 생성 최소화
- 적절한 데이터 구조 선택
- 메모리 누수 방지

### 7.2 알고리즘 최적화
- 시간 복잡도 고려
- 공간 복잡도 고려
- 캐싱 활용

## 8. 보안 규칙

### 8.1 입력 검증
```kotlin
fun processUserInput(input: String): String {
    require(input.isNotBlank()) { "Input cannot be blank" }
    require(input.length <= MAX_LENGTH) { "Input too long" }
    
    // 입력 검증 후 처리
    return input.trim()
}
```

### 8.2 민감한 정보 처리
- 로그에 민감한 정보 출력 금지
- 암호화된 통신 사용
- 적절한 권한 검증

## 9. 버전 관리

### 9.1 커밋 메시지 규칙
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅
refactor: 코드 리팩토링
test: 테스트 추가/수정
chore: 빌드 프로세스 또는 보조 도구 변경
```

### 9.2 브랜치 전략
- `main`: 프로덕션 코드
- `develop`: 개발 코드
- `feature/기능명`: 새로운 기능 개발
- `hotfix/버그명`: 긴급 버그 수정

## 10. 코드 리뷰 체크리스트

### 10.1 기능적 검토
- [ ] 요구사항을 정확히 구현했는가?
- [ ] 예외 상황을 적절히 처리했는가?
- [ ] 성능에 문제가 없는가?

### 10.2 코드 품질 검토
- [ ] 코드가 읽기 쉬운가?
- [ ] 중복 코드가 없는가?
- [ ] 적절한 주석이 있는가?
- [ ] 테스트 코드가 있는가?

### 10.3 보안 검토
- [ ] 입력 검증이 적절한가?
- [ ] 민감한 정보가 노출되지 않는가?
- [ ] 적절한 권한 검증이 있는가?

## 11. 도구 및 설정

### 11.1 코드 포맷팅
- IDE 자동 포맷팅 사용
- 일관된 들여쓰기 (4 spaces 또는 2 spaces)
- 최대 줄 길이 제한 (80-120 characters)

### 11.2 린터 설정
- Kotlin: ktlint
- Java: Checkstyle, PMD
- Android: Android Lint

### 11.3 IDE 설정
- 코드 템플릿 사용
- 자동 import 정리
- 실시간 오류 검사 활성화 