SELECT DISTINCT site.site_id, site_number, site.campground_id, max_occupancy, accessible, max_rv_length, utilities, daily_fee, open_from_mm, open_to_mm FROM site
LEFT JOIN reservation ON reservation.site_id = site.site_id
JOIN campground ON campground.campground_id = site.campground_id
WHERE campground.park_id = 1
AND (site.site_id NOT IN (SELECT site_id FROM reservation)
OR site.site_id NOT IN (SELECT site.site_id FROM site
JOIN reservation ON reservation.site_id = site.site_id
WHERE to_date > '2020-06-01' AND from_date < '2020-06-05'))
ORDER BY site.site_id LIMIT 5;



SELECT site_number, site_id, campground_id, max_occupancy, accessible,
 max_rv_length, utilities, daily_fee, open_from_mm, open_to_mm, OVER (PARTITION BY new_rank order by new_rank) AS rank_row
FROM
        (SELECT DISTINCT site_number, site.site_id, site.campground_id, max_occupancy, accessible,
        max_rv_length, utilities, daily_fee, CAST(open_from_mm AS INT), CAST(open_to_mm AS INT), ROW_NUMBER OVER(PARTITION BY site.category_id ORDER BY site_number)
        FROM site
        LEFT JOIN reservation ON reservation.site_id = site.site_id
        JOIN campground ON campground.campground_id = site.campground_id
        WHERE park_id = 1
        AND (site.site_id NOT IN(SELECT site_id FROM reservation)
        OR site.site_id NOT IN(SELECT site.site_id FROM site
        JOIN reservation ON reservation.site_id = site.site_id
        WHERE to_date > '2020-03-01' AND from_date < '2020-03-30'))
        ORDER BY site.campground_id, site_number) as subquery
WHERE rank_row <=5;