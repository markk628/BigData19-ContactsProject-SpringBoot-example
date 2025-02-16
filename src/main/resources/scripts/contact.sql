CREATE DATABASE contact;

USE contact;

SHOW TABLES;

DROP TABLE IF EXISTS members_contacts;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS relationship;

CREATE TABLE members(
	member_id			 VARCHAR(50) NOT NULL,
	member_password      VARCHAR(50) NOT NULL,
	PRIMARY KEY (member_id)
);

CREATE TABLE relationship(
	relationship_id     INT AUTO_INCREMENT,
	relationship    	VARCHAR(2) NOT NULL,
	PRIMARY KEY (relationship_id)
);

CREATE TABLE contacts (
	contact_id  		INT AUTO_INCREMENT,
	contact_name		VARCHAR(30) NOT NULL,
	contact_number 		VARCHAR(11) NOT NULL,
	contact_address 	VARCHAR(255) NOT NULL,
	relationship_id     INT NOT NULL,
	PRIMARY KEY (contact_id),
	FOREIGN KEY (relationship_id)
			REFERENCES relationship(relationship_id)
);

CREATE TABLE members_contacts(
	member_id     VARCHAR(50) NOT NULL,
	contact_id    INT NOT NULL,
	foreign key(member_id)
			REFERENCES members(member_id),
	foreign key(contact_id)
			REFERENCES contacts(contact_id)
);

commit;

SELECT * FROM members;
SELECT * FROM members_contacts;
SELECT * FROM contacts;
SELECT * FROM relationship;

INSERT INTO relationship (relationship) VALUES ('가족'); -- 1
INSERT INTO relationship (relationship) VALUES ('친구'); -- 2
INSERT INTO relationship (relationship) VALUES ('기타'); -- 3
INSERT INTO members VALUES ('test', 'test');
INSERT INTO members VALUES ('test2', 'test2');
INSERT INTO members VALUES ('test3', 'test3');
INSERT INTO contacts (contact_name, contact_number, contact_address, relationship_id) VALUES ('a','010', 'Seoul', 1);		-- 1
INSERT INTO contacts (contact_name, contact_number, contact_address, relationship_id) VALUES ('b','020', 'Busan', 2);		-- 2
INSERT INTO contacts (contact_name, contact_number, contact_address, relationship_id) VALUES ('c','030', 'Incheon', 3);	-- 3
INSERT INTO members_contacts VALUES ('test', 1);
INSERT INTO members_contacts VALUES ('test', 2);
INSERT INTO members_contacts VALUES ('test2', 3);

WITH contacts_relationship AS (
	SELECT c.contact_id,
		   c.contact_name,
		   c.contact_number,
		   c.contact_address,
		   r.relationship
	  FROM contacts c
	 INNER JOIN relationship r
	 	ON c.relationship_id = r.relationship_id
),
members_and_contacts AS (
	SELECT c.contact_id,
		   c.contact_name,
		   c.contact_number,
		   c.contact_address,
		   c.relationship,
		   m.member_id
	  FROM members_contacts m
	 INNER JOIN contacts_relationship c
	 	ON m.contact_id = c.contact_id
)
SELECT *
  FROM members_and_contacts m
 WHERE m.member_id = :memberId
;

WITH contacts_relationship AS (
	SELECT c.contact_id,
		   c.contact_name,
		   c.contact_number,
		   c.contact_address,
		   r.relationship
	  FROM contacts c
	 INNER JOIN relationship r
	 	ON c.relationship_id = r.relationship_id
),
members_and_contacts AS (
	SELECT c.contact_id,
		   c.contact_name,
		   c.contact_number,
		   c.contact_address,
		   c.relationship,
		   m.member_id
	  FROM members_contacts m
	 INNER JOIN contacts_relationship c
	 	ON m.contact_id = c.contact_id
)
SELECT *
  FROM members_and_contacts m
 WHERE m.member_id = :memberId
   AND m.contact_name LIKE '%h%'
;

WITH contacts_relationship AS (
	SELECT c.contact_id,
		   c.contact_name,
		   c.contact_number,
		   c.contact_address,
		   r.relationship
	  FROM contacts c
	 INNER JOIN relationship r
	 	ON c.relationship_id = r.relationship_id
),
members_and_contacts AS (
	SELECT c.contact_id,
		   c.contact_name,
		   c.contact_number,
		   c.contact_address,
		   c.relationship,
		   m.member_id
	  FROM members_contacts m
	 INNER JOIN contacts_relationship c
	 	ON m.contact_id = c.contact_id
)
SELECT *
  FROM members_and_contacts m
 WHERE m.member_id = :memberId
   AND m.contact_id = :contactId
;

UPDATE contacts c
   SET c.contact_name = :name,
  	   c.contact_number = :number,
  	   c.contact_address = :address,
  	   c.relationship_id = :relationship
 WHERE c.contact_id = :contactId
 ;

DELETE FROM contacts c
 WHERE c.contact_id = :contactId
;

DELETE FROM members_contacts m
 WHERE m.member_id = :memberId
   AND m.contact_id = :contactId
;