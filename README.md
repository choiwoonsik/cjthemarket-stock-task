# 온라인 쇼핑몰 상품 재고 차감 API 구현 명세

### 제출자: 최운식

---

# 사용 기술

- Kotlin 1.9.25 (Java 17)
- Spring Boot 3.3.5
- MySQL 8.3
- Redisson

# 구현 목적

- 재고 차감 시 동시성 있는 환경에서도 요청의 누락 없이 정상적으로 이뤄지도록 한다.
- 재고는 0개 미만으로 떨이지면 안되며, DB의 일관성이 유지되도록 제어한다.

# 접근 방법

**<재고 요청 누락 방지>**

재고 차감은 동시 다발적으로 발생할 수 있으므로 동시성 방어를 해야한다. 이때 일반적으로 서버는 scale out을 통해 여러 인스턴스가 띄어진 상태이므로 분산 환경에 대한 대비가 필요하다.

이러한 분산 환경에서 동시성을 방어하기 위해서는 비관적 DB락, 분산락을 사용할 수 있다. 비관적락도 동시성 방어는 가능하지만 트랜잭션을 물고 대기한다는 단점이 있다. 따라서 여기선 Redisson을 사용하여
분산락을 통해 동시성을 방어한다.

**<재고 0건 미만 방지>**

재고 차감 시 재고가 0개 미만으로 떨어지면 안될것이다. 이를 방어하는건 서비스 로직에서도 처리하겠지만, 테이블 컬럼의 제약조건을 넣어서 최종적으로 방어한다. 트랜잭션이 커밋될 때 일관성이 깨지는 경우 트랜잭션
롤백을 일으켜 해당 차감 요청은 반환된다.

- 1차적으로 애플리케이션 로직으로 방어한다.
- 동시성 이슈로 요청이 중첩되어 들어오는 경우를 분산락으로 방어한다.
- DB 제약조건을 통해 재고가 0건 미만으로 떨이지면 제약 조건 위배를 통해 트랜잭션 롤백을 일으켜 방어한다.

# 실행 방법

1. MySQL, Redis 서버를 실행한다.

```shell
cd ./tools
docker-compose up -d
```

2. Flyway를 통해 DB 마이그레이션을 실행한다.
- `flywayMigrate` 시 서비스 DB에 테스트를 위한 데이터 상품 4개, 재고 4개를 초기화하도록 했습니다.

```shell
./gradlew flywayClean
./gradlew flywayMigrate
./gradlew flywayCleanTestDB
```

3. QueryDSL을 통해 Q클래스를 생성한다.

```shell
./gradlew kaptKotlin
```

4. 단위 테스트를 실행한다.

```shell
./gradlew test
```

5. 서버를 실행한다.

# DB

- URL: `jdbc:mysql://localhost:3306/`
- 데이터베이스명 : `stock`, `stock_test (테스트 DB)`
- 사용자 : `stock_admin`
- 비밀번호 : `stock`

1. 상품 테이블

```sql
CREATE TABLE products
(
    id             BIGINT AUTO_INCREMENT,
    name           VARCHAR(255) NOT NULL COMMENT '상품명',
    original_price BIGINT       NOT NULL COMMENT '상품 원가',
    date_created   DATETIME(6)  NOT NULL,
    date_updated   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) DEFAULT CHARSET = utf8mb4
    COMMENT '상품';
```

2. 상품 재고 테이블

```sql
CREATE TABLE stocks
(
    id           BIGINT AUTO_INCREMENT,
    quantity     BIGINT      NOT NULL COMMENT '상품 재고 수량' CHECK ( quantity >= 0 ),
    product_id   BIGINT      NOT NULL COMMENT '상품 ID',
    date_created DATETIME(6) NOT NULL,
    date_updated DATETIME(6) NOT NULL,
    UNIQUE KEY product_id (product_id),
    PRIMARY KEY (id)
) DEFAULT CHARSET = utf8mb4
    COMMENT '상품 재고';
```

- 상품과 재고는 다른 테이블로 분리하였습니다. 모놀리식이든 MSA 환경이든 상품과 재고 관리는 분리하는게 좋다고 생각했습니다.
    - 상품의 수정과 재고의 수정은 분리되는게 맞다고 보았습니다.
- 상품과 재고는 1:1 관계로 재고는 상품을 참조하는 외래키를 갖도록 했습니다.
    - 상품이 있고 재고가 없는 경우는 가능하지만, 상품이 없는데 재고가 있는 경우는 불가능하므로 재고가 상품 참조를 갖도록 했습니다.
    - 추후에 상품 하나에 여러 창고 기반의 N개 재고가 필요하다면 1:N 관계로 확장할 수 있습니다.
- 재고는 0개 미만으로 떨어지면 안된다고 가정하고 제약 조건을 걸었습니다.
    - 분산 시스템의 경우 동시성 이슈로 재고가 0개 미만으로 떨어질 수 있기에 DB 제약조건을 통해 방어하고자 했습니다. (서비스 로직과 별도로)

# API

- [API Swagger 문서](http://localhost:8080/swagger-ui.html)
- [노션 정리 문서](https://woonsik.notion.site/API-130efd1222118037bce4fadb72f129cb?pvs=74)

### 상품 조회

**URL**

- `GET api/v1/product`

**파라미터**

```json
?id=111
```

**응답**

```json
{
  "data": {
    "id": 1,
    "name": "비비고 만두",
    "original_price": 4900
  },
  "success": true,
  "message": "성공"
}
```

### 상품 재고 조회

**URL**

- `GET api/v1/stock`

**파라미터**

```json
product_id=1
```

**응답**

```json
{
  "data": {
    "id": 1,
    "product_id": 1,
    "stock_quantity": 10
  },
  "success": true,
  "message": "성공"
}
```

### 상품 재고 차감

**URL**

- `PUT api/v1/stock/decrease`

**BODY**

```json
{
  "product_id": 1,
  "decrease_quantity": 1
}
```

**응답**

```json
{
  "data": {
    "id": 1,
    "product_id": 1,
    "stock_quantity": 7
  },
  "success": true,
  "message": "성공"
}
```

### 상품 재고 설정 (테스트를 위한 임시 API)

**URL**

- `PUT api/v1/stock/set`

**BODY**

```json
{
  "product_id": 1,
  "set_quantity": 100
}
```

**응답**

```json
{
  "data": {
    "id": 1,
    "product_id": 1,
    "stock_quantity": 100
  },
  "success": true,
  "message": "성공"
}
```