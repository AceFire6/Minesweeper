JFLAGS = -g -classpath $(CLASSPATH)
JC = javac
CLASSPATH = ./src/
JAR_DIR = ./jar/
NAME = Minesweeper
APP_VERSION ?= 1.0
JARFILE_NAME = $(NAME)-$(APP_VERSION).jar
JARFILE_PATH = $(JAR_DIR)$(JARFILE_NAME)
# Default to 1.0 if APP_VERSION isn't set
NATIVE_BUILDS_DIR = ./builds/
MANIFEST = $(CLASSPATH)MANIFEST.MF
MAIN_CLASS = Minesweeper

CLASSES = \
	Minesweeper.java \
	MinesweeperGUI.java \
	MyButton.java

PACKAGE_COMMAND = jpackage \
		--input . \
		--main-class $(MAIN_CLASS) \
		--main-jar $(JARFILE_PATH) \
		--dest $(NATIVE_BUILDS_DIR) \
		--app-version $(APP_VERSION) \
		--name $(NAME) \
		--description "Minecraft themed minesweeper clone" \
		--about-url https://github.com/AceFire6/Minesweeper

.PHONY: classes manifest jar run run-jar clean clean-all clean-manifest clean-jar \
	native-builds-dir prep-package-native package-native-macos package-native-linux package-native-windows
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

$(JARFILE_PATH): classes manifest
	@echo Creating $(JARFILE_NAME);
	@echo "@cd $(CLASSPATH); jar cvfm $(JARFILE_NAME) $(notdir $(MANIFEST)) $(MAIN_CLASS).class *.{class,png,jpg};"
	@cd $(CLASSPATH); jar cvfm $(JARFILE_NAME) $(notdir $(MANIFEST)) $(MAIN_CLASS).class *.{class,png,jpg};
	
	@if [ ! -d $(JAR_DIR) ]; then\
		echo Making directory $(JAR_DIR);\
		mkdir $(JAR_DIR);\
	fi
	
	@echo Moving $(JARFILE_NAME) to $(JAR_DIR)
	@mv $(CLASSPATH)$(JARFILE_NAME) $(JAR_DIR)

jar: $(JARFILE_PATH)

native-builds-dir: 
	@if [ ! -d $(NATIVE_BUILDS_DIR) ]; then\
		@echo Creating native builds dir $(NATIVE_BUILDS_DIR);\
		mkdir $(NATIVE_BUILDS_DIR);\
	fi

output-build-version:
	@echo Building version $(APP_VERSION)

prep-package-native: jar native-builds-dir output-build-version

package-native-linux: prep-package-native
	@echo Creating native Linux package
	@$(PACKAGE_COMMAND) --icon "./icons/icon.ico"

package-native-windows: prep-package-native
	@echo Creating native Windows package
	@$(PACKAGE_COMMAND) --icon "./icons/icon.ico"

package-native-macos: prep-package-native
	@echo Creating native MacOS package
	@$(PACKAGE_COMMAND) --icon "./icons/icon.icns"

run: classes
	@echo Running $(MAIN_CLASS)...;
	@java $(MAIN_CLASS)
	@echo Closing...;

run-jar:
	@if [ -f $(JARFILE_PATH) ]; then\
		java -jar $(JARFILE_PATH);\
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
