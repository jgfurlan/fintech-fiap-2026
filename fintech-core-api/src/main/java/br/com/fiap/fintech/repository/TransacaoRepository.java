package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaUsuarioIdUsuario(Long idUsuario);

    List<Transacao> findByContaUsuarioIdUsuarioAndTpTransacao(Long idUsuario, String tpTransacao);

    Optional<Transacao> findByIdTransacaoAndContaUsuarioIdUsuario(Long idTransacao, Long idUsuario);

    Optional<Transacao> findByIdTransacaoAndContaUsuarioIdUsuarioAndTpTransacao(Long idTransacao, Long idUsuario, String tpTransacao);

    List<Transacao> findByContaUsuarioIdUsuarioAndTpTransacaoOrderByDtTransacaoDescIdTransacaoDesc(Long idUsuario, String tpTransacao);

    Optional<Transacao> findFirstByContaUsuarioIdUsuarioAndTpTransacaoOrderByDtTransacaoDescIdTransacaoDesc(Long idUsuario, String tpTransacao);

    List<Transacao> findByContaIdConta(Long idConta);
}
