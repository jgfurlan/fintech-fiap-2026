package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTpCategoria(String tpCategoria);

    Optional<Categoria> findByNmCategoria(String nmCategoria);
}
