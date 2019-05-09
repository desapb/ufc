# Exercício - Algoritmo Eleições

## Configuração

- Java 1.8
- IDE Eclipse

## Execução

- A classe main da aplicação é o P.java.
- Todo processo possuirá um PID definido pela JVM.
- Ao executar a classe o construtor irá registrar o processo no RMI Registry, sendo o PID sua chave.
- A porta disponibilizada será gerada dinamicamente, tendo como intervalo de 2000 a 2999.
- Uma Thread será iniciada e uma nova eleição ocorrerá de tempos em tempos.
- O tempo para a eleição é gerado de forma randomica e poderá ocorrer em um intervalo entre 30 e 60 segundos.
- Ao iniciar uma eleição será verificado se existem processos com PID maior.
- Não havendo, o Processo se autodeclara líder e notifica todos os outros processos.
- Caso haja um processo com PID maior o Processo retira-se das Eleções e aguarda que o maior Processo se declare lider e notifique os demais.
- A aplicação também verifica se o Líder está ativo. Caso não esteja, ocorre a exclusão do Processo no RMI Registry e executa uma nova eleição.


