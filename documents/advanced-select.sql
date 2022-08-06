
SELECT listings.listno,housetype,date_start, (POWER(LONGTITUDE - 5,2) + POWER(LATITUDE - 5,2)) as 'Distance' 
FROM amenities join LISTINGS join CALENDAR ON CALENDAR.listno = LISTINGS.listno
WHERE (POWER(LONGTITUDE - 5,2) + POWER(LATITUDE - 5,2) <= POWER(5, 2))
AND DATE_START <= '2022-3-6' AND DATE_END >= "2022-4-7" AND PRICE >= 0 AND PRICE <= 9999 ORDER BY Distance ASC;

SELECT listno, housetype,longtitude,latitude, address, postal, 
city, country, date_start, date_end, price FROM AMENITIES 
NATURAL JOIN LISTINGS NATURAL JOIN CALENDAR WHERE ABS(POSTAL - 15000) <= 8000;

select ownership.SIN, count(ownership.sin) as 'Hosted #', country 
from listings JOIN OWNERSHIP JOIN User 
WHERE COUNTRY
AND listings.listno = ownership.listno AND user.sin = ownership.sin
GROUP BY ownership.sin, country 
ORDER BY COUNT(ownership.sin) DESC;

select count(listno) from listings b natural join ownership c 
WHERE country = "Canada" AND CITY = 'Toronto' group by b.country;

select uname, ownership.SIN, count(ownership.sin), country
from listings JOIN OWNERSHIP JOIN User 
WHERE country = "Canada"
AND listings.listno = ownership.listno AND user.sin = ownership.sin
GROUP BY ownership.sin, country
HAVING (10 * COUNT(ownership.sin) > (
select count(listno) from listings b natural JOIN ownership c 
WHERE country = "Canada" group by b.country))
ORDER BY COUNT(ownership.sin) DESC;

select uname, renter.sin, count(renter.sin)
from renter join booking on renter.sin = booking.rentersin
join user on renter.sin = user.sin
where date_start >= "2022-1-1" and date_end <= "2022-12-12"
group by renter.sin order by count(renter.sin) desc;

select uname, ownership.SIN, count(ownership.sin), city, country 
from listings JOIN OWNERSHIP JOIN User 
WHERE country = 'Canada' AND CITY like 'Toronto' AND listings.listno = ownership.listno AND user.sin = ownership.sin 
GROUP BY ownership.sin, country 
HAVING (10 * COUNT(ownership.sin) > (select count(listno) from listings b natural JOIN ownership c 
WHERE country like 'Canada' AND CITY like 'Toronto' group by b.country)) 
ORDER BY COUNT(ownership.sin) DESC;

select uname, renter.sin, count(renter.sin), city 
from listings join booking on booking.listno = listings.listno
join renter on renter.sin = booking.rentersin
join user on renter.sin = user.SIN
where date_start >= "2022-1-1" and date_end <= "2022-12-12" and city = "Toronto" 
group by renter.sin 
having (count(renter.sin) >= 2)  
order by count(renter.sin) asc;

select uname, b.rentersin, count(b.rentersin) 
from listings join (select * from booking where cancel like "yes") as b on listings.listno = b.listno
join renter on renter.sin = b.rentersin 
join user on user.sin = renter.sin
where date_start >= "2021-8-8"
group by b.rentersin
order by count(b.rentersin) desc;

select uname, ownership.sin, count(ownership.sin) + "as" 
from user natural join ownership 
join (select * from booking where cancel like "yes") as b on ownership.listno = b.listno
where date_start >= "2021-8-8"
group by ownership.sin
order by count(ownership.sin) desc;