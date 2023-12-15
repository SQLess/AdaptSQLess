 (SELECT (COLLATION(_UTF8MB4'2008')) AS `f3`
 FROM (SELECT `col_decimal(40, 20)_undef_unsigned` AS `f7`,
 `col_decimal(40, 20)_key_signed` AS `f10`,
 `col_decimal(40, 20)_undef_signed` AS `f9`
 FROM `table_3_utf8_undef`) AS `t2`
 JOIN (SELECT `col_float_key_unsigned` AS `f11`,
 `col_decimal(40, 20)_key_unsigned` AS `f8`,
 `col_double_undef_signed` AS `f12` FROM `table_3_utf8_undef` )
 AS `t3`)