-- ============================================================
-- Query 2 : تدقيق الأرقام المالية والـ Ledgers (TC-FN-001 -> TC-FN-012)
-- شغّلها بالترتيب ده على قاعدة sakila
-- ============================================================
USE sakila;

-- ============================================================
-- GROUP 1: TC-FN-001 to TC-FN-004 | SUM / AVG / COUNT
-- ============================================================

SELECT '>>> TC-FN-001: إجمالي المبيعات لكل فرع' AS TEST_CASE;
SELECT s.store_id, COUNT(p.payment_id) AS total_transactions, SUM(p.amount) AS total_sales
FROM payment p
JOIN staff s ON p.staff_id = s.staff_id
GROUP BY s.store_id;

SELECT '>>> TC-FN-002: متوسط الفاتورة وعدد العمليات لكل عميل' AS TEST_CASE;
SELECT c.customer_id, CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
       COUNT(p.payment_id) AS total_transactions, ROUND(AVG(p.amount), 2) AS avg_ticket_size
FROM customer c
JOIN payment p ON c.customer_id = p.customer_id
GROUP BY c.customer_id, customer_name;

SELECT '>>> TC-FN-003: إيراد وعدد الإيجارات لكل تصنيف فيلم' AS TEST_CASE;
SELECT cat.name AS category_name, COUNT(p.payment_id) AS rental_count, SUM(p.amount) AS category_revenue
FROM payment p
JOIN rental r ON p.rental_id = r.rental_id
JOIN inventory i ON r.inventory_id = i.inventory_id
JOIN film_category fc ON i.film_id = fc.film_id
JOIN category cat ON fc.category_id = cat.category_id
GROUP BY cat.name;

SELECT '>>> TC-FN-004: الإيراد الشهري' AS TEST_CASE;
SELECT DATE_FORMAT(p.payment_date, '%Y-%m') AS revenue_month, SUM(p.amount) AS monthly_revenue
FROM payment p
GROUP BY revenue_month;

-- ============================================================
-- GROUP 2: TC-FN-005 to TC-FN-008 | HAVING
-- ============================================================

SELECT '>>> TC-FN-005: HAVING — موظفين مبيعاتهم فوق 15000' AS TEST_CASE;
SELECT st.staff_id, CONCAT(st.first_name, ' ', st.last_name) AS staff_name, SUM(p.amount) AS total_sales
FROM staff st
JOIN payment p ON st.staff_id = p.staff_id
GROUP BY st.staff_id, staff_name
HAVING SUM(p.amount) > 15000;

SELECT '>>> TC-FN-006: HAVING — عملاء إيجارات كتير ودفع قليل' AS TEST_CASE;
SELECT c.customer_id, CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
       COUNT(r.rental_id) AS rental_count, COALESCE(SUM(p.amount), 0) AS total_paid
FROM customer c
JOIN rental r ON c.customer_id = r.customer_id
LEFT JOIN payment p ON r.rental_id = p.rental_id
GROUP BY c.customer_id, customer_name
HAVING COUNT(r.rental_id) >= 20 AND COALESCE(SUM(p.amount), 0) < 50;

SELECT '>>> TC-FN-007: HAVING — أفلام بتتأجر كتير وإيرادها ضعيف' AS TEST_CASE;
SELECT f.film_id, f.title, COUNT(r.rental_id) AS times_rented, SUM(p.amount) AS total_revenue
FROM film f
JOIN inventory i ON f.film_id = i.film_id
JOIN rental r ON i.inventory_id = r.inventory_id
JOIN payment p ON r.rental_id = p.rental_id
GROUP BY f.film_id, f.title
HAVING COUNT(r.rental_id) >= 15 AND SUM(p.amount) < 40;

SELECT '>>> TC-FN-008: HAVING — تصنيفات متوسط الدفع فيها فوق 4' AS TEST_CASE;
SELECT cat.name AS category_name, ROUND(AVG(p.amount), 2) AS avg_payment
FROM payment p
JOIN rental r ON p.rental_id = r.rental_id
JOIN inventory i ON r.inventory_id = i.inventory_id
JOIN film_category fc ON i.film_id = fc.film_id
JOIN category cat ON fc.category_id = cat.category_id
GROUP BY cat.name
HAVING AVG(p.amount) > 4.00;

-- ============================================================
-- GROUP 3: TC-FN-009 to TC-FN-012 | Views & Rounding
-- ============================================================

SELECT '>>> TC-FN-009: مقارنة View sales_by_store بحساب يدوي' AS TEST_CASE;
SELECT store, manager, total_sales FROM sales_by_store;
SELECT s.store_id, SUM(p.amount) AS manual_total_sales
FROM payment p
JOIN staff s ON p.staff_id = s.staff_id
GROUP BY s.store_id;

SELECT '>>> TC-FN-010: مقارنة View sales_by_film_category بحساب يدوي' AS TEST_CASE;
SELECT category, total_sales FROM sales_by_film_category;
SELECT cat.name AS category, SUM(p.amount) AS manual_total_sales
FROM payment p
JOIN rental r ON p.rental_id = r.rental_id
JOIN inventory i ON r.inventory_id = i.inventory_id
JOIN film_category fc ON i.film_id = fc.film_id
JOIN category cat ON fc.category_id = cat.category_id
GROUP BY cat.name;

SELECT '>>> TC-FN-011: صفوف amount مش متطابقة مع ROUND(amount,2)' AS TEST_CASE;
SELECT payment_id, amount, ROUND(amount, 2) AS rounded_amount
FROM payment
WHERE amount <> ROUND(amount, 2);

SELECT '>>> TC-FN-012: مقارنة SUM(ROUND) مع ROUND(SUM) لكل فرع' AS TEST_CASE;
SELECT s.store_id,
       SUM(ROUND(p.amount, 2)) AS sum_of_rounded,
       ROUND(SUM(p.amount), 2) AS rounded_of_sum
FROM payment p
JOIN staff s ON p.staff_id = s.staff_id
GROUP BY s.store_id
HAVING SUM(ROUND(p.amount, 2)) <> ROUND(SUM(p.amount), 2);
