CREATE TABLE status(
    entity_id int NOT NULL AUTO_INCREMENT,
    status_code varchar(25),
    status_name varchar(50),
    PRIMARY KEY (entity_id)
);

CREATE TABLE ticket(
    entity_id int NOT NULL AUTO_INCREMENT, 
    ticket_code varchar(25) UNIQUE,
    title varchar(100), 
    description text, 
    created_by varchar(25), 
    assigned_to varchar(25), 
    created_at datetime, 
    updated_at datetime, 
    status_id int,
    PRIMARY KEY (entity_id)
);

ALTER TABLE ticket 
	ADD CONSTRAINT fk_ticket_status 
    FOREIGN KEY (status_id) 
    REFERENCES status(entity_id);
