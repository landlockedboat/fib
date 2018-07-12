GEOC_BOOST_PATH	= $$[GEOC_BOOST_PATH]
GEOC_CGAL_PATH	= $$[GEOC_CGAL_PATH]

CONFIG += qt
CONFIG += opengl
CONFIG += thread

QT += opengl


INCLUDEPATH += ../../src
DEPENDPATH += ../../src

!isEmpty (GEOC_CGAL_PATH) {
	INCLUDEPATH	+= "$${GEOC_CGAL_PATH}/include"
	DEPENDPATH	+= "$${GEOC_CGAL_PATH}/include"
	LIBS		+= "-L$${GEOC_CGAL_PATH}/lib"
}
else {
	INCLUDEPATH	+= ../../CGAL-3.8/include
	DEPENDPATH	+= ../../CGAL-3.8/include
	LIBS		+= -L../../CGAL-3.8/lib
}

!isEmpty (GEOC_BOOST_PATH) {
	INCLUDEPATH	+= "$${GEOC_BOOST_PATH}"
	DEPENDPATH	+= "$${GEOC_BOOST_PATH}"
	LIBS		+= "-L$${GEOC_BOOST_PATH}/lib"
}

win32 {
	DEFINES					+= WIN32 _SCL_SECURE_NO_WARNINGS _CRT_SECURE_NO_WARNINGS
	QMAKE_CXXFLAGS_RELEASE	= /O2
	QMAKE_CXXFLAGS			= /EHsc /W1 /wd4251
}
else {
	QMAKE_CXXFLAGS			+= -frounding-math
	QMAKE_CXXFLAGS_DEBUG	+= -g
}


QMAKE_CLEAN += ../../bin/ex1

HEADERS += ExampleApp.h

LIBS += -lQtOpenGL
LIBS += -lgeoc
LIBS += -L../../bin

win32 {
	LIBS += -lCGAL-vc100-mt
}
else {
	LIBS += -lCGAL
}

unix|macx {
	LIBS += -lrt
	LIBS += -lGLU
}

SOURCES += main.cc
SOURCES += ExampleApp.cc
SOURCES += MyObserver.cc

TARGET=ex1
DESTDIR=../../bin
