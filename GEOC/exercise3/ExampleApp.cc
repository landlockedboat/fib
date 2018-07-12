#include "ExampleApp.h"
#include "MyObserver.h"
#include <geoc/gfx/Graphics.h>
#include <geoc/io/loader/ObjectLoader.h>
#include <geoc/io/loader/Loader.h>
#include <geoc/io/screen/ScreenInput.h>
#include <geoc/io/screen/PointState.h>
#include <geoc/io/screen/CircleState.h>
#include <geoc/scene/SceneManager.h>
#include <geoc/app/GeocWidget.h>
#include <cstdio>

using namespace geoc;
using namespace std;

void ExampleApp::init(int argc, char** argv)
{
    GeocWidget& w = geocWidget();
    
    myObserver = new MyObserver(&w.sceneManager(), &outputSystem());
    
    PointState* pointState = new PointState;
    CircleState* circleState = new CircleState;
    
    ScreenInput& screenInput = w.screenInput();
    screenInput.setTransition(Keyboard::Key_P, pointState);
    screenInput.setTransition(Keyboard::Key_C, circleState);
    
    Loader<Point>* pLoader = new Loader<Point>;
    Loader<CircleEnt>* cLoader = new Loader<CircleEnt>;
    
    ObjectLoader& oLoader = w.objectLoader();
    oLoader.attach(pLoader);
    oLoader.attach(cLoader);
    
    pointState->attach(myObserver);
    circleState->attach(myObserver);
    pLoader->attach(myObserver);
    cLoader->attach(myObserver);
    w.sceneManager().attachObserver(myObserver);
    
    if (argc > 1) loadScene(argv[1]);
    
    w.gfx().setPointSize(3);
    w.gfx().setWireframe(true);
    w.gfx().setCulling(false);
    
    printf("Example application initialised.\n");
}


void ExampleApp::shutdown()
{
    printf("Example application shutting down.\n");
    
    safe_delete(myObserver);
}
