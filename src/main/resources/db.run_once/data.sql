INSERT INTO tariffs (
    id, name, call_rate_same_operator, call_rate_other_operator,
    free_incoming_minutes, monthly_fee) VALUES
                                            (11, 'Классика', 1.5, 2.5, null, null),
                                            (12, 'Помесячный', 1.5, 2.5, 50, 100);


-- Добавление тестовых данных в таблицу абонентов
INSERT INTO subscribers (phone_number, balance, tariff_id) VALUES
    ('1234567890', 50.0, 11),
    ('2234567890', 150.0, 12),
    ('3234567890', 1200.0, 11),
    ('4234567890', 5000.0, 11),
    ('5234567890', 0.1, 12),
    ('9987654321', 0.1, 11),
    ('8987654321', 123456.7, 11),
    ('7987654321', 123456.7, 12),
    ('6987654321', 0.0, 11),
    ('5987654321', 0.0, 12);

