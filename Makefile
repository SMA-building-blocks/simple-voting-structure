QUORUM ?= 3
PATH_PROJECT_JAR = target/simple_voting_structure-0.0.1-SNAPSHOT.jar
PROJECT_GROUP    = simple_voting_structure
JADE_AGENTS      = simple_voting_structure:$(PROJECT_GROUP).App($(QUORUM));
JADE_FLAGS 		 = -gui -agents "$(JADE_AGENTS)"

.PHONY:
	clean
	build-and-run

build-and-run:
	@echo "Building and executing the project..."
	make build run

build:
	@echo "Building the project"
	mvn clean install

run:
	@echo "Executing the project with the last build"
	java -cp $(PATH_PROJECT_JAR) jade.Boot $(JADE_FLAGS)

clean:
	@echo "Removing the existing build and auto-generated files"
	mvn clean
	rm -f APDescription.txt; rm -f MTPs-Main-Container.txt

help:
	@echo "This program has the following commands to execute:"
	@echo "	$$ make build "
	@echo "	  It's responsible for Building the Jar of this Building Block"
	@echo "	$$ make run"
	@echo "	  It's responsible for executing the program with graphic interface of JADE"
	@echo "	$$ make clean"
	@echo "	  It's responsible for cleaning the generated files"
	@echo "	$$ make build-and-run"
	@echo "	  Executes both commands build and run in this order"
	@echo "	$$ make help"
	@echo "	  Shows this help resume"
	@echo ""
	@echo "If wanted it's possible to change the quantity of the voting agents by adding the variable QUORUM to the command, as seen in the next line"
	@echo "	$$ make build-and-run QUORUM=<Quantity of voters>"