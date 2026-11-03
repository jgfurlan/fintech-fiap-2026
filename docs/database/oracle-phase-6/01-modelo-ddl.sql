-- =====================================================================
-- FINTECH - MODELO FISICO (Oracle) - baseado na modelagem da Fase 3
-- Entidades: T_USUARIO, T_CONTA, T_CATEGORIA, T_TRANSACAO,
--            T_INVESTIMENTO, T_META
-- T_TRANSACAO concentra RECEITA e DESPESA, diferenciadas pela coluna
-- tp_transacao ('R' = receita / entrada, 'D' = despesa / saida).
-- =====================================================================

-- ---------------------------------------------------------------------
-- SEQUENCES
-- ---------------------------------------------------------------------
CREATE SEQUENCE sq_usuario      START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE sq_conta        START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE sq_categoria    START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE sq_transacao    START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE sq_investimento START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE sq_meta         START WITH 1 INCREMENT BY 1 NOCACHE;

-- ---------------------------------------------------------------------
-- T_USUARIO
-- ---------------------------------------------------------------------
CREATE TABLE t_usuario (
    id_usuario      NUMBER(10)      NOT NULL,
    nm_usuario      VARCHAR2(100)   NOT NULL,
    ds_email        VARCHAR2(100)   NOT NULL,
    ds_senha        VARCHAR2(50)    NOT NULL,
    dt_cadastro     DATE            DEFAULT SYSDATE NOT NULL,
    CONSTRAINT pk_usuario      PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuario_email UNIQUE (ds_email)
);

-- ---------------------------------------------------------------------
-- T_CONTA
-- ---------------------------------------------------------------------
CREATE TABLE t_conta (
    id_conta        NUMBER(10)      NOT NULL,
    id_usuario      NUMBER(10)      NOT NULL,
    tp_conta        VARCHAR2(50)    NOT NULL,
    vl_saldo_atual  NUMBER(15,2)    DEFAULT 0 NOT NULL,
    CONSTRAINT pk_conta         PRIMARY KEY (id_conta),
    CONSTRAINT fk_conta_usuario FOREIGN KEY (id_usuario)
        REFERENCES t_usuario (id_usuario)
);

-- ---------------------------------------------------------------------
-- T_CATEGORIA
-- ---------------------------------------------------------------------
CREATE TABLE t_categoria (
    id_categoria    NUMBER(10)      NOT NULL,
    nm_categoria    VARCHAR2(50)    NOT NULL,
    tp_categoria    CHAR(1)         NOT NULL,
    CONSTRAINT pk_categoria      PRIMARY KEY (id_categoria),
    CONSTRAINT ck_categoria_tipo CHECK (tp_categoria IN ('R','D'))
);

-- ---------------------------------------------------------------------
-- T_TRANSACAO (receita quando tp_transacao = 'R', despesa quando 'D')
-- ---------------------------------------------------------------------
CREATE TABLE t_transacao (
    id_transacao    NUMBER(10)      NOT NULL,
    id_conta        NUMBER(10)      NOT NULL,
    id_categoria    NUMBER(10)      NOT NULL,
    vl_transacao    NUMBER(15,2)    NOT NULL,
    dt_transacao    DATE            NOT NULL,
    ds_transacao    VARCHAR2(255),
    tp_transacao    CHAR(1)         NOT NULL,
    CONSTRAINT pk_transacao         PRIMARY KEY (id_transacao),
    CONSTRAINT fk_transacao_conta   FOREIGN KEY (id_conta)     REFERENCES t_conta (id_conta),
    CONSTRAINT fk_transacao_categ   FOREIGN KEY (id_categoria) REFERENCES t_categoria (id_categoria),
    CONSTRAINT ck_transacao_tipo    CHECK (tp_transacao IN ('R','D')),
    CONSTRAINT ck_transacao_valor   CHECK (vl_transacao > 0)
);

-- ---------------------------------------------------------------------
-- T_INVESTIMENTO
-- ---------------------------------------------------------------------
CREATE TABLE t_investimento (
    id_investimento NUMBER(10)      NOT NULL,
    id_conta        NUMBER(10)      NOT NULL,
    tp_investimento VARCHAR2(50)    NOT NULL,
    vl_aplicado     NUMBER(15,2)    NOT NULL,
    dt_aplicacao    DATE            NOT NULL,
    tx_rendimento   NUMBER(5,2),
    CONSTRAINT pk_investimento       PRIMARY KEY (id_investimento),
    CONSTRAINT fk_investimento_conta FOREIGN KEY (id_conta) REFERENCES t_conta (id_conta),
    CONSTRAINT ck_investimento_valor CHECK (vl_aplicado > 0)
);

-- ---------------------------------------------------------------------
-- T_META (parte do modelo da Fase 3; sem comandos exigidos nesta entrega)
-- ---------------------------------------------------------------------
CREATE TABLE t_meta (
    id_meta         NUMBER(10)      NOT NULL,
    id_usuario      NUMBER(10)      NOT NULL,
    ds_meta         VARCHAR2(255)   NOT NULL,
    vl_objetivo     NUMBER(15,2)    NOT NULL,
    dt_limite       DATE,
    vl_atual        NUMBER(15,2)    DEFAULT 0,
    CONSTRAINT pk_meta         PRIMARY KEY (id_meta),
    CONSTRAINT fk_meta_usuario FOREIGN KEY (id_usuario) REFERENCES t_usuario (id_usuario)
);
