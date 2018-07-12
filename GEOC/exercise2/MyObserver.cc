#include "MyObserver.h"
#include <geoc/scene/SceneManager.h>
#include <geoc/io/output/OutputSystem.h>
#include <geoc/io/output/FileOutput.h>
#include <sstream>
#include <string>
#include <algorithm>
#include <sstream>


using namespace geoc;
using namespace std;


MyObserver::MyObserver(SceneManager* scenemgr, OutputSystem* outputSys) :
    sceneMgr(scenemgr), outputSystem(outputSys), triangle(0), num_points(0)
{
    FileOutput* fOutput = new FileOutput("exercise2-solution.txt");
    outputSystem->attach(fOutput);
}


void MyObserver::enters(geoc::Point* p)
{
    sceneMgr->attach(p);
    
    num_points++;
    ostringstream label;
    label << num_points;
    p->setLabel(label.str());
    
    if (triangle != 0)
    {
	Colour3 colour;
	string desc;
	classify(*triangle, p->position(), colour, desc);
	
	p->colour = colour;
	outputSystem->write(desc);
    }
    else
    {
	p->colour = Colour3(1.0, 1.0, 1.0);
    }
}


void MyObserver::enters(geoc::TriangleEnt* t)
{
    sceneMgr->attach(t);
    triangle = t;
}


void MyObserver::leaves(geoc::Entity* e)
{
    if (triangle == e) triangle = 0;
}

void MyObserver::sceneCleared()
{
    num_points = 0;
}

