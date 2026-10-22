# InvScan — Documentação do processo e das decisões (Trabalho 3: Persistência com Room)

## 1. Visão geral

O **InvScan** é um aplicativo Android de inventário de patrimônio, feito em Kotlin com Jetpack Compose. Ele permite cadastrar itens, organizá-los por local, marcar patrimônios como conferidos, pesquisar por nome, código ou série e consultar os detalhes de patrimônios e locais.

No Trabalho 3, **patrimônios e locais passaram a ser gravados em um banco local com Room 3** e continuam disponíveis depois que o app é fechado e reaberto. Nenhuma tela nova foi criada: as telas, listas, formulários e botões que já existiam passaram a ler e escrever no banco.

## 2. Demonstração em vídeo

O vídeo está em [`video/InvScanExample.mp4`](video/InvScanExample.mp4) (aprox. 1 min 30 s, gravado em um dispositivo Android) e mostra o funcionamento do app e da persistência dos dados.

Ele percorre o fluxo principal: cadastrar um local, cadastrar um patrimônio vinculado a esse local (com tipo, série, prateleira, fotos e código), abrir o detalhe do item e editá-lo. Em seguida, o app é fechado pela lista de recentes e reaberto, e a tela Início continua mostrando 1 item, 1 conferido e 1 local, o que comprova que os dados foram gravados no banco.

## 3. Como o projeto estava no Trabalho 2 e o que mudou

| Aspecto | Trabalho 1 | Trabalho 2 | Trabalho 3 (atual) |
|---|---|---|---|
| Telas | 3, estilizadas | 11, com `NavHost` e navegação real | As mesmas 11 telas |
| Dados | Fixos no código | Listas `mutableStateListOf` na memória | Listas **espelhadas** em um banco Room |
| Ao fechar o app | Nada era salvo | Todos os dados sumiam | Patrimônios e locais permanecem |
| Organização | Um único `MainActivity.kt` | Pastas por responsabilidade | Mesma organização, com `AppDatabase`, DAOs e entidades em `data/` |

No Trabalho 2, o `InvScanViewModel` guardava tudo em `mutableStateListOf`, e as telas desenhavam essas listas. O que mudou para o Trabalho 3 foi:

- as classes `Patrimonio` e `Local` viraram **entidades Room** (`@Entity`);
- foram criados dois **DAOs** (`PatrimonioDAO` e `LocalDAO`) e o `AppDatabase`;
- o `InvScanViewModel` passou a **carregar o banco ao ser criado** e a **gravar cada inserção, edição e remoção** em corrotinas no `Dispatchers.IO`;
- as telas **não foram alteradas**: continuam lendo as mesmas listas do ViewModel.

## 4. Telas e funcionalidades

| # | Tela (arquivo) | Responsabilidade | Print |
|---|---|---|---|
| 1 | **Início** (`InicioScreen`) | Resumo do inventário, pesquisa e atalhos | [01](prints/01-inicio.png) |
| 2 | **Menu** (`MenuScreen`) | Acesso às funcionalidades | [02](prints/02-menu.png) |
| 3 | **Patrimônios** (`PatrimoniosScreen`) | Lista, pesquisa, conferência, exclusão e criação | [03](prints/03-patrimonios.png) |
| 4 | **Detalhe do patrimônio** (`PatrimonioDetalheScreen`) | Exibe e edita o item, seu local e suas imagens | [04](prints/04-detalhe-patrimonio.png), [05](prints/05-detalhe-patrimonio-edicao.png) |
| 5 | **Criar item** (`CriarItemScreen`) | Cadastro de patrimônio | [06](prints/06-criar-item.png) |
| 6 | **Locais** (`LocaisScreen`) | Lista e cadastra, edita e remove locais | [07](prints/07-locais.png) |
| 7 | **Detalhe do local** (`LocalDetalheScreen`) | Edita o local, lista seus itens e mostra o progresso de conferência | [08](prints/08-detalhe-local.png) |
| 8 | **Escanear código** (`EscanearScreen`) | Localiza um patrimônio pelo código digitado | [09](prints/09-escanear.png) |
| 9 | **Usuário** (`UsuarioScreen`) | Perfil e conquistas | [10](prints/10-usuario.png) |
| 10 | **Configurações** (`ConfiguracoesScreen`) | Tema escuro, texto grande e acesso ao suporte | [11](prints/11-configuracoes.png), [12](prints/12-configuracoes-tema-escuro.png) |
| 11 | **Suporte** (`SuporteScreen`) | Perguntas frequentes e contato | [13](prints/13-suporte.png) |

A leitura de código por câmera não foi implementada: a tela "Escanear" pesquisa o código digitado. As imagens são escolhidas pelo Photo Picker do Android.

### Navegação

As rotas ficam em `navigation/Rotas.kt` e o `NavHost` em `navigation/AppNavigation.kt`, junto com o `Scaffold`, as barras superior e inferior e os `Snackbar`s. As telas de detalhe recebem o `id` pela rota (`patrimonio/{id}` e `local/{id}`).

| Tela | Rota |
|---|---|
| Início | `inicio` |
| Menu | `menu` |
| Patrimônios | `patrimonios` |
| Detalhe do patrimônio | `patrimonio/{id}` |
| Criar item | `criar_item` |
| Locais | `locais` |
| Detalhe do local | `local/{id}` |
| Escanear | `escanear` |
| Usuário | `usuario` |
| Configurações | `configuracoes` |
| Suporte | `suporte` |

## 5. Estrutura de arquivos e pastas

Pacote principal: `com.example.exibirpatrimoniosactivity` (em `app/src/main/java/`).

```text
InvScan/
├── README.md
├── build.gradle.kts, settings.gradle.kts, gradle/libs.versions.toml
├── docs/
│   ├── DOCUMENTACAO.md
│   ├── prints/            Capturas de tela de cada tela
│   └── video/             Vídeo de demonstração da persistência
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/
        └── java/com/example/exibirpatrimoniosactivity/
            ├── MainActivity.kt
            ├── data/
            │   ├── AppDatabase.kt        Banco Room (Singleton)
            │   ├── Patrimonio.kt         Entidade "patrimonios" + enum TipoPatrimonio
            │   ├── PatrimonioDAO.kt      DAO de patrimônios
            │   ├── Local.kt              Entidade "locais"
            │   ├── LocalDAO.kt           DAO de locais
            │   ├── PerfilUsuario.kt      Perfil (somente em memória)
            │   ├── Configuracoes.kt      Configurações (somente em memória)
            │   └── Conquista.kt          Conquista (calculada, não é gravada)
            ├── navigation/
            │   ├── AppNavigation.kt      NavHost, Scaffold, barras e Snackbars
            │   └── Rotas.kt              Rotas centralizadas
            ├── viewmodel/
            │   └── InvScanViewModel.kt   Estado das telas + acesso ao banco
            └── ui/
                ├── components/           Barras, Campos, Cartoes, Imagens
                ├── screens/              As 11 telas
                └── theme/                Color, Dimens, Theme, Type
```

## 6. Estrutura do Room

### Entidades

**`Patrimonio`** (tabela `patrimonios`):

| Campo | Tipo | Por que existe |
|---|---|---|
| `id` | `Int`, `@PrimaryKey(autoGenerate = true)` | Identifica o item e é usado na rota `patrimonio/{id}` |
| `codigo` | `String` | Código da etiqueta do patrimônio (ex.: `2-004321`), usado na busca por código |
| `nome`, `serie`, `descricao` | `String` | Dados exibidos e pesquisados nas listas e no detalhe |
| `tipo` | `TipoPatrimonio` (enum) | Notebook, Computador ou Outro, com ícone próprio na interface |
| `localId` | `Int` | Liga o item ao seu local, sem precisar duplicar os dados do local |
| `prateleira` | `String` | Posição do item dentro do local |
| `conferido` | `Boolean` | Marca o item como conferido no inventário |
| `imagens` | `String` | O banco só guarda texto: as URIs das fotos são unidas por quebra de linha |

**`Local`** (tabela `locais`): `id` (`@PrimaryKey(autoGenerate = true)`), `nome` e `descricao`.

### DAOs

Os dois DAOs têm os quatro métodos básicos, todos `suspend`:

```kotlin
@Dao
interface PatrimonioDAO {
    @Insert suspend fun inserir(patrimonio: Patrimonio)
    @Update suspend fun atualizar(patrimonio: Patrimonio)
    @Delete suspend fun deletar(patrimonio: Patrimonio)
    @Query("SELECT * FROM patrimonios ORDER BY id DESC")
    suspend fun buscarPatrimonios(): List<Patrimonio>
}
```

`LocalDAO` segue o mesmo formato, com `buscarLocais()` (`SELECT * FROM locais`).

### Database

```kotlin
@Database(entities = [Patrimonio::class, Local::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patrimonioDAO(): PatrimonioDAO
    abstract fun localDAO(): LocalDAO
    // Singleton: companion object + @Volatile + synchronized
}
```

O banco se chama `app_database` e é criado uma única vez pelo padrão Singleton (`companion object`, `@Volatile` e `synchronized`).

## 7. Decisões tomadas

### Como estruturamos as queries do DAO

Mantivemos só os métodos básicos (inserir, atualizar, deletar e buscar todos). A pesquisa por nome, código e série, a busca por código, a lista de itens de um local e o progresso de conferência são feitos no `InvScanViewModel`, **filtrando as listas já carregadas em memória**. Como um inventário de sala tem poucos itens, isso evitou reescrever a lógica que já funcionava desde o Trabalho 2.

### Como o ViewModel usa o banco

Foi a decisão central do trabalho. Em vez de trocar as listas das telas, o ViewModel mantém as listas `mutableStateListOf` e as trata como **espelho do banco**:

1. **Ao iniciar**, o ViewModel lê `buscarLocais()` e `buscarPatrimonios()` e preenche as listas.
2. **A cada alteração** (adicionar, editar, conferir, remover), o ViewModel atualiza a lista em memória, para a tela reagir na hora, e grava a mesma mudança no banco.
3. **Toda operação de banco** roda em `CoroutineScope(Dispatchers.IO).launch { ... }` e dentro de `try/catch` com `Log.e(...)`, para não travar a interface.

Isso permitiu que as 11 telas continuassem iguais.

### Outras decisões

- **IDs:** o ViewModel calcula o próximo id (`maior id + 1`) para poder devolvê-lo imediatamente à navegação (a tela de detalhe do item recém-criado abre pelo id). Como o id é conhecido antes da gravação assíncrona, a navegação não depende do banco ter terminado de inserir.
- **Enum no banco:** `TipoPatrimonio` é guardado diretamente na entidade.
- **Imagens:** só as referências (URIs) são guardadas, como texto separado por quebra de linha. Não há upload de arquivos.
- **Exclusão de local:** é bloqueada quando ainda existem patrimônios vinculados a ele, para nenhum item ficar apontando para um local removido.
- **O que não vai para o banco:** perfil, configurações (tema escuro, texto grande, confirmar exclusão) e conquistas continuam em memória. As conquistas são calculadas a partir dos itens e locais salvos.

### Dificuldades: de `mutableStateListOf` para o Room

- **Room 3:** o projeto usa `androidx.room3` (e não o `androidx.room` das aulas), com o compilador via **KSP**. O plugin KSP precisou ser declarado no `build.gradle.kts` da raiz (`apply false`) e aplicado no módulo `app`, antes da linha `ksp(...)`. O projeto não declara `room-ktx`: os DAOs `suspend` funcionam só com o `room3-runtime`.
- **Operações fora da thread principal:** o Room não permite acesso ao banco na thread principal, então toda leitura e escrita foi movida para `Dispatchers.IO`.
- **Dados texto-only:** a lista de imagens teve de virar um único campo `String`, com funções de apoio (`listaImagens` e `paraTexto()`) para converter nos dois sentidos.

## 8. Teste de persistência

O teste de aceitação do trabalho é: adicionar um item, fechar o app completamente e abri-lo de novo, e o item continua lá.

- **Vídeo:** [`video/InvScanExample.mp4`](video/InvScanExample.mp4) mostra o teste completo (seção 2).
- **Resultado esperado e observado:** após cadastrar o local "Pará Lanches" e o item "PC RECEPÇÃO" e fechar o app pela lista de recentes, a tela Início, ao reabrir, mostra 1 item, 1 conferido e 1 local.

## 9. Dependências, configuração e execução

Versões usadas pelo projeto (`gradle/libs.versions.toml` e `app/build.gradle.kts`):

| Item | Versão |
|---|---|
| Android Gradle Plugin | 9.3.2 |
| Kotlin | 2.2.10 |
| KSP (`com.google.devtools.ksp`) | 2.3.4 |
| Compose BOM | 2026.02.01 |
| Room 3 (`room3-runtime` e `room3-compiler`) | 3.0.3 |
| Navigation Compose | 2.9.0 (declarada no módulo) e 2.10.2 (catálogo) |
| Lifecycle ViewModel Compose | 2.9.0 |
| Activity Compose | 1.13.0 |
| Material 3 | 1.3.1 (declarada no módulo) e 1.4.0 (catálogo) |
| `minSdk` / `targetSdk` / `compileSdk` | 24 / 37 / 37 |

Dependências do Room, no `app/build.gradle.kts`:

```kotlin
val room_version = "3.0.3"
implementation("androidx.room3:room3-runtime:$room_version")
ksp("androidx.room3:room3-compiler:$room_version")
```

O plugin KSP é declarado no `build.gradle.kts` da raiz (`id("com.google.devtools.ksp") version "2.3.4" apply false`) e aplicado em `app/build.gradle.kts`.

### Como executar

1. Clone o repositório e abra a pasta raiz no Android Studio.
2. Execute **Sync Project with Gradle Files**.
3. Inicie um emulador ou conecte um dispositivo Android com depuração USB.
4. Selecione a configuração `app` e clique em **Run**.

## 10. Limitações e próximos passos

- Leitura de código de barras/QR pela câmera ainda não foi implementada.
- Perfil, configurações e conquistas ainda não são persistidos.
- As telas de detalhe obtêm o item pelo `id` na lista que o ViewModel carregou do banco, e não por uma consulta própria no DAO (por exemplo, `buscarPorId`).
- Apenas as URIs das imagens são guardadas; é preciso avaliar a validade das permissões de acesso a elas entre execuções.
- O banco está na `version = 1` e não possui migrações.
