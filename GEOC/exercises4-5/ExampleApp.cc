#include "ExampleApp.h"
#include "MyObserver.h"
#include <geoc/gfx/Graphics.h>
#include <geoc/io/loader/ObjectLoader.h>
#include <geoc/io/loader/Loader.h>
#include <geoc/app/GeocWidget.h>
#include <cstdio>
#include <cstring>
#include <iostream>

#include <geoc/geometry/CgalTriangulation.h>
#include <geoc/scene/TriangulationEnt.h>


using namespace geoc;
using namespace std;


void ExampleApp::init(int argc, char** argv)
{
    parse_args(argc, argv);
    
    GeocWidget& w = geocWidget();
    
    w.gfx().setPointSize(3);
    w.gfx().setLighting(true);
    w.gfx().setAntialiasing(false);
    
    myObserver = new MyObserver(&w.sceneManager(), &outputSystem(), &w.gfx(), enable_3d);
    
    ObjectLoader& oLoader = w.objectLoader();
    
    boost::function<void (std::istream&, TriangulationEnt&)> load =
	    boost::bind(&TriangulationEnt::read, _1, _2);
    
    if (use_cgal)
    {
	boost::function<TriangulationEnt* ()> build_cgal_triang =
		boost::bind(&make_triangulation_ent<CgalTriangulation>);
	
	Loader<TriangulationEnt>* ctLoader = new Loader<TriangulationEnt>(build_cgal_triang, load);
	oLoader.attach(ctLoader);
	ctLoader->attach(myObserver);
    }
    
    boost::function<TriangulationEnt* ()> build_student_triang =
	    boost::bind(&make_student_triangulation);
    
    Loader<TriangulationEnt>* tLoader = new Loader<TriangulationEnt>(build_student_triang, load);
    oLoader.attach(tLoader);
    tLoader->attach(myObserver);
    
    if (argc > 1)
    {
	for (int i = 1; i < argc; ++i)
	{
	    if (strcmp(argv[i], "--cgal") != 0 &&
		    strcmp(argv[i], "--3d") != 0)
	    {
		loadScene(argv[i]);
	    }
	}
    }
    
    printf("Example application initialised.\n");
}


void ExampleApp::shutdown()
{
    printf("Example application shutting down.\n");
    
    safe_delete(myObserver);
}


void ExampleApp::keyPressed(geoc::GeocWidget&, Keyboard::key key)
{
    bool changed = false;
    
    switch(key)
    {
    case Keyboard::Key_B:	//2D Raw
	myObserver->setDrawMode(TRIANGULATION_2D_RAW);
	changed = true;
	break;
	
    case Keyboard::Key_H:	//2D Pruned
	myObserver->setDrawMode(TRIANGULATION_2D_PRUNED);
	changed = true;
	break;
	
    case Keyboard::Key_N:	//3D Pruned and Gray.
	myObserver->setDrawMode(TRIANGULATION_3D_PRUNED_AND_GRAY);
	changed = true;
	break;
	
	
    case Keyboard::Key_J:	//3D Pruned
	myObserver->setDrawMode(TRIANGULATION_3D_PRUNED);
	changed = true;
	break;
	
    case Keyboard::Key_M:	//3D Pruned and Smooth
	myObserver->setDrawMode(TRIANGULATION_3D_PRUNED_AND_SMOOTH);
	changed = true;
	break;
	
    default: break;
    }
    
    if (changed) redisplay();
}


void ExampleApp::parse_args(int argc, char** argv)
{
    for (int i = 1; i < argc; ++i)
    {
	if (strcmp(argv[i], "--3d") == 0) enable_3d = true;
	else if (strcmp(argv[i], "--cgal") == 0) use_cgal = true;
    }
}
