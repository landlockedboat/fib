#include <geoc/geoc.h>
#include <geoc/GeocException.h>
#include "ExampleApp.h"
#include <QApplication>
#include <exception>
#include <cstdio>

using namespace geoc;
using namespace std;

int main(int argc, char** argv)
{
    try
    {
	QApplication qt_application(argc, argv);
	ExampleApp app;
	app.setup(argc, argv);
	return qt_application.exec();
    }
    catch (GeocException& e)
    {
	printf("Geoc exception caught: %s\n", e.what());
    }
    catch (exception& e)
    {
	printf("Exception caught: %s\n", e.what());
    }
    
    return 0;
}
