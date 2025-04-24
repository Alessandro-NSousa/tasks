CREATE TABLE tarefa (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(250) NOT NULL,
    colaborador_id UUID,
    CONSTRAINT fk_membro
    FOREIGN KEY (colaborador_id)
    REFERENCES colaborador(id)
);