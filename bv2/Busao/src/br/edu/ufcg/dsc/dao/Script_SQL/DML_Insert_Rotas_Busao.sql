-- CAMPINA GRANDE

--Linha 092:
INSERT INTO Rota (empresaId, nome, cor, url, numOnibus, numOnibusFDS, latInicial, lgnInicial, direcao, FDS) 
	VALUES (1, '092', 'Branca', 'urllllll', 6, 4, -7.223373,  -35.893029, 'A', 'T');

INSERT INTO Horario (rotaId, difEntreOnibus, difEntreOnibusFDS, horaInicio, horaFim, horaInicioFDS, horaFimFDS, tempoPerTotal, tempoPerTotalFDS)
	VALUES (1, 16, 24, '04:42', '24:10', '05:19', '23:50', 96, 96);

--Interarea 245
INSERT INTO Rota (empresaId, nome, url, numOnibus, numOnibusFDS, latInicial, lgnInicial, direcao, FDS) 
	VALUES (2 '245-A', 'urllllll', 6, 5, MUDA ATRIBUTOS,  MUDA ATRIBUTOS, 'A', 'T');

INSERT INTO Horario (rotaId, difEntreOnibus, difEntreOnibusFDS, horaInicio, horaFim, horaInicioFDS, horaFimFDS, tempoPerTotal, tempoPerTotalFDS)
	VALUES (2, 10, 15, '05:00', '23:55', '05:00', '24:10', 110, 110);

INSERT INTO Rota (empresaId, nome, url, numOnibus, numOnibusFDS, latInicial, lgnInicial, direcao, FDS) 
	VALUES (2 '245-B', 'urllllll', 6, 5, MUDA ATRIBUTOS,  MUDA ATRIBUTOS, 'A', 'T');

INSERT INTO Horario (rotaId, difEntreOnibus, difEntreOnibusFDS, horaInicio, horaFim, horaInicioFDS, horaFimFDS, tempoPerTotal, tempoPerTotalFDS)
	VALUES (3, 10, 15, '05:00', '23:55', '05:00', '24:10', 110, 110);