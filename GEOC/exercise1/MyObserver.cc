#include "MyObserver.h"
#include <geoc/scene/SceneManager.h>
#include <geoc/io/output/OutputSystem.h>
#include <geoc/io/output/FileOutput.h>
#include <sstream>
#include <string>

using namespace geoc;
using namespace std;

MyObserver::MyObserver(SceneManager* scenemgr, OutputSystem* outputsys)
    : sceneMgr(scenemgr), outputSystem(outputsys)
{
    FileOutput* fOutput	= new FileOutput("exercise1-solution.txt");
    outputSystem->attach(fOutput);
    sceneCleared();
}

MyObserver::~MyObserver()
{
}

void MyObserver::enters(LineSegmentEnt* s)
{
    sceneMgr->attach(s);
    num_segments++;
    
    ostringstream os;
    os << num_segments;
    s->setLabel(os.str());
    
    if (num_segments % 2)
    {
	previous_segment = s;
    }
    
    else
    {
	Colour3 colour;
	string desc;
	classifyIntersection(*previous_segment, *s, colour, desc);
	
	previous_segment->colour = colour;
	s->colour = colour;
	
	ostringstream os;
	os << "Pair " << pair_num << ": " << desc;
	outputSystem->write(os);
	pair_num++;
    }
}

void MyObserver::leaves(Entity* e)
{
    if (previous_segment == e)
    {
	num_segments = 0;
    }
}

void MyObserver::sceneCleared()
{
    num_segments	= 0;
    pair_num	= 1;
}
