QUORUM ?= 2
PATH_PROJECT_JAR = target/simple_voting_structure-0.0.1-SNAPSHOT.jar
PROJECT_GROUP    = simple_voting_structure
JADE_AGENTS      = simple_voting_structure:$(PROJECT_GROUP).App($(QUORUM));
JADE_FLAGS 		 = -gui -agents "$(JADE_AGENTS)"

.PHONY:
	clean
	build-and-run

build-and-run:
	@echo "Gerando a build e executando o projeto"
	make build run

build:
	@echo "Gerando a build do projeto"
	mvn clean install

run:
	@echo "Executando o projeto com a última build criada"
	java -cp $(PATH_PROJECT_JAR) jade.Boot $(JADE_FLAGS)

clean:
	@echo "Removendo a build do projeto"
	mvn clean
	rm -f APDescription.txt; rm -f MTPs-Main-Container.txt

help:
	@echo "Para mudar a quantidade de agentes votantes utilize o seguinte comando:"
	@echo "make build-and-run QUORUM=<QUANTIDADE DE VOTANTES>"