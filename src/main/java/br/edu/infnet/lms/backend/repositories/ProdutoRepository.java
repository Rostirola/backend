package br.edu.infnet.lms.backend.repositories;

import br.edu.infnet.lms.backend.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository  extends JpaRepository<ProdutoModel, UUID> {

}
