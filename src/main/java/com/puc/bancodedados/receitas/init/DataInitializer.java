package com.puc.bancodedados.receitas.init;

import com.puc.bancodedados.receitas.model.*;
import com.puc.bancodedados.receitas.model.ids.ReceitaIngredienteId;
import com.puc.bancodedados.receitas.model.ids.ReceitaLivroId;
import com.puc.bancodedados.receitas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmpregadoRepository empregadoRepository;
    private final CozinheiroRepository cozinheiroRepository;
    private final DegustadorRepository degustadorRepository;
    private final EditorRepository editorRepository;
    private final CategoriaRepository categoriaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final LivroRepository livroRepository;
    private final ReceitaRepository receitaRepository;
    private final ReceitaIngredienteRepository receitaIngredienteRepository;
    private final TesteRepository testeRepository;
    private final ReceitaLivroRepository receitaLivroRepository;

    private static final List<String> PRIMEIROS_NOMES = Arrays.asList(
            "Miguel", "Arthur", "Gael", "Heitor", "Theo", "Davi", "Gabriel", "Bernardo", "Samuel", "João",
            "Helena", "Alice", "Laura", "Maria", "Sophia", "Isabella", "Luísa", "Júlia", "Heloísa", "Lívia",
            "Enzo", "Pedro", "Lucas", "Matheus", "Rafael", "Guilherme", "Gustavo", "Felipe", "Bruno", "Vinícius",
            "Beatriz", "Mariana", "Ana", "Gabriela", "Yasmin", "Larissa", "Letícia", "Nicole", "Giovanna", "Clara");

    private static final List<String> SOBRENOMES = Arrays.asList(
            "Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes",
            "Costa", "Ribeiro", "Martins", "Carvalho", "Almeida", "Lopes", "Soares", "Fernandes", "Vieira", "Barbosa");

    private static final List<String> TIPOS_RECEITA = Arrays.asList(
            "Torta de", "Bolo de", "Frango", "Carne de Panela com", "Risoto de", "Sopa de", "Salada de", "Suco de",
            "Vitamina de", "Mousse de");

    private static final List<String> COMPLEMENTOS_RECEITA = Arrays.asList(
            "Maçã", "Chocolate", "Limão", "Morango", "Frango com Catupiry", "Brócolis", "Quatro Queijos", "Legumes",
            "Beterraba", "Laranja com Gengibre");

    public DataInitializer(EmpregadoRepository empregadoRepository, CozinheiroRepository cozinheiroRepository,
            DegustadorRepository degustadorRepository, EditorRepository editorRepository,
            CategoriaRepository categoriaRepository, IngredienteRepository ingredienteRepository,
            RestauranteRepository restauranteRepository, LivroRepository livroRepository,
            ReceitaRepository receitaRepository, ReceitaIngredienteRepository receitaIngredienteRepository,
            TesteRepository testeRepository, ReceitaLivroRepository receitaLivroRepository) {
        this.empregadoRepository = empregadoRepository;
        this.cozinheiroRepository = cozinheiroRepository;
        this.degustadorRepository = degustadorRepository;
        this.editorRepository = editorRepository;
        this.categoriaRepository = categoriaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.livroRepository = livroRepository;
        this.receitaRepository = receitaRepository;
        this.receitaIngredienteRepository = receitaIngredienteRepository;
        this.testeRepository = testeRepository;
        this.receitaLivroRepository = receitaLivroRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Iniciando a inserção de dados em massa...");

        
        List<Cozinheiro> cozinheiros = criarCozinheirosComEmpregados(100);

        
        List<Categoria> categorias = criarCategorias();
        List<Ingrediente> ingredientes = criarIngredientes();

        
        criarEditoresEDegustadores(cozinheiros);

        List<Livro> livros = criarLivros();

        
        criarRestaurantes(cozinheiros);
        List<Receita> receitas = criarReceitas(cozinheiros, categorias);

        associarIngredientesAReceitas(receitas, ingredientes);
        associarReceitasALivros(receitas, livros);

        System.out.println("Dados iniciais inseridos com sucesso!");
    }

    private List<Cozinheiro> criarCozinheirosComEmpregados(int quantidade) {
        List<Cozinheiro> cozinheiros = new ArrayList<>();
        long rgInicial = 100000001L;
        LocalDate dataContratoMaisAntiga = LocalDate.of(2018, Month.MARCH, 15);

        for (int i = 0; i < quantidade; i++) {
            String nome = PRIMEIROS_NOMES.get(randomInt(PRIMEIROS_NOMES.size())) + " "
                    + SOBRENOMES.get(randomInt(SOBRENOMES.size()));
            LocalDate dataAdmissao = randomDate(2018, 2022);
            BigDecimal salario = new BigDecimal(3000 + randomInt(4000));

            Empregado empregado = new Empregado(rgInicial + i, nome, dataAdmissao, salario);

            LocalDate dataContrato = (i < 3) ? dataContratoMaisAntiga
                    : empregado.getDataAdmissao().plusMonths(randomInt(6) + 1);
            String nomeFantasia = "Chef " + empregado.getNomeEmpregado().split(" ")[0];

            Cozinheiro cozinheiro = new Cozinheiro(null, empregado, nomeFantasia, dataContrato, randomInt(5) + 2,
                    randomInt(20) + 10, new HashSet<>(), new HashSet<>());
            cozinheiros.add(cozinheiro);
        }
        return cozinheiroRepository.saveAll(cozinheiros);
    }

    private List<Categoria> criarCategorias() {
        List<Categoria> categorias = Arrays.asList(
                new Categoria(null, "Massas", new HashSet<>()),
                new Categoria(null, "Sobremesas", new HashSet<>()),
                new Categoria(null, "Carnes", new HashSet<>()),
                new Categoria(null, "Saladas", new HashSet<>()),
                new Categoria(null, "Sopas", new HashSet<>()),
                new Categoria(null, "Aves", new HashSet<>()),
                new Categoria(null, "Bebidas", new HashSet<>()));
        return categoriaRepository.saveAll(categorias);
    }

    private List<Ingrediente> criarIngredientes() {
        List<Ingrediente> ingredientes = Arrays.asList(
                new Ingrediente(null, "Farinha de Trigo", "Tipo 1", new HashSet<>()),
                new Ingrediente(null, "Açúcar Refinado", "Branco", new HashSet<>()),
                new Ingrediente(null, "Sal Marinho", "Grosso", new HashSet<>()),
                new Ingrediente(null, "Ovo de Galinha", "Tipo Grande Branco", new HashSet<>()),
                new Ingrediente(null, "Leite Integral", "Tipo A", new HashSet<>()),
                new Ingrediente(null, "Manteiga Sem Sal", "Pote 200g", new HashSet<>()),
                new Ingrediente(null, "Peito de Frango", "Congelado", new HashSet<>()),
                new Ingrediente(null, "Batata Inglesa", "Tipo Asterix", new HashSet<>()),
                new Ingrediente(null, "Cebola", "Branca", new HashSet<>()),
                new Ingrediente(null, "Alho", "Triturado", new HashSet<>()),
                new Ingrediente(null, "Tomate", "Italiano", new HashSet<>()),
                new Ingrediente(null, "Arroz Arbóreo", "Para risoto", new HashSet<>()),
                new Ingrediente(null, "Queijo Parmesão", "Ralado na hora", new HashSet<>()),
                new Ingrediente(null, "Azeite de Oliva", "Extra Virgem", new HashSet<>()));
        return ingredienteRepository.saveAll(ingredientes);
    }

    private void criarEditoresEDegustadores(List<Cozinheiro> cozinheiros) {

        if (cozinheiros.size() > 98) {
            Empregado empEditor = cozinheiros.get(98).getEmpregado();

            Editor editor = new Editor(null, empEditor, LocalDate.now(), new HashSet<>());
            editorRepository.save(editor);
        }
        if (cozinheiros.size() > 99) {
            Empregado empDegustador = cozinheiros.get(99).getEmpregado();

            Degustador degustador = new Degustador(null, empDegustador, LocalDate.now(), new HashSet<>());
            degustadorRepository.save(degustador);
        }
    }

    private List<Livro> criarLivros() {
        Optional<Editor> editorOpt = editorRepository.findAll().stream().findFirst();
        if (editorOpt.isEmpty())
            return Collections.emptyList();
        Editor editor = editorOpt.get();

        List<Livro> livros = Arrays.asList(
                new Livro("978-8535914849", "A Culinária Rústica", editor, new HashSet<>()),
                new Livro("978-8571646287", "Sabores do Brasil", editor, new HashSet<>()),
                new Livro("978-8501066736", "Doce Equilíbrio", editor, new HashSet<>()));
        return livroRepository.saveAll(livros);
    }

    private void criarRestaurantes(List<Cozinheiro> cozinheiros) {
        List<Restaurante> restaurantes = new ArrayList<>();
        for (Cozinheiro cozinheiro : cozinheiros) {
            restaurantes.add(new Restaurante(null, "Restaurante do " + cozinheiro.getNomeFantasia(), cozinheiro));
        }
        restauranteRepository.saveAll(restaurantes);
    }

    private List<Receita> criarReceitas(List<Cozinheiro> cozinheiros, List<Categoria> categorias) {
        List<Receita> receitas = new ArrayList<>();
        for (Cozinheiro cozinheiro : cozinheiros) {
            int numReceitas = randomInt(8) + 2;
            for (int i = 0; i < numReceitas; i++) {
                String nomeReceita = TIPOS_RECEITA.get(randomInt(TIPOS_RECEITA.size())) + " "
                        + COMPLEMENTOS_RECEITA.get(randomInt(COMPLEMENTOS_RECEITA.size()));
                LocalDate dataCriacao = randomDate(cozinheiro.getDtContrato().getYear() + 1, 2025);

                Receita receita = new Receita(null, nomeReceita, "Modo de preparo detalhado aqui...", dataCriacao,
                        randomInt(4) + 1, cozinheiro, categorias.get(randomInt(categorias.size())),
                        new HashSet<>(), new HashSet<>(), new HashSet<>());
                receitas.add(receita);
            }
        }
        Map<String, Receita> receitasUnicas = new HashMap<>();
        for (Receita r : receitas) {
            String chave = r.getCozinheiro().getCozinheiroRg() + "-" + r.getNomeReceita();
            receitasUnicas.putIfAbsent(chave, r);
        }

        return receitaRepository.saveAll(new ArrayList<>(receitasUnicas.values()));
    }

    private void associarIngredientesAReceitas(List<Receita> receitas, List<Ingrediente> ingredientes) {
        List<ReceitaIngrediente> associacoes = new ArrayList<>();
        for (Receita receita : receitas) {
            int numIngredientes = randomInt(5) + 3;
            Collections.shuffle(ingredientes);
            for (int i = 0; i < numIngredientes; i++) {
                Ingrediente ingrediente = ingredientes.get(i);
                ReceitaIngredienteId id = new ReceitaIngredienteId(receita.getId(), ingrediente.getId());
                BigDecimal quantidade = new BigDecimal(randomInt(500) + 1);

                ReceitaIngrediente ri = new ReceitaIngrediente(id, receita, ingrediente, quantidade, "g");
                associacoes.add(ri);
            }
        }
        receitaIngredienteRepository.saveAll(associacoes);
    }

    private void associarReceitasALivros(List<Receita> receitas, List<Livro> livros) {
        if (livros.isEmpty())
            return;
        List<ReceitaLivro> associacoes = new ArrayList<>();

        for (Receita receita : receitas) {
            if (ThreadLocalRandom.current().nextDouble() < 0.3) {
                Livro livro = livros.get(randomInt(livros.size()));
                ReceitaLivroId id = new ReceitaLivroId(livro.getIsbn(), receita.getId());

                if (associacoes.stream().noneMatch(rl -> rl.getId().equals(id))) {
                    associacoes.add(new ReceitaLivro(id, livro, receita));
                }
            }
        }
        receitaLivroRepository.saveAll(associacoes);
    }

    private int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    private LocalDate randomDate(int startYear, int endYear) {
        long minDay = LocalDate.of(startYear, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(endYear, 1, 1).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}