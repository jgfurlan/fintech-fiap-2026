package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByUsuarioIdUsuario(Long idUsuario);

    Optional<Conta> findByIdContaAndUsuarioIdUsuario(Long idConta, Long idUsuario);
}
