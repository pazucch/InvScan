# InvScan

App Android (Kotlin + Jetpack Compose) de inventário de patrimônio. Trabalho 2 (MAF) da disciplina de Desenvolvimento de Aplicativos Móveis.

**Integrantes:** Mateus P., Ellyson V., André G.

📄 **Documentação do processo e das decisões (com prints):** [`docs/DOCUMENTACAO.md`](docs/DOCUMENTACAO.md)

## O que o app faz
- Lista, pesquisa, cadastra, edita e remove **patrimônios**
- Marca itens como **conferidos** durante o inventário
- Organiza os itens em **locais** (outra lista, com detalhe próprio)
- Busca um item pelo código da etiqueta
- Perfil do usuário com conquistas, configurações (tema escuro, texto grande) e suporte

## Como rodar
1. Clone o repositório e abra no **Android Studio**.
2. Aguarde o *Gradle Sync*. No módulo `app`, as dependências abaixo devem estar presentes:
   ```kotlin
   implementation("androidx.navigation:navigation-compose:2.9.0")      // ou a mais recente
   implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")
   implementation("androidx.compose.material:material-icons-extended")
   ```
3. Inicie um emulador (espere aparecer a tela inicial do Android) ou conecte um celular com depuração USB e clique em **Run**.

## Telas e rotas
| Tela | Rota |
|---|---|
| Início | `inicio` |
| Menu (grade de funcionalidades) | `menu` |
| Patrimônios (lista 1) | `patrimonios` |
| Detalhe do patrimônio | `patrimonio/{id}` |
| Criar item | `criar_item` |
| Locais (lista 2) | `locais` |
| Detalhe do local | `local/{id}` |
| Escanear código | `escanear` |
| Usuário | `usuario` |
| Configurações | `configuracoes` |
| Suporte | `suporte` |

## Estrutura do código
```
navigation/     Rotas e NavHost (AppNavigation.kt)
viewmodel/      InvScanViewModel (estado em memória)
data/           data classes e dados de exemplo
ui/components/  componentes reutilizáveis
ui/screens/     as 11 telas
ui/theme/       cores, tipografia, formas e tema claro/escuro
```

> Os dados ficam só na memória: ao fechar o app, o que foi adicionado se perde.
