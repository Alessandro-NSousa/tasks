CREATE TABLE tarefa (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(250) NOT NULL,
    status VARCHAR(100),
    criacao date,
    usuario_id UUID,
    CONSTRAINT fk_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuario(id)
);