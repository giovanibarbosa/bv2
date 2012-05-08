-- Regiao
INSERT INTO Regiao (nome) values ('Nordeste');
INSERT INTO Regiao (nome) values ('Sudeste');
INSERT INTO Regiao (nome) values ('Centro-Oeste');
INSERT INTO Regiao (nome) values ('Sul');
INSERT INTO Regiao (nome) values ('Norte');

-- Estado
INSERT INTO Estado (regiaoId, nome) values (1, 'Paraiba');
INSERT INTO Estado (regiaoId, nome) values (1, 'Ceara');
INSERT INTO Estado (regiaoId, nome) values (1, 'Pernambuco');
INSERT INTO Estado (regiaoId, nome) values (1, 'Bahia');
INSERT INTO Estado (regiaoId, nome) values (1, 'Rio Grande do Norte');

-- Cidade
INSERT INTO Cidade (estadoId, valorTarifa, latitude, longitude, nome) values (1, 2.10, -07.13, -35.52, 'Campina Grande');
INSERT INTO Cidade (estadoId, valorTarifa, latitude, longitude, nome) values (1, 2.30, -07.06, -34.51, 'Joao Pessoa');
INSERT INTO Cidade (estadoId, valorTarifa, latitude, longitude, nome) values (2, 2.00, -03.43, -38.32, 'Fortaleza');
INSERT INTO Cidade (estadoId, valorTarifa, latitude, longitude, nome) values (3, 2.15, -08.00, -35.00, 'Recife');
INSERT INTO Cidade (estadoId, valorTarifa, latitude, longitude, nome) values (4, 2.50, -13.00, -38.30, 'Salvador');
INSERT INTO Cidade (estadoId, valorTarifa, latitude, longitude, nome) values (5, 2.20, -05.47, -35.13, 'Natal');

--Pontos Turisticos
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (3, 'Parque do Povo', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (3, 'Acude Velho', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (4, 'Ponta de Seixas', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (4, 'Cabo Branco', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (4, 'Estacao Ciencia', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (4, 'Centro Historico', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (4, 'Por do Sol do Jacare', -0, -0.0);
INSERT INTO PontosTuristicos (cidadeId, nome, latitude, longitude) values (4, 'Areia Vermelha', -0, -0.0);

--Telefones Uteis
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (1, '190', 'Policia');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (1, '192', 'SAMU');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (1, '193', 'Bombeiros');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (2, '190', 'Policia');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (2, '192', 'SAMU');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (2, '193', 'Bombeiros');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (3, '190', 'Policia');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (3, '192', 'SAMU');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (3, '193', 'Bombeiros');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (4, '190', 'Policia');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (4, '192', 'SAMU');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (4, '193', 'Bombeiros');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (5, '190', 'Policia');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (5, '192', 'SAMU');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (5, '193', 'Bombeiros');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (6, '190', 'Policia');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (6, '192', 'SAMU');
INSERT INTO TelefoneUtil (cidadeId, telefone, orgao) values (6, '193', 'Bombeiros');

--Empresa
INSERT INTO Empresa (cidadeId, nome) values (3, 'Cabral');
INSERT INTO Empresa (cidadeId, nome) values (3, 'Nacional');
INSERT INTO Empresa (cidadeId, nome) values (3, 'Trans Nacional');
INSERT INTO Empresa (cidadeId, nome) values (3, 'Borborema');
INSERT INTO Empresa (cidadeId, nome) values (3, 'Cruzeiro');


