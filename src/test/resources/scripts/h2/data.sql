-- Inserindo as questões
INSERT INTO question (externalId, text, type, required, placeholder, minValue, maxValue, step) VALUES
('birthdate', 'Qual sua data de nascimento?',
    'DATE', TRUE, 'Dia / Mês / Ano', NULL, NULL, NULL);

INSERT INTO question (externalId, text, type, required) VALUES
('gender', 'Com qual gênero você se identifica?',
    'RADIO', TRUE);

INSERT INTO question (externalId, text, type, required) VALUES
('q3_frequency', 'Com que frequência você urina?',
    'RADIO', TRUE);

INSERT INTO question (externalId, text, type, required) VALUES
('q4_amount', 'Gostaríamos de saber a quantidade de urina que você pensa que perde (Assinale a alternativa que melhor representa a sua percepção)',
    'RADIO', TRUE);

INSERT INTO question (externalId, text, type, required, minValue, maxValue, step) VALUES
('q5_interference', 'Em geral, quanto que perder urina interfere em sua vida diária? (0 = não interfere, 10 = interfere muito)',
    'SLIDER', TRUE, 0, 10, 1);

INSERT INTO question (externalId, text, type, required) VALUES
('q6_when', 'Em que momentos você perde urina? (Assinale todas as alternativas que se aplicam a você)',
    'CHECKBOX', TRUE);

-- Inserindo as opções para a questão de gênero
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'gender'), 'Masculino', 'M'),
((SELECT id FROM question WHERE externalId = 'gender'), 'Feminino', 'F');

-- Inserindo as opções para a questão de frequência
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Nunca', '0'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Uma vez por semana ou menos', '1'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Duas a três vezes por semana', '2'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Uma vez ao dia', '3'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Diversas vezes ao dia', '4'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'O tempo todo', '5');

-- Inserindo as opções para a questão de quantidade
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Nenhuma', '0'),
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Uma pequena quantidade', '2'),
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Uma quantidade moderada', '4'),
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Uma quantidade elevada', '6');

-- Inserindo as opções para a questão de momentos
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Nunca', '0'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Antes de chegar ao banheiro', '1'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Perco quando tusso ou espirro', '2'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Perco quando estou dormindo', '3'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Perco quando estou realizando atividades físicas', '4'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Perco quando terminei de urinar e estou me vestindo', '5'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Perco sem razão aparente', '6'),
((SELECT id FROM question WHERE externalId = 'q6_when'), 'Perco o tempo todo', '7');

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Inserindo dados de teste para usuários e diário miccional
INSERT INTO credential (passwordHash, salt) VALUES
('1thzHtxdZlMY1Eyy90PCHNPUkbetaYCC8JjWIAgmxvo=', 'DghbE3gLRofkyVuZZ37oQA=='),
('z943fyO/J1bjRnoXt7Segm2h2c6e6tuS/bHMlSCPZJs=', '1HCmqwTC12DzqNpVKdQ0Uw==');

INSERT INTO patientProfile (birthDate, gender, iciq3answer, iciq4answer, iciq5answer, iciqScore, urinationLoss) VALUES
('1980-05-15', 'M', 0, 0, 3, 3, '1,2,3'),
('1990-10-20', 'F', 2, 2, 6, 10, '2,3,4,5');

INSERT INTO preferences (highContrast, bigFont, reminderCalendar, reminderCalendarSchedule, reminderWorkout, reminderWorkoutSchedule, encouragingMessages, workoutMediaType) VALUES
(FALSE, FALSE, TRUE, '09:00', TRUE, '18:00', TRUE, 'VIDEO'),
(TRUE, TRUE, FALSE, NULL, FALSE, NULL, TRUE, 'IMAGE');

INSERT INTO role(description, permissionLevel, reason, hasDocument) VALUES
('Usuário comum', 1, 'Acesso padrão ao aplicativo.', FALSE),
('Usuário comum', 1, 'Acesso padrão ao aplicativo.', FALSE),
('Administrador', 3, 'Acesso total ao sistema para gerenciamento e manutenção.', FALSE);

-- Inserindo usuários (agora incluindo roleId)
INSERT INTO appUser (name, email, credentialId, patientProfileId, preferencesId, roleId) VALUES
('Usuário 1', 'usuario1@example.com', 1, 1, 1, 1),
('Usuária 2', 'usuario2@example.com', 2, 2, 2, 2);

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Inserindo dados do calendário
INSERT INTO calendarDay (dateValue, userId, leakageLevel, eventsCount, completedExercises, notesPreview, dayTitle) VALUES
('2025-06-01', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'LOW', 1, 1, 'Dia tranquilo.', 'seg'),
('2025-06-05', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'LOW', 1, 2, 'Pouca urgência.', 'qui'),
('2025-06-10', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'MEDIUM', 1, 0, NULL, 'ter'),
('2025-06-15', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'HIGH', 1, 3, 'Vazamento noturno.', 'dom'),
('2025-07-02', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'NONE', 0, 2, 'Sem eventos.', 'qua'),
('2025-07-08', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'LOW', 1, 1, NULL, 'ter'),
('2025-07-14', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'MEDIUM', 1, 2, 'Dia agitado.', 'seg'),
('2025-08-03', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'HIGH', 1, 0, 'Vazamento após café.', 'dom'),
('2025-08-10', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'NONE', 1, 1, NULL, 'dom'),
('2025-08-20', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'LOW', 1, 2, 'Pouca urgência.', 'qua'),
('2025-06-03', (SELECT id FROM appUser WHERE name = 'Usuária 2'), 'MEDIUM', 1, 1, 'Dia normal.', 'ter'),
('2025-07-05', (SELECT id FROM appUser WHERE name = 'Usuária 2'), 'HIGH', 1, 2, 'Vazamento noturno.', 'sab'),
('2025-07-18', (SELECT id FROM appUser WHERE name = 'Usuária 2'), 'NONE', 0, 1, NULL, 'sex'),
('2025-08-07', (SELECT id FROM appUser WHERE name = 'Usuária 2'), 'LOW', 1, 2, 'Pouca urgência.', 'qui'),
('2025-08-22', (SELECT id FROM appUser WHERE name = 'Usuária 2'), 'MEDIUM', 1, 0, NULL, 'sex'),
('2025-09-02', (SELECT id FROM appUser WHERE name = 'Usuário 1'), 'LOW', 1, 1, 'Dia tranquilo.', 'ter');

-- Inserindo dados de urinação
INSERT INTO urinationData (calendarDayId, timeValue, amount, leakage, reason, urgency) VALUES
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-01' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '12:30', 'LOW', FALSE, 'Almoço', FALSE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-05' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '09:00', 'LOW', FALSE, NULL, FALSE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-10' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '07:45', 'MEDIUM', TRUE, 'Acordou com urgência', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-15' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '23:30', 'HIGH', TRUE, 'Sono profundo', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-07-08' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '15:20', 'LOW', FALSE, 'Após exercício', FALSE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-07-14' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '18:00', 'MEDIUM', TRUE, 'Trânsito', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-03' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '08:45', 'HIGH', TRUE, 'Café da manhã', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-10' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '11:00', 'NONE', FALSE, NULL, FALSE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-20' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '14:30', 'LOW', FALSE, 'Almoço', FALSE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-03' AND userId = (SELECT id FROM appUser WHERE name = 'Usuária 2')), '09:30', 'MEDIUM', TRUE, 'Trabalho', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-07-05' AND userId = (SELECT id FROM appUser WHERE name = 'Usuária 2')), '22:00', 'HIGH', TRUE, 'Sono profundo', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-07' AND userId = (SELECT id FROM appUser WHERE name = 'Usuária 2')), '13:00', 'LOW', FALSE, 'Almoço', FALSE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-22' AND userId = (SELECT id FROM appUser WHERE name = 'Usuária 2')), '16:45', 'MEDIUM', TRUE, 'Trânsito', TRUE),
((SELECT id FROM calendarDay WHERE dateValue = '2025-09-02' AND userId = (SELECT id FROM appUser WHERE name = 'Usuário 1')), '18:42', 'LOW', FALSE, 'Cafézinho da tarde', FALSE);

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--

-- Inserindo dados da rede social

INSERT INTO content (title, description, subtitle, subContent, authorId, repost) VALUES
('Receita funcional para o café da manhã', 'Comece o dia com uma panqueca de banana e aveia batida com um fio de mel e canela, assada em frigideira antiaderente. Rica em fibras solúveis, ajuda a regular o intestino e reduzir o esforço que pressiona a bexiga. Evite adoçar em excesso e prefira leite vegetal se houver sensibilidade. Combine com uma porção de iogurte natural para proteína.', 'Benefícios para o trato urinário', 'A fibra solúvel da aveia auxilia no trânsito intestinal, diminuindo constipação e a pressão abdominal que pode agravar episódios de escape.', 1, FALSE),
('Exercício respiratório para relaxar o assoalho pélvico', 'A prática da respiração diafragmática reduz a tensão involuntária dos músculos do assoalho pélvico e melhora o controle neuromuscular. Deite-se com as mãos sobre o abdome, inspire contando até quatro e sinta o diafragma descendo; expire lentamente contando até seis, permitindo que o períneo relaxe. Repita por 5 a 10 minutos, duas vezes ao dia, especialmente após atividades que geram esforço. Integre esses exercícios a uma rotina de alongamento suave.', 'Ponto de atenção', 'Se houver dor ou aumento dos sintomas, interrompa e consulte um fisioterapeuta especializado.', 1, FALSE),
('Minha jornada pós-parto com incontinência', 'Após o segundo parto experimentei escapes ao espirrar e ao correr; inicialmente ignorei por vergonha, até perceber que afetava minha rotina social. Procurei atendimento fisioterapêutico, aprendi a identificar e contrair corretamente o assoalho pélvico e passei a fortalecer o core com exercícios graduais. Em três meses notei redução dos episódios e mais confiança para voltar a atividades físicas. A troca de experiências com outras mães foi fundamental para manter a motivação.', NULL, NULL, 2, FALSE),
('Mito: só quem teve filhos sofre com incontinência', 'A incontinência urinária não é exclusiva de quem já teve filhos; ela pode surgir por diversos motivos em diferentes faixas etárias. Problemas neurológicos, obesidade, cirurgia pélvica, alterações hormonais e predisposição genética são fatores que aumentam o risco. Identificar o tipo de incontinência é essencial para direcionar o tratamento adequado e eficaz. Informação correta evita estigmas e promove busca precoce por ajuda.', 'Fatores de risco', 'Sedentarismo, excesso de peso, tabagismo e constipação crônica aumentam a probabilidade de desenvolver sintomas.', 1, FALSE),
('Desafio: 10 minutos de autocuidado por dia', 'Reserve 10 minutos diários para práticas simples: alongamento leve, respiração guiada, hidratação consciente e registro rápido de humor. Pequenos hábitos acumulam efeito na redução do estresse, melhoram o sono e ajudam no controle muscular do períneo ao longo do tempo. Documente os progressos na comunidade para manter a responsabilidade e inspirar outras pessoas. Experimente variar a atividade a cada dia para manter o engajamento.', NULL, NULL, 2, FALSE),
('Posso fazer pilates com incontinência?', 'Sim, pilates pode ser uma ferramenta eficaz quando adaptado às necessidades de quem tem incontinência; o foco é na ativação controlada do core e no equilíbrio da pressão intra-abdominal. Instrutores com formação em saúde da mulher devem priorizar exercícios com respiração coordenada e evitar movimentos que gerem esforço abrupto sem suporte muscular. Comece com sessões individuais ou em pequenos grupos até ganhar controle e segurança. Monitore sintomas e ajuste progressão conforme orientação profissional.', 'Dicas para praticar com segurança', 'Procure profissionais que realizem avaliação inicial do assoalho pélvico e ofereçam progressão personalizada.', 1, FALSE);

INSERT INTO comment (contentId, authorId, text) VALUES
(1, 2, 'Adorei a receita! Vou testar no café da manhã de amanhã.'),
(2, 1, 'Exercício simples e eficaz. Já sinto mais relaxamento.'),
(3, 2, 'Obrigada por compartilhar sua história. Me identifiquei muito!'),
(4, 1, 'Muito bom desmistificar esses mitos. Informação é tudo!'),
(5, 2, 'Ótimo desafio! Já estou incorporando na minha rotina diária.'),
(6, 1, 'Pilates tem sido ótimo para meu controle. Recomendo buscar bons instrutores.');

INSERT INTO contentLikes (contentId, userId) VALUES
(1, 2),
(2, 1),
(3, 2),
(4, 1),
(5, 2),
(6, 1),
(1, 1),
(2, 2);
