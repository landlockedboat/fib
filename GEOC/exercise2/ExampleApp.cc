#include "ExampleApp.h"
#include "MyObserver.h"
#include <geoc/gfx/Graphics.h>
#include <geoc/io/loader/ObjectLoader.h>
#include <geoc/io/loader/Loader.h>
#include <geoc/io/screen/ScreenInput.h>
#include <geoc/io/screen/PointState.h>
#include <geoc/io/screen/TriangleState.h>
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
    TriangleState* triangleState = new TriangleState;
    
    ScreenInput& screenInput = w.screenInput();
    screenInput.setTransition(Keyboard::Key_P, pointState);
    screenInput.setTransition(Keyboard::Key_T, triangleState);
    
    Loader<Point>*	pLoader	= new Loader<Point>;
    Loader<TriangleEnt>* tLoader = new Loader<TriangleEnt>;
    
    ObjectLoader& oLoader = w.objectLoader();
    oLoader.attach(pLoader);
    oLoader.attach(tLoader);
    
    pointState->attach(myObserver);
    triangleState->attach(myObserver);
    pLoader->attach(myObserver);
    tLoader->attach(myObserver);
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
