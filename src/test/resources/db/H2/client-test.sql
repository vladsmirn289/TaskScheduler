INSERT INTO client (id, login, password) VALUES (100, 'simpleUser', '12345');
INSERT INTO client (id, login, password) VALUES (101, 'secondUser', '16284');
INSERT INTO client (id, login, password) VALUES (102, 'anotherUser', '59134');

INSERT INTO client_roles (client_id, roles) VALUES (100, 'USER');
INSERT INTO client_roles (client_id, roles) VALUES (101, 'USER');
INSERT INTO client_roles (client_id, roles) VALUES (102, 'USER');