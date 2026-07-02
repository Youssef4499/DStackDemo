-- ============================================================
-- 05 - Security & End-to-End Tester
-- Test Cases: DB-TC-076 -> DB-TC-090
-- Source: Sakila_DB_Test_Cases.xlsx (worksheet: DB)
-- ============================================================

USE sakila;

-- DB-TC-076: Verify SQL injection via string literal in last_name filter is rejected or returns no unintended data
SELECT actor_id, first_name, last_name
FROM actor
WHERE last_name = 'GUINESS'' OR ''1''=''1';

-- DB-TC-077: Verify staff.password column is not stored in plain text
SELECT staff_id, username, password
FROM staff;

-- DB-TC-078: Verify staff picture (BLOB) column does not expose unintended binary data via SQL query
SELECT staff_id, first_name, last_name, email, username, store_id, active
FROM staff;

-- DB-TC-079: Verify customer email data is correctly stored and retrievable (data mapping validation)
SELECT customer_id, first_name, last_name, email
FROM customer
WHERE customer_id = 1;

-- DB-TC-080: Verify SQL injection via UNION-based attack on film title query returns no extra rows
SELECT film_id, title
FROM film
WHERE title = 'ACADEMY DINOSAUR'' UNION SELECT 1,username FROM staff-- ';

-- DB-TC-081: Verify inactive customers (active=0) are excluded from active customer queries
SELECT COUNT(*) AS active_customers FROM customer WHERE active = 1;
SELECT COUNT(*) AS inactive_customers FROM customer WHERE active = 0;
SELECT customer_id, first_name, last_name FROM customer WHERE active = 0 LIMIT 5;

-- DB-TC-082: Verify end-to-end rental transaction: customer rents a film, payment is recorded, rental is returned
INSERT INTO rental (rental_date, inventory_id, customer_id, staff_id)
VALUES (NOW(), 10, 1, 1);
SET @rid = LAST_INSERT_ID();
INSERT INTO payment (customer_id, staff_id, rental_id, amount, payment_date)
VALUES (1, 1, @rid, 4.99, NOW());
UPDATE rental SET return_date = NOW() WHERE rental_id = @rid;
SELECT r.rental_id, r.return_date, p.amount
FROM rental r JOIN payment p ON r.rental_id = p.rental_id
WHERE r.rental_id = @rid;

-- DB-TC-083: Verify that the utf8mb4 character set handles extended characters in actor names
INSERT INTO actor (first_name, last_name)
VALUES ('ÏÑTL', 'CHÂR');
SELECT actor_id, first_name, last_name
FROM actor
WHERE last_name = 'CHÂR';

-- DB-TC-084: Verify that searching for an actor by name with Arabic characters returns no result (English-only data)
SELECT actor_id, first_name, last_name
FROM actor
WHERE first_name = 'محمد';

-- DB-TC-085: Verify customer_list view shows correct active/inactive notes field mapping
SELECT ID, name, notes
FROM customer_list
WHERE notes = 'active'
LIMIT 5;
SELECT ID, name, notes
FROM customer_list
WHERE notes = ''
LIMIT 5;

-- DB-TC-086: Verify that email field in customer accepts NULL values (nullable column)
INSERT INTO customer (store_id, first_name, last_name, address_id, create_date)
VALUES (1, 'NOEMAIL', 'CUSTOMER', 5, NOW());
SELECT customer_id, first_name, email
FROM customer
WHERE first_name='NOEMAIL'
ORDER BY customer_id DESC LIMIT 1;

-- DB-TC-087: Verify inventory_in_stock function returns FALSE for an item currently rented out
SELECT inventory_id FROM rental WHERE return_date IS NULL LIMIT 1;
-- Use the returned inventory_id:
SELECT inventory_in_stock(4) AS is_in_stock;

-- DB-TC-088: Verify inventory_held_by_customer returns the correct customer_id for a checked-out item
SELECT inventory_held_by_customer(4) AS held_by_customer_id;
SELECT customer_id FROM rental
WHERE inventory_id=4 AND return_date IS NULL;

-- DB-TC-089: Verify that film_not_in_stock procedure returns correct count of unavailable copies
CALL film_not_in_stock(1, 1, @count);
SELECT @count AS not_in_stock_count;
SELECT COUNT(*) AS manual_check
FROM inventory
WHERE film_id=1 AND store_id=1 AND NOT inventory_in_stock(inventory_id);

-- DB-TC-090: Verify that the nicer_but_slower_film_list view returns properly formatted actor names
SELECT FID, title, actors
FROM nicer_but_slower_film_list
WHERE FID = 1;
