SELECT country_id, country FROM country;
SELECT city_id, city, country_id FROM city;
SELECT city_id, city, country_id FROM city WHERE city_id=26;

SELECT ci.city_id, ci.city, ci.country_id, co.country FROM city ci JOIN country co ON ci.country_id = co.country_id;

SELECT COUNT(*) FROM rental;
SELECT * FROM rental LIMIT 11;

SELECT * FROM rental WHERE rental_id BETWEEN (1*100) AND (1*100+100);
SELECT customer_id, staff_id FROM rental WHERE rental_id BETWEEN (13*100) AND (13*100+100);

DESC customer;

SELECT CONCAT(CONCAT(last_name, ' '), first_name) FROM customer;
SELECT CONCAT(last_name, ' ', first_name) FROM customer;