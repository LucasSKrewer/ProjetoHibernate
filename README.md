# ProjetoHibernate

Projeto Java com Hibernate e interface Swing — cadastro de Categoria, Fornecedor e Produto.

## Tecnologias

- Java
- Hibernate (JPA) — config em `src/META-INF/persistence.xml`
- Swing (formulários em `src/br/view/`)
- Ant (NetBeans) — `build.xml`

## Estrutura

```
src/
  br/connection/ConnectionFactory.java
  br/model/bean/        — Categoria, Fornecedor, Produto
  br/model/dao/         — CategoriaDAO, FornecedorDAO, ProdutoDAO
  br/view/              — frmCategoria, frmFornecedor, frmProduto
lib/                    — dependências JAR
```

## Status

Projeto acadêmico de 2020 (NetBeans/Ant). Mantido como arquivo histórico — não recebe manutenção nem aceita PRs.

## Licença

[MIT](LICENSE).
