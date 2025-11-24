CREATE DATABASE dailyiu_backend_storage_database;
GO

USE dailyiu_backend_storage_database;
GO

-- Tabela para armazenar mídias (imagens, vídeos, etc.)
CREATE TABLE media (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    url NVARCHAR(255) NOT NULL,
    contentType NVARCHAR(10) NOT NULL,
    contentSize BIGINT NOT NULL,
    altText NVARCHAR(255) DEFAULT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--

CREATE TABLE credential (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    passwordHash NVARCHAR(255) NOT NULL,
    salt NVARCHAR(255) NOT NULL
);
GO

CREATE TABLE patientProfile (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    birthDate DATE NOT NULL,
    gender NVARCHAR(10) NOT NULL,
    iciq3answer INT NOT NULL,
    iciq4answer INT NOT NULL,
    iciq5answer INT NOT NULL,
    iciqScore INT NOT NULL,
    urinationLoss NVARCHAR(26) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE preferences (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    highContrast BIT NOT NULL DEFAULT 0,
    bigFont BIT NOT NULL DEFAULT 0,
    darkMode BIT NOT NULL DEFAULT 0,
    reminderCalendar BIT NOT NULL DEFAULT 0,
    reminderCalendarSchedule TIME,
    reminderWorkout BIT NOT NULL DEFAULT 0,
    reminderWorkoutSchedule TIME,
    encouragingMessages BIT NOT NULL DEFAULT 1,
    notificationToken NVARCHAR(255) NOT NULL DEFAULT ''
);
GO

CREATE TABLE role (
    id INT IDENTITY(1,1) PRIMARY KEY,
    description NVARCHAR(50) NOT NULL,
    permissionLevel INT NOT NULL,
    reason NVARCHAR(255) NOT NULL,
    hasDocument BIT NOT NULL DEFAULT 0,
    documentType NVARCHAR(50),
    documentValue NVARCHAR(255),
    conceivedBy BIGINT DEFAULT NULL,
    conceivedAt DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- Tabela principal de usuários
CREATE TABLE appUser (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    profilePictureId BIGINT DEFAULT NULL,
    credentialId BIGINT NOT NULL,
    patientProfileId BIGINT DEFAULT NULL,
    preferencesId BIGINT NOT NULL,
    roleId INT NOT NULL,
    strikes INT NOT NULL DEFAULT 0,
    blocked BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (profilePictureId) REFERENCES media(id),
    FOREIGN KEY (credentialId) REFERENCES credential(id),
    FOREIGN KEY (patientProfileId) REFERENCES patientProfile(id),
    FOREIGN KEY (preferencesId) REFERENCES preferences(id),
    FOREIGN KEY (roleId) REFERENCES role(id)
);
GO

ALTER TABLE role
ADD CONSTRAINT FK_ConceivedBy FOREIGN KEY (conceivedBy) REFERENCES appUser(id);
GO

-- Tabela para armazenar códigos OTP para recuperação de senha

CREATE TABLE OTP (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    userId BIGINT NOT NULL,
    code NVARCHAR(255) NOT NULL,
    expirationTime BIGINT NOT NULL,
    used BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Tabela principal de questões com campos adicionais
CREATE TABLE question (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    externalId NVARCHAR(50),
    text NVARCHAR(MAX) NOT NULL,
    type NVARCHAR(10) NOT NULL,
    required BIT NOT NULL DEFAULT 0,
    placeholder NVARCHAR(255),
    minValue INT,
    maxValue INT,
    step INT
);
GO

-- Tabela para armazenar as opções das questões (para RADIO e CHECKBOX)
CREATE TABLE questionOption (
    id INT IDENTITY(1,1) PRIMARY KEY,
    questionId BIGINT,
    label NVARCHAR(255) NOT NULL,
    textValue NVARCHAR(50) NOT NULL,
    FOREIGN KEY (questionId) REFERENCES question(id)
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Tabela para armazenar registros para o diário miccional
CREATE TABLE calendarDay (
    id INT IDENTITY(1,1) PRIMARY KEY,
    dateValue DATE NOT NULL,
    userId BIGINT NOT NULL,
    leakageLevel NVARCHAR(20) NOT NULL,
    eventsCount INT NOT NULL DEFAULT 0,
    completedExercises INT NOT NULL DEFAULT 0,
    notesPreview NVARCHAR(MAX),
    dayTitle NVARCHAR(10) NOT NULL,
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE urinationData (
    id INT IDENTITY(1,1) PRIMARY KEY,
    calendarDayId INT NOT NULL,
    timeValue TIME NOT NULL,
    amount NVARCHAR(20) NOT NULL,
    leakage BIT NOT NULL DEFAULT 0,
    reason NVARCHAR(MAX),
    urgency BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (calendarDayId) REFERENCES calendarDay(id)
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Tabela de exercícios e planos de treino

CREATE TABLE exerciseAttribute (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    type NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) DEFAULT NULL
);
GO

CREATE TABLE exerciseCategory (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(255) DEFAULT NULL
);
GO

CREATE TABLE exercise (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    categoryId INT NOT NULL,
    instructions NVARCHAR(MAX) NOT NULL,
    repetitions INT NOT NULL,
    sets INT NOT NULL,
    restTime INT NOT NULL,
    duration INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (categoryId) REFERENCES exerciseCategory(id)
);
GO

CREATE TABLE exerciseExerciseAttribute (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    attributeId INT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (attributeId) REFERENCES exerciseAttribute(id)
);
GO

CREATE TABLE exerciseMedia (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);
GO

CREATE TABLE workout (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
    totalDuration INT NOT NULL,
    difficultyLevel NVARCHAR(50) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE workoutExercise (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    workoutId BIGINT NOT NULL,
    exerciseId BIGINT NOT NULL,
    exerciseOrder INT NOT NULL,
    FOREIGN KEY (workoutId) REFERENCES workout(id),
    FOREIGN KEY (exerciseId) REFERENCES exercise(id)
);
GO

CREATE TABLE workoutPlan (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) DEFAULT NULL,
    daysPerWeek INT NOT NULL,
    totalWeeks INT NOT NULL,
    iciqScoreMin INT NOT NULL,
    iciqScoreMax INT NOT NULL,
    ageMin INT NOT NULL,
    ageMax INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE workoutPlanWorkout (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    workoutPlanId BIGINT NOT NULL,
    workoutId BIGINT NOT NULL,
    workoutOrder INT NOT NULL,
    FOREIGN KEY (workoutPlanId) REFERENCES workoutPlan(id),
    FOREIGN KEY (workoutId) REFERENCES workout(id)
);
GO

CREATE TABLE userWorkoutPlan (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    userId BIGINT NOT NULL,
    workoutPlanId BIGINT NOT NULL,
    startDate DATETIME2 NOT NULL DEFAULT GETDATE(),
    endDate DATETIME2 DEFAULT NULL,
    totalProgress INT DEFAULT NULL,
    weekProgress INT DEFAULT NULL,
    currentWeek INT NOT NULL DEFAULT 1,
    nextWorkout INT DEFAULT NULL,
    lastWorkoutDate DATETIME2 DEFAULT NULL,
    completed BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (userId) REFERENCES appUser(id),
    FOREIGN KEY (workoutPlanId) REFERENCES workoutPlan(id)
);
GO

CREATE TABLE exerciseFeedback (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    workoutId BIGINT NOT NULL,
    exerciseId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    evaluation NVARCHAR(20) NOT NULL,
    rating INT NOT NULL,
    comments NVARCHAR(MAX),
    completedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (workoutId) REFERENCES workout(id),
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

-- Tabela de conteúdos com funcionalidades de rede social

CREATE TABLE contentCategory (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(255),
    auditable BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE content (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
    subtitle NVARCHAR(255),
    subContent NVARCHAR(MAX),
    authorId BIGINT,
    repost BIT NOT NULL DEFAULT 0,
    repostFromcontentId BIGINT DEFAULT NULL,
    repostByAuthorId BIGINT DEFAULT NULL,
    visible BIT NOT NULL DEFAULT 1,
    striked BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (repostByAuthorId) REFERENCES appUser(id)
);
GO

CREATE TABLE contentContentCategory (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    categoryId INT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (categoryId) REFERENCES contentCategory(id)
);
GO

CREATE TABLE comment (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    authorId BIGINT NOT NULL,
    text NVARCHAR(MAX) NOT NULL,
    reply BIT NOT NULL DEFAULT 0,
    replyToCommentId BIGINT DEFAULT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (replyToCommentId) REFERENCES comment(id)
);
GO

CREATE TABLE contentLikes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE commentLikes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    commentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (commentId) REFERENCES comment(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE contentMedia (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);
GO

CREATE TABLE savedContent (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    savedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE report (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    reportedByUserId BIGINT NOT NULL,
    reason NVARCHAR(MAX) NOT NULL,
    valid BIT NOT NULL DEFAULT 1,
    handled BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (reportedByUserId) REFERENCES appUser(id)
);
GO

-- Stored procedure para recomendar conteúdos com base nas categorias curtidas pelo usuário,
-- excluindo conteúdos já curtidos, dando bônus a autores cujas publicações o usuário curtiu
-- e priorizando publicações mais recentes. Suporta paginação (pageNumber e pageSize)
CREATE PROCEDURE sp_GetRecommendedContents
    @UserId BIGINT,
    @PageNumber INT,
    @PageSize INT,
    @TotalCount BIGINT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    IF @PageNumber < 1 SET @PageNumber = 1;
    IF @PageSize <= 0 SET @PageSize = 10;

    DECLARE @Offset INT = (@PageNumber - 1) * @PageSize;

    -- Usamos uma tabela temporária porque em T-SQL um CTE só pode ser seguido por uma única instrução.
    -- Primeiro populamos #filtered_contents com os scores, depois obtemos o total e aplicamos paginação.

    WITH liked_authors AS (
        SELECT DISTINCT c.authorId
        FROM content c
        JOIN contentLikes cl ON c.id = cl.contentId
        WHERE cl.userId = @UserId
    ),
    score_by_content AS (
        SELECT cc.contentId,
               COUNT(DISTINCT cc.categoryId) AS common_count
        FROM contentContentCategory cc
        JOIN contentContentCategory liked_cc ON liked_cc.categoryId = cc.categoryId
        JOIN contentLikes cl ON cl.contentId = liked_cc.contentId AND cl.userId = @UserId
        GROUP BY cc.contentId
    ),
    filtered_contents AS (
        SELECT c.id AS contentId,
               sbc.common_count,
               (sbc.common_count + CASE WHEN la.authorId IS NOT NULL THEN 1 ELSE 0 END) AS score
        FROM content c
        JOIN score_by_content sbc ON sbc.contentId = c.id
        LEFT JOIN liked_authors la ON la.authorId = c.authorId
        WHERE c.visible = 1 AND c.striked = 0
    )
    -- Insere o resultado em uma tabela temporária para poder reutilizar em múltiplas instruções
    SELECT contentId, common_count, score
    INTO #filtered_contents
    FROM filtered_contents;

    -- Total de resultados (antes da paginação)
    SELECT @TotalCount = COUNT(*) FROM #filtered_contents;

    IF @TotalCount > 0
    BEGIN
        -- Resultado paginado: selecionar colunas da tabela content e aplicar ordenação por score e data
        SELECT c.id, c.title, c.description, c.subtitle, c.subContent, c.authorId, c.repost,
               c.repostFromcontentId, c.repostByAuthorId, c.visible, c.createdAt, c.updatedAt
        FROM #filtered_contents fc
        JOIN content c ON c.id = fc.contentId
        ORDER BY fc.score DESC, c.createdAt DESC
        OFFSET @Offset ROWS FETCH NEXT @PageSize ROWS ONLY;
    END
    ELSE
    BEGIN
        -- Fallback: se não há conteúdos com categorias em comum (usuário sem likes),
        -- retorna conteúdos visíveis recentes que o usuário ainda não curtiu.
        SELECT @TotalCount = COUNT(*)
        FROM content c
        WHERE c.visible = 1 AND c.striked = 0
          AND NOT EXISTS (SELECT 1 FROM contentLikes cl2 WHERE cl2.contentId = c.id AND cl2.userId = @UserId);

        SELECT c.id, c.title, c.description, c.subtitle, c.subContent, c.authorId, c.repost,
               c.repostFromcontentId, c.repostByAuthorId, c.visible, c.createdAt, c.updatedAt
        FROM content c
        WHERE c.visible = 1 AND c.striked = 0
          AND NOT EXISTS (SELECT 1 FROM contentLikes cl2 WHERE cl2.contentId = c.id AND cl2.userId = @UserId)
        ORDER BY c.createdAt DESC
        OFFSET @Offset ROWS FETCH NEXT @PageSize ROWS ONLY;
    END

    DROP TABLE #filtered_contents;
END
GO
