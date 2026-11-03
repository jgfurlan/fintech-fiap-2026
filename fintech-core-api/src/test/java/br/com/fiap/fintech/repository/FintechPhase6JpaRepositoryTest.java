package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FintechPhase6JpaRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private InvestimentoRepository investimentoRepository;

    @Autowired
    private MetaRepository metaRepository;

    private Usuario testUsuario;
    private Conta testConta;
    private Categoria categAlimentacao;
    private Categoria categSalario;

    @BeforeEach
    void setUp() {
        // Base Setup for Phase 6 Entities
        testUsuario = new Usuario("João Gabriel Furlan", "rm570397@fiap.com.br", "senha123");
        testUsuario = usuarioRepository.save(testUsuario);

        testConta = new Conta(testUsuario, "CORRENTE", new BigDecimal("1500.00"));
        testConta = contaRepository.save(testConta);

        categSalario = new Categoria("Salário", Categoria.TIPO_RECEITA);
        categSalario = categoriaRepository.save(categSalario);

        categAlimentacao = new Categoria("Alimentação", Categoria.TIPO_DESPESA);
        categAlimentacao = categoriaRepository.save(categAlimentacao);
    }

    @Test
    @DisplayName("Fase 6 - 1.1 e 4.1: Cadastrar e consultar dados de um novo usuário via Sequence")
    void testCadastrarEConsultarUsuario() {
        Usuario novoUsuario = new Usuario("Maria Silva", "maria@fiap.com.br", "segredo456");
        Usuario salvo = usuarioRepository.save(novoUsuario);

        assertNotNull(salvo.getIdUsuario());
        assertTrue(salvo.getIdUsuario() > 0);
        assertEquals("Maria Silva", salvo.getNmUsuario());
        assertEquals("maria@fiap.com.br", salvo.getDsEmail());
        assertNotNull(salvo.getDtCadastro());

        // 4.1: Consultar por código
        Optional<Usuario> encontrado = usuarioRepository.findById(salvo.getIdUsuario());
        assertTrue(encontrado.isPresent());
        assertEquals("Maria Silva", encontrado.get().getNmUsuario());

        // Consulta por email
        Optional<Usuario> porEmail = usuarioRepository.findByDsEmail("maria@fiap.com.br");
        assertTrue(porEmail.isPresent());
        assertEquals(salvo.getIdUsuario(), porEmail.get().getIdUsuario());
    }

    @Test
    @DisplayName("Fase 6 - 2.1: Alterar os dados de um usuário pelo seu código de referência")
    void testAlterarUsuario() {
        testUsuario.setNmUsuario("João G. O. Furlan");
        testUsuario.setDsEmail("jgfurlan.novo@fiap.com.br");
        testUsuario.setDsSenha("novaSenha789");
        Usuario atualizado = usuarioRepository.save(testUsuario);

        Usuario recuperado = usuarioRepository.findById(testUsuario.getIdUsuario()).orElseThrow();
        assertEquals("João G. O. Furlan", recuperado.getNmUsuario());
        assertEquals("jgfurlan.novo@fiap.com.br", recuperado.getDsEmail());
        assertEquals("novaSenha789", recuperado.getDsSenha());
    }

    @Test
    @DisplayName("Fase 6 - 1.2: Cadastrar e consultar conta bancária vinculada ao usuário")
    void testCadastrarEConsultarConta() {
        Conta poupanca = new Conta(testUsuario, "POUPANCA", new BigDecimal("500.00"));
        Conta salva = contaRepository.save(poupanca);

        assertNotNull(salva.getIdConta());
        assertTrue(salva.getIdConta() > 0);
        assertEquals("POUPANCA", salva.getTpConta());
        assertEquals(new BigDecimal("500.00"), salva.getVlSaldoAtual());

        List<Conta> contasDoUsuario = contaRepository.findByUsuarioIdUsuario(testUsuario.getIdUsuario());
        assertEquals(2, contasDoUsuario.size()); // testConta (corrente) + poupanca
    }

    @Test
    @DisplayName("Fase 6 - 1.3 e 2.2: Cadastrar e alterar receita ('R') conferindo vínculo com usuário")
    void testCadastrarEAlterarReceita() {
        Transacao receita = new Transacao(
                testConta,
                categSalario,
                new BigDecimal("5000.00"),
                LocalDate.of(2026, 9, 5),
                "Salário Mensal",
                Transacao.TIPO_RECEITA
        );
        Transacao salva = transacaoRepository.save(receita);

        assertNotNull(salva.getIdTransacao());
        assertTrue(salva.isReceita());
        assertEquals("R", salva.getTpTransacao());

        // 2.2: Alterar dados da receita conferindo que pertence ao usuário
        Optional<Transacao> encontrada = transacaoRepository.findByIdTransacaoAndContaUsuarioIdUsuarioAndTpTransacao(
                salva.getIdTransacao(),
                testUsuario.getIdUsuario(),
                "R"
        );
        assertTrue(encontrada.isPresent());

        Transacao txParaAtualizar = encontrada.get();
        txParaAtualizar.setVlTransacao(new BigDecimal("5500.00"));
        txParaAtualizar.setDsTransacao("Salário com Bonificação");
        transacaoRepository.save(txParaAtualizar);

        Transacao atualizada = transacaoRepository.findById(salva.getIdTransacao()).orElseThrow();
        assertEquals(new BigDecimal("5500.00"), atualizada.getVlTransacao());
        assertEquals("Salário com Bonificação", atualizada.getDsTransacao());
    }

    @Test
    @DisplayName("Fase 6 - 1.4, 2.3 e 4.2: Cadastrar, alterar e consultar despesa ('D') por usuário e id")
    void testCadastrarAlterarEConsultarDespesa() {
        Transacao despesa = new Transacao(
                testConta,
                categAlimentacao,
                new BigDecimal("120.50"),
                LocalDate.of(2026, 9, 10),
                "Supermercado",
                Transacao.TIPO_DESPESA
        );
        Transacao salva = transacaoRepository.save(despesa);

        assertNotNull(salva.getIdTransacao());
        assertTrue(salva.isDespesa());

        // 4.2: Consultar despesa específica por id_usuario e id_transacao
        Optional<Transacao> consultada = transacaoRepository.findByIdTransacaoAndContaUsuarioIdUsuarioAndTpTransacao(
                salva.getIdTransacao(),
                testUsuario.getIdUsuario(),
                "D"
        );
        assertTrue(consultada.isPresent());
        assertEquals(new BigDecimal("120.50"), consultada.get().getVlTransacao());

        // 2.3: Alterar despesa conferindo que conta pertence ao usuário
        Transacao despParaAtualizar = consultada.get();
        despParaAtualizar.setVlTransacao(new BigDecimal("145.80"));
        despParaAtualizar.setDsTransacao("Supermercado Semanal");
        transacaoRepository.save(despParaAtualizar);

        Transacao atualizada = transacaoRepository.findById(salva.getIdTransacao()).orElseThrow();
        assertEquals(new BigDecimal("145.80"), atualizada.getVlTransacao());
        assertEquals("Supermercado Semanal", atualizada.getDsTransacao());
    }

    @Test
    @DisplayName("Fase 6 - 5.1: Consultar todas as despesas de um usuário ordenadas da mais recente à mais antiga")
    void testConsultarDespesasOrdenadas() {
        Transacao d1 = new Transacao(testConta, categAlimentacao, new BigDecimal("50.00"),
                LocalDate.of(2026, 9, 1), "Lanche 1", Transacao.TIPO_DESPESA);
        Transacao d2 = new Transacao(testConta, categAlimentacao, new BigDecimal("80.00"),
                LocalDate.of(2026, 9, 15), "Almoço", Transacao.TIPO_DESPESA);
        Transacao d3 = new Transacao(testConta, categAlimentacao, new BigDecimal("200.00"),
                LocalDate.of(2026, 9, 20), "Jantar", Transacao.TIPO_DESPESA);

        transacaoRepository.saveAll(List.of(d1, d2, d3));

        List<Transacao> despesasOrdenadas = transacaoRepository
                .findByContaUsuarioIdUsuarioAndTpTransacaoOrderByDtTransacaoDescIdTransacaoDesc(
                        testUsuario.getIdUsuario(),
                        Transacao.TIPO_DESPESA
                );

        assertEquals(3, despesasOrdenadas.size());
        assertEquals("Jantar", despesasOrdenadas.get(0).getDsTransacao());
        assertEquals(LocalDate.of(2026, 9, 20), despesasOrdenadas.get(0).getDtTransacao());
        assertEquals("Almoço", despesasOrdenadas.get(1).getDsTransacao());
        assertEquals(LocalDate.of(2026, 9, 15), despesasOrdenadas.get(1).getDtTransacao());
        assertEquals("Lanche 1", despesasOrdenadas.get(2).getDsTransacao());
        assertEquals(LocalDate.of(2026, 9, 1), despesasOrdenadas.get(2).getDtTransacao());
    }

    @Test
    @DisplayName("Fase 6 - 1.5, 2.4, 4.3 e 5.2: Ciclo completo de Investimentos")
    void testCicloInvestimentos() {
        // 1.5: Cadastrar novo investimento
        Investimento inv1 = new Investimento(
                testConta,
                "CDB 100% CDI",
                new BigDecimal("1000.00"),
                LocalDate.of(2026, 8, 1),
                new BigDecimal("10.75")
        );
        Investimento inv2 = new Investimento(
                testConta,
                "LCI DI",
                new BigDecimal("2500.00"),
                LocalDate.of(2026, 9, 10),
                new BigDecimal("9.20")
        );

        investimentoRepository.save(inv1);
        investimentoRepository.save(inv2);

        // 4.3: Consultar investimento específico (id_usuario + id_investimento)
        Optional<Investimento> consultado = investimentoRepository.findByIdInvestimentoAndContaUsuarioIdUsuario(
                inv1.getIdInvestimento(),
                testUsuario.getIdUsuario()
        );
        assertTrue(consultado.isPresent());
        assertEquals("CDB 100% CDI", consultado.get().getTpInvestimento());

        // 2.4: Alterar dados do investimento
        Investimento paraAtualizar = consultado.get();
        paraAtualizar.setVlAplicado(new BigDecimal("1200.00"));
        paraAtualizar.setTxRendimento(new BigDecimal("11.00"));
        investimentoRepository.save(paraAtualizar);

        Investimento atualizado = investimentoRepository.findById(inv1.getIdInvestimento()).orElseThrow();
        assertEquals(new BigDecimal("1200.00"), atualizado.getVlAplicado());
        assertEquals(new BigDecimal("11.00"), atualizado.getTxRendimento());

        // 5.2: Consultar todos os investimentos ordenados da mais recente à mais antiga
        List<Investimento> investimentosOrdenados = investimentoRepository
                .findByContaUsuarioIdUsuarioOrderByDtAplicacaoDescIdInvestimentoDesc(testUsuario.getIdUsuario());

        assertEquals(2, investimentosOrdenados.size());
        assertEquals("LCI DI", investimentosOrdenados.get(0).getTpInvestimento());
        assertEquals(LocalDate.of(2026, 9, 10), investimentosOrdenados.get(0).getDtAplicacao());
    }

    @Test
    @DisplayName("Fase 6: Cadastro e consulta de Meta financeira para o usuário")
    void testCadastrarEConsultarMeta() {
        Meta meta = new Meta(
                testUsuario,
                "Reserva de Emergência",
                new BigDecimal("10000.00"),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("3000.00")
        );
        Meta salva = metaRepository.save(meta);

        assertNotNull(salva.getIdMeta());
        assertTrue(salva.getIdMeta() > 0);
        assertEquals("Reserva de Emergência", salva.getDsMeta());
        assertEquals(new BigDecimal("10000.00"), salva.getVlObjetivo());
        assertEquals(new BigDecimal("3000.00"), salva.getVlAtual());

        List<Meta> metas = metaRepository.findByUsuarioIdUsuario(testUsuario.getIdUsuario());
        assertEquals(1, metas.size());
        assertEquals("Reserva de Emergência", metas.get(0).getDsMeta());
    }

    @Test
    @DisplayName("Fase 6 - 6: Consulta para Dashboard (última despesa e último investimento)")
    void testConsultaDashboard() {
        // Criar transações com diferentes datas
        transacaoRepository.save(new Transacao(testConta, categAlimentacao, new BigDecimal("30.00"),
                LocalDate.of(2026, 9, 1), "Café", Transacao.TIPO_DESPESA));
        transacaoRepository.save(new Transacao(testConta, categAlimentacao, new BigDecimal("150.00"),
                LocalDate.of(2026, 9, 25), "Última Compra do Mês", Transacao.TIPO_DESPESA));

        // Criar investimentos com diferentes datas
        investimentoRepository.save(new Investimento(testConta, "Poupança", new BigDecimal("200.00"),
                LocalDate.of(2026, 8, 15), new BigDecimal("6.00")));
        investimentoRepository.save(new Investimento(testConta, "Tesouro Selic", new BigDecimal("3000.00"),
                LocalDate.of(2026, 9, 28), new BigDecimal("10.50")));

        // Buscar última despesa
        Optional<Transacao> ultimaDespesa = transacaoRepository
                .findFirstByContaUsuarioIdUsuarioAndTpTransacaoOrderByDtTransacaoDescIdTransacaoDesc(
                        testUsuario.getIdUsuario(),
                        Transacao.TIPO_DESPESA
                );

        assertTrue(ultimaDespesa.isPresent());
        assertEquals("Última Compra do Mês", ultimaDespesa.get().getDsTransacao());
        assertEquals(new BigDecimal("150.00"), ultimaDespesa.get().getVlTransacao());
        assertEquals(LocalDate.of(2026, 9, 25), ultimaDespesa.get().getDtTransacao());

        // Buscar último investimento
        Optional<Investimento> ultimoInvestimento = investimentoRepository
                .findFirstByContaUsuarioIdUsuarioOrderByDtAplicacaoDescIdInvestimentoDesc(testUsuario.getIdUsuario());

        assertTrue(ultimoInvestimento.isPresent());
        assertEquals("Tesouro Selic", ultimoInvestimento.get().getTpInvestimento());
        assertEquals(new BigDecimal("3000.00"), ultimoInvestimento.get().getVlAplicado());
        assertEquals(LocalDate.of(2026, 9, 28), ultimoInvestimento.get().getDtAplicacao());
    }
}
