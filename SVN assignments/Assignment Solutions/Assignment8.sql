CREATE database IF NOT EXISTS youngry2 ;
USE youngry2;
CREATE TABLE country SELECT * FROM world.country;
INSERT INTO country (Code, Name, Population, Continent) VALUES ('NHD', 'Nowhereland', 3, 'Antarctica');
UPDATE country SET Name = 'Nowhere land' WHERE Name = 'Nowhereland';
CREATE VIEW SimplifiedInfo AS SELECT Code, Name, Continent, Population, SurfaceArea, IndepYear, GovernmentForm FROM country;
DELETE FROM SimplifiedInfo where Code = 'NHD';
-- Question 1
SELECT * FROM SimplifiedInfo where GovernmentForm = 'Republic';
-- Question 2
SELECT COUNT(*) FROM SimplifiedInfo WHERE GovernmentForm = 'Republic';
-- Question 3
SELECT COUNT(*) FROM SimplifiedInfo WHERE GovernmentForm LIKE '%Republic';
-- Question 4
SELECT COUNT(*) FROM SimplifiedInfo WHERE GovernmentForm LIKE '%Republic' GROUP BY GovernmentForm ORDER BY COUNT(*) DESC;
-- Question 5
SELECT GovernmentForm, COUNT(GovernmentForm) FROM SimplifiedInfo GROUP BY GovernmentForm HAVING COUNT(GovernmentForm) > 1;
-- Question 6
Select Continent, Count(GovernmentForm) From (Select Name, Continent, GovernmentForm FROM SimplifiedInfo WHERE GovernmentForm Like '%Republic') as A Group by Continent;
-- Question 7
Select Name, MAX(SurfaceArea) FROM SimplifiedInfo GROUP BY Name ORDER BY MAX(SurfaceArea) DESC LIMIT 1;
-- Question 8
Select AVG(Population) as 'AveragePopulation' FROM (Select Name, Population, GovernmentForm FROM SimplifiedInfo HAVING GovernmentForm Like '%Monarchy') as A;
-- Question 9
Select Continent, AVG(Population) FROM (Select Name, Population, GovernmentForm, Continent FROM SimplifiedInfo HAVING GovernmentForm Like '%Monarchy') as A GROUP BY Continent;
-- Question 10
SELECT Name, GovernmentForm FROM SimplifiedInfo Where GovernmentForm LIKE '%Republic' ORDER BY GovernmentForm, Name;
-- Question 11
SELECT Name, GovernmentForm, IndepYear From SimplifiedInfo WHERE GovernmentForm NOT LIKE '%Republic' HAVING IndepYear > 1800;