SELECT t1."c1" AS "f1", (t1."c2" + t2."c3") AS "f2" FROM "table_1" AS "t1" JOIN "table_2"
AS "t2" ON t1."c5" = t2."c5" WHERE "f2" > 10 AND t2."c6" < "f2" GROUP BY "f1", "f2"
HAVING SUM("f2") > 20 FOR UPDATE SKIP LOCKED;