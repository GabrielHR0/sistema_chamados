-- V13: add relationship between andares and blocos
ALTER TABLE andares
    ADD CONSTRAINT fk_andares_blocos
    FOREIGN KEY (bloco_id) REFERENCES blocos(id) ON DELETE CASCADE;
