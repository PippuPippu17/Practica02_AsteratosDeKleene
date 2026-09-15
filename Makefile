# =============================================================================
#  Práctica 02: Sistema de Archivos vs. Bases de Datos
#  Equipo: Asteratos de Kleene
#
#  Objetivos disponibles:
#    make            Compila el proyecto (equivale a 'make compile').
#    make run        Compila y ejecuta la aplicación.
#    make doc        Genera la documentación Javadoc en SRC/Doc/.
#    make zip        Arma el archivo de entrega con la estructura solicitada.
#    make clean      Borra los .class y la documentación generada.
#    make help       Muestra esta ayuda.
# =============================================================================

# ---------------------------------------------------------------- Variables --
EQUIPO   := AsteratosDeKleene
SRC      := SRC
DOCS     := Docs
DOC_DIR  := $(SRC)/Doc
MAIN     := Main
ZIP      := Practica02_$(EQUIPO).zip

JAVAC        := javac
JAVA         := java
JAVADOC      := javadoc
JAVA_RELEASE := 21

# Los mensajes de la aplicación llevan acentos, así que fijamos el encoding
# de forma explícita para no depender de la configuración de cada máquina.
JAVACFLAGS   := -encoding UTF-8 --release $(JAVA_RELEASE) -Xlint:-serial
JAVADOCFLAGS := -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 \
                -private -quiet -Xdoclint:none

# Fuentes y clases. Los .class se dejan junto a los .java porque así lo pide
# el diagrama de entregables de la práctica.
FUENTES := $(wildcard $(SRC)/*.java) $(wildcard $(SRC)/exceptions/*.java)
CLASES  := $(FUENTES:.java=.class)

# Centinela que evita recompilar cuando ningún fuente ha cambiado.
STAMP := $(SRC)/.build

# ------------------------------------------------------------------ Objetivos --
.PHONY: all compile run doc zip clean help
.DEFAULT_GOAL := compile

all: compile doc

## compile: compila todas las clases dejando los .class dentro de SRC/.
compile: $(STAMP)

$(STAMP): $(FUENTES)
	@echo ">> Compilando $(words $(FUENTES)) archivos fuente..."
	@$(JAVAC) $(JAVACFLAGS) -d $(SRC) $(FUENTES)
	@touch $@
	@echo ">> Compilación terminada."

## run: ejecuta la aplicación desde la raíz del repositorio.
# Se ejecuta desde aquí y no desde SRC/ porque Main resuelve las rutas de los
# archivos CSV como './SRC/archivo.csv'.
run: compile
	@echo ">> Ejecutando $(MAIN)..."
	@$(JAVA) -cp $(SRC) $(MAIN)

## doc: genera la documentación Javadoc en SRC/Doc/.
doc: $(DOC_DIR)/index.html

$(DOC_DIR)/index.html: $(FUENTES)
	@echo ">> Generando documentación en $(DOC_DIR)/..."
	@mkdir -p $(DOC_DIR)
	@$(JAVADOC) $(JAVADOCFLAGS) -d $(DOC_DIR) $(FUENTES)
	@echo ">> Documentación generada."

## zip: arma el entregable con la estructura que pide la práctica.
zip: compile doc
	@echo ">> Empaquetando $(ZIP)..."
	@rm -f $(ZIP)
	@zip -qr $(ZIP) $(SRC) $(wildcard $(DOCS)) $(wildcard README_*.pdf) \
		-x '*.DS_Store' '*/.git/*' '$(SRC)/.build'
	@echo ">> Listo: $(ZIP)"
	@unzip -l $(ZIP) | tail -n +2

## clean: borra los .class y la documentación generada.
clean:
	@echo ">> Limpiando archivos generados..."
	@rm -f $(SRC)/*.class $(SRC)/exceptions/*.class $(STAMP)
	@rm -rf $(DOC_DIR)
	@rm -f $(ZIP)
	@echo ">> Listo."

## help: muestra los objetivos disponibles.
help:
	@echo "Objetivos disponibles:"
	@echo "  make compile   Compila el proyecto (objetivo por omisión)."
	@echo "  make run       Compila y ejecuta la aplicación."
	@echo "  make doc       Genera el Javadoc en $(DOC_DIR)/."
	@echo "  make all       Compila y genera la documentación."
	@echo "  make zip       Arma $(ZIP) con la estructura de entrega."
	@echo "  make clean     Borra .class, documentación y el zip."
