SELECT TO_CHAR(REG, 'YYYY-MM-DD') AS REG, COUNT(*) AS COUNT
FROM (SELECT *
	FROM USERACTION 
	WHERE ACTION = ? 
		AND TO_CHAR(REG, 'YYYY-MM-DD') = TO_CHAR(SYSDATE - 1, 'YYYY-MM-DD'))
GROUP BY TO_CHAR(REG, 'YYYY-MM-DD')