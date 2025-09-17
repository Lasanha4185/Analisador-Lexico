# Analisador Léxico em Java

Este projeto é um analisador léxico (scanner) desenvolvido em Java como parte do estudo de compiladores. O programa lê um arquivo de código-fonte com uma sintaxe customizada e o converte em uma sequência de **tokens**, que representa a primeira fase fundamental de um compilador.

O analisador é implementado como uma **Máquina de Estados Finitos (Autômato Finito Determinístico)**, lendo o código-fonte caractere por caractere e transitando entre diferentes estados para reconhecer os padrões que formam os tokens.

## ✨ Funcionalidades

O analisador léxico é capaz de reconhecer e tokenizar os seguintes elementos:

-   **Palavras Reservadas:** `if`, `else`, `int`, `float`, `print`.
-   **Identificadores:** Nomes de variáveis que começam com uma letra e podem ser seguidos por letras ou números (ex: `soma`, `var1`, `resultado`).
-   **Números:**
    -   Inteiros (`123`, `42`).
    -   Decimais (`123.456`, `.456`).
    -   Validação de formato (rejeita números como `12.`).
-   **Operadores:**
    -   **Matemáticos:** `+`, `-`, `*`, `/`.
    -   **Relacionais:** `>`, `>=`, `<`, `<=`, `!=`, `==`.
    -   **Atribuição:** `=`.
-   **Símbolos e Pontuação:**
    -   Parênteses: `(`, `)`.
-   **Comentários (Ignorados):**
    -   **Linha única:** Começando com `#` até o final da linha.
    -   **Múltiplas linhas:** Delimitados por `/*` e `*/`.
-   **Tratamento de Erros:**
    -   O analisador para a execução e reporta um erro léxico detalhado, incluindo a **linha e a coluna** exatas onde o erro ocorreu.
    -   Detecta símbolos não reconhecidos (ex: `@`, `ç`, `~`).
    -   Detecta números malformados e comentários de múltiplas linhas não fechados.

## 🚀 Como Usar

### Pré-requisitos
-   [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (versão 11 ou superior).

### Estrutura de Arquivos
Para que os comandos funcionem corretamente, organize seu projeto da seguinte forma:

```
/seu-projeto
├── src/
│   ├── lexical/
│   │   └── Scanner.java
│   ├── util/
│   │   ├── Token.java
│   │   └── TokenType.java
│   └── Main.java
│
└── codigo_exemplo.mc
```

### Compilação e Execução

1.  **Abra o terminal** na pasta raiz do seu projeto (`/seu-projeto`).

2.  **Compile os arquivos `.java`:**
    ```bash
    javac -d . src/util/TokenType.java src/util/Token.java src/lexical/Scanner.java src/Main.java
    ```
    *O `-d .` compila os arquivos e organiza os `.class` dentro das pastas de pacotes `lexical` e `util`.*

3.  **Execute o analisador**, passando um arquivo de código como argumento:
    ```bash
    java Main codigo_exemplo.mc
    ```
