package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

    List<Meta> findByUsuarioIdUsuario(Long idUsuario);

    Optional<Meta> findByIdMetaAndUsuarioIdUsuario(Long idMeta, Long idUsuario);
}
