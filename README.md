# Estrutura de Votação Simples

## Autores

| **Identificação** | **Nome** | **Formação** |
| :-: | :-: | :-: |
| <img src="https://github.com/dartmol203.png" width=100 height=100 alt="André Corrêa da Silva" class="img-thumbnail image"> | André Corrêa da Silva | Graduando em Engenharia de Software (UnB) |
| <img src="https://github.com/gabrielm2q.png" width=100 height=100 alt="Gabriel Mariano da Silva" class="img-thumbnail image"> | Gabriel Mariano da Silva | Graduando em Engenharia de Software (UnB) |

*Tabela 1: Identificação dos Autores*

## Descrição

O *building block* contido neste repositório tem por objetivo a implementação de uma Estrutura de Votação Simples, onde é proposto que o agente mediador escolha um valor numérico inteiro dado um intervalo pré-definido e que os agentes votantes busquem, dentro deste intervalo, escolher um número. O agente votante cuja escolha numérica mais se aproximar do valor definido pelo mediador é estabelecido como o vencedor. Há também a possibilidade de empate.

<!-- ### Projeto em Execução

<img src="" alt="Descrição do Print">

*Figura 1: Print do Projeto em Execução* -->

## Requisitos Técnicos

1. **Criação de votação:** mediante solicitação de um agente votante, é esperado que o agente mediador seja capaz de criar uma votação com um código de identificação único no contexto de execução e um intervalo de valores numéricos definido;
2. **Compartilhamento de votação:** mediante recebimento do código de votação do mediador, é esperado que o agente votante que solicitou a votação compartilhe esse código com os outros agentes votantes, os quais devem se registrar no DF com o devido código para a efetiva participação na votação;
3. **Solicitação dos votos:** após a visualização no DF dos agentes participantes da votação estabelecida, o agente mediador deve solicitar aos mesmos seus votos;
4. **Fornecimento dos votos:** os agentes votantes devem, mediante requisição do agente, enviar seus votos ao agente mediador, os quais devem ser compostos por
um número dentro do intervalo proposto;
5. **Contabilização do resultado:** após o recebimento dos votos de todos os agentes votantes, o agente mediador deve ser capaz de avaliar os votos recebidos e determinar o vencedor da votação ou o empate da mesma;
6. **Informação do resultado:** após a contabilização do resultado, o agente mediador deve informar a todos os agentes votantes participantes o resultado da votação;
7. **Deleção da votação:** após a realização da votação, o agente mediador se responsabilizará por remover do DF os nomes dos agentes votantes da votação realizada.

## Requisitos para Execução

Para a efetiva execução do *building block* disposto no repositório, se faz necessária a instalação e configuração do *software* *Maven* em sua máquina. Para tal, basta seguir as instruções de instalação dispostas na [**documentação do *Maven***](https://maven.apache.org/install.html). Para o desenvolvimento do *building block*, foi utilizado o *Maven* na versão **3.8.7**. Além disso, todas as instruções de execução consideram o uso de sistemas operacionais baseados em *Linux*.

## Como Executar?

Para a execução do *building block*, é possível utilizar-se do *Makefile* adicionado ao repositório ao seguir os seguintes passos:

- Primeiramente, clone o repositório em sua máquina:

```bash
git clone https://github.com/SMA-building-blocks/simple-voting-structure.git
```

- Em seguida, vá para a pasta do repositório:

```bash
cd simple-voting-structure
```

- Para realizar a *build* do projeto e executá-lo em seguida, execute o seguinte comando:

```bash
make build-and-run
```

> 🚨 **IMPORTANTE:** Ao executar o projeto, primeiro será realizada a criação de todos os agentes participantes. Logo após, para a efetiva realização do propósito desejado pelo *building block*, é necessário pressionar **ENTER** no terminal para a continuidade da execução do código. Esta decisão foi tomada em prol de uma facilitação do uso do *sniffer* para a visualização da comunicação entre os agentes participantes.

- É possível realizar apenas a *build* do projeto com o seguinte comando:

```bash
make build
```

- Similarmente, é possível rodar o projeto após a geração de sua build com o seguinte comando:

```bash
make run
```

- É possível alterar a quantidade de agentes participantes ao passar a variável **QUORUM** seguida do número desejado, como pode ser visto abaixo (onde N representa o número desejado de agentes): 

```bash
make build-and-run QUORUM=N"
```

- Por fim, para apagar os arquivos derivados da *build* do projeto, execute o seguinte comando:

```bash
make clean
```

- Para ter acesso a uma série de informações úteis para a execução do building block, basta executar o seguinte comando:

```bash
make help
```

## Fontes e Referências

[*Jade Project*](https://jade-project.gitlab.io/). <br />
[*Maven*](https://maven.apache.org/).
