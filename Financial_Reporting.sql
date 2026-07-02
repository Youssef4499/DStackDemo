-- ============================================================
-- 03 - Financial & Reporting Tester
-- Test Cases: DB-TC-041 -> DB-TC-060
-- Source: Sakila_DB_Test_Cases.xlsx (worksheet: DB)
-- ============================================================

USE sakila;

-- DB-TC-041: Verify total payment revenue matches SUM in view sales_by_store
SELECT SUM(amount) AS direct_total FROM payment;
SELECT SUM(total_sales) AS view_total FROM sales_by_store;

-- DB-TC-042: Verify average payment amount per customer using AVG()
SELECT customer_id,
       COUNT(*) AS num_payments,
       ROUND(AVG(amount), 2) AS avg_payment
FROM payment
GROUP BY customer_id
ORDER BY avg_payment DESC
LIMIT 10;

-- DB-TC-043: Verify COUNT of distinct customers who made at least one payment
SELECT COUNT(DISTINCT p.customer_id) AS paying_customers,
       (SELECT COUNT(*) FROM customer) AS total_customers
FROM payment p;

-- DB-TC-044: Verify sales_by_film_category view returns correct SUM per category
SELECT category, total_sales
FROM sales_by_film_category
ORDER BY total_sales DESC
LIMIT 5;

-- DB-TC-045: Verify film_list view includes actors for every film that has actor assignments
SELECT FID, title, actors
FROM film_list
WHERE actors IS NULL
LIMIT 10;

-- DB-TC-046: Verify total number of films per category using GROUP BY and COUNT
SELECT c.name AS category, COUNT(fc.film_id) AS film_count
FROM category c
JOIN film_category fc ON c.category_id = fc.category_id
GROUP BY c.name
ORDER BY film_count DESC;

-- DB-TC-047: Verify HAVING clause filters categories with fewer than 50 films
SELECT c.name, COUNT(fc.film_id) AS film_count
FROM category c
JOIN film_category fc ON c.category_id = fc.category_id
GROUP BY c.name
HAVING film_count >= 50
ORDER BY film_count DESC;

-- DB-TC-048: Verify total revenue per staff member using SUM(amount) with GROUP BY
SELECT s.staff_id,
       CONCAT(s.first_name,' ',s.last_name) AS staff_name,
       SUM(p.amount) AS total_collected
FROM payment p
JOIN staff s ON p.staff_id = s.staff_id
GROUP BY s.staff_id
ORDER BY total_collected DESC;

-- DB-TC-049: Verify customer_list view returns correctly joined address, city, and country per customer
SELECT ID, name, address, city, country
FROM customer_list
WHERE address IS NULL OR city IS NULL OR country IS NULL
LIMIT 10;

-- DB-TC-050: Verify rewards_report procedure output for minimum 1 purchase and $1 amount
CALL rewards_report(1, 1.00, @count);
SELECT @count AS rewardee_count;

-- DB-TC-051: Verify rewards_report procedure rejects zero min_monthly_purchases parameter
CALL rewards_report(0, 5.00, @count);

-- DB-TC-052: Verify film_in_stock procedure returns correct count for a specific film and store
CALL film_in_stock(1, 1, @count);
SELECT @count AS in_stock_count;
-- Cross-check:
SELECT COUNT(*) FROM inventory
WHERE film_id=1 AND store_id=1 AND inventory_in_stock(inventory_id);

-- DB-TC-053: Verify get_customer_balance function returns correct balance for customer_id=1
SELECT get_customer_balance(1, NOW()) AS customer_balance;
SELECT IFNULL(SUM(f.rental_rate),0) AS rent_fees
FROM film f
JOIN inventory i ON f.film_id=i.film_id
JOIN rental r ON i.inventory_id=r.inventory_id
WHERE r.customer_id=1;
SELECT IFNULL(SUM(amount),0) AS paid FROM payment WHERE customer_id=1;

-- DB-TC-054: Verify SUM of all rental_rate values per film rating category
SELECT rating,
       COUNT(*) AS film_count,
       ROUND(SUM(rental_rate), 2) AS total_rental_rate,
       ROUND(AVG(rental_rate), 2) AS avg_rental_rate
FROM film
GROUP BY rating
ORDER BY rating;

-- DB-TC-055: Verify actor_info view returns correct film grouping per actor
SELECT actor_id, first_name, last_name, film_info
FROM actor_info
WHERE actor_id = 1;

-- DB-TC-056: Verify top 10 customers by total payment amount using SUM and ORDER BY
SELECT customer_id,
       SUM(amount) AS total_spent
FROM payment
GROUP BY customer_id
ORDER BY total_spent DESC
LIMIT 10;

-- DB-TC-057: Verify COUNT of films with a replacement_cost above $20
SELECT COUNT(*) AS expensive_films
FROM film
WHERE replacement_cost > 20.00;

-- DB-TC-058: Verify SUM of payments per month to detect seasonal revenue patterns
SELECT YEAR(payment_date) AS yr,
       MONTH(payment_date) AS mo,
       COUNT(*) AS transactions,
       ROUND(SUM(amount), 2) AS monthly_revenue
FROM payment
GROUP BY yr, mo
ORDER BY yr, mo;

-- DB-TC-059: Verify staff_list view joins address, city, and country correctly
SELECT ID, name, address, city, country
FROM staff_list
WHERE address IS NULL OR city IS NULL OR country IS NULL;

-- DB-TC-060: Verify COUNT of films available in inventory per store
SELECT store_id,
       COUNT(*) AS total_inventory_items,
       COUNT(DISTINCT film_id) AS distinct_films
FROM inventory
GROUP BY store_id
ORDER BY store_id;
