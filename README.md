## Cache Simulator

Este repositório contém um simulador de cache parametrizável por linha de comando <br>

São considerados para a simulação endereços de 32 bits endereçados a byte <br>

A entrada é um arquivo binário que contém valores inteiros sem sinal de 4 bytes (a.k.a. 32 bits) que são os endereços requisitados à memória cache <br>

Os valores são lidos do arquivo e é simulado acesso aos endereços de memória e no final da simulação é retornado um relatório com informações de Hits, Misses, Tipos dos Misses <br>

Foi feito como o trabalho da cadeira de Arquitetura e Organização de Computadores II <br>

## Funcionalidades

O usuário define as configurações da cache a ser simulada. Pode escolher o número de conjuntos, tamanho do bloco, nível de associatividade e a política de substituição <br>

Existem 5 políticas de substituição implementadas, são elas <br>
`Random`, <br>
`Least Recently Used`, <br> 
`First in Firt out`, <br> 
`Least Frequently Used`, <br>
`Most Recently Used ` <br>

É possivel obter a saída em 2 tipos diferentes de formatação (com e sem labels) <br>

## Linha de comando

A forma de utilizar o simulador é a seguinte <br>
`java -jar cache_simulator.jar <nsets> <bsize> <assoc> <replacement> <formato_saida> <arquivo_entrada>` <br>

`nsets` -> é o número de conjuntos da cache a ser simulada <br>
`bsize` -> é o número de bytes presentes em cada bloco <br>
`assoc` -> é a associatividade a ser utilizada <br>
`replacement` -> é a política de substituição a ser utilizada, pode ser: <br>
**L** -> LRU <br>
**F** -> FiFo <br>
**R** -> Random <br>
**MRU** -> MRU <br>
**LFU** -> LFU <br>
`formato_saida`-> é a forma que os dados serão exibidos, quando igual a 1 a saída tem o seguinte formato: <br> 
`total_acessos hit_hate miss_rate percentual_misses_compulsorios percentual_misses_capacidade percentual_misses_conflito` <br>

quando diferente de 1, tem o seguinte formato:

Total de acessos: `total_acessos`<br>
Total Hits: `total_hits`<br>
Total Misses: `total_misses`<br>
Total Misses Compulsórios: `total_misses_compulsorios`<br>
Total Misses Capacidade: `total_misses_capacidade`<br>
Total Misses Conflito: `total_misses_conflito`<br>
Taxa de Hit: `hit_rate`<br>
Taxa de Misses: `miss_rate`<br>
Taxa Misses Compulsórios: `percentual_misses_compulsorios`<br>
Taxa Misses Capacidade: `percentual_misses_capacidade`<br>
Taxa Misses Conflito: `percentual_misses_conflito`<br>
    
`arquivo_entrada` -> é o caminho para um arquivo binário que contém os endereços que serão requisitados na simulação. Cada endereço deve ser representado como inteiro de 32 bits sem sinal. <br>

Um exemplo: <br>
`java -jar .\cache_simulator.jar 512 8 2 L 0 ./src/main/data/vortex.in.sem.persons.bin` <br>
A linha de comando acima simula uma cache de tamanho total 8KB, com 512 conjuntos, cada conjunto possui duas linhas (associatividade = 2), cada linha possui 8 bytes de informação, a política de substituição utilizada é a LRU e o formato de saída é livre e o arquivo de entrada é ./src/main/data/vortex.in.sem.persons.bin <br>

### Observações
Os valores de `nsets`, `bsize` e `assoc` devem ser potências de 2. <br>
Existem arquivos com alguns endereços que podem ser utilizados como teste, dentro da pasta `./src/main/data/`, cada arquivo possui uma versão em `.txt` e a versão `.bin` <br>
O tempo de simulação para caches muito grandes e com associatividade muito alta ou totalmente associativas para as políticas LRU e LFU pode ser alto.