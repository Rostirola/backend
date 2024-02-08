package br.edu.infnet.lms.backend.controllers;

import br.edu.infnet.lms.backend.dtos.ProdutoRecordDto;
import br.edu.infnet.lms.backend.models.ProdutoModel;
import br.edu.infnet.lms.backend.repositories.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoModel> salvarProduto(@RequestBody @Valid ProdutoRecordDto produtoRecordDto) {
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoRecordDto, produtoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produtoModel));
    }

    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoModel>> getProdutos() {
        List<ProdutoModel> produtoModelList = produtoRepository.findAll();
        if (!produtoModelList.isEmpty()) {
            for (ProdutoModel produto : produtoModelList) {
                UUID id = produto.getIdProduto();
                produto.add(linkTo(methodOn(ProdutoController.class).getProduto(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoModelList);
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<Object> getProduto(@PathVariable(value = "id")UUID id) {
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if(produto0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        produto0.get().add(linkTo(methodOn(ProdutoController.class).getProdutos()).withRel("Lista de produtos"));
        return ResponseEntity.status(HttpStatus.OK).body(produto0.get());
    }

    @PutMapping("/produto/{id}")
    public ResponseEntity<Object> atualizaProduto(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProdutoRecordDto produtoRecordDto) {
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if (produto0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        var produtoModel = produto0.get();
        BeanUtils.copyProperties(produtoRecordDto, produtoModel);
        return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produtoModel));
    }

    @DeleteMapping("/produto/{id}")
    public ResponseEntity<Object> deletaProduto(@PathVariable(value = "id") UUID id) {
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if (produto0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        produtoRepository.delete(produto0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso.");
    }
}
