/*Password: 12345*/
INSERT INTO client (id, login, password) VALUES (100, 'simpleUser', '$2y$08$uCloDWruiPYVMdpTjmiVKu6L2NBQsQZ0C745hI1H/eDr/aY0JnzKy');
/*Password: 16284*/
INSERT INTO client (id, login, password) VALUES (101, 'secondUser', '$2y$08$dsqYwVvjkHI1J6Tx2G/suOYF05qs/3nTUa3oXpb5FX6xBKLfX569q');
/*Password: 59134*/
INSERT INTO client (id, login, password) VALUES (102, 'anotherUser', '$2y$08$ePHx9Wt/978DZQ.4NvpM1.VYjiww6xl5A5.C.4wMIfYIs/dRVirvS');

INSERT INTO client_roles (client_id, roles) VALUES (100, 'USER');
INSERT INTO client_roles (client_id, roles) VALUES (101, 'USER');
INSERT INTO client_roles (client_id, roles) VALUES (102, 'USER');