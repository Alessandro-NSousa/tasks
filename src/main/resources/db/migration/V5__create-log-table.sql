CREATE TABLE log (
     id UUID PRIMARY KEY,
     ativo BOOLEAN,
     data_criacao TIMESTAMP,
     descricao VARCHAR(255),
     classe_entidade VARCHAR(255),
     identidade UUID
);