JFLAGS = -g -classpath $(CLASSPATH)
JC = javac
CLASSPATH = ./src/
JAR_DIR = ./jar/
NATIVE_BUILDS_DIR = ./builds/
MANIFEST = $(CLASSPATH)MANIFEST.MF
MAIN_CLASS = Minesweeper
NAME = Minesweeper

CLASSES = \
	Minesweeper.java \
	MinesweeperGUI.java \
	MyButton.java

.PHONY: classes manifest jar run run-jar clean clean-all clean-manifest clean-jar
default: classes

classes: $(addprefix $(CLASSPATH),$(CLASSES:.java=.class))

%.class: %.java
	@echo Compiling java class: $<;
	@$(JC) $(JFLAGS) $?;

$(MANIFEST):
	@echo Creating $(MANIFEST);
	@touch $(MANIFEST);
	@echo Main-Class: $(MAIN_CLASS) > $(MANIFEST);

manifest: $(MANIFEST)

$(JAR_DIR)$(NAME).jar: classes manifest
	@echo Creating $(NAME).jar;
	@cd $(CLASSPATH); jar cvfm $(NAME).jar $(notdir $(MANIFEST)) $(MAIN_CLASS).class *.{class,png,jpg};
	
	@if [ ! -d $(JAR_DIR) ]; then\
		echo Making directory $(JAR_DIR);\
		mkdir $(JAR_DIR);\
	fi
	
	@echo Moving $(NAME).jar to $(JAR_DIR)
	@mv $(CLASSPATH)$(NAME).jar $(JAR_DIR)

jar: $(JAR_DIR)$(NAME).jar

package-native: jar
	@if [ ! -d $(NATIVE_BUILDS_DIR) ]; then\
		@echo Creating native builds dir $(NATIVE_BUILDS_DIR);\
		mkdir $(NATIVE_BUILDS_DIR);\
	fi
	@echo Creating native package
	@jpackage \
		--input . \
		--main-class $(MAIN_CLASS) \
		--main-jar $(JAR_DIR)$(NAME).jar \
		--name $(NAME) \
		--icon "./minesweeper-title.png" \
		--description "Minecraft themed minesweeper clone" \
		--about-url https://github.com/AceFire6/Minesweeper

run: classes
	@echo Running $(MAIN_CLASS)...;
	@java $(MAIN_CLASS)
	@echo Closing...;

run-jar:
	@if [ -f $(JAR_DIR)$(NAME).jar ]; then\
		java -jar $(JAR_DIR)$(NAME).jar;\
	else\
		echo You must run make jar first.;\
	fi

clean-all: clean clean-manifest clean-jar

clean:
	@echo Cleaning class files.
	@$(RM) $(CLASSPATH)*.class

clean-manifest:
	@if [ -f $(MANIFEST) ]; then\
		echo Removing created manifest file.;\
		rm $(MANIFEST);\
	fi

clean-jar:
	@if [ -d $(JAR_DIR) ]; then\
		echo Remove $(JAR_DIR);\
		rm -rf $(JAR_DIR);\
	fi