-- =====================================================================
-- FINTECH - FASE 6 - COMANDOS SQL (Oracle)
-- Baseado no modelo fisico da Fase 3: T_USUARIO, T_CONTA, T_CATEGORIA,
-- T_TRANSACAO (receita/despesa unificadas via tp_transacao), T_INVESTIMENTO.
--
-- Mascaras de substituicao:
--   TEXTO   -> '[NOME DO CAMPO]'
--   NUMERO  -> [VALOR DO CAMPO]
--
-- Observacao: T_TRANSACAO e T_INVESTIMENTO nao possuem coluna id_usuario
-- direta (o vinculo e via id_conta -> T_CONTA -> id_usuario). Por isso,
-- toda alteracao/consulta "por usuario + codigo do registro" confere
-- tambem que a conta do registro pertence ao usuario informado.
-- =====================================================================


-- =====================================================================
-- 1. CADASTRO
-- =====================================================================

-- 1.1 Cadastrar os dados de um novo usuario.
INSERT INTO t_usuario (id_usuario, nm_usuario, ds_email, ds_senha, dt_cadastro)
VALUES (sq_usuario.NEXTVAL, '[NOME DO USUARIO]', '[EMAIL DO USUARIO]', '[SENHA DO USUARIO]', SYSDATE);

-- 1.2 Cadastrar os dados da conta bancaria de um usuario.
INSERT INTO t_conta (id_conta, id_usuario, tp_conta, vl_saldo_atual)
VALUES (sq_conta.NEXTVAL, [CODIGO DO USUARIO], '[TIPO DA CONTA]', [VALOR DO SALDO]);

-- 1.3 Cadastrar os dados de uma nova receita (entrada de dinheiro) para um usuario.
-- tp_transacao = 'R' identifica a transacao como receita.
INSERT INTO t_transacao (id_transacao, id_conta, id_categoria, vl_transacao, dt_transacao, ds_transacao, tp_transacao)
VALUES (sq_transacao.NEXTVAL, [CODIGO DA CONTA], [CODIGO DA CATEGORIA], [VALOR DA RECEITA],
        TO_DATE('[DATA DA RECEITA]', 'DD/MM/YYYY'), '[DESCRICAO DA RECEITA]', 'R');

-- 1.4 Cadastrar os dados de uma nova despesa (gasto) de um usuario.
-- tp_transacao = 'D' identifica a transacao como despesa.
INSERT INTO t_transacao (id_transacao, id_conta, id_categoria, vl_transacao, dt_transacao, ds_transacao, tp_transacao)
VALUES (sq_transacao.NEXTVAL, [CODIGO DA CONTA], [CODIGO DA CATEGORIA], [VALOR DA DESPESA],
        TO_DATE('[DATA DA DESPESA]', 'DD/MM/YYYY'), '[DESCRICAO DA DESPESA]', 'D');

-- 1.5 Cadastrar os dados de um novo investimento feito por um usuario.
INSERT INTO t_investimento (id_investimento, id_conta, tp_investimento, vl_aplicado, dt_aplicacao, tx_rendimento)
VALUES (sq_investimento.NEXTVAL, [CODIGO DA CONTA], '[TIPO DO INVESTIMENTO]', [VALOR APLICADO],
        TO_DATE('[DATA DA APLICACAO]', 'DD/MM/YYYY'), [TAXA DE RENDIMENTO]);


-- =====================================================================
-- 2. ALTERACAO
-- =====================================================================

-- 2.1 Alterar os dados de um usuario (nome, e-mail, etc.) a partir do seu codigo de referencia.
UPDATE t_usuario
   SET nm_usuario = '[NOME DO USUARIO]',
       ds_email   = '[EMAIL DO USUARIO]',
       ds_senha   = '[SENHA DO USUARIO]'
 WHERE id_usuario = [CODIGO DO USUARIO];

-- 2.2 Alterar os dados de uma receita ja cadastrada, usando o codigo do usuario e o codigo da transacao.
UPDATE t_transacao
   SET id_conta     = [CODIGO DA CONTA],
       id_categoria = [CODIGO DA CATEGORIA],
       vl_transacao = [VALOR DA RECEITA],
       dt_transacao = TO_DATE('[DATA DA RECEITA]', 'DD/MM/YYYY'),
       ds_transacao = '[DESCRICAO DA RECEITA]'
 WHERE id_transacao = [CODIGO DA RECEITA]
   AND tp_transacao = 'R'
   AND id_conta IN (SELECT id_conta FROM t_conta WHERE id_usuario = [CODIGO DO USUARIO]);

-- 2.3 Alterar os dados de uma despesa ja cadastrada, usando o codigo do usuario e o codigo da transacao.
UPDATE t_transacao
   SET id_conta     = [CODIGO DA CONTA],
       id_categoria = [CODIGO DA CATEGORIA],
       vl_transacao = [VALOR DA DESPESA],
       dt_transacao = TO_DATE('[DATA DA DESPESA]', 'DD/MM/YYYY'),
       ds_transacao = '[DESCRICAO DA DESPESA]'
 WHERE id_transacao = [CODIGO DA DESPESA]
   AND tp_transacao = 'D'
   AND id_conta IN (SELECT id_conta FROM t_conta WHERE id_usuario = [CODIGO DO USUARIO]);

-- 2.4 Alterar os dados de um investimento ja cadastrado, usando o codigo do usuario e o codigo do investimento.
UPDATE t_investimento
   SET id_conta        = [CODIGO DA CONTA],
       tp_investimento = '[TIPO DO INVESTIMENTO]',
       vl_aplicado     = [VALOR APLICADO],
       dt_aplicacao    = TO_DATE('[DATA DA APLICACAO]', 'DD/MM/YYYY'),
       tx_rendimento   = [TAXA DE RENDIMENTO]
 WHERE id_investimento = [CODIGO DO INVESTIMENTO]
   AND id_conta IN (SELECT id_conta FROM t_conta WHERE id_usuario = [CODIGO DO USUARIO]);


-- =====================================================================
-- 3. CONSULTAS SIMPLES
-- =====================================================================

-- 3.1 Consultar os dados de um usuario especifico (usando o codigo dele).
SELECT id_usuario, nm_usuario, ds_email, dt_cadastro
  FROM t_usuario
 WHERE id_usuario = [CODIGO DO USUARIO];

-- 3.2 Consultar os dados de uma despesa especifica (usando o codigo do usuario e o codigo da despesa).
SELECT t.id_transacao, t.id_conta, t.id_categoria, t.vl_transacao, t.dt_transacao, t.ds_transacao
  FROM t_transacao t
  JOIN t_conta c ON c.id_conta = t.id_conta
 WHERE c.id_usuario   = [CODIGO DO USUARIO]
   AND t.id_transacao = [CODIGO DA DESPESA]
   AND t.tp_transacao = 'D';

-- 3.3 Consultar os dados de um investimento especifico (usando o codigo do usuario e o codigo do investimento).
SELECT i.id_investimento, i.id_conta, i.tp_investimento, i.vl_aplicado, i.dt_aplicacao, i.tx_rendimento
  FROM t_investimento i
  JOIN t_conta c ON c.id_conta = i.id_conta
 WHERE c.id_usuario      = [CODIGO DO USUARIO]
   AND i.id_investimento = [CODIGO DO INVESTIMENTO];


-- =====================================================================
-- 4. CONSULTAS ORDENADAS
-- =====================================================================

-- 4.1 Consultar todas as despesas de um usuario, da mais recente a mais antiga.
SELECT t.id_transacao, t.ds_transacao, t.vl_transacao, t.dt_transacao
  FROM t_transacao t
  JOIN t_conta c ON c.id_conta = t.id_conta
 WHERE c.id_usuario   = [CODIGO DO USUARIO]
   AND t.tp_transacao = 'D'
 ORDER BY t.dt_transacao DESC, t.id_transacao DESC;

-- 4.2 Consultar todos os investimentos de um usuario, do mais recente ao mais antigo.
SELECT i.id_investimento, i.tp_investimento, i.vl_aplicado, i.dt_aplicacao, i.tx_rendimento
  FROM t_investimento i
  JOIN t_conta c ON c.id_conta = i.id_conta
 WHERE c.id_usuario = [CODIGO DO USUARIO]
 ORDER BY i.dt_aplicacao DESC, i.id_investimento DESC;


-- =====================================================================
-- 5. CONSULTA PARA O DASHBOARD
-- Retorna UMA linha: dados principais do usuario + ultima despesa e
-- ultimo investimento (considerando todas as contas do usuario).
-- =====================================================================
SELECT u.id_usuario,
       u.nm_usuario,
       u.ds_email,
       d.id_transacao    AS id_ultima_despesa,
       d.ds_transacao    AS ds_ultima_despesa,
       d.vl_transacao    AS vl_ultima_despesa,
       d.dt_transacao    AS dt_ultima_despesa,
       i.id_investimento AS id_ultimo_investimento,
       i.tp_investimento AS tp_ultimo_investimento,
       i.vl_aplicado     AS vl_ultimo_investimento,
       i.dt_aplicacao    AS dt_ultimo_investimento
  FROM t_usuario u
  LEFT JOIN (SELECT t.*
               FROM t_transacao t
               JOIN t_conta c ON c.id_conta = t.id_conta
              WHERE c.id_usuario   = [CODIGO DO USUARIO]
                AND t.tp_transacao = 'D'
              ORDER BY t.dt_transacao DESC, t.id_transacao DESC
              FETCH FIRST 1 ROW ONLY) d
    ON 1 = 1
  LEFT JOIN (SELECT i.*
               FROM t_investimento i
               JOIN t_conta c ON c.id_conta = i.id_conta
              WHERE c.id_usuario = [CODIGO DO USUARIO]
              ORDER BY i.dt_aplicacao DESC, i.id_investimento DESC
              FETCH FIRST 1 ROW ONLY) i
    ON 1 = 1
 WHERE u.id_usuario = [CODIGO DO USUARIO];
