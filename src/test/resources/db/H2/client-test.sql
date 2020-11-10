INSERT INTO client (id, login, password) VALUES (1, 'simpleUser', '12345');
INSERT INTO client (id, login, password) VALUES (2, 'secondUser', '16284');
INSERT INTO client (id, login, password) VALUES (3, 'anotherUser', '59134');

INSERT INTO client_roles (client_id, roles) VALUES (1, 'USER');
INSERT INTO client_roles (client_id, roles) VALUES (2, 'USER');
INSERT INTO client_roles (client_id, roles) VALUES (3, 'USER');