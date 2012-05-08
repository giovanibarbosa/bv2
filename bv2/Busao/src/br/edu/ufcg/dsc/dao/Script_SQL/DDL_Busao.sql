CREATE TABLE Regiao (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nome VARCHAR(30) NOT NULL,
  CONSTRAINT regiaoPKC PRIMARY KEY(id)
);

CREATE TABLE Estado (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  regiaoId INTEGER UNSIGNED NOT NULL,
  nome VARCHAR(30),
  CONSTRAINT estadoPKC PRIMARY KEY(id),
  CONSTRAINT estadoFKT FOREIGN KEY (regiaoId) REFERENCES Regiao (id) ON DELETE CASCADE
);

CREATE TABLE Cidade (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  estadoId INTEGER UNSIGNED NOT NULL,
  valorTarifa DOUBLE PRECISION NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  nome VARCHAR(30) NOT NULL,
  CONSTRAINT cidadePKC PRIMARY KEY(id),
  CONSTRAINT cidadeFKT FOREIGN KEY (estadoId) REFERENCES Estado (id) ON DELETE CASCADE
);

CREATE TABLE Atualizacao (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  cidadeId INTEGER UNSIGNED NOT NULL,
  versao INTEGER NOT NULL,
  data DATE NOT NULL,
  CONSTRAINT atualizacaoPKC PRIMARY KEY(id),
  CONSTRAINT atualizacaoFKT FOREIGN KEY (cidadeId) REFERENCES Cidade (id) ON DELETE CASCADE
);

CREATE TABLE Anuncio_Rota (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  icone VARCHAR(255) NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  telefone CHAR(8),
  descricao VARCHAR(255),
  views INTEGER,
  CONSTRAINT anuncioRotaPKC PRIMARY KEY(id),
  CONSTRAINT validaTelefoneAnuncio CHECK (00000000 < TO_NUMBER(telefone) < 999999999)
);

CREATE TABLE Anuncios (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nome VARCHAR(45) NOT NULL,
  link VARCHAR(255) NULL,
  endereco VARCHAR(255),
  imagem VARCHAR(255),
  telefone CHAR(8),
  numAcessos INTEGER,
  CONSTRAINT anuncioPKC PRIMARY KEY(id),
  CONSTRAINT validaTelefone CHECK (00000000 < TO_NUMBER(telefone) < 999999999)
);

CREATE TABLE CidadeHasAnuncios (
  cidadeId INTEGER UNSIGNED NOT NULL,
  anuncioId INTEGER UNSIGNED NOT NULL,
  CONSTRAINT cidadeAnuncioPKC PRIMARY KEY(cidadeId, anuncioId),
  CONSTRAINT cidadeAnuncioFKT FOREIGN KEY (cidadeId) REFERENCES Cidade (id) ON DELETE CASCADE,
  CONSTRAINT cidadeAnuncio2FKT FOREIGN KEY (anuncioId) REFERENCES Anuncios (id) ON DELETE CASCADE
);

CREATE TABLE PontosTuristicos (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  cidadeId INTEGER UNSIGNED NOT NULL,
  nome VARCHAR(30) NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  CONSTRAINT pontoTuristicoPKC PRIMARY KEY(id),
  CONSTRAINT pontoTuristicoFKT FOREIGN KEY (cidadeId) REFERENCES Cidade (id) ON DELETE CASCADE
);

CREATE TABLE TelefoneUtil (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  cidadeId INTEGER UNSIGNED NOT NULL,
  telefone VARCHAR(8) NOT NULL,
  orgao VARCHAR(30) NOT NULL,
  CONSTRAINT telefoneUtilPKC PRIMARY KEY(id),
  CONSTRAINT telefoneUtilFKT FOREIGN KEY (cidadeId) REFERENCES Cidade (id) ON DELETE CASCADE
);

CREATE TABLE Empresa (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  cidadeId INTEGER UNSIGNED NOT NULL,
  nome VARCHAR(20) NOT NULL,
  anoFundacao YEAR,
  CONSTRAINT empresaPKC PRIMARY KEY(id),
  CONSTRAINT empresaFKT FOREIGN KEY (cidadeId) REFERENCES Cidade (id) ON DELETE CASCADE
);

CREATE TABLE Rota (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  empresaId INTEGER UNSIGNED NOT NULL,
  nome VARCHAR(20) NOT NULL,
  via VARCHAR(20) NULL,
  cor VARCHAR(20) NULL,
  url VARCHAR(255) NOT NULL,
  latInicial DOUBLE PRECISION NOT NULL,
  lgnInicial DOUBLE PRECISION NOT NULL,
  direcao CHAR(1) NOT NULL,
  CONSTRAINT rotaPKC PRIMARY KEY(id),
  CONSTRAINT rotaFKT FOREIGN KEY (empresaId) REFERENCES Empresa (id) ON DELETE CASCADE,
  CONSTRAINT validaDirecao CHECK (UPPER(direcao) IN ('H', 'A')),
  CONSTRAINT validaFDS CHECK (UPPER(fds) IN ('T', 'F'))
);

CREATE TABLE Horario (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  rotaId INTEGER UNSIGNED NOT NULL,
  difEntreOnibus INTEGER UNSIGNED NOT NULL,
  horaInicio TIME NOT NULL,
  horaFim TIME NOT NULL,
  tempoPerTotal INTEGER UNSIGNED NOT NULL,
  numOnibus INTEGER NOT NULL,
  dias	VARCHAR(50) NOT NULL,
  CONSTRAINT horarioPKC PRIMARY KEY(id),
  CONSTRAINT horarioFKT FOREIGN KEY (rotaId) REFERENCES Rota (id) ON DELETE CASCADE
);

CREATE TABLE PontoDeRota (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  rotaId INTEGER UNSIGNED NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  CONSTRAINT pontoRotaPKC PRIMARY KEY(id),
  CONSTRAINT pontoRotaFKT FOREIGN KEY (rotaId) REFERENCES Rota (id) ON DELETE CASCADE
);

CREATE TABLE Onibus (
  id INTEGER UNSIGNED NOT NULL,
  empresaId INTEGER UNSIGNED NOT NULL,
  capacidade INTEGER,
  identificador VARCHAR(6) NOT NULL,
  GPS CHAR(1),
  latitude DOUBLE PRECISION,
  longitude DOUBLE PRECISION,
  CONSTRAINT validaGPS CHECK (UPPER(GPS) IN ('T', 'F')),
  CONSTRAINT onibusPKC PRIMARY KEY(id),
  CONSTRAINT onibusFKT FOREIGN KEY (empresaId) REFERENCES Empresa (id) ON DELETE CASCADE
);

CREATE TABLE RotaHasOnibus (
  rotaId INTEGER UNSIGNED NOT NULL,
  onibusId INTEGER UNSIGNED NOT NULL,
  CONSTRAINT rotaOnibusPKC PRIMARY KEY(rotaId, onibusId),
  CONSTRAINT rotaOnibusFKT FOREIGN KEY (rotaId) REFERENCES Rota (id) ON DELETE CASCADE,
  CONSTRAINT rotaOnibus2FKT FOREIGN KEY (onibusId) REFERENCES Onibus (id) ON DELETE CASCADE
);