# Mutant

## Instalação

### Requisitos

* Java 10

### Executando localmente

O comando abaixo iniciará o serviço expondo na porta 8080 os _endpoints_ `/mutant` e `/stats`. 

```sh
$ ./mvnw clean spring-boot:run
```

## Usando a API

A api está disponibilizada no endereço https://fpereira-mutant.herokuapp.com.

### Com curl

```sh
# Exemplo de mutante:
$ curl -X POST -v -d '{"dna":["AAAAGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}' \
       -H "Content-type: application/json" https://fpereira-mutant.herokuapp.com/mutant
```

```sh
# Exemplo de humano:
$ curl -X POST -v -d '{"dna":["ATGCGA","CGATGC","TTATGT","AGAAGG","CGCCTA","TCACTG"]}' \
       -H "Content-type: application/json" https://fpereira-mutant.herokuapp.com/mutant
```

```sh
# Exemplo de cálculo das estatísticas:
$ curl https://fpereira-mutant.herokuapp.com/stats
```
