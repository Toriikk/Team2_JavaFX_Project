CREATE TABLE BOOKS (
    ISBN NUMBER NOT NULL,
    TITLE VARCHAR2(100),
    AUTHOR VARCHAR2(100),
    YEAR NUMBER NOT NULL
);

COMMIT;
SELECT
    *
FROM books;

DELETE FROM books;

ALTER TABLE books drop COLUMN year;

SELECT
    *
FROM books;

ALTER TABLE books add year NUMBER NOT NULL;

ALTER TABLE books drop COLUMN year;

ALTER TABLE books add regdate VARCHAR2(10) NOT NULL ;  

ALTER TABLE books add price number NOT NULL ;

ALTER TABLE books add quantity number NOT NULL;

SELECT
    *
FROM books;

ALTER TABLE books drop COLUMN regdate;
ALTER TABLE books add regdate  VARCHAR2(10);

commit;

SELECT
    *
FROM books;

DROP TABLE BOOKS;

DROP TABLE EXERCISE;

ALTER TABLE books drop COLUMN year;

COMMIT;
