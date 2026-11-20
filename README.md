# Java 커머스 콘솔 애플리케이션

## 1. 프로젝트 개요
- **프로젝트명**: Java 콘솔 기반 실시간 커머스 플랫폼 (팀스파르타 내일배움캠프 CH2 커머스 과제)
- **개발 환경**: Java 17 (OpenJDK 17), Windows PowerShell, Git/GitHub
- **설계 방향**:
  - **역할과 책임(R&R)의 분리**: 메인 실행(`Main`), 시스템 흐름 제어(`CommerceSystem`), 관리자 기능(`AdminManager`), 도메인 엔티티(`Product`, `Category`, `Customer`, `Grade`, `Cart`, `CartItem`) 간의 명확한 책임 분리
  - **캡슐화 및 불변성 보장**: 모든 인스턴스 필드 `private` 선언, 생성자 및 Setter를 통한 입력값 유효성 검증, 방어적 복사(`new ArrayList<>`) 및 불변 리스트(`Collections.unmodifiableList`) 반환
  - **도메인 기반 패키지 구조**: 기능과 도메인 성격에 따라 하위 패키지를 체계적으로 분리하여 유지보수성 및 확장성 확보

---

## 2. 디렉터리 및 패키지 구조

```
src/
└── com/
    └── example/
        └── commerce/
            │   # 애플리케이션 진입점 및 샘플 데이터 초기화
            ├── Main.java                          
            │
            ├── product/
            │   # 상품 정보 및 재고 차감 로직
            │   └── Product.java                   
            │
            ├── category/
            │   # 카테고리 정보, 상품 목록 보관, 상품 검색/추가/삭제 및 스트림 가격 필터링
            │   └── Category.java                  
            │
            ├── customer/
            │   # 고객 정보 관리 및 등급별 할인율 Enum
            │   ├── Customer.java                  
            │   └── Grade.java                     
            │
            ├── cart/
            │   # 장바구니 개별 항목 및 장바구니 비즈니스 로직(담기, 합계 계산, 람다 기반 특정 상품 제거)
            │   ├── CartItem.java                  
            │   └── Cart.java                      
            │
            └── system/
                # 전체 콘솔 UI 흐름 제어 및 관리자 전용 기능(인증, 상품 관리)
                ├── CommerceSystem.java            
                └── AdminManager.java              
```

### 클래스별 역할 정리
| 패키지 | 클래스 / Enum | 주요 역할 및 책임 |
|---|---|---|
| `com.example.commerce` | `Main` | 프로그램 시작점(`main`), 샘플 고객 및 3개 카테고리(12개 상품) 데이터 구성 및 `CommerceSystem` 실행 |
| `com.example.commerce.product` | `Product` | 상품명, 가격, 설명, 재고수량을 캡슐화하고 재고 차감(`decreaseStock`) 수행 |
| `com.example.commerce.category` | `Category` | 카테고리명과 상품 리스트 보관, 상품명 중복 검증, 상품 추가/검색/삭제, Stream 기반 가격대별 필터링 제공 |
| `com.example.commerce.customer` | `Customer` | 고객명, 이메일, 회원 등급(`Grade`) 관리 |
| `com.example.commerce.customer` | `Grade` | 회원 등급(`BRONZE`, `SILVER`, `GOLD`, `PLATINUM`)별 할인율(0%, 5%, 10%, 15%) 및 할인/최종 결제 금액 계산 |
| `com.example.commerce.cart` | `CartItem` | 장바구니에 담긴 개별 상품 및 수량 관리, 수량 증가(`addQuantity`), 항목 소계(`getTotalPrice`) 계산 |
| `com.example.commerce.cart` | `Cart` | 장바구니 목록 관리, 상품 추가(재고 초과 방어), 총액 계산, 비우기, 상품 제거 및 람다/스트림 기반 상품명 제거 |
| `com.example.commerce.system` | `CommerceSystem` | 메인 메뉴 및 카테고리 메뉴 출력, 장바구니 상태에 따른 메뉴 동적 분기, 주문 확정 및 취소 흐름 제어 |
| `com.example.commerce.system` | `AdminManager` | 관리자 인증(비밀번호 3회 시도), 상품 추가(중복 차단)·수정(가격/설명/재고)·삭제(장바구니 동기화 삭제) 제어 |

---

## 3. 실행 방법 (Windows PowerShell 기준)

프로젝트 루트 디렉터리(`C:\workspace\commerce-project`)에서 Windows PowerShell을 실행하고 아래 명령어를 순서대로 입력합니다.

### 1) 컴파일
출력 디렉터리(`out`)를 생성하고 `src` 하위의 모든 `.java` 소스 파일을 UTF-8 인코딩으로 컴파일합니다.

```powershell
New-Item -ItemType Directory -Force -Path out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)
```

### 2) 실행
콘솔 코드페이지를 UTF-8(`65001`)로 설정한 뒤 애플리케이션을 실행합니다.

```powershell
chcp 65001
java "-Dfile.encoding=UTF-8" -cp out com.example.commerce.Main
```

> **관리자 모드 안내**: 메인 메뉴에서 `6. 관리자 모드`를 선택한 후 비밀번호 입력창에 `admin123`을 입력하면 관리자 기능에 접근할 수 있습니다.

---

## 4. 단계별 주요 기능

### 필수 기능 (STEP 1 ~ STEP 4)
- **도메인 모델 설계**: `Product`, `Category`, `Customer` 클래스를 정의하고 생성자 주입 및 Getter 제공
- **시스템 흐름 제어**: `CommerceSystem`을 통해 카테고리 조회, 상품 상세 정보 조회, 뒤로가기(`0`), 종료(`0`) 흐름 구현
- **캡슐화 및 방어적 복사**:
  - 모든 인스턴스 필드를 `private`으로 선언
  - 생성자 및 Setter에서 문자열 `null`/공백(`trim().isEmpty()`) 검증, 가격/재고 음수 검증 수행
  - `Category.getProducts()` 및 `Cart.getItems()` 조회 시 `Collections.unmodifiableList`를 반환하여 외부에서의 임의 조작 방지
  - `Category` 및 `CommerceSystem` 생성자/Setter에서 `new ArrayList<>()`로 방어적 복사 적용

### 도전 기능 Lv 1: 장바구니 및 주문 관리
- **장바구니 담기**: 카테고리 내 상품 선택 시 확인/취소 단계를 거쳐 장바구니(`Cart`)에 상품 추가 (수량 누적 관리)
- **동적 메뉴 출력**:
  - 장바구니가 비어 있을 때: 카테고리 메뉴, `0. 종료`, `6. 관리자 모드`만 출력
  - 장바구니에 1개 이상의 상품이 담기면 `[ 주문 관리 ]` 블록(`4. 장바구니 확인`, `5. 주문 취소`) 동적 노출
- **빈 장바구니 예외 처리**: 장바구니가 비어 있을 때 `4` 또는 `5` 입력 시 `"장바구니가 비어 있어 주문 관리 기능을 사용할 수 없습니다."` 안내 출력
- **재고 확인 및 주문 확정**:
  - 주문 확정 전 장바구니의 모든 상품에 대해 실시간 재고를 검증
  - `Product.decreaseStock()`을 통해 상품 객체 스스로 재고를 차감하도록 책임 위임
  - 주문 완료 후 재고 변경 내역(`X 재고가 N개 → M개로 업데이트되었습니다.`) 출력 및 장바구니 초기화(`clear()`)
- **주문 취소**: 진행 중인 주문 확인 후 장바구니를 비우고 메인 메뉴로 복귀

### 도전 기능 Lv 2: 관리자 모드
- **비밀번호 인증**: `6. 관리자 모드` 진입 시 `admin123` 인증 수행. 최대 3회 시도 가능하며 3회 연속 실패 시 메인 메뉴로 강제 복귀
- **상품 추가**: 카테고리를 선택하고 상품명, 가격, 설명, 재고를 입력받아 등록. 동일 카테고리 내 중복 상품명(`Category.containsProductName()`) 존재 시 추가 차단
- **상품 수정**: 전체 카테고리에서 상품명으로 검색 후 가격, 설명, 재고수량 중 선택하여 `Product`의 Setter로 안전하게 수정
- **상품 삭제 및 동기화**:
  - 카테고리별 상품 목록에서 상품을 선택하여 삭제
  - 카테고리에서 상품 삭제 시 장바구니에 담긴 내역도 `Cart.removeProduct()`를 통해 동기화되어 함께 제거
  - 장바구니의 상품이 모두 삭제되면 메인 메뉴의 `[ 주문 관리 ]` 블록이 자동으로 비활성화

### 도전 기능 Lv 3: Enum, 람다 & 스트림 활용 고급 기능
- **`Grade` Enum 등급별 할인**:
  - `BRONZE`(0%), `SILVER`(5%), `GOLD`(10%), `PLATINUM`(15%) 등급 정의
  - 주문 확정 시 고객 등급을 선택받아 할인 금액(`calculateDiscountAmount`) 및 최종 결제 금액(`calculateFinalPrice`)을 계산하여 영수증 형태로 출력
- **가격대별 Stream 필터링**:
  - 카테고리 진입 시 하위 메뉴 제공 (`1. 전체 상품 보기`, `2. 가격대별 필터링 (100만원 이하)`, `3. 가격대별 필터링 (100만원 초과)`, `0. 뒤로가기`)
  - `Category.filterProductsUpToPrice()`, `Category.filterProductsAbovePrice()`에서 `stream().filter()`를 활용하여 필터링 수행
  - 상품 목록 출력 시 `IntStream.range().forEach()` 스트림 기반 순회 적용
  - 필터링 결과가 비어 있을 경우 `"해당 가격대에 상품이 없습니다."` 안내 출력
- **람다·스트림 기반 장바구니 특정 상품 제거**:
  - 장바구니 확인 메뉴에서 `3. 장바구니 상품 제거` 옵션 제공
  - `Cart.removeProductByName()`에서 `stream().filter()`와 람다식을 적용해 입력된 상품명을 제외한 항목만 수집하여 장바구니 갱신

---

## 5. 예외 처리 전략

| 구분 | 발생 상황 | 처리 방식 및 전략 |
|---|---|---|
| **입력 유효성 검증** | 문자열 필드(`name`, `description`, `email`, `categoryName`)에 `null` 또는 공백 문자열 입력 | `IllegalArgumentException` 발생으로 무결성 보장 |
| **수치 유효성 검증** | 가격(`price`), 재고(`stock`), 수량(`quantity`)에 음수 값 전달 | `IllegalArgumentException` 발생으로 비정상 수치 차단 |
| **콘솔 입력 방어** | 메뉴 선택 시 숫자가 아닌 문자 또는 범위 밖 숫자 입력 | `NumberFormatException` 처리 및 조건문 분기를 통해 프로그램이 비정상 종료되지 않고 안내 메시지 출력 후 재입력 대기 |
| **빈 장바구니 접근 방어** | 장바구니가 비어 있는 상태에서 주문 관리 메뉴(`4`, `5`) 입력 | 안내 메시지(`"장바구니가 비어 있어 주문 관리 기능을 사용할 수 없습니다."`) 출력 후 메인 메뉴 유지 |
| **재고 초과 방어** | 장바구니에 상품을 추가할 때 기존 장바구니 수량 + 추가 수량이 재고를 초과하는 경우 | `IllegalStateException` 처리 및 경고 메시지 출력 후 장바구니 담기 차단 |
| **주문 시점 재고 부족** | 주문 확정 시점에 상품 재고가 부족한 경우 | 각 항목 재고 사전 검증 후 주문 차단 및 안내 메시지 출력 |
| **관리자 인증 실패** | 관리자 비밀번호를 3회 연속 잘못 입력 | 남은 횟수 안내 후 3회 초과 시 관리자 모드 진입을 차단하고 메인 메뉴로 복귀 |
| **상품명 중복 방어** | 관리자 모드에서 상품 추가 시 동일 카테고리 내 이미 존재하는 상품명 입력 | 대소문자 및 앞뒤 공백을 무시하여 중복 검증 후 추가 중단 |
| **필터링 결과 없음** | 특정 가격대 필터링 시 조건에 맞는 상품이 0개인 경우 | 안내 메시지 출력 후 카테고리 하위 메뉴로 안전하게 복귀 |
| **장바구니 삭제 실패** | 장바구니에 존재하지 않는 상품명을 입력하여 제거 시도 | 존재 여부 확인 후 안내 메시지 출력 및 메뉴 상태 유지 |
