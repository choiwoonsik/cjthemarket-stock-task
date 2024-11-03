insert ignore into `stock`.`products`
    (id, name, original_price, date_created, date_updated)
values (1, '비비고 만두', '4900', now(6), now(6)),
       (2, '비비고 김치', '5900', now(6), now(6)),
       (3, '비비고 떡볶이', '3900', now(6), now(6)),
       (4, '비비고 라면', '2900', now(6), now(6))
;

insert ignore into `stock`.`stocks`
    (id, quantity, product_id, date_created, date_updated)
values (1, 10, 1, now(6), now(6)),
       (2, 20, 2, now(6), now(6)),
       (3, 0, 3, now(6), now(6)),
       (4, 100000000, 4, now(6), now(6))
;