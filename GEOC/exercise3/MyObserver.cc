#include "MyObserver.h"
#include <geoc/scene/SceneManager.h>
#include <geoc/io/output/OutputSystem.h>
#include <geoc/io/output/FileOutput.h>
#include <geoc/scene/Point.h>
#include <geoc/scene/CircleEnt.h>
#include <geoc/scene/Entity.h>
#include <string>
#include <sstream>


using namespace geoc;
using namespace std;


MyObserver::MyObserver(SceneManager* scenemgr, OutputSystem* outputSys) :
    sceneMgr(scenemgr), outputSystem(outputSys), circle(0), num_points(0)
{
    FileOutput* fOutput = new FileOutput("exercise3-solution.txt");
    outputSystem->attach(fOutput);
}


void MyObserver::enters(geoc::Point* p)
{
    sceneMgr->attach(p);
    
    num_points++;
    ostringstream label;
    label << num_points;
    p->setLabel(label.str());
    
    if (circle)
    {
	Colour3 colour;
	string desc;
	classify(*circle, p->position(), colour, desc);
	p->colour = colour;
	outputSystem->write(desc);
    }
}


void MyObserver::enters(geoc::CircleEnt* c)
{
    circle = c;
    c->colour = Colour3(0.0, 0.0, 0.0);
    sceneMgr->attach(c);
}


void MyObserver::leaves(geoc::Entity* e)
{
    if (circle == e) circle = 0;
}

void MyObserver::sceneCleared()
{
    num_points = 0;
}
