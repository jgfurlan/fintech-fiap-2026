package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {

    List<Investimento> findByContaUsuarioIdUsuario(Long idUsuario);

    Optional<Investimento> findByIdInvestimentoAndContaUsuarioIdUsuario(Long idInvestimento, Long idUsuario);

    List<Investimento> findByContaUsuarioIdUsuarioOrderByDtAplicacaoDescIdInvestimentoDesc(Long idUsuario);

    Optional<Investimento> findFirstByContaUsuarioIdUsuarioOrderByDtAplicacaoDescIdInvestimentoDesc(Long idUsuario);

    List<Investimento> findByContaIdConta(Long idConta);
}
