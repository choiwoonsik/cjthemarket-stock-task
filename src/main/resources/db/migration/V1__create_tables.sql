CREATE TABLE products
(
    id             BIGINT AUTO_INCREMENT,
    name           VARCHAR(255) NOT NULL COMMENT '상품명',
    original_price BIGINT       NOT NULL COMMENT '상품 원가',
    date_created   DATETIME(6)  NOT NULL,
    date_updated   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) DEFAULT CHARSET = utf8mb4
    COMMENT '상품'
;

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
    COMMENT '상품 재고'
;