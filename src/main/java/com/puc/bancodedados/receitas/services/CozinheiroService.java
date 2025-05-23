package com.puc.bancodedados.receitas.services;

import com.puc.bancodedados.receitas.dtos.CozinheiroRequestDTO;
import com.puc.bancodedados.receitas.dtos.CozinheiroResponseDTO;
import com.puc.bancodedados.receitas.model.Cozinheiro;
import com.puc.bancodedados.receitas.model.Empregado;
import com.puc.bancodedados.receitas.repository.CozinheiroRepository;
import com.puc.bancodedados.receitas.repository.EmpregadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CozinheiroService {

    private final CozinheiroRepository cozinheiroRepository;
    private final EmpregadoRepository empregadoRepository;

    @Autowired
    public CozinheiroService(CozinheiroRepository cozinheiroRepository, EmpregadoRepository empregadoRepository) {
        this.cozinheiroRepository = cozinheiroRepository;
        this.empregadoRepository = empregadoRepository;
    }

    @Transactional
    public CozinheiroResponseDTO criarCozinheiro(CozinheiroRequestDTO requestDTO) {
        Empregado empregado = empregadoRepository.findById(requestDTO.cozinheiroRg())
                .orElseThrow(() -> new EntityNotFoundException("Empregado com RG '" + requestDTO.cozinheiroRg() + "' não encontrado para associar ao cozinheiro."));

        if (cozinheiroRepository.existsById(requestDTO.cozinheiroRg())) {
            throw new IllegalArgumentException("Cozinheiro com RG '" + requestDTO.cozinheiroRg() + "' já existe.");
        }

        Cozinheiro cozinheiro = new Cozinheiro();
        cozinheiro.setEmpregado(empregado);
        cozinheiro.setNomeFantasia(requestDTO.nomeFantasia());
        cozinheiro.setDtContrato(requestDTO.dtContrato());
        cozinheiro.setMetaMensalReceitas(requestDTO.metaMensalReceitas());
        cozinheiro.setPrazoInicialDias(requestDTO.prazoInicialDias());

        Cozinheiro cozinheiroSalvo = cozinheiroRepository.save(cozinheiro);

        return mapToResponseDTO(cozinheiroSalvo);
    }

    @Transactional(readOnly = true)
    public List<CozinheiroResponseDTO> listarCozinheiros() {
        return cozinheiroRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CozinheiroResponseDTO buscarCozinheiroPorRg(Long rg) {
        Cozinheiro cozinheiro = cozinheiroRepository.findById(rg)
                .orElseThrow(() -> new EntityNotFoundException("Cozinheiro não encontrado com RG: " + rg));
        return mapToResponseDTO(cozinheiro);
    }

    @Transactional
    public CozinheiroResponseDTO atualizarCozinheiro(Long rg, CozinheiroRequestDTO requestDTO) {
        Cozinheiro cozinheiroExistente = cozinheiroRepository.findById(rg)
                .orElseThrow(() -> new EntityNotFoundException("Cozinheiro não encontrado com RG: " + rg));

        if (!rg.equals(requestDTO.cozinheiroRg())) {
            throw new IllegalArgumentException("O RG do cozinheiro no corpo da requisição (" + requestDTO.cozinheiroRg() +
                    ") não corresponde ao RG na URL (" + rg + ").");
        }
        cozinheiroExistente.setNomeFantasia(requestDTO.nomeFantasia());
        cozinheiroExistente.setDtContrato(requestDTO.dtContrato()); // Update dtContrato
        cozinheiroExistente.setMetaMensalReceitas(requestDTO.metaMensalReceitas());
        cozinheiroExistente.setPrazoInicialDias(requestDTO.prazoInicialDias());

        Cozinheiro cozinheiroAtualizado = cozinheiroRepository.save(cozinheiroExistente);
        return mapToResponseDTO(cozinheiroAtualizado);
    }

    @Transactional
    public void deletarCozinheiro(Long rg) {
        Cozinheiro cozinheiro = cozinheiroRepository.findById(rg)
                .orElseThrow(() -> new EntityNotFoundException("Cozinheiro não encontrado com RG: " + rg));
        if ((cozinheiro.getRestaurantes() != null && !cozinheiro.getRestaurantes().isEmpty()) ||
                (cozinheiro.getReceitas() != null && !cozinheiro.getReceitas().isEmpty())) {
            throw new IllegalStateException("Cozinheiro não pode ser deletado pois possui restaurantes ou receitas associadas.");
        }
        cozinheiroRepository.deleteById(rg);
    }

    private CozinheiroResponseDTO mapToResponseDTO(Cozinheiro cozinheiro) {
        return new CozinheiroResponseDTO(
                cozinheiro.getCozinheiroRg(),
                cozinheiro.getEmpregado() != null ? cozinheiro.getEmpregado().getNomeEmpregado() : null,
                cozinheiro.getNomeFantasia(),
                cozinheiro.getDtContrato(), // Include dtContrato
                cozinheiro.getMetaMensalReceitas(),
                cozinheiro.getPrazoInicialDias()
        );
    }
}