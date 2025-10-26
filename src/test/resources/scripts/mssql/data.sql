USE dailyiu_backend_storage_database;
GO

-- Inserindo as questões
INSERT INTO question (externalId, text, type, required, placeholder, minValue, maxValue, step) VALUES
('birthdate', 'Qual sua data de nascimento?', 
    'DATE', 1, 'Dia / Mês / Ano', NULL, NULL, NULL);

INSERT INTO question (externalId, text, type, required) VALUES
('gender', 'Com qual gênero você se identifica?',
    'RADIO', 1);

INSERT INTO question (externalId, text, type, required) VALUES
('q3_frequency', 'Com que frequência você perde urina?',
    'RADIO', 1);

INSERT INTO question (externalId, text, type, required) VALUES
('q4_amount', 'Gostaríamos de saber a quantidade de urina que você pensa que perde (Assinale a alternativa que melhor representa a sua percepção)',
    'RADIO', 1);

INSERT INTO question (externalId, text, type, required, minValue, maxValue, step) VALUES
('q5_interference', 'Em geral, quanto que perder urina interfere em sua vida diária? (0 = não interfere, 10 = interfere muito)',
    'SLIDER', 1, 0, 10, 1);

INSERT INTO question (externalId, text, type, required) VALUES
('q6_when', 'Em que momentos você perde urina? (Assinale todas as alternativas que se aplicam a você)',
    'CHECKBOX', 1);
GO

-- Inserindo as opções para a questão de gênero
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'gender'), 'Masculino', 'M'),
((SELECT id FROM question WHERE externalId = 'gender'), 'Feminino', 'F');
GO

-- Inserindo as opções para a questão de frequência
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Nunca', '0'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Uma vez por semana ou menos', '1'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Duas a três vezes por semana', '2'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Uma vez ao dia', '3'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'Diversas vezes ao dia', '4'),
((SELECT id FROM question WHERE externalId = 'q3_frequency'), 'O tempo todo', '5');
GO

-- Inserindo as opções para a questão de quantidade
INSERT INTO questionOption (questionId, label, textValue) VALUES
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Nenhuma', '0'),
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Uma pequena quantidade', '2'),
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Uma quantidade moderada', '4'),
((SELECT id FROM question WHERE externalId = 'q4_amount'), 'Uma quantidade elevada', '6');
GO

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
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Inserindo dados de teste para usuários e diário miccional
INSERT INTO credential (passwordHash, salt) VALUES
('1thzHtxdZlMY1Eyy90PCHNPUkbetaYCC8JjWIAgmxvo=', 'DghbE3gLRofkyVuZZ37oQA=='),
('z943fyO/J1bjRnoXt7Segm2h2c6e6tuS/bHMlSCPZJs=', '1HCmqwTC12DzqNpVKdQ0Uw==');
GO


INSERT INTO patientProfile (birthDate, gender, iciq3answer, iciq4answer, iciq5answer, iciqScore, urinationLoss) VALUES
('1980-05-15', 'M', 0, 0, 3, 3, '1,2,3'),
('1990-10-20', 'F', 2, 2, 6, 10, '2,3,4,5');
GO

INSERT INTO preferences (highContrast, bigFont, reminderCalendar, reminderCalendarSchedule, reminderWorkout, reminderWorkoutSchedule, encouragingMessages) VALUES
(0, 0, 1, '09:00', 1, '18:00', 1),
(1, 1, 0, NULL, 0, NULL, 1);

INSERT INTO role(description, permissionLevel, reason, hasDocument) VALUES
('Usuário comum', 1, 'Acesso padrão ao aplicativo.', 0),
('Usuário comum', 1, 'Acesso padrão ao aplicativo.', 0),
('Administrador', 3, 'Acesso total ao sistema para gerenciamento e manutenção.', 0);

-- Inserindo usuários
INSERT INTO appUser (name, email, credentialId, patientProfileId, preferencesId, roleId) VALUES
('Usuário 1', 'usuario1@example.com', 1, 1, 1, 1),
('Usuária 2', 'usuario2@example.com', 2, 2, 2, 2);
GO

-- Inserindo dados do calendário
INSERT INTO calendarDay (dateValue, userId, leakageLevel, eventsCount, completedExercises, notesPreview, dayTitle) VALUES
('2025-06-01', 1, 'LOW', 1, 1, 'Dia tranquilo.', 'seg'),
('2025-06-05', 1, 'LOW', 1, 2, 'Pouca urgência.', 'qui'),
('2025-06-10', 1, 'MEDIUM', 1, 0, NULL, 'ter'),
('2025-06-15', 1, 'HIGH', 1, 3, 'Vazamento noturno.', 'dom'),
('2025-07-02', 1, 'NONE', 0, 2, 'Sem eventos.', 'qua'),
('2025-07-08', 1, 'LOW', 1, 1, NULL, 'ter'),
('2025-07-14', 1, 'MEDIUM', 1, 2, 'Dia agitado.', 'seg'),
('2025-08-03', 1, 'HIGH', 1, 0, 'Vazamento após café.', 'dom'),
('2025-08-10', 1, 'NONE', 1, 1, NULL, 'dom'),
('2025-08-20', 1, 'LOW', 1, 2, 'Pouca urgência.', 'qua'),
('2025-06-03', 2, 'MEDIUM', 1, 1, 'Dia normal.', 'ter'),
('2025-07-05', 2, 'HIGH', 1, 2, 'Vazamento noturno.', 'sab'),
('2025-07-18', 2, 'NONE', 0, 1, NULL, 'sex'),
('2025-08-07', 2, 'LOW', 1, 2, 'Pouca urgência.', 'qui'),
('2025-08-22', 2, 'MEDIUM', 1, 0, NULL, 'sex'),
('2025-09-02', 1, 'LOW', 1, 1, 'Dia tranquilo.', 'ter');
GO

-- Inserindo dados de urinação
INSERT INTO urinationData (calendarDayId, timeValue, amount, leakage, reason, urgency) VALUES
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-01' AND userId = 1), '12:30', 'LOW', 0, 'Almoço', 0),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-05' AND userId = 1), '09:00', 'LOW', 0, NULL, 0),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-10' AND userId = 1), '07:45', 'MEDIUM', 1, 'Acordou com urgência', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-15' AND userId = 1), '23:30', 'HIGH', 1, 'Sono profundo', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-07-08' AND userId = 1), '15:20', 'LOW', 0, 'Após exercício', 0),
((SELECT id FROM calendarDay WHERE dateValue = '2025-07-14' AND userId = 1), '18:00', 'MEDIUM', 1, 'Trânsito', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-03' AND userId = 1), '08:45', 'HIGH', 1, 'Café da manhã', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-10' AND userId = 1), '11:00', 'NONE', 0, NULL, 0),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-20' AND userId = 1), '14:30', 'LOW', 0, 'Almoço', 0),
((SELECT id FROM calendarDay WHERE dateValue = '2025-06-03' AND userId = 2), '09:30', 'MEDIUM', 1, 'Trabalho', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-07-05' AND userId = 2), '22:00', 'HIGH', 1, 'Sono profundo', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-07' AND userId = 2), '13:00', 'LOW', 0, 'Almoço', 0),
((SELECT id FROM calendarDay WHERE dateValue = '2025-08-22' AND userId = 2), '16:45', 'MEDIUM', 1, 'Trânsito', 1),
((SELECT id FROM calendarDay WHERE dateValue = '2025-09-02' AND userId = 1), '18:42', 'LOW', 0, 'Cafézinho da tarde', 0);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--

-- Inserindo dados da rede social

INSERT INTO contentCategory (name, description, auditable) VALUES
('Alimentação e Nutrição', 'Dicas de alimentos que ajudam na saúde do trato urinário, receitas funcionais, e orientações nutricionais.', 0),
('Hábitos Saudáveis', 'Rotinas que favorecem o bem-estar, como exercícios físicos, sono, hidratação e autocuidado.', 0),
('Dicas de Fisioterapia Pélvica', 'Conteúdos educativos sobre exercícios, técnicas e orientações fisioterapêuticas.', 1),
('Depoimentos e Histórias Reais', 'Espaço para relatos de superação, experiências com tratamentos e apoio emocional.', 0),
('Mitos e Verdades', 'Desmistificação de crenças populares sobre incontinência urinária e saúde íntima.', 1),
('Profissionais Respondem', 'Sessão para perguntas e respostas com especialistas (fisioterapeutas, nutricionistas, médicos).', 1);
GO

INSERT INTO content (title, description, subtitle, subContent, authorId, repost) VALUES
('Receita funcional para o café da manhã', 'Comece o dia com uma panqueca de banana e aveia batida com um fio de mel e canela, assada em frigideira antiaderente. Rica em fibras solúveis, ajuda a regular o intestino e reduzir o esforço que pressiona a bexiga. Evite adoçar em excesso e prefira leite vegetal se houver sensibilidade. Combine com uma porção de iogurte natural para proteína.', 'Benefícios para o trato urinário', 'A fibra solúvel da aveia auxilia no trânsito intestinal, diminuindo constipação e a pressão abdominal que pode agravar episódios de escape.', 1, 0),
('Exercício respiratório para relaxar o assoalho pélvico', 'A prática da respiração diafragmática reduz a tensão involuntária dos músculos do assoalho pélvico e melhora o controle neuromuscular. Deite-se com as mãos sobre o abdome, inspire contando até quatro e sinta o diafragma descendo; expire lentamente contando até seis, permitindo que o períneo relaxe. Repita por 5 a 10 minutos, duas vezes ao dia, especialmente após atividades que geram esforço. Integre esses exercícios a uma rotina de alongamento suave.', 'Ponto de atenção', 'Se houver dor ou aumento dos sintomas, interrompa e consulte um fisioterapeuta especializado.', 1, 0),
('Minha jornada pós-parto com incontinência', 'Após o segundo parto experimentei escapes ao espirrar e ao correr; inicialmente ignorei por vergonha, até perceber que afetava minha rotina social. Procurei atendimento fisioterapêutico, aprendi a identificar e contrair corretamente o assoalho pélvico e passei a fortalecer o core com exercícios graduais. Em três meses notei redução dos episódios e mais confiança para voltar a atividades físicas. A troca de experiências com outras mães foi fundamental para manter a motivação.', null, null, 2, 0),
('Mito: só quem teve filhos sofre com incontinência', 'A incontinência urinária não é exclusiva de quem já teve filhos; ela pode surgir por diversos motivos em diferentes faixas etárias. Problemas neurológicos, obesidade, cirurgia pélvica, alterações hormonais e predisposição genética são fatores que aumentam o risco. Identificar o tipo de incontinência é essencial para direcionar o tratamento adequado e eficaz. Informação correta evita estigmas e promove busca precoce por ajuda.', 'Fatores de risco', 'Sedentarismo, excesso de peso, tabagismo e constipação crônica aumentam a probabilidade de desenvolver sintomas.', 1, 0),
('Desafio: 10 minutos de autocuidado por dia', 'Reserve 10 minutos diários para práticas simples: alongamento leve, respiração guiada, hidratação consciente e registro rápido de humor. Pequenos hábitos acumulam efeito na redução do estresse, melhoram o sono e ajudam no controle muscular do períneo ao longo do tempo. Documente os progressos na comunidade para manter a responsabilidade e inspirar outras pessoas. Experimente variar a atividade a cada dia para manter o engajamento.', null, null, 2, 0),
('Posso fazer pilates com incontinência?', 'Sim, pilates pode ser uma ferramenta eficaz quando adaptado às necessidades de quem tem incontinência; o foco é na ativação controlada do core e no equilíbrio da pressão intra-abdominal. Instrutores com formação em saúde da mulher devem priorizar exercícios com respiração coordenada e evitar movimentos que gerem esforço abrupto sem suporte muscular. Comece com sessões individuais ou em pequenos grupos até ganhar controle e segurança. Monitore sintomas e ajuste progressão conforme orientação profissional.', 'Dicas para praticar com segurança', 'Procure profissionais que realizem avaliação inicial do assoalho pélvico e ofereçam progressão personalizada.', 1, 0);
GO

INSERT INTO contentContentCategory(contentId, categoryId) VALUES
(1, 1),
(2, 3),
(3, 4),
(4, 5),
(5, 2),
(6, 6);
GO

INSERT INTO media (url, contentType, contentSize, altText) VALUES
('http://127.0.0.1:10000/devstoreaccount1/media/91b2247e-070f-4668-8d1f-11063629811a.jpg', 'image/jpeg', '107015', 'Porção de panquecas de banana e aveia com mel e canela em um prato branco sobre uma mesa de madeira clara.'),
('http://127.0.0.1:10000/devstoreaccount1/media/ffd6af37-eab6-49d2-8276-36f13252617f.png', 'image/png', '420209', 'Ilustração de uma mulher sentada em uma cadeira, com as mãos sobre o abdome, demonstrando a respiração diafragmática.'),
('http://127.0.0.1:10000/devstoreaccount1/media/3467b840-3aa0-4fe9-b8fb-2dcc7bbea020.png', 'image/png', '1405504', 'Foto de uma mulher sentada em uma poltrona, com seu bebê no colo e expressão de cansaço e mão sobre a testa.'),
('http://127.0.0.1:10000/devstoreaccount1/media/80e5d0be-4516-4a65-b5da-967fdd8823c9.jpg', 'image/jpeg', '158010', 'Foto de uma mulher serena, com os olhos fechados e expressão tranquila, em um ambiente claro com seu bebê no colo, enrolado em um cobertor.'),
('http://127.0.0.1:10000/devstoreaccount1/media/573aa04e-716d-4c74-b009-3b83f10e3834.jpg', 'image/jpeg', '63847', 'Ilustração de uma mulher no campo em posição de meditação, acima de sua cabeça flutuam ícones representando autocuidado, como um coração, livros e pesos de academia.'),
('http://127.0.0.1:10000/devstoreaccount1/media/79964e42-cb11-4b4a-9c49-2a9687bcef9b.png', 'image/png', '938243', 'Foto de uma mulher praticando pilates em um estúdio, sentada em cima do joelho, alongando a parte lateral do corpo com os dois braços estendidos para cima.');
GO

INSERT INTO contentMedia (contentId, mediaId) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6);
GO

INSERT INTO comment (contentId, authorId, text) VALUES
(1, 2, 'Adorei a receita! Vou testar no café da manhã de amanhã.'),
(2, 1, 'Exercício simples e eficaz. Já sinto mais relaxamento.'),
(3, 2, 'Obrigada por compartilhar sua história. Me identifiquei muito!'),
(4, 1, 'Muito bom desmistificar esses mitos. Informação é tudo!'),
(5, 2, 'Ótimo desafio! Já estou incorporando na minha rotina diária.'),
(6, 1, 'Pilates tem sido ótimo para meu controle. Recomendo buscar bons instrutores.');
GO

INSERT INTO contentLikes (contentId, userId) VALUES
(1, 2),
(2, 1),
(3, 2),
(4, 1),
(5, 2),
(6, 1),
(1, 1),
(2, 2);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--
-- Inserindo dados de exercícios físicos

INSERT INTO exerciseCategory (name, description) VALUES
('Kegel', 'Exercícios focados na contração e relaxamento do assoalho pélvico, fundamentais para o fortalecimento muscular e controle urinário. Podem ser feitos em qualquer posição e adaptados para diferentes níveis de habilidade.'),
('Alongamento', 'Movimentos suaves que promovem flexibilidade, aliviam tensões na região lombar e pélvica, e melhoram a postura. Podem ser usados como preparação ou complemento aos exercícios de força.'),
('Mobilidade Pélvica', 'Sequências que estimulam o movimento consciente da pelve, melhorando a coordenação, a circulação local e a percepção corporal. Indicados para quem sente rigidez ou desconforto na região.'),
('Respiração e Relaxamento', 'Técnicas respiratórias que ativam o diafragma e promovem relaxamento do assoalho pélvico. Essenciais para reduzir tensão muscular, melhorar o controle abdominal e favorecer a recuperação pós-parto.');
GO

INSERT INTO exerciseAttribute (name, type, description) VALUES
('Fortalecimento do Assoalho Pélvico', 'BENEFIT', 'Melhora a força e resistência dos músculos do assoalho pélvico, reduzindo episódios de incontinência.'),
('Aumento da Flexibilidade', 'BENEFIT', 'Promove maior amplitude de movimento na região pélvica e lombar, aliviando tensões musculares.'),
('Melhora da Postura', 'BENEFIT', 'Contribui para o alinhamento corporal adequado, reduzindo sobrecarga na região pélvica.'),
('Redução do Estresse', 'BENEFIT', 'Técnicas de respiração e relaxamento que ajudam a diminuir a tensão muscular e o estresse geral.');
GO

INSERT INTO exercise (title, categoryId, instructions, repetitions, sets, restTime, duration) VALUES
('Exercícios de Kegel Básicos', 1, 'Deitada de costas ou sentada, contraia o períneo por 5 segundos e relaxe.', 10, 3, 10, 80),
('Exercícios de Ponte', 1, 'Deitada de costas, contraia o bumbum, elevando-o da cama ou do chão enquanto inspira (puxa o ar)', 10, 3, 15, 90),
('Alongamento do Gato-Vaca', 2, 'Em posição de quatro apoios, alterne entre arquear e arredondar as costas.', 10, 2, 10, 60),
('Mobilização Pélvica em Círculos', 3, 'Sentada, faça movimentos circulares com a pelve, alternando sentido horário e anti-horário.', 10, 2, 10, 60),
('Respiração Diafragmática', 4, 'Deitada ou sentada, inspire profundamente pelo nariz, expandindo o abdome, e expire lentamente pela boca.', 5, 3, 15, 75);
GO

INSERT INTO exerciseExerciseAttribute (exerciseId, attributeId) VALUES
((SELECT id FROM exercise WHERE title = 'Exercícios de Kegel Básicos'), (SELECT id FROM exerciseAttribute WHERE name = 'Fortalecimento do Assoalho Pélvico')),
((SELECT id FROM exercise WHERE title = 'Exercícios de Ponte'), (SELECT id FROM exerciseAttribute WHERE name = 'Fortalecimento do Assoalho Pélvico')),
((SELECT id FROM exercise WHERE title = 'Alongamento do Gato-Vaca'), (SELECT id FROM exerciseAttribute WHERE name = 'Aumento da Flexibilidade')),
((SELECT id FROM exercise WHERE title = 'Mobilização Pélvica em Círculos'), (SELECT id FROM exerciseAttribute WHERE name = 'Aumento da Flexibilidade')),
((SELECT id FROM exercise WHERE title = 'Respiração Diafragmática'), (SELECT id FROM exerciseAttribute WHERE name = 'Redução do Estresse'));
GO

INSERT INTO workout (name, description, totalDuration, difficultyLevel) VALUES
('Rotina Matinal para Fortalecimento', 'Sequência de exercícios para iniciar o dia com foco no fortalecimento do assoalho pélvico e mobilidade pélvica.', 300, 'BEGINNER'),
('Alongamento e Relaxamento Noturno', 'Série de alongamentos e técnicas de respiração para relaxar antes de dormir, aliviando tensões acumuladas.', 240, 'BEGINNER');
GO

INSERT INTO workoutExercise (workoutId, exerciseId, exerciseOrder) VALUES
((SELECT id FROM workout WHERE name = 'Rotina Matinal para Fortalecimento'), (SELECT id FROM exercise WHERE title = 'Exercícios de Kegel Básicos'), 1),
((SELECT id FROM workout WHERE name = 'Rotina Matinal para Fortalecimento'), (SELECT id FROM exercise WHERE title = 'Mobilização Pélvica em Círculos'), 2),
((SELECT id FROM workout WHERE name = 'Alongamento e Relaxamento Noturno'), (SELECT id FROM exercise WHERE title = 'Alongamento do Gato-Vaca'), 1),
((SELECT id FROM workout WHERE name = 'Alongamento e Relaxamento Noturno'), (SELECT id FROM exercise WHERE title = 'Respiração Diafragmática'), 2);
GO

INSERT INTO workoutPlan (name, description, daysPerWeek, totalWeeks, iciqScoreMin, iciqScoreMax, ageMin, ageMax) VALUES
('Plano de 4 Semanas para Iniciantes', 'Programa gradual para fortalecer o assoalho pélvico e melhorar o controle urinário ao longo de um mês.', 3, 4, 2, 7, 18, 65),
('Plano de Manutenção Semanal', 'Rotina semanal para manter os ganhos de força e flexibilidade do assoalho pélvico.', 2, 8, 6, 15, 18, 65);
GO

INSERT INTO workoutPlanWorkout (workoutPlanId, workoutId, workoutOrder) VALUES
((SELECT id FROM workoutPlan WHERE name = 'Plano de 4 Semanas para Iniciantes'), (SELECT id FROM workout WHERE name = 'Rotina Matinal para Fortalecimento'), 1),
((SELECT id FROM workoutPlan WHERE name = 'Plano de 4 Semanas para Iniciantes'), (SELECT id FROM workout WHERE name = 'Alongamento e Relaxamento Noturno'), 2),
((SELECT id FROM workoutPlan WHERE name = 'Plano de Manutenção Semanal'), (SELECT id FROM workout WHERE name = 'Rotina Matinal para Fortalecimento'), 1);
GO

INSERT INTO userWorkoutPlan (userId, workoutPlanId, startDate, endDate, totalProgress, weekProgress, currentWeek, nextWorkout, completed) VALUES
(1, (SELECT id FROM workoutPlan WHERE name = 'Plano de 4 Semanas para Iniciantes'), '2025-06-01', '2025-06-28', 12, 3, 4, null, 1),
(2, (SELECT id FROM workoutPlan WHERE name = 'Plano de Manutenção Semanal'), '2025-07-01', NULL, 0, 0, 1, 1, 0);
GO