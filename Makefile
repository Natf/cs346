#
# define compiler and compiler flag variables

JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Server.java \
		RunProject.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class