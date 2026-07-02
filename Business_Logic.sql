-- ============================================================
-- 02 - Business Logic & Timeline Tester
-- Test Cases: DB-TC-021 -> DB-TC-040
-- Source: Sakila_DB_Test_Cases.xlsx (worksheet: DB)
-- ============================================================

USE sakila;

-- DB-TC-021: Verify no orphan addresses exist without a valid city_id
SELECT a.address_id, a.city_id
FROM address a
LEFT JOIN city c ON a.city_id = c.city_id
WHERE c.city_id IS NULL;

-- DB-TC-022: Verify no orphan cities exist without a valid country_id
SELECT c.city_id, c.country_id
FROM city c
LEFT JOIN country co ON c.country_id = co.country_id
WHERE co.country_id IS NULL;

-- DB-TC-023: Verify no rental has a return_date earlier than its rental_date
SELECT rental_id, rental_date, return_date
FROM rental
WHERE return_date IS NOT NULL
  AND return_date < rental_date;

-- DB-TC-024: Verify all active customers have a valid store assignment
SELECT c.customer_id, c.store_id
FROM customer c
LEFT JOIN store s ON c.store_id = s.store_id
WHERE c.active = 1
  AND s.store_id IS NULL;

-- DB-TC-025: Verify all payments reference a real customer
SELECT p.payment_id, p.customer_id
FROM payment p
LEFT JOIN customer c ON p.customer_id = c.customer_id
WHERE c.customer_id IS NULL;

-- DB-TC-026: Verify all inventory items reference an existing film
SELECT i.inventory_id, i.film_id
FROM inventory i
LEFT JOIN film f ON i.film_id = f.film_id
WHERE f.film_id IS NULL;

-- DB-TC-027: Verify that deleting a film cascades correctly to film_text via trigger
SELECT film_id, title FROM film WHERE title='DEFAULT RATE TEST' LIMIT 1;
-- Use the returned film_id:
DELETE FROM film WHERE title='DEFAULT RATE TEST';
SELECT film_id FROM film_text WHERE film_id = (SELECT MAX(film_id)+1 FROM film);

-- DB-TC-028: Verify INS trigger propagates new film data to film_text on INSERT
INSERT INTO film (title, description, language_id, rental_duration, rental_rate, replacement_cost)
VALUES ('TRIGGER TEST FILM', 'Test description for trigger', 1, 3, 2.99, 12.99);
SET @new_id = LAST_INSERT_ID();
SELECT film_id, title, description FROM film_text WHERE film_id = @new_id;

-- DB-TC-029: Verify UPDATE trigger keeps film_text in sync with film.title changes
UPDATE film SET title = 'UPDATED TITLE TEST' WHERE film_id = 1;
SELECT f.title AS film_title, ft.title AS film_text_title
FROM film f
JOIN film_text ft ON f.film_id = ft.film_id
WHERE f.film_id = 1;

-- DB-TC-030: Verify that a rental cannot be inserted for a non-existent inventory item
INSERT INTO rental (rental_date, inventory_id, customer_id, staff_id)
VALUES (NOW(), 999999, 1, 1);

-- DB-TC-031: Verify that payment_date is never NULL for any payment record
SELECT COUNT(*) AS null_payment_dates
FROM payment
WHERE payment_date IS NULL;

-- DB-TC-032: Verify that all staff members are assigned to an existing store
SELECT s.staff_id, s.store_id
FROM staff s
LEFT JOIN store st ON s.store_id = st.store_id
WHERE st.store_id IS NULL;

-- DB-TC-033: Verify that film rental_duration is never zero or negative
SELECT film_id, title, rental_duration
FROM film
WHERE rental_duration <= 0;

-- DB-TC-034: Verify that rental records with no return date are currently outstanding
SELECT COUNT(*) AS outstanding_rentals
FROM rental
WHERE return_date IS NULL;

-- DB-TC-035: Verify a customer's create_date is never after their last rental date
SELECT c.customer_id, c.create_date, r.rental_date
FROM customer c
JOIN rental r ON c.customer_id = r.customer_id
WHERE r.rental_date < c.create_date;

-- DB-TC-036: Verify no film is assigned to the same category more than once
SELECT film_id, category_id, COUNT(*) AS cnt
FROM film_category
GROUP BY film_id, category_id
HAVING cnt > 1;

-- DB-TC-037: Verify no actor appears in film_actor more than once per film
SELECT actor_id, film_id, COUNT(*) AS cnt
FROM film_actor
GROUP BY actor_id, film_id
HAVING cnt > 1;

-- DB-TC-038: Verify that payment amounts are never negative
SELECT payment_id, amount
FROM payment
WHERE amount < 0;

-- DB-TC-039: Verify that each store has exactly one manager (staff member) assigned
SELECT manager_staff_id, COUNT(*) AS store_count
FROM store
GROUP BY manager_staff_id
HAVING store_count > 1;

-- DB-TC-040: Verify all payments reference either a valid rental_id or NULL (never an invalid rental_id)
SELECT p.payment_id, p.rental_id
FROM payment p
LEFT JOIN rental r ON p.rental_id = r.rental_id
WHERE p.rental_id IS NOT NULL
  AND r.rental_id IS NULL;
