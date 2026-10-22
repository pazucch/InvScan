# InvScan — Documentação do processo e das decisões

> **Grupo:** Mateus F., Ellyson V., André G.  
> **Disciplina:** Desenvolvimento de Aplicativos Móveis

## 1. Visão geral

O **InvScan** é um aplicativo Android para inventário de patrimônio, desenvolvido em Kotlin com Jetpack Compose. Ele permite cadastrar itens, organizar os itens por local, marcar patrimônios conferidos, pesquisar por nome/código/série e consultar os detalhes de patrimônios e locais.

**Mudança importante em relação à documentação anterior:** patrimônios e locais agora são persistidos em um banco de dados local usando **Room 3**. Perfil e configurações ainda ficam apenas no estado em memória. Portanto, não é correto dizer que todos os dados se perdem ao fechar o aplicativo.

## 2. Evolução do projeto

No Trabalho 1, o app tinha três telas estilizadas em um único arquivo `MainActivity.kt`, com dados fixos e botões sem ações implementadas. A versão atual separa a navegação, os modelos de dados, o ViewModel, os componentes e as telas.

| Aspecto | Trabalho 1 | Versão atual |
|---|---|---|
| Telas | 3 | 11 telas navegáveis |
| Navegação | Sem navegação funcional | `NavHost`, objeto `Rotas` e barras compartilhadas |
| Patrimônios | Dados fixos | Cadastro, edição, exclusão, pesquisa e conferência |
| Locais | Sem gerenciamento completo | Cadastro, edição, exclusão condicionada e detalhes |
| Persistência | Dados fixos no código | Room 3 para patrimônios e locais |
| Organização | Um arquivo principal grande | Pastas por responsabilidade |
| Aparência | Barras inconsistentes | Tema compartilhado, modo escuro e texto ampliado |

## 3. Funcionalidades e telas

| # | Tela | Responsabilidade |
|---|---|---|
| 1 | **Início** | Resumo do inventário, pesquisa e atalhos para locais e busca por código |
| 2 | **Menu** | Acesso às funcionalidades |
| 3 | **Patrimônios** | Lista, pesquisa, marcação de conferência, exclusão e criação |
| 4 | **Detalhe do patrimônio** | Exibe e permite editar os dados do item, seu local e suas imagens |
| 5 | **Criar item** | Formulário de cadastro de patrimônio |
| 6 | **Locais** | Lista e permite cadastrar, editar e remover locais |
| 7 | **Detalhe do local** | Exibe/edita o local, lista os itens vinculados e mostra o progresso de conferência |
| 8 | **Escanear código** | Localiza um patrimônio pelo código digitado |
| 9 | **Usuário** | Perfil e conquistas baseadas no uso do app |
| 10 | **Configurações** | Tema escuro, texto grande e acesso ao suporte |
| 11 | **Suporte** | Perguntas frequentes e formulário de contato |

A leitura de código por câmera ainda não foi implementada: a tela chamada “Escanear” pesquisa o código que a pessoa digita. A seleção de imagens utiliza o Photo Picker do Android.

## 4. Navegação

As rotas ficam centralizadas em `navigation/Rotas.kt` e o `NavHost` fica em `navigation/AppNavigation.kt`. O `Scaffold` compartilhado controla a barra superior, a barra inferior e as mensagens de `Snackbar`.

Fluxos principais:

- Início → patrimônio → detalhe do patrimônio → detalhe do local.
- Patrimônios → criar item → detalhe do novo patrimônio.
- Início → Locais → detalhe do local → patrimônio relacionado.
- Início → busca por código → detalhe do patrimônio encontrado.
- Menu → funcionalidades; Configurações → Suporte.

As telas recebem o `InvScanViewModel` e callbacks de navegação, em vez de controlar diretamente o `NavController`.

## 5. Persistência local com Room 3

A camada de dados foi ampliada para que os patrimônios e locais não dependam apenas de listas temporárias.

### Arquivos principais

| Arquivo | Responsabilidade |
|---|---|
| `data/AppDatabase.kt` | Define o banco `app_database` e fornece acesso aos DAOs |
| `data/Patrimonio.kt` | Entidade `patrimonios`, com código, nome, série, descrição, tipo, local, prateleira, estado de conferência e referências de imagem |
| `data/Local.kt` | Entidade `locais`, com nome e descrição |
| `data/PatrimonioDAO.kt` | Operações de inserir, atualizar, excluir e consultar patrimônios |
| `data/LocalDAO.kt` | Operações de inserir, atualizar, excluir e consultar locais |
| `viewmodel/InvScanViewModel.kt` | Carrega os dados do banco e sincroniza as alterações da interface com os DAOs |

O banco usa duas entidades: `Patrimonio` e `Local`. O vínculo é representado por `localId` em cada patrimônio. O ViewModel carrega os registros ao ser inicializado e executa as operações de banco em corrotinas no dispatcher de I/O.

### O que é persistido?

- **Persistido em Room:** patrimônios e locais.
- **Ainda não persistido:** perfil do usuário e configurações, incluindo tema escuro e texto grande.
- **Imagens:** a entidade armazena referências/URIs como texto, separadas por quebra de linha. Não há upload remoto de arquivos.

A exclusão de um local é bloqueada quando ainda existem patrimônios vinculados a ele, evitando deixar itens apontando para um local removido.

## 6. Dependências e configuração do ambiente

O ZIP disponibilizado para esta documentação contém os arquivos Kotlin e os Markdown, mas **não contém** `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, `AndroidManifest.xml` ou os arquivos de recursos. Por isso, não é possível confirmar as versões exatas já utilizadas pelo projeto. As dependências abaixo devem ser comparadas com a configuração do projeto Android Studio original; evite declarar versões conflitantes ou duplicar plugins já configurados.

### Bibliotecas usadas pelo código

| Dependência | Para que é usada |
|---|---|
| AndroidX Activity Compose (`androidx.activity:activity-compose`) | `ComponentActivity`, `setContent`, integração do Compose e seleção de imagens |
| Jetpack Compose UI/Foundation/Runtime | Layouts, estado e componentes de interface |
| Material 3 (`androidx.compose.material3:material3`) | `Scaffold`, campos, botões, diálogos, barras e tema |
| Material Icons Extended (`androidx.compose.material:material-icons-extended`) | Ícones usados pelas telas |
| Navigation Compose (`androidx.navigation:navigation-compose`) | Rotas e navegação entre telas |
| Lifecycle ViewModel Compose (`androidx.lifecycle:lifecycle-viewmodel-compose`) | Obter o ViewModel dentro da composição |
| Room 3 runtime (`androidx.room3:room3-runtime`) | Banco de dados local |
| Room 3 compiler (`androidx.room3:room3-compiler`) | Geração de código para banco, entidades e DAOs |
| Kotlin coroutines | Execução assíncrona das operações de banco |

### Exemplo de dependências no `app/build.gradle.kts`

Este exemplo é um ponto de partida, não uma cópia garantida da configuração atual. O projeto deve usar versões compatíveis com seu Android Gradle Plugin, Kotlin, Compose e KSP. Se o projeto utiliza o Compose BOM, mantenha o BOM existente e não fixe versões individuais para as bibliotecas Compose sem necessidade.

```kotlin
dependencies {
    // Compose BOM deve estar configurado no projeto.
    implementation("androidx.activity:activity-compose:<versao-compativel>")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.navigation:navigation-compose:<versao-compativel>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:<versao-compativel>")

    val roomVersion = "3.0.3"
    implementation("androidx.room3:room3-runtime:$roomVersion")
    ksp("androidx.room3:room3-compiler:$roomVersion")
}
```

O Room 3 usa o pacote `androidx.room3`, exige processamento por **KSP** e oferece APIs de DAO baseadas em corrotinas. Não substitua essas dependências por `androidx.room:room-runtime`/`kapt` sem migrar o código, pois são configurações de Room 2.x.

O plugin KSP precisa estar configurado com uma versão compatível com a versão do Kotlin. Se o projeto ainda não o declarar, adicione-o ao bloco `plugins` do módulo ou ao gerenciamento central de plugins, conforme a organização do projeto:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "<versao-ksp-compativel-com-kotlin>"
}
```

### Como executar

1. Abra o projeto Android Studio completo, não apenas a pasta de arquivos Kotlin.
2. Confira o plugin KSP e as dependências no Gradle.
3. Execute **Sync Project with Gradle Files**.
4. Corrija conflitos de versões ou erros de compilação antes de executar.
5. Inicie um emulador ou conecte um dispositivo Android com depuração USB habilitada.
6. Selecione a configuração do app e clique em **Run**.

## 7. Organização do código

```text
data/             Entidades Room, banco, DAOs e modelos
navigation/       Rotas e NavHost
viewmodel/        Estado compartilhado e operações de persistência
ui/components/    Barras, campos, cartões e imagens reutilizáveis
ui/screens/       As 11 telas
ui/theme/         Cores, tipografia, dimensões e tema
```

- **Rotas centralizadas:** `Rotas.kt` evita espalhar strings de rota pelo projeto.
- **Navegação centralizada:** `AppNavigation.kt` mantém o `NavHost`, as barras e os `Snackbar`s num único lugar.
- **ViewModel compartilhado:** as telas usam o mesmo `InvScanViewModel` para consultar e alterar patrimônios e locais.
- **Componentes reutilizáveis:** componentes visuais compartilhados reduzem duplicação e mantêm consistência.
- **Tema único:** as cores usam `MaterialTheme.colorScheme`, permitindo tema claro/escuro; a opção de texto grande altera a escala tipográfica usada pelo tema.

## 8. Complexidade da tela de detalhes

A tela de detalhe do patrimônio oferece edição no próprio local, relaciona o patrimônio a um local e permite navegar para o detalhe desse local. O detalhe do local reúne os itens associados e calcula o progresso de conferência a partir da quantidade de itens conferidos em relação ao total.

Essas relações permitem responder perguntas comuns de inventário, como onde o item está e quais outros patrimônios estão no mesmo local.

## 9. Limitações e próximos passos

- Implementar leitura de código de barras/QR pela câmera.
- Persistir perfil e configurações, caso seja necessário mantê-los entre execuções.
- Avaliar a persistência e as permissões de acesso às imagens selecionadas.
- Incluir os arquivos Gradle, manifesto, recursos e capturas de tela no repositório completo para que a configuração e a documentação possam ser reproduzidas sem depender do ambiente local.
