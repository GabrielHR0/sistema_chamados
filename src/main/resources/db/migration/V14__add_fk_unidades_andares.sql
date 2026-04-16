-- V14: add relationship between unidades and andares
ALTER TABLE unidades
    ADD CONSTRAINT fk_unidades_andares
    FOREIGN KEY (andar_id) REFERENCES andares(id) ON DELETE CASCADE;
