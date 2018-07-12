#include "MyObserver.h"
#include <geoc/geoc.h>
#include <geoc/scene/SceneManager.h>
#include <geoc/io/output/OutputSystem.h>

#include <cstdio>

using namespace geoc;
using namespace std;

MyObserver::MyObserver(SceneManager* scenemgr, OutputSystem* outputSys,
		       Graphics* graphics, bool enable3d)
    : sceneMgr(scenemgr), outputSystem(outputSys), gfx(graphics), enable_3d(enable3d), t(0)
{}


void MyObserver::enters(TriangulationEnt* t)
{
    this->t = t;
    t->prepare(gfx, enable_3d);
    sceneMgr->attach(t);
    outputSystem->write("Triangulation loaded");
}


void MyObserver::sceneCleared()
{
    t = 0;
}


void MyObserver::setDrawMode(TRIANGULATION_DRAW_MODE mode)
{
    if (t) t->drawMode = mode;
}
