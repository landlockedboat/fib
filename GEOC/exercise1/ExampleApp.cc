#include "ExampleApp.h"
#include "MyObserver.h"
#include <geoc/gfx/Graphics.h>
#include <geoc/io/loader/ObjectLoader.h>
#include <geoc/io/loader/Loader.h>
#include <geoc/io/screen/ScreenInput.h>
#include <geoc/io/screen/LineSegmentState.h>
#include <geoc/scene/SceneManager.h>
#include <geoc/app/GeocWidget.h>
#include <cstdio>

using namespace geoc;
using namespace std;

void ExampleApp::init(int argc, char** argv)
{
    GeocWidget& w = geocWidget();
    w.gfx().setPointSize(3);
    
    myObserver = new MyObserver(&w.sceneManager(), &outputSystem());
    
    ScreenInput& screenInput = w.screenInput();
    LineSegmentState* segmentState = new LineSegmentState;
    screenInput.setTransition(Keyboard::Key_S, segmentState);
    
    ObjectLoader& oLoader = w.objectLoader();
    Loader<LineSegmentEnt>* sLoader = new Loader<LineSegmentEnt>;
    oLoader.attach(sLoader);
    
    segmentState->attach(myObserver);
    sLoader->attach(myObserver);
    w.sceneManager().attachObserver(myObserver);
    
    if (argc > 1) loadScene(argv[1]);
    
    printf("Example application initialised.\n");
}


void ExampleApp::shutdown()
{
    printf("Example application shutting down.\n");
    
    safe_delete(myObserver);
}
