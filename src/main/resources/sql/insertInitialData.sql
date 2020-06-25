INSERT INTO groupapp(name) VALUES('worker')
INSERT INTO groupapp(name) VALUES('hr')
INSERT INTO groupapp(name) VALUES('admin')

INSERT INTO groupapp_roles(groupapp_name, roles) VALUES('worker', 'WORKER')
INSERT INTO groupapp_roles(groupapp_name, roles) VALUES('hr', 'WORKER')
INSERT INTO groupapp_roles(groupapp_name, roles) VALUES('hr', 'HUMAN_RESOURCES')
INSERT INTO groupapp_roles(groupapp_name, roles) VALUES('admin', 'WORKER')
INSERT INTO groupapp_roles(groupapp_name, roles) VALUES('admin', 'HUMAN_RESOURCES')
INSERT INTO groupapp_roles(groupapp_name, roles) VALUES('admin', 'FULL_CONTROL')

INSERT INTO userapp(username, password, groupapp_name) VALUES('guestWorker', 'PBKDF2WithHmacSHA512:3072:YKK12OQLbn99tCui7SKENoBDeJrmo8z6O6U+/T/vfyaUGU5S+JYPTEkyHd2ikdD5AnvA5YQEMMXzFQpd+2siyw==:Ql5wAzGY0+A1xYLQmTbV6zd3JfqxN+rKrltjd+iYOPQ=', 'worker')
INSERT INTO userapp(username, password, groupapp_name) VALUES('guestHr', 'PBKDF2WithHmacSHA512:3072:YKK12OQLbn99tCui7SKENoBDeJrmo8z6O6U+/T/vfyaUGU5S+JYPTEkyHd2ikdD5AnvA5YQEMMXzFQpd+2siyw==:Ql5wAzGY0+A1xYLQmTbV6zd3JfqxN+rKrltjd+iYOPQ=', 'hr')
INSERT INTO userapp(username, password, groupapp_name) VALUES('demoAdmin', 'PBKDF2WithHmacSHA512:3072:YKK12OQLbn99tCui7SKENoBDeJrmo8z6O6U+/T/vfyaUGU5S+JYPTEkyHd2ikdD5AnvA5YQEMMXzFQpd+2siyw==:Ql5wAzGY0+A1xYLQmTbV6zd3JfqxN+rKrltjd+iYOPQ=', 'admin')
INSERT INTO userapp(username, password, groupapp_name) VALUES('maina', 'PBKDF2WithHmacSHA512:3072:owNrEJUGw+uovdxSSpbEGzjmVCieD8cLsWylnEiHnHTdiQaTCPKH68MV+VfZCAvr9Mlg4yNZarkx5cn3SkY6UQ==:XP0xQRlSQaX0akrpklZwKzZl2QWDIMI3KY478c6R7Qg=', 'admin')

INSERT INTO employmentcontract(contracttime, name, hoursdaily, version) VALUES(0, 'Full time', 8, 0)
INSERT INTO employmentcontract(contracttime, name, hoursdaily, version) VALUES(1, 'Part time', 4, 0)

INSERT INTO lunchbreakticket(name, value, version) VALUES('Ticket 5', 5, 0)
INSERT INTO lunchbreakticket(name, value, version) VALUES('Ticket 7', 6.97, 0)

INSERT INTO worker(name, contract_id, userapp_username, version) VALUES('Mainardi Davide', 1, 'maina', 0)
INSERT INTO worker(name, contract_id, userapp_username, version) VALUES('Rossi Mario', 1, 'guestWorker', 0)
INSERT INTO worker(name, contract_id, userapp_username, version) VALUES('Neri Lucia', 2, 'guestHr', 0)
INSERT INTO worker(name, contract_id, userapp_username, version) VALUES('Bianchi Luigi', 1, null, 0)
INSERT INTO worker(name, contract_id, userapp_username, version) VALUES('Verdi Alessandro', 2, null, 0)
